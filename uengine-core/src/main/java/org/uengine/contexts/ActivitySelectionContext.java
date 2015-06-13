package org.uengine.contexts;

import java.io.Serializable;

import org.uengine.kernel.GlobalContext;

public class ActivitySelectionContext implements Serializable{

	private static final long serialVersionUID = GlobalContext.SERIALIZATION_UID;

	String instanceId;
		public String getInstanceId() {
			return instanceId;
		}
		public void setInstanceId(String targetInstanceId) {
			this.instanceId = targetInstanceId;
		}

	String tracingTag;
		public String getTracingTag() {
			return tracingTag;
		}
		public void setTracingTag(String targetTracingTag) {
			this.tracingTag = targetTracingTag;
		}

}
