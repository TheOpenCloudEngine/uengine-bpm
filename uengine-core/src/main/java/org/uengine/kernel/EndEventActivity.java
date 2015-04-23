package org.uengine.kernel;


public class EndEventActivity extends DefaultActivity{

	@Override
	protected void executeActivity(ProcessInstance instance) throws Exception {
		// TODO Auto-generated method stub
		if (instance != null && instance.isSubProcess()) {
			instance.getProcessDefinition().returnToMainProcess(instance);
		}
		fireComplete(instance);
	}
}
