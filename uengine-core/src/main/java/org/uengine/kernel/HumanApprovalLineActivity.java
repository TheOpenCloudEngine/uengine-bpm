package org.uengine.kernel;

import java.util.List;

import org.metaworks.FieldDescriptor;
import org.metaworks.Type;
import org.metaworks.inputter.RadioInput;
import org.uengine.util.ActivityForLoop;

/**
 * TODO Insert type comment for FormApprovalLineActivity.
 * 
 * @author <a href="mailto:bigmahler@users.sourceforge.net">Jong-Uk Jeong</a>
 * @version $Id: HumanApprovalLineActivity.java,v 1.1 2012/02/13 05:29:12 sleepphoenix4 Exp $
 * @version $Revision: 1.1 $
 */
public class HumanApprovalLineActivity extends ScopeActivity {
	private static final long serialVersionUID = org.uengine.kernel.GlobalContext.SERIALIZATION_UID;
	
	public static final int LOOPINGOPTION_AUTO = 0;
	public static final int LOOPINGOPTION_REPEATONREJECT = 1;
	public static final int LOOPINGOPTION_FINISHONREJECT = 2;

	public final static String KEY_APPR_LINE_STATUS = "KEY_APPR_LINE_STATUS";

	public static void metaworksCallback_changeMetadata(Type type){
		FieldDescriptor fd = type.getFieldDescriptor("ReferencerRole");

		fd = type.getFieldDescriptor("ReceiverRole");
	
		//TODO loopingOption
		fd = type.getFieldDescriptor("LoopingOption");
		fd.setInputter(new RadioInput(new String[]{"Auto","Loop","Finish"}, new Integer[]{HumanApprovalLineActivity.LOOPINGOPTION_AUTO, HumanApprovalLineActivity.LOOPINGOPTION_REPEATONREJECT, HumanApprovalLineActivity.LOOPINGOPTION_FINISHONREJECT}));
		//fd.setDisplayName("");
		
	}
	
	public HumanApprovalLineActivity(){
		setName("");
		setLoopingOption(LOOPINGOPTION_FINISHONREJECT);
		HumanApprovalActivity draftActivity = new HumanApprovalActivity();
		draftActivity.setName(GlobalContext.getLocalizedMessage("activitytypes.org.uengine.kernel.HumanApprovalActivity.draft.message", "Draft"));
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
		if(getStatusOutPV()!=null){
			getStatusOutPV().set(instance, "", status);
		}
	}
	
	public String getApprovalLineStatus(ProcessInstance instance) throws Exception{
		return (String)instance.getProperty(getTracingTag(), KEY_APPR_LINE_STATUS);
	}
	
	public void resetApprovalLine(){
		
		List appActList = getChildActivities();
		String draftActivityTT = getDraftActivity().getTracingTag();

		for(int i=0 ; i<appActList.size();i++){
			Activity act  = (Activity)appActList.get(i);
			if(!act.getTracingTag().equals(draftActivityTT)){
				removeChildActivity(act);
			}
		}
	}
	
	public void addApprovalActivity(ProcessInstance instance, String approveType, String activityName, String approverEndpoint) throws Exception{
		
		HumanApprovalActivity humanApprovalActivity = (HumanApprovalActivity) getDraftActivity().clone();
		ComplexActivity parent = (HumanApprovalLineActivity) humanApprovalActivity.getParentActivity();
		RoleMapping approver = RoleMapping.create();
		approver.setEndpoint(approverEndpoint);
		approver.fill(instance);
		
		humanApprovalActivity.setName(activityName);
		humanApprovalActivity.setTracingTag(null);
		humanApprovalActivity.setRole(null);
		humanApprovalActivity.setViewMode(true);
		humanApprovalActivity.setArbitraryApprovable(false);
		humanApprovalActivity.setNotificationWorkitem(false);
		
		if(HumanApprovalActivity.APPROVALTYPE_ARBITRARY_APPROVAL.equals(approveType)){			
			humanApprovalActivity.setArbitraryApprovable(true);
		}else if(HumanApprovalActivity.APPROVALTYPE_POST_APPROVAL.equals(approveType)){
			humanApprovalActivity.setNotificationWorkitem(true);
		}else if(HumanApprovalActivity.APPROVALTYPE_CONSENT.equals(approveType)){
			AllActivity allAct = new AllActivity();
			parent.addChildActivity(allAct);
			parent = allAct;
		}
		
		if(humanApprovalActivity.getParameters()!=null){
			for(int j=0; j<humanApprovalActivity.getParameters().length; j++){
				humanApprovalActivity.getParameters()[j].setDirection(ParameterContext.DIRECTION_IN);
			}					
		}
		
		humanApprovalActivity.setApprover(instance, approver);
		
		addChildActivity(humanApprovalActivity);
	}
	
