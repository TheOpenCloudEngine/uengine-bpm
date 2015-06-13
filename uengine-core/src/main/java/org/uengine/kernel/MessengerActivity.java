package org.uengine.kernel;

import org.uengine.kernel.GlobalContext;
import org.uengine.util.UEngineUtil;

/**
 * @author Jinyoung Jang
 */

public class MessengerActivity extends EMailActivity{

	private static final long serialVersionUID = org.uengine.kernel.GlobalContext.SERIALIZATION_UID;
	protected static final String MESSENGER_SERVICE = "messengerServer";

	public MessengerActivity(){
		setName("Messenger Activity");
		setPortType("MSNMessengerService");
		setOperationName("sendMessage");
		
		//recode
		setRole(ProcessDefinition.MESSENGER_SERVICE_ROLE);		
	}

	public void executeActivity(final ProcessInstance instance) throws Exception{			
		StringBuffer generating = evaluateContent( instance, getContents());
		
		setParameters(new Object[]{
			getToRole().getMapping(instance, getTracingTag()).getEndpoint(),
			generating.toString()
		});

		invokeWebService(instance);
	} 
}		
	