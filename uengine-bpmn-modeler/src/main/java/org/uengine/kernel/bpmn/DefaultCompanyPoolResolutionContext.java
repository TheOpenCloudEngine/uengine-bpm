package org.uengine.kernel.bpmn;

import org.metaworks.ContextAware;
import org.metaworks.MetaworksContext;
import org.metaworks.annotation.ServiceMethod;
import org.uengine.kernel.GlobalContext;
import org.uengine.kernel.view.DynamicDrawGeom;
import org.uengine.webservice.JaxRSWebServiceConnector;

public class DefaultCompanyPoolResolutionContext extends PoolResolutionContext implements ContextAware {

	private static final long serialVersionUID = GlobalContext.SERIALIZATION_UID;

	transient MetaworksContext metaworksContext;
		public MetaworksContext getMetaworksContext() {
			return metaworksContext;
		}
		public void setMetaworksContext(MetaworksContext metaworksContext) {
			this.metaworksContext = metaworksContext;
		}
		
	public DefaultCompanyPoolResolutionContext(){
		this.setMetaworksContext(new MetaworksContext());
		this.getMetaworksContext().setWhen(MetaworksContext.WHEN_EDIT);
		
		setWebServiceConnector(new JaxRSWebServiceConnector());
	}
	
	String linkedId;
		public String getLinkedId() {
			return linkedId;
		}
		public void setLinkedId(String linkedId) {
			this.linkedId = linkedId;
		}

	String displayName;
		public String getDisplayName() {
			return displayName;
		}
		public void setDisplayName(String displayName) {
			this.displayName = displayName;
		}
	String webserviceUrl;
		public String getWebserviceUrl() {
			return webserviceUrl;
		}
		public void setWebserviceUrl(String webserviceUrl) {
			this.webserviceUrl = webserviceUrl;
		}

	
	@ServiceMethod(callByContent=true)
	public void refresh() throws Exception{
		getWebServiceConnector().setLinkedId(this.getLinkedId());
		getWebServiceConnector().setWebserviceUrl(this.getWebserviceUrl());
		getWebServiceConnector().load();
		
		this.setDisplayName(getWebServiceConnector().getWebServiceName());
		
	}	
	
	public DynamicDrawGeom drawActivitysOnDesigner() throws Exception{
		return this.getWebServiceConnector().drawActivitysOnDesigner();
	}
}
