package org.uengine.kernel;

import java.util.*;

import org.uengine.kernel.GlobalContext;


/**
 * @author Kinam Jung, Jinyoung Jang
 */

public class RoleExist extends Condition{
	private static final long serialVersionUID = org.uengine.kernel.GlobalContext.SERIALIZATION_UID;

	String roleName;
		public String getRoleName() {
			return roleName;
		}
		public void setRoleName(String roleName) {
			this.roleName = roleName;
		}
	
	Role role;
		public Role getRole() {
			return role;
		}	
		public void setRole(Role role) {
			this.role = role;
		}

	public RoleExist(String roleName){
		setRoleName(roleName);
	}
	
	public RoleExist(String roleName, String description){
		setRoleName(roleName);
		getDescription().setText(description);
	}

	public RoleExist(Role role){
		setRole(role);
	}

	public RoleExist(Role role, String description){
		setRole(role);
		getDescription().setText(description);
	}

	public boolean isMet(ProcessInstance instance, String scope) throws Exception{
		Role actualRole = getRole();
		if(actualRole==null && getRoleName()!=null){
			actualRole = instance.getProcessDefinition().getRole(getRoleName());
			
			if(actualRole == null)
				throw new UEngineException("RoleExist condition:: can't find role named as [" + getRoleName() + "]");
		}
		
		if(actualRole == null)
			throw new UEngineException("RoleExist condition:: Role is not set");
		
		RoleMapping rm = actualRole.getMapping(instance);
		
		if(rm==null || rm.size() == 0)
			return false;
		
		return true;		
	}
	
	
	public String toString(){
		String desc = getDescription().getText();
		
		if(desc==null){
			String strRoleName = (getRole() != null ? getRole().toString() : getRoleName());
			
			if(strRoleName!=null)
				return strRoleName + " " + GlobalContext.getLocalizedMessage("roleexist.exists.displayname", "exists");
			else
				return "<Role> exists";
		}
		else
			return desc; 
	}

	public ValidationContext validate(Map options){
		ValidationContext validationContext = super.validate(options);
		
		Activity activity = (Activity)options.get("activity");
				 
		Role shouldBeExist = activity.getProcessDefinition().getRole(getRole() != null ? getRole().getName() : getRoleName());
		
		if(shouldBeExist == null)
			validationContext.addWarning(activity.getActivityLabel()+" unrecognized role name '" + getRoleName() + "' is used in a condition expression" );

		return validationContext;		
	}

}