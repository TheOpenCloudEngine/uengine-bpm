package org.uengine.queue.messagequeue;
        
import org.uengine.kernel.GlobalContext;
import org.uengine.kernel.ProcessInstance;
import org.uengine.processmanager.ProcessManagerBean;


import javax.jms.Message;
import javax.jms.MapMessage;

import javax.naming.InitialContext;
import javax.naming.Context;
import javax.naming.NamingException;

import javax.ejb.EJBException;
import javax.ejb.MessageDrivenContext;
import javax.ejb.RemoveException;

//JMS queuing concerned
import javax.jms.Session;
import javax.jms.QueueConnectionFactory;
import javax.jms.QueueConnection;
import javax.jms.QueueSession;
import javax.jms.Queue;
import javax.jms.QueueSender;
//end

import java.io.*;
import java.rmi.RemoteException;

public class MessageProcessorBean
   implements javax.ejb.MessageDrivenBean, javax.jms.MessageListener
{
   
   static long RETRY_DELAY = 3000L;
   static int RETRY_LIMIT = 10;

	/**
	 * 
	 * @uml.property name="ejbContext"
	 * @uml.associationEnd 
	 * @uml.property name="ejbContext" multiplicity="(0 1)"
	 */
	MessageDrivenContext ejbContext;

	/**
	 * 
	 * @uml.property name="jndiContext"
	 * @uml.associationEnd 
	 * @uml.property name="jndiContext" multiplicity="(0 1)"
	 */
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
		ProcessInstance instance = null;
		
		ProcessManagerBean pmb = new ProcessManagerBean();
		pmb.setSessionContext(null);

		try
		{         
			System.out.println ("MessageProcessor::onMessage() called..");
			
			MapMessage message = (MapMessage)omessage;
			String instanceId = message.getString("instance");
			String msg = message.getString("message");
			String str_payload = message.getString("payload");
			
			ByteArrayInputStream bis = new ByteArrayInputStream(str_payload.getBytes("ISO-8859-1"));
			Object payload = GlobalContext.deserialize(bis, String.class);
			
			
			instance = pmb.getProcessInstance(instanceId);

			if(instance.isRunning("")){ // if STOP signaled, don't execute anymore.
				//review: message would effect only one instance. it is not common in BPEL
				instance.getProcessDefinition().fireMessage(msg, instance, payload);
				pmb.applyChanges();
			}
			
		} 
		catch(Throwable e)
		{
System.out.println("MessageProcessorBean::onMessage(): fail to process message. Reason:[" + e.getMessage() + "]. try to redeliver... ");
			//e.printStackTrace();
			try {
				pmb.cancelChanges();
			} catch (RemoteException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
			ejbContext.setRollbackOnly();
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
   
	public static void queueMessage(String msg, String instanceId, Object payload) throws Exception{	
		try{
			InitialContext jndiContext = GlobalContext.getInitialContext();
			QueueConnectionFactory factory = (QueueConnectionFactory)jndiContext.lookup ("ConnectionFactory");
					
			Queue resultQueue = (Queue)jndiContext.lookup ("queue/uEngine-MessageQueue");
			QueueConnection connect = factory.createQueueConnection ();
															   //it doesn't need to issue new transaction context. it may occur an error in such env doesn't support two-phase commit.
			QueueSession session = connect.createQueueSession (false, Session.AUTO_ACKNOWLEDGE);
			QueueSender sender = session.createSender (resultQueue);
					
			MapMessage message = session.createMapMessage();
				 
			message.setJMSRedelivered(true);
			message.setLongProperty("JMS_JBOSS_REDELIVERY_DELAY", RETRY_DELAY); 
			message.setIntProperty("JMS_JBOSS_REDELIVERY_LIMIT", RETRY_LIMIT);
//			message.setBoolean("retry", true);
			message.setStringProperty ("MessageFormat", "Version 1.0");
			message.setString("instance", instanceId);
			message.setString("message", msg);
			
			//review:
			ByteArrayOutputStream bao = new ByteArrayOutputStream();
			GlobalContext.serialize(payload, bao, String.class);
			
			message.setString("payload", bao.toString("ISO-8859-1"));
			//only available for JBoss 3.2.2
			sender.send (message);
			connect.close ();
					
		}catch(Exception jmse){
			throw jmse;//.printStackTrace();
		}
	}

}