	HumanApprovalActivity draftActivity;
	public HumanApprovalActivity getDraftActivity(){
		if(draftActivity!=null)
			return draftActivity;
		
		ActivityForLoop findingLoop = new ActivityForLoop(){
			public void logic(Activity activity){
				if(activity instanceof HumanApprovalActivity){
					stop(activity);
				}
			}
		};
		
		findingLoop.run(this);		
		this.draftActivity = (HumanApprovalActivity)findingLoop.getReturnValue(); 
		
		return draftActivity;
	}
	
	//referencer (11.23)
	protected void afterExecute(ProcessInstance instance) throws Exception {
		super.afterExecute(instance);
	
	}
	
	//receiver (11.23)
	protected void afterComplete(ProcessInstance instance) throws Exception {
		
		boolean isSimulate =  false;
		
		super.afterComplete(instance);
		if(getReceiverRole()!=null &&!isSimulate){
			RoleMapping receivers = getReceiverRole().getMapping(instance);
			if(receivers!=null){
				receivers.setDispatchingOption(Role.DISPATCHINGOPTION_RECEIVE);
				instance.putRoleMapping("receiver_" + getTracingTag(), receivers);
			}
		}
	}
	
	protected void onEvent(String command, ProcessInstance instance, Object payload) throws Exception{
		//review: performance: need to use 'Hashtable' to locate the command or directly invocation from fire... methods.
		boolean isSimulate =  false;
		
		if (command.equals(CHILD_DONE) && getDraftActivity().equals(payload) && !isSimulate) {
			if (getReferencerRole() != null) {
				List<Activity> childActs = this.getChildActivities();
				for (int i=0; i<childActs.size(); i++) {
					if (childActs.get(i) instanceof HumanActivity) {
						HumanActivity humanAct = ((HumanActivity)childActs.get(i));
						humanAct.setReferenceRole(getReferencerRole());
					}/* else if (childActs.get(i) instanceof ComplexActivity) {
						ComplexActivity complexActivity = (ComplexActivity) childActs.get(i);
						complexActivity.onEvent(command, instance, payload);
					}*/
				}
//				RoleMapping referencers = getReferencerRole().getMapping(instance);
//				if (referencers != null) {
//					referencers.setDispatchingOption(Role.DISPATCHINGOPTION_REFERENCE);
//					instance.putRoleMapping("referencer_" + getTracingTag(),referencers);
//				}
			}
		}
	
		super.onEvent(command, instance, payload);
	}
	
	Role referencerRole;
		public Role getReferencerRole() {
			return referencerRole;
		}
		public void setReferencerRole(Role referencerRole) {
			this.referencerRole = referencerRole;
		}

	Role receiverRole;
		public Role getReceiverRole() {
			return receiverRole;
		}
		public void setReceiverRole(Role receiverRole) {
			this.receiverRole = receiverRole;
		}

		
	int loopingOption;
		public int getLoopingOption() {
			return loopingOption;
		}
		public void setLoopingOption(int loopingOption) {
			this.loopingOption = loopingOption;
		}
	
	ProcessVariable statusOutPV;

		public ProcessVariable getStatusOutPV() {
			return statusOutPV;
		}
	
		public void setStatusOutPV(ProcessVariable statusOut) {
			this.statusOutPV = statusOut;
		}
}
