package org.uengine.kernel.bpmn;

import org.metaworks.annotation.ServiceMethod;
//import org.uengine.kernel.designer.ui.DynamicDrawGeom;

public abstract class PoolResolutionContext implements java.io.Serializable {

	private static final long serialVersionUID = org.uengine.kernel.GlobalContext.SERIALIZATION_UID;
	
	public PoolResolutionContext(){
		
	}
	
	abstract public String getLinkedId();
	abstract public String getWebserviceUrl();
	abstract public String getDisplayName();
	
		
	@ServiceMethod(callByContent=true)
	public Object openPicker() throws Exception{
		return null;
	}
	@ServiceMethod(callByContent=true)
	public void refresh() throws Exception{
		
	}
	
//	@ServiceMethod(callByContent=true)
//	public DynamicDrawGeom drawActivitysOnDesigner() throws Exception{
//		return null;
//	}
}
