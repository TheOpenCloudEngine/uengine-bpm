package org.uengine.kernel.bpmn;


import org.uengine.kernel.MessageListener;
import org.uengine.kernel.ProcessInstance;

public class SignalEvent extends Event implements MessageListener {

	@Override
	protected void executeActivity(ProcessInstance instance) throws Exception {
		//start listens...
	}
	public boolean onMessage(ProcessInstance instance, Object payload)	throws Exception {
//		Vector activityInstances =  instance.getCurrentRunningActivities();
//		for(int i=0; i<activityInstances.size(); i++){
//			Activity nextAct = (Activity) activityInstances.get(i);
//			nextAct.stop(instance);
//		}
		
		fireComplete(instance);
		
		return true;
	}
	
	public SignalEvent(){
		super();
		if( this.getName() == null ){
			setName(this.getClass().getSimpleName());
		}
//		setActivityStop(PASS_ACTIVITY);
	}

	public String getMessage() {
		return "event";//getClass().getName();  //just simply return the event name as it's classname.
	}

}
