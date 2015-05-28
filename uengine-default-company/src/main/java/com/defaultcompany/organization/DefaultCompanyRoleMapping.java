package com.defaultcompany.organization;

import org.uengine.kernel.GlobalContext;
import org.uengine.kernel.ProcessInstance;
import org.uengine.kernel.RoleMapping;

public class DefaultCompanyRoleMapping extends RoleMapping{
	
	private static final long serialVersionUID = GlobalContext.SERIALIZATION_UID;
	
	final static String EXT_PROP_KEY_NateOnMessengerId = "EXT_PROP_KEY_NATEON_ID";

	public void fill(ProcessInstance instance) throws Exception {
		if(GlobalContext.isDesignTime()) return;

	}
	
	public static void main(String args[]) throws Exception{
		RoleMapping rm = new DefaultCompanyRoleMapping();
		rm.fill(null);
	}
}