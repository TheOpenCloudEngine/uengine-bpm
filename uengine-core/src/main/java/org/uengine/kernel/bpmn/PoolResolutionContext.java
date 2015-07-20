package org.uengine.kernel.bpmn;

import org.metaworks.annotation.ServiceMethod;
import org.uengine.kernel.view.DynamicDrawGeom;
import org.uengine.webservice.WebServiceConnector;

public abstract class PoolResolutionContext implements java.io.Serializable {

	private static final long serialVersionUID = org.uengine.kernel.GlobalContext.SERIALIZATION_UID;

	WebServiceConnector webServiceConnector;
	public WebServiceConnector getWebServiceConnector() {
		return webServiceConnector;
	}
	public void setWebServiceConnector(WebServiceConnector webServiceConnector) {
		this.webServiceConnector = webServiceConnector;
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
	
	@ServiceMethod(callByContent=true)
	public DynamicDrawGeom drawActivitysOnDesigner() throws Exception{
		return null;
	}
}
