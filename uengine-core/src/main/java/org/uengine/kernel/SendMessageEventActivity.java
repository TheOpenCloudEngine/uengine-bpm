package org.uengine.kernel;

public class SendMessageEventActivity extends EventActivity {
	public SendMessageEventActivity(){
		super();
		if( this.getName() == null ){
			setName(this.getClass().getSimpleName());
		}
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
