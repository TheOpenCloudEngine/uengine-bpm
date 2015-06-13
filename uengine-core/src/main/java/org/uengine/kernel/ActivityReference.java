package org.uengine.kernel;

import java.io.Serializable;

public class ActivityReference implements Serializable{

	private static final long serialVersionUID = GlobalContext.SERIALIZATION_UID;

	Activity activity;	
	String absoluteTracingTag;
	
	public ActivityReference(){
	}
	
	public Activity getActivity() {
		return activity;
	}
	public void setActivity(Activity activity) {
		this.activity = activity;
	}

	public String getAbsoluteTracingTag() {
		return absoluteTracingTag;
	}

	public void setAbsoluteTracingTag(String absoluteTracingTag) {
		this.absoluteTracingTag = absoluteTracingTag;
	}

}
