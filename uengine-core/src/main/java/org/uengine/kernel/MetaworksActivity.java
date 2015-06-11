package org.uengine.kernel;

import org.uengine.kernel.GlobalContext;

/**
 * @author Jinyoung Jang
 */
public class MetaworksActivity extends HumanActivity{
	private static final long serialVersionUID = org.uengine.kernel.GlobalContext.SERIALIZATION_UID;
	public MetaworksActivity(){
		super();
		setName("metaworks");
	}
		
	public String getTool(){
		return "applicationHandler?" + (getApplicationName()!=null ? "defaultApplication=" + getApplicationName() : "");
	}
	public void setTool(String toolName){
		// ignore
	}
	
	String applicationName;
		public String getApplicationName() {
			return applicationName;
		}
		public void setApplicationName(String string) {
			applicationName = string;
		}

}
