package org.uengine.kernel;


public class ActivityInstanceContext {
	
	ActivityInstanceContext(Activity activity, ProcessInstance instance){
		setActivity(activity);
		setInstance(instance);
	}

	Activity activity;
		public Activity getActivity() {
			return activity;
		}
		public void setActivity(Activity activity) {
			this.activity = activity;
		}
	
	ProcessInstance instance;
		public ProcessInstance getInstance() {
			return instance;
		}
		public void setInstance(ProcessInstance instance) {
			this.instance = instance;
		}
	
	public boolean equals(Object arg0) {
		if(!(arg0 instanceof ActivityInstanceContext)) return false;
		ActivityInstanceContext aic = ((ActivityInstanceContext)arg0);
		
		return aic.getActivity() == getActivity() && aic.getInstance() == getInstance();
	}
}
