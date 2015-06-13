package org.uengine.queue.faultqueue;

 
import java.rmi.RemoteException;

import org.uengine.kernel.FaultContext;
import org.uengine.kernel.ProcessInstance;
import org.uengine.kernel.UEngineException;
import org.uengine.processmanager.ProcessManagerBean;
import org.uengine.processmanager.ProcessManagerFactoryBean;

import javax.jms.Message;
import javax.jms.ObjectMessage;

import javax.naming.InitialContext;
import javax.naming.Context;
import javax.naming.NamingException;

import javax.ejb.EJBException;
import javax.ejb.MessageDrivenContext;
import javax.ejb.RemoveException;

public class FaultProcessorBean
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
		
		ProcessManagerBean pmb = null;
		try{
			ObjectMessage message = (ObjectMessage)omessage;			
			FaultParameter faultParameter = (FaultParameter)message.getObject();
						
			String instanceId = faultParameter.getInstanceId();
			tracingTag = faultParameter.getTracingTag();
			UEngineException fault = faultParameter.getFault();
			
			pmb = new ProcessManagerBean();
			pmb.setSessionContext(null);
			
			try{
				instance = pmb.getProcessInstance(instanceId);
			}catch(Exception e){
				throw new UEngineException("Fault has been occurred during a new instance initiates.", fault);
			}
System.out.println("FaultProcessorBean::onMessage: fault is " + fault + "\n detail is " + fault.getDetails());
			//TODO: because of Oracle's max size of varchar
			fault.setDetails(null);
			//
			
			instance.fireFault(tracingTag, fault);
			pmb.applyChanges();

		}catch(Exception e){
			if(pmb!=null)
				try {
					pmb.cancelChanges();
				} catch (RemoteException e1) {
				}
				
			throw new EJBException(e);		
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

}

