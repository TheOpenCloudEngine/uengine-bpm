package org.uengine.kernel;

import java.util.Map;

import org.uengine.util.ActivityForLoop;

public class ApprovalLineActivity extends ComplexActivity {
	public final static String KEY_APPR_LINE_STATUS = "KEY_APPR_LINE_STATUS";
	
	public ApprovalLineActivity(){
		setName("");
		ApprovalActivity draftActivity = new ApprovalActivity();
		draftActivity.setName(GlobalContext.getLocalizedMessage("activitytypes.org.uengine.kernel.approvalactivity.draft.message", "Draft"));
		addChildActivity(draftActivity);
	}

	protected void executeActivity(ProcessInstance instance) throws Exception {
		super.executeActivity(instance);
		
/*		if(getCurrentStep(instance) > 1){
			fireComplete(instance);
		}
*/	}
	
	public void setApprovalLineStatus(ProcessInstance instance, String status) throws Exception{
		instance.setProperty(getTracingTag(), KEY_APPR_LINE_STATUS, status);
	}
	
	public String getApprovalLineStatus(ProcessInstance instance) throws Exception{
		return (String)instance.getProperty(getTracingTag(), KEY_APPR_LINE_STATUS);
	}
	
	ApprovalActivity draftActivity;
	public ApprovalActivity getDraftActivity(){
		if(draftActivity!=null)
			return draftActivity;
		
		ActivityForLoop findingLoop = new ActivityForLoop(){
			public void logic(Activity activity){
				if(activity instanceof ApprovalActivity){
					stop(activity);
				}
			}
		};
		
		findingLoop.run(this);		
		this.draftActivity = (ApprovalActivity)findingLoop.getReturnValue(); 
		
		return draftActivity;
	}
}
