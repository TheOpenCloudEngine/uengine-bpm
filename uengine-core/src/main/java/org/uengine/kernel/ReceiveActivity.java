package org.uengine.kernel;

/**
 * @author Jinyoung Jang
 */
import java.io.Serializable;
import java.util.*;

import org.metaworks.annotation.Hidden;


public class ReceiveActivity extends DefaultActivity implements MessageListener{
	private static final long serialVersionUID = org.uengine.kernel.GlobalContext.SERIALIZATION_UID;

	/**
	 * @deprecated messageDefinition will be used instead
	 * 
	 * @uml.property name="message"
	 */
	String message;
		@Hidden
		public String getMessage() {
			if (message == null && getMessageDefinition() != null)
				setMessage(getMessageDefinition().getName());

			return message;
		}

		/**
		 * @deprecated setMessageDefinition will be used instead
		 * 
		 * @uml.property name="message"
		 */
		public void setMessage(String value) {
			message = value;
		}

	/**
	 * @deprecated parameters will be used instead
	 * 
	 * ProcessVariable output;	
	 * /**
	 * @deprecated getParameters will be used instead
	 * 
	 * public ProcessVariable getOutput(){
	 * return output;
	 * }
	 * /**
	 * @deprecated setParameters will be used instead
	 * 
	 * public void setOutput(ProcessVariable value){
	 * output = value;
	 * }
	 */
	 /** @uml.property name="messageDefinition"
	 * @uml.associationEnd 
	 * @uml.property name="messageDefinition" multiplicity="(0 1)"
	 */
	MessageDefinition messageDefinition;
		@Hidden
		public MessageDefinition getMessageDefinition() {
			return messageDefinition;
		}
		public void setMessageDefinition(MessageDefinition definition) {
			if( definition == null) definition = new MessageDefinition();
			messageDefinition = definition;
			setMessage(definition.getName());
		}

	ParameterContext[] parameters;
		public ParameterContext[] getParameters() {
			return parameters;
		}
		public void setParameters(ParameterContext[] contexts) {
			parameters = contexts;
		}

	Role fromRole;
	@Hidden
		public Role getFromRole() {
			return fromRole;
		}
		public void setFromRole(Role role) {
			fromRole = role;
		}	
		
/////////////////////////

	public ReceiveActivity(){
		super("Receive");
	}

/*	protected void afterPreviousActivityExecute(ActivityInstance instance)
		throws Exception {
		super.afterPreviousActivityExecute(instance);
		
		System.out.println("ReceiveActivity::waiting for message : "+message);
		getProcessDefinition().addMessageListener(message, instance, getTracingTag()); //subscribes to JMS topic
	}*/

	protected void executeActivity(ProcessInstance instance) throws Exception{
		System.out.println("ReceiveActivity::waiting for message : "+message);
		getProcessDefinition().addMessageListener(instance, this); //subscribes to JMS topic
	}
	
	//TODO: hot-spot
	protected void onEvent(String command, ProcessInstance instance, Object payload) throws Exception{	
		if(!isMyMessage(command, instance, payload)){
			super.onEvent(command, instance, payload);
			return; 
		}

		onReceive(instance, payload);
	}
	
	protected boolean isMyMessage(String message, ProcessInstance instance, Object payload){
		return message.equals(Activity.PREPIX_MESSAGE+ "_"+ getMessage());
	}
	
	protected void onReceive(ProcessInstance instance, Object payload) throws Exception{
		
		instance.addDebugInfo(this);
		
		if(payload!=null && (getParameters()!=null)){

			Vector payloads = null;
			ParameterContext[] paramCtxs = getParameters();

			if(payload instanceof Vector){
System.out.println("ReceiveActivity::payload is " + payload);
				payloads = (Vector)payload;

				//TODO: test this
				if(!testMyMessage(instance, payloads)) return;

				int i=0; 
				for(Iterator iter = payloads.iterator(); iter.hasNext(); i++){
					paramCtxs[i].getVariable().set(instance, "", (java.io.Serializable)iter.next());						
				}
			}else if(payload instanceof ResultPayload){
				savePayload(instance, (ResultPayload)payload);
			}
			
			//TODO: when user rollback this receive activity, listener should be added again.
			getProcessDefinition().removeMessageListener(getMessage(), instance, getTracingTag());
		}
			
		fireComplete(instance);
	}
	
	public void savePayload(ProcessInstance instance, ResultPayload resultPayload) throws Exception{
		
		
		KeyedParameter[] processVariableChanges = resultPayload.getProcessVariableChanges();
		if(processVariableChanges!=null)
		for(int i=0; i<processVariableChanges.length; i++){
			String variableKey = processVariableChanges[i].getKey();
			Object variableValue = processVariableChanges[i].getValue();
			
			boolean saveVariableValue = true;
			
			if(variableValue instanceof CommandVariableValue){
				saveVariableValue = !((CommandVariableValue)variableValue).doCommand(instance, variableKey);
			}
			
			if(saveVariableValue)
				instance.set("", processVariableChanges[i].getKey(), (Serializable)processVariableChanges[i].getValue());				
		}
	}

	protected boolean testMyMessage(ProcessInstance instance, Vector payloads) throws Exception{
		if(fromRole==null) return true;
		
		ProcessVariable identifier = fromRole.getIdentifier();
		System.out.println("	test my role:: fromRole=" + fromRole);			
		System.out.println("				:: identifier=" + identifier);			

		if(identifier==null) return true;
		
		ParameterContext[] parameters = getParameters(); 
		Object identifierValue = identifier.get(instance, "");
		System.out.println("				:: value of identifier=" + identifierValue);			
		
		for(int i=0; i<parameters.length; i++){
			if(identifier.equals(parameters[i].getVariable())){
				Object paramValue = payloads.elementAt(i);
				System.out.println("				:: value of parameter=" + paramValue);			
				if(identifierValue.equals(paramValue)){
					//when the filter met
					return true;
				}
			}
		}
		
		return false;
	}

	public ValidationContext validate(Map options){
		ValidationContext validationContext  = super.validate(options);
		
//		if(getMessage()== null)
//			validationContext.addWarning(getActivityLabel()+" Message must be specified.");
			
		if(getParameters()!=null){
			ParameterContext[] parameters = getParameters();
			for(int i=0; i<parameters.length; i++){
				if(parameters[i].getVariable()==null){
					validationContext.addWarning(getActivityLabel()+" All of the parameters must be bound with variable.");
					break;
				}
			}
		}

		return validationContext;
	}
	
	public void fireReceived(ProcessInstance instance, Object payload) throws Exception{
		onReceive(instance, payload);
	}


	public boolean onMessage(ProcessInstance instance, Object payload) throws Exception {
		onEvent(Activity.PREPIX_MESSAGE+ "_"+ getMessage(), instance, payload);
		
		return true;
	}

}