package org.uengine.kernel;

import java.util.List;
import java.util.Map;

import org.uengine.contexts.TextContext;

/**
 * TODO Insert type comment for FormApprovalActivity.
 * 
 * @author <a href="mailto:bigmahler@users.sourceforge.net">Jong-Uk Jeong</a>
 * @version $Id: HumanApprovalActivity.java,v 1.1 2012/02/13 05:29:12 sleepphoenix4 Exp $
 * @version $Revision: 1.1 $
 */
public class HumanApprovalActivity extends HumanActivity {
	
	private static final long serialVersionUID = GlobalContext.SERIALIZATION_UID;
	
	public final static String KEY_APPR_STATUS = "KEY_APPR_STATUS";
	public final static String SIGN_DRAFT = "SIGN_DRAFT";
	public final static String SIGN_APPROVED = "SIGN_APPROVED";
	public final static String SIGN_ARBITRARY_APPROVED = "SIGN_ARBITRARY_APPROVED";
	public final static String SIGN_REJECT = "SIGN_REJECT";
	
	public final static String APPROVALTYPE_POST_APPROVAL = "POST_APPROVAL";
	public final static String APPROVALTYPE_ARBITRARY_APPROVAL = "ARBITRARY_APPROVAL";
	public final static String APPROVALTYPE_APPROVAL = "APPROVAL";
	public final static String APPROVALTYPE_CONSENT = "CONSENT";
	
	public HumanApprovalActivity(){
		setName("HumanApproval");
		setTool("approvalHandlers/humanApprovalHandler");
	}
	
	public String getTool() {
		return super.getTool();
    }

	public Map createParameter(ProcessInstance instance) throws Exception {
		// TODO Auto-generated method stub
		Map parameterMap = super.createParameter(instance);
		String trcTag="";
		Activity parent = getParentActivity();
		
		while(true){
			if(parent instanceof HumanApprovalLineActivity ){
				trcTag = parent.getTracingTag();
				break;
			}else{
				parent = parent.getParentActivity();
				if(parent == null) break;
			}
		}

		
		parameterMap.put("approvalLineActivityTT",trcTag );
				
		return parameterMap;
	}

	public RoleMapping getActualMapping(ProcessInstance instance) throws Exception {
		// TODO Auto-generated method stub
		if(getApprover(instance)==null) return super.getActualMapping(instance);
		
		return getApprover(instance);
	}
	
	protected String createApproverRoleName(){
		if(role!=null) return role.getName();
		return "approver_"+getApprovalLineActivity().getTracingTag()+"_"+getTracingTag();
	}
	
