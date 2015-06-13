package org.uengine.kernel;

import org.uengine.kernel.bpmn.Event;

public class SendErrorEvent extends Event {
	
	public SendErrorEvent(){
		super();
		if( this.getName() == null ){
			setName(this.getClass().getSimpleName());
		}
//		setActivityStop(STOP_ACTIVITY);
	}
	@Override
	protected void executeActivity(ProcessInstance instance) throws Exception {
		//start listens...
		ProcessDefinition mainProcessDefinition = instance.getProcessDefinition();
		EventMessagePayload eventMessagePayload = new EventMessagePayload();
		eventMessagePayload.setEventName(this.getName());
		
		mainProcessDefinition.fireMessage(this.getName(), instance, eventMessagePayload);
	}
}
