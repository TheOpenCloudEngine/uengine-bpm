/*
 * Created on 2004. 12. 28.
 */
package org.uengine.kernel;

import java.util.Map;


/**
 * @author Jinyoung Jang
 */
public class DirectRoleResolutionContext extends RoleResolutionContext {

	private static final long serialVersionUID = org.uengine.kernel.GlobalContext.SERIALIZATION_UID;

	/* (non-Javadoc)
	 * @see org.uengine.kernel.RoleResolutionContext#getActualMapping(org.uengine.kernel.ProcessDefinition, org.uengine.kernel.ProcessInstance, java.lang.String, java.util.Map)
	 */
	public RoleMapping getActualMapping(
			ProcessDefinition pd,
			ProcessInstance instance,
			String tracingTag,
			Map options
	) throws Exception {
		// TODO Auto-generated method stub
		
		RoleMapping rm = RoleMapping.create();
		rm.setEndpoint(getEndpoint());
		rm.setResourceName(getResourceName());
		rm.fill(instance);
		
		return rm;
	}

	/* (non-Javadoc)
	 * @see org.uengine.kernel.RoleResolutionContext#getDisplayName()
	 */
	public String getDisplayName() {
		// TODO Auto-generated method stub
		return getResourceName();
	}
	
	String endpoint;
		public String getEndpoint() {
			return endpoint;
		}
		public void setEndpoint(String string) {
			endpoint = string;
		}

	String resourceName;
		public String getResourceName() {
			return resourceName;
		}
		public void setResourceName(String resourceName) {
			this.resourceName = resourceName;
		}
		
	public String getName() {
		return "Direct Mapping";
	}

}
