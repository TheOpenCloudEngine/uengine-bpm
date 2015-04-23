package org.uengine.kernel;


public class ReferenceBlockActivity extends ComplexActivity{

	private static final long serialVersionUID = GlobalContext.SERIALIZATION_UID;
	
	Role referencerRole;

		public Role getReferencerRole() {
			return referencerRole;
		}
	
		public void setReferencerRole(Role referencerRole) {
			this.referencerRole = referencerRole;
		}

	protected void queueActivity(Activity act, ProcessInstance instance) throws Exception {

		if(act instanceof HumanActivity){
			HumanActivity humanActivity = (HumanActivity)act;
			//humanActivity.get
		}
		
		super.queueActivity(act, instance);
	}

	protected void afterComplete(ProcessInstance instance) throws Exception {
		super.afterComplete(instance);
		
		//instance.putRoleMapping("referencer_" + getTracingTag(), (RoleMapping)null);
	}

	protected void afterExecute(ProcessInstance instance) throws Exception {
		super.afterExecute(instance);

		RoleMapping referencers = getReferencerRole().getMapping(instance);
		
		instance.putRoleMapping("referencer_" + getTracingTag(), referencers);
	}

	
}
