package org.uengine.kernel;

import java.util.Map;

import org.uengine.contexts.TextContext;

public class ApprovalActivity extends URLActivity {
	
	private static final long serialVersionUID = GlobalContext.SERIALIZATION_UID;
	
	public final static String KEY_APPR_STATUS = "KEY_APPR_STATUS";
	public final static String SIGN_DRAFT = "SIGN_DRAFT";
	public final static String SIGN_APPROVED = "SIGN_APPROVED";
	public final static String SIGN_ARBITRARY_APPROVED = "SIGN_ARBITRARY_APPROVED";
	public final static String SIGN_REJECT = "SIGN_REJECT";
	
	public ApprovalActivity(){
		setName("Approval Step");
		setTool("approvalHandler");
	}
	
	public String getTool() {
		return super.getTool();
    }

//	public TextContext getName() {
//		if(getApprovalLineActivity()!=null){
//			TextContext tc = TextContext.createInstance();
//			tc.setText(getApprovalLineActivity().getChildActivities().indexOf(this) < 0 ? "Draft" : "Approve");
//			
//			return tc;
//		}
//		
//		return super.getName();
//	}

	public Map createParameter(ProcessInstance instance) throws Exception {
		// TODO Auto-generated method stub
		Map parameterMap = super.createParameter(instance);
		
		parameterMap.put("approvalLineActivityTT", getParentActivity().getTracingTag());
		
		return parameterMap;
	}

	public RoleMapping getActualMapping(ProcessInstance instance) throws Exception {
		// TODO Auto-generated method stub
		if(getApprover()==null) return super.getActualMapping(instance);
		
		return getApprover();
	}
	
	RoleMapping approver;
		public RoleMapping getApprover() {
			return approver;
		}
		public void setApprover(RoleMapping approver) {
			this.approver = approver;
		}

	public Role getRole() {
		return getApprover()==null ? super.getRole() : new Role(){
			
			public TextContext getDisplayName() {
				TextContext tc = TextContext.createInstance();
				
				tc.setText("user");
				return tc;
			}

			public RoleMapping getMapping(ProcessInstance inst, String tracingTag) throws Exception {
				return getApprover();
			}
		};
	}
		
	public void arbitraryApprove(ProcessInstance instance) throws Exception{
		onComplete(instance, null);	
		setApprovalStatus(instance, SIGN_ARBITRARY_APPROVED);
		getApprovalLineActivity().fireComplete(instance);
	}
	
	public void rejectApprove(ProcessInstance instance) throws Exception{
		setApprovalStatus(instance, SIGN_REJECT);
		getApprovalLineActivity().getDraftActivity().compensateToThis(instance);
	}
	
	public void setApprovalStatus(ProcessInstance instance, String status) throws Exception{
		instance.setProperty(getTracingTag(), KEY_APPR_STATUS, status);
		getApprovalLineActivity().setApprovalLineStatus(instance, status);
	}
	
	public String getApprovalStatus(ProcessInstance instance) throws Exception{
		return (String)instance.getProperty(getTracingTag(), KEY_APPR_STATUS);
	}
	
	ApprovalLineActivity approvalLineActivity;
	public ApprovalLineActivity getApprovalLineActivity() {
		if (approvalLineActivity != null) {
			return approvalLineActivity;
		}

		Activity tracing = this;

		do {
			if (ApprovalLineActivity.class.isAssignableFrom(tracing.getClass())) {
				approvalLineActivity = (ApprovalLineActivity) tracing;

				return approvalLineActivity;
			}

			tracing = tracing.getParentActivity();
		} while (tracing != null);

		return null;
	}

	protected void afterComplete(ProcessInstance instance) throws Exception {
		setApprovalStatus(instance, SIGN_APPROVED);
		super.afterComplete(instance);
	}	
	
}
