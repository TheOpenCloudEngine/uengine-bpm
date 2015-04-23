package org.uengine.kernel;

public class ReceiveErrorEventActivity extends EventActivity implements MessageListener {
	public ReceiveErrorEventActivity(){
		super();
		if( this.getName() == null ){
			setName(this.getClass().getSimpleName());
		}
		setActivityStop(STOP_ACTIVITY);
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
