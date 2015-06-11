package org.uengine.kernel;

/**
 * @author Jinyoung Jang
 */

import java.util.List;


public class CompensatableActivity extends ComplexActivity{
	private static final long serialVersionUID = org.uengine.kernel.GlobalContext.SERIALIZATION_UID;

	public CompensatableActivity(){
		super();
		setName("compensatable");
	}
	
	protected void onEvent(String command, ProcessInstance instance, Object payload) throws Exception{
		//dispatching leaf childs
		if(command.equals(CHILD_DONE)){//compensate have only single child
			int currStep = getCurrentStep(instance);
			if(currStep == 0)			
				fireComplete(instance);
			else
				fireCompensate(instance);
		}
		else
		super.onEvent(command, instance, payload);
	}

	protected void executeActivity(ProcessInstance instance) throws Exception{		
		List<Activity> childActivities = getChildActivities();
		Activity child = (Activity)childActivities.get(0);
		queueActivity(child, instance);
	}

	public void compensate(ProcessInstance instance) throws Exception {
		List<Activity> childActivities = getChildActivities();
		//compensate child first		
		Activity child = (Activity)childActivities.get(0);
		child.compensate(instance);
		
		//execute the its own compensate activity
		child = (Activity)childActivities.get(1);
		queueActivity(child, instance);
		
		setCurrentStep(instance, 1);
		//super.compensate(instance);
	}

}