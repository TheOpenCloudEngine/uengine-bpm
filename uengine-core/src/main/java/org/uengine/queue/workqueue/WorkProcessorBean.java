package org.uengine.queue.workqueue;
        
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.rmi.RemoteException;

import javax.ejb.EJBException;
import javax.ejb.MessageDrivenContext;
import javax.ejb.RemoveException;
import javax.jms.MapMessage;
import javax.jms.Message;
import javax.jms.ObjectMessage;
import javax.jms.Queue;
import javax.jms.QueueConnection;
import javax.jms.QueueConnectionFactory;
import javax.jms.QueueSender;
import javax.jms.QueueSession;
import javax.jms.Session;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.uengine.kernel.Activity;
import org.uengine.kernel.GlobalContext;
import org.uengine.kernel.ProcessInstance;
import org.uengine.kernel.UEngineException;
import org.uengine.processmanager.ProcessManagerFactoryBean;
import org.uengine.processmanager.ProcessManagerRemote;
import org.uengine.queue.faultqueue.FaultParameter;

public class WorkProcessorBean
   implements javax.ejb.MessageDrivenBean, javax.jms.MessageListener
{

	MessageDrivenContext ejbContext;
	Context jndiContext;

   
   public void setMessageDrivenContext (MessageDrivenContext mdc)
   {
      ejbContext = mdc;
      try
      {
         jndiContext = new InitialContext ();
      } catch(NamingException ne)
      {
         throw new EJBException (ne);
      }
   }
   
   public void ejbCreate () {}
   
   public void onMessage (Message omessage)
   {
   	String tracingTag = null;
	ProcessInstance instance = null;
	Activity activity = null;
//	boolean bRetry = false; 
	
	ProcessManagerRemote pmb = null;
	try
	{      
		pmb = (new ProcessManagerFactoryBean()).getProcessManager();
		
		System.out.println ("WorkProcessor::onMessage() called...");
		
		MapMessage message = (MapMessage)omessage;
	
		
		String instanceId = message.getString("instance");
		String definitionName = message.getString("processDefinition");
		tracingTag = message.getString("tracingTag");
//		bRetry = message.getBoolean("retry");
//		String serviceName = message.getString("service");
//		String operationName = message.getString("operationName");

		
		instance = pmb.getProcessInstance(instanceId);
		if(instance.isRunning("")){ // if STOP signaled, don't execute anymore.
			activity = instance.getProcessDefinition().getActivity(tracingTag);
			activity.setRetryCount(instance, 5);
			instance.execute(tracingTag);
			pmb.applyChanges();
		}
		
		//ProcessDefinition pd = ProcessDefinition.getDefinition(definitionName, instance.getProcessTransactionContext());
		
/*		Parameter parameter = (Parameter)message.getObject("parameter");
		String endpoint = parameter.getEndpoint();
		
		Object[] parameters = parameter.getParameters();
		ServiceProvider sp = GlobalContext.getServiceProvider(serviceName);		
		for(int i=0; i<parameters.length; i++){
			if(parameters[i] instanceof ProcessVariable){
				parameters[i] = instance.get(tracingTag, ((ProcessVariable)parameters[i]).getName());
			}
		}
		Object result = sp.invokeService(endpoint, operationName, parameters);*/
		
	} 
	catch(Throwable e)
	{
		try{
			pmb.cancelChanges();
		}catch(Exception ex){
			try{
				pmb.remove();
			}catch(Exception ex2){}
		}
		
		if(tracingTag !=null && instance!=null){
			UEngineException ue = null;
			
			if(!(e instanceof UEngineException)){
				ByteArrayOutputStream bao = new ByteArrayOutputStream();
				e.printStackTrace(new PrintStream(bao));
				try{			
						ue = new UEngineException("uEngine Exception: " + e + "("+e.getMessage()+")", e);
						ue.setDetails(bao.toString());
				}catch(Exception e3){
					e3.printStackTrace();
				}
				
			}else
				ue = (UEngineException)e;
			
//			if(ejbContext.getEnvironment())
			try{		
				//fireFault must not be rolled back 
				if(ue!=null){
					fireFault(instance, tracingTag, ue);
					
					/*LocalProcessManagerHomeLocal lpmh = (LocalProcessManagerHomeLocal)jndiContext.lookup("LocalProcessManagerHomeLocal");
					LocalProcessManagerLocal lpm = lpmh.create();
					lpm.fireFault(instance.getInstanceId(), tracingTag, ue);*/
					
					//instance.fireFault(tracingTag, ue);
				}
			}catch(Exception ex){
				ex.printStackTrace();
			}
		}
		
//		if(bRetry!=null)
		ejbContext.setRollbackOnly();
//		throw new EJBException((Exception)e);
	}finally{
		try {
			pmb.remove();
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (RemoveException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
   }
   
   public void ejbRemove ()
   {
      try
      {
         jndiContext.close ();
         ejbContext = null;
      } catch(NamingException ignored) { }
   }
   

	public static void fireFault(ProcessInstance instance, String tracingTag, UEngineException fault){
		try{
			InitialContext jndiContext = GlobalContext.getInitialContext();
			
			QueueConnectionFactory factory = (QueueConnectionFactory)jndiContext.lookup ("ConnectionFactory");
				
			Queue resultQueue = (Queue)jndiContext.lookup ("queue/uEngine-FaultQueue");
			QueueConnection connect = factory.createQueueConnection ();
			QueueSession session = connect.createQueueSession (false, Session.AUTO_ACKNOWLEDGE);
			QueueSender sender = session.createSender (resultQueue);
				
			ObjectMessage message = session.createObjectMessage();
			message.setStringProperty ("MessageFormat", "FaultMessage Version 1.0");
			message.setObject(new FaultParameter(instance.getInstanceId(), tracingTag, fault));

			//only available for JBoss 3.2.2
			sender.send (message);
			connect.close ();
				
		}catch(Exception jmse){
			jmse.printStackTrace();
		}
	}
	
	public static void queueActivity(final Activity act, final ProcessInstance instance) throws Exception{	
		try{
			//check if act has its process definition
			if(act.getProcessDefinition()==null)
				throw new UEngineException("The activity '" + act.getName() + "' is not belong to its definition", "Check the definition");
			
			InitialContext jndiContext = GlobalContext.getInitialContext();
			QueueConnectionFactory factory = (QueueConnectionFactory)jndiContext.lookup ("ConnectionFactory");
					
			Queue resultQueue = (Queue)jndiContext.lookup ("queue/uEngine-WorkQueue");
			QueueConnection connect = factory.createQueueConnection ();
															   //it doesn't need to issue new transaction context. it may occur an error in such env doesn't support two-phase commit.
			QueueSession session = connect.createQueueSession (false, Session.AUTO_ACKNOWLEDGE);
			QueueSender sender = session.createSender (resultQueue);
					
			MapMessage message = session.createMapMessage();
				 
			message.setStringProperty ("MessageFormat", "Version 1.0");
			if(act.getRetryLimit()>0){
				message.setJMSRedelivered(true);
				message.setLongProperty("JMS_JBOSS_REDELIVERY_DELAY", ((long)act.getRetryDelay() * 1000)); 
							  // or use "JMS_JBOSS_SCHEDULED_DELIVERY"					
				message.setIntProperty("JMS_JBOSS_REDELIVERY_LIMIT", act.getRetryLimit());
	//			message.setBoolean("retry", true);
			}else{
				message.setJMSRedelivered(false);
				message.setIntProperty("JMS_JBOSS_REDELIVERY_LIMIT", 0); //I couldn't force the message not to be redelivered till setting this 
				message.setLongProperty("JMS_JBOSS_REDELIVERY_DELAY", 15000L); 
	//			message.setBoolean("retry", false);
			}
					
			message.setString("instance", instance.getInstanceId());
			message.setString("processDefinition", act.getProcessDefinition().getId());
			message.setString("tracingTag", act.getTracingTag());
			//only available for JBoss 3.2.2
			sender.send (message);
			connect.close ();
					
		}catch(Exception jmse){
			throw jmse;//.printStackTrace();
		}
	}

}

