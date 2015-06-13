package org.uengine.kernel;

public class ApprovalSubActivity extends DefaultActivity{

	protected void executeActivity(ProcessInstance instance) throws Exception {

		//결재 시작
		//시작한 후 얻은 키정보를 얻음 ex) approvalId
		
		instance.setProperty(getTracingTag(), "_approvalId", "123");
		instance.getProperty(getTracingTag(), "_approvalId");
	}
	

}

/* JSP
 * 
 * ProcessInstance instance = pm.getProcessInstance(instanceId);
 * Activity activity = instance.getProcessDefinition().getActivity(tracingTag);
 * activity.fireComplete(instance);
 * 
 * 
 */
