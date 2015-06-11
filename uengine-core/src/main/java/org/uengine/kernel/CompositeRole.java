package org.uengine.kernel;

import org.uengine.contexts.TextContext;

public class CompositeRole extends Role {
	private static final long serialVersionUID = org.uengine.kernel.GlobalContext.SERIALIZATION_UID;
	
	Role[] roles;

	public RoleMapping getMapping(ProcessInstance inst, String tracingTag) throws Exception {
		
		RoleMapping rm = null;
		
		if(inst.getRoleMapping(getName()) != null){
			rm = inst.getRoleMapping(getName());
		}else{
			if(roles.length > 0)
				for(int i=0; i<roles.length; i++){					
					Role role = (Role) roles[i].clone();
					role.setName(getName());
					if(roles[i].getRoleResolutionContext() != null)
						role.setRoleResolutionContext(roles[i].getRoleResolutionContext());
					
					RoleMapping theMapping = role.getRoleResolutionContext().getActualMapping(inst.getProcessDefinition(), inst, tracingTag, new java.util.Hashtable());
					
//					RoleMapping theMapping = role.getMapping(inst, tracingTag);	
					if(theMapping==null) continue;
					
					if(rm==null){
						rm = (RoleMapping)theMapping.clone();
					}else{			
						rm.moveToAdd();
			
						if(theMapping.size() >1)
							rm.getMultipleMappings().addAll(theMapping.getMultipleMappings());
						else{
							//rm.moveToAdd();
							rm.replaceCurrentRoleMapping(theMapping);
						}
					}
				}
		}		
		inst.putRoleMapping(getName(), rm);
		
		if(rm!=null)
			rm.beforeFirst();
		
		return rm;
	}

	public Role[] getRoles() {
		return roles;
	}

	public void setRoles(Role[] roles) {
		this.roles = roles;
	}
	
	public TextContext getDisplayName() {
		if( roles == null ){
			return super.getDisplayName();
		}
		
		StringBuffer displayName = new StringBuffer();
		for (Role role : roles) {
			if (displayName.length() > 0) displayName.append("+");
			displayName.append(role.getDisplayName());
		}
		
		TextContext dispTextContext = TextContext.createInstance();
		dispTextContext.setText(displayName.toString());
		
		return dispTextContext;
	}

	public String getName() {
		if( roles == null ){
			return super.getName();
		}
		
		StringBuffer roleName = new StringBuffer();
		for (Role role : roles) {
			if (roleName.length() > 0) roleName.append("+");
			roleName.append(role.getName());
		}
		
		return roleName.toString();
	}

	public boolean containsMapping(ProcessInstance instance, RoleMapping testingRoleMapping) throws Exception {
		
		for(int i=0; i<roles.length; i++){
			if(roles[i].containsMapping(instance, testingRoleMapping)) return true;	
		}
		
		return false;
	}
}

