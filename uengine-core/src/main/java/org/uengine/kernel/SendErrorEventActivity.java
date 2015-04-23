package org.uengine.kernel;

public class SendErrorEventActivity extends EventActivity {
	
	public SendErrorEventActivity(){
		super();
		if( this.getName() == null ){
			setName(this.getClass().getSimpleName());
		}
		setActivityStop(STOP_ACTIVITY);
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
