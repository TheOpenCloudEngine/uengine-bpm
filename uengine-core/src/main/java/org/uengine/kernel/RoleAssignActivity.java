package org.uengine.kernel;

import org.uengine.kernel.GlobalContext;


/**
 * @author Jinyoung Jang
 */

public class RoleAssignActivity extends DefaultActivity{
	private static final long serialVersionUID = org.uengine.kernel.GlobalContext.SERIALIZATION_UID;

	Role role;
		public Role getRole() {
			return role;
		}
		public void setRole(Role value) {
			role = value;
		}

	ProcessVariable variableForURI;
		public ProcessVariable getVariableForURI() {
			return variableForURI;
		}
		public void setVariableForURI(ProcessVariable value) {
			variableForURI = value;
		}

	public RoleAssignActivity(){
		super("role assign");
	}
	
	protected void executeActivity(ProcessInstance instance) throws Exception{
		
		String resourceURI = getVariableForURI().get(instance, "").toString();
		
		RoleMapping roleMapping = RoleMapping.create();
			roleMapping.setEndpoint(resourceURI);
			roleMapping.setName(getRole().getName());
			
		roleMapping.fill(instance);
			
		instance.putRoleMapping(roleMapping);
		
		fireComplete(instance);
	}
	
}