package org.uengine.kernel;

public class FaultContext {

	Activity causeActivity;
	UEngineException fault;
	
	
	public Activity getCauseActivity() {
		return causeActivity;
	}
	public void setCauseActivity(Activity causeActivity) {
		this.causeActivity = causeActivity;
	}
	public UEngineException getFault() {
		return fault;
	}
	public void setFault(UEngineException fault) {
		this.fault = fault;
	}
}