	RoleMapping approver;
		public RoleMapping getApprover(ProcessInstance instance) {
			RoleMapping rm;
			try {
				rm = instance.getRoleMapping(createApproverRoleName());
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
			
			if(rm != null)
				return rm;
			else
				return approver;
		}
		public void setApprover(ProcessInstance instance, RoleMapping approver) {
			this.approver = approver;
			try {
				instance.putRoleMapping(createApproverRoleName(), approver);
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}

	public Role getRole() {
		return approver==null ? super.getRole() : new Role(){
			
			public TextContext getDisplayName() {
				TextContext tc = TextContext.createInstance();
				
				tc.setText(createApproverRoleName());
				return tc;
			}

			public RoleMapping getMapping(ProcessInstance inst, String tracingTag) throws Exception {
				
				return getApprover(inst);
			}
			
		};
	}
	boolean isViewMode=false;
		public boolean isViewMode() {
			return isViewMode;
		}
	
		public void setViewMode(boolean isViewMode) {
			this.isViewMode = isViewMode;
		}

	public void arbitraryApprove(ProcessInstance instance) throws Exception{
		setApprovalStatus(instance, SIGN_ARBITRARY_APPROVED);
		
		String taskId = getTaskIds(instance)[0];		
		KeyedParameter[] parameters = new KeyedParameter[]{};		
		instance.getWorkList().completeWorkItem(taskId, parameters, instance.getProcessTransactionContext());
		setEndTime(instance, GlobalContext.getNow(instance.getProcessTransactionContext()));
		
		getApprovalLineActivity().fireComplete(instance);
		
		EventMessagePayload emp = new EventMessagePayload();
		emp.setTriggerTracingTag(getTracingTag());
		getApprovalLineActivity().fireEventHandlers(instance, EventHandler.TRIGGERING_BY_ARBITRARYFINISHED, emp);
	}
	
	public void rejectApprove(ProcessInstance instance) throws Exception{
		setApprovalStatus(instance, SIGN_REJECT);
		
		int loopingOption = getApprovalLineActivity().getLoopingOption();
		
		String taskId = getTaskIds(instance)[0];		
		KeyedParameter[] parameters = new KeyedParameter[]{};		
		instance.getWorkList().completeWorkItem(taskId, parameters, instance.getProcessTransactionContext());

		if(loopingOption == HumanApprovalLineActivity.LOOPINGOPTION_AUTO || loopingOption == HumanApprovalLineActivity.LOOPINGOPTION_REPEATONREJECT){
			getApprovalLineActivity().getDraftActivity().compensateToThis(instance);
		}else {
			setEndTime(instance, GlobalContext.getNow(instance.getProcessTransactionContext()));
			
//			동의시 상태 관리를 위하여 추가된 소스
			if (this.getParentActivity().getClass() == AllActivity.class) {
				List<Activity> faActs = ((AllActivity)this.getParentActivity()).getChildActivities();
				for (Activity faAct : faActs) {
					if( faAct instanceof HumanApprovalActivity){
						if (Activity.STATUS_RUNNING.equals(faAct.getStatus(instance)) && !this.getTracingTag().equals(faAct.getTracingTag())) {
							((HumanApprovalActivity)faAct).cancelWorkItem(instance, Activity.STATUS_CANCELLED);
							((HumanApprovalActivity)faAct).setStatus(instance, Activity.STATUS_CANCELLED);
						}
					}
				}
				this.getParentActivity().fireComplete(instance);
			} else {
				getApprovalLineActivity().fireComplete(instance);
			}
			
//			원본 소스
//			getApprovalLineActivity().fireComplete(instance);			
		}

		EventMessagePayload emp = new EventMessagePayload();
		emp.setTriggerTracingTag(getTracingTag());
		getApprovalLineActivity().fireEventHandlers(instance, EventHandler.TRIGGERING_BY_REJECTED, emp);
	}
	
	public void setApprovalStatus(ProcessInstance instance, String status) throws Exception{
		instance.setProperty(getTracingTag(), KEY_APPR_STATUS, status);
		getApprovalLineActivity().setApprovalLineStatus(instance, status);
	}
	
	public String getApprovalStatus(ProcessInstance instance) throws Exception{
		if(instance==null) return null;
		
		return (String)instance.getProperty(getTracingTag(), KEY_APPR_STATUS);
	}
	
	HumanApprovalLineActivity humanApprovalLineActivity;
	public HumanApprovalLineActivity getApprovalLineActivity() {
		if (humanApprovalLineActivity != null) {
			//return humanApprovalLineActivity;
		}

		Activity tracing = this;

		do {
			if (HumanApprovalLineActivity.class.isAssignableFrom(tracing.getClass())) {
				humanApprovalLineActivity = (HumanApprovalLineActivity) tracing;

				return humanApprovalLineActivity;
			}

			tracing = tracing.getParentActivity();
		} while (tracing != null);

		return null;
	}

	protected void afterComplete(ProcessInstance instance) throws Exception {
		//is Draft
		if(this == getApprovalLineActivity().getDraftActivity()){
			setApprovalStatus(instance, SIGN_DRAFT);
		//is approve
		}else{
			if(!isNotificationWorkitem())
				setApprovalStatus(instance, SIGN_APPROVED);
		}
		super.afterComplete(instance);
		
		EventMessagePayload emp = new EventMessagePayload();
		emp.setTriggerTracingTag(getTracingTag());

		if(this == getApprovalLineActivity().getDraftActivity()){
			getApprovalLineActivity().fireEventHandlers(instance, EventHandler.TRIGGERING_BY_DRAFTED, emp);
		}else{
			getApprovalLineActivity().fireEventHandlers(instance, EventHandler.TRIGGERING_BY_APPROVED, emp);
		}
	}

	boolean isArbitraryApprovable;
		public boolean isArbitraryApprovable() {
			return isArbitraryApprovable;
		}
		public void setArbitraryApprovable(boolean isArbitraryApprovable) {
			this.isArbitraryApprovable = isArbitraryApprovable;
		}

}
