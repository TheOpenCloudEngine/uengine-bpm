package org.uengine.kernel;

import java.util.HashMap;

import org.metaworks.FieldDescriptor;
import org.metaworks.Type;
import org.metaworks.inputter.RadioInput;
import org.uengine.contexts.ActivitySelectionContext;

public class BackActivity extends DefaultActivity{
	
	private static final long serialVersionUID = org.uengine.kernel.GlobalContext.SERIALIZATION_UID;
	
	public final static int TargetActivityPV_OR_TargetActivity = 0;
	public final static int TargetActivityPV = 1;
	public final static int TargetActivity = 2;
	public final static int Flag = 3;

	public BackActivity(){
		super();
		setName("back");
	}
	
	public static void metaworksCallback_changeMetadata(Type type){
	
		FieldDescriptor fd = type.getFieldDescriptor("TargetSource");
		fd.setInputter(new RadioInput(
				new String[]{
						"TargetActivityPV",
						"TargetActivity",
						"TargetActivityPV or targetActivity",
						"Flag"
				}, new Object[]{
						BackActivity.TargetActivityPV, 
						BackActivity.TargetActivity,
						BackActivity.TargetActivityPV_OR_TargetActivity,
						BackActivity.Flag
				}
			)
		);
	}
	
	public void executeActivity(ProcessInstance instance)
		throws Exception {
		
		ActivitySelectionContext activitySelection = null;
		
		if(getTargetSource()== BackActivity.TargetActivityPV){
			activitySelection = (ActivitySelectionContext)getTargetActivityPV().get(instance, "");
		}else if(getTargetSource()== BackActivity.TargetActivity){
			activitySelection = getTargetActivity();
		}else if(getTargetSource()== BackActivity.Flag){
			activitySelection = FlagActivity.getFlagLocation(instance, getFlag());
		}else if(getTargetSource()== BackActivity.TargetActivityPV_OR_TargetActivity){
			if(getTargetActivityPV()!=null){
				activitySelection = (ActivitySelectionContext)getTargetActivityPV().get(instance, "");
			}else{
				activitySelection = getTargetActivity();
			}
		}
		
		String actualTargetInstanceId = "";
		String actaulTargetTracingTag = "";
		try{
			actualTargetInstanceId = evaluateContent(instance, activitySelection.getInstanceId()).toString();
			actaulTargetTracingTag = evaluateContent(instance, activitySelection.getTracingTag()).toString();
		}catch (Exception e) {
			throw new UEngineException("BackActivity tried to back to unpassed activity or '"+getFlag()+"' doesn't exist.",e);
		}
		
		HashMap options = new HashMap();
		options.put("ptc", instance.getProcessTransactionContext());
		ProcessInstance targetProcessInstance = ProcessInstance.create().getInstance(actualTargetInstanceId, options);
		ProcessDefinition targetProcessDefinition = targetProcessInstance.getProcessDefinition();
		Activity targetActivity = targetProcessDefinition.getActivity(actaulTargetTracingTag);
		/*java.util.List compensatedActivityList = */targetActivity.compensateToThis(targetProcessInstance);
		
		if(getStatus(instance).equals(Activity.STATUS_READY) || getStatus(instance).equals(Activity.STATUS_CANCELLED))
			return; //that means this back activity is rolled back as well, in that case, the flow containning back activity should be terminated.  
		
/*		for(int i=0; i<compensatedActivityList.size(); i++){
			
			final Activity theActivity = (Activity)compensatedActivityList.get(i);
			ActivityForLoop containsCheck = new ActivityForLoop(){
				public void logic(Activity activity) {
					if(activity == BackActivity.this){
						stop("found!");
					}					
				}				
			};
			
			containsCheck.run(theActivity);
			if(containsCheck.getReturnValue()!=null){
				return;
			}
		}
*/		
		fireComplete(instance);
	}
	
	ActivitySelectionContext targetActivity;
		public ActivitySelectionContext getTargetActivity() {
			return targetActivity;
		}
		public void setTargetActivity(ActivitySelectionContext targetActivity) {
			this.targetActivity = targetActivity;
		}
	
	ProcessVariable targetActivityPV;
		public ProcessVariable getTargetActivityPV() {
			return targetActivityPV;
		}
		public void setTargetActivityPV(ProcessVariable targetActivityPV) {
			this.targetActivityPV = targetActivityPV;
		}
		
	String flag;

		public String getFlag() {
			return flag;
		}
	
		public void setFlag(String flag) {
			this.flag = flag;
		}
	
	int targetSource;

		public int getTargetSource() {
			return targetSource;
		}
	
		public void setTargetSource(int targetSource) {
			this.targetSource = targetSource;
		}
}