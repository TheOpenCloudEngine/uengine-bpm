package org.uengine.kernel;

public class ReceiveMessageEventActivity extends EventActivity implements MessageListener {
	public ReceiveMessageEventActivity(){
		super();
		if( this.getName() == null ){
			setName(this.getClass().getSimpleName());
		}
	}
	@Override
	protected void executeActivity(ProcessInstance instance) throws Exception {
		//start listens...
	}

	public boolean onMessage(ProcessInstance instance, Object payload) throws Exception {
		fireComplete(instance);
		return true;
	}

	public String getMessage() {
		return this.getName();
	}
}
