package org.uengine.webservice;

import org.metaworks.annotation.Hidden;

public class ApplyProperties {

	String id;
		@Hidden
		public String getId() {
			return id;
		}
		public void setId(String id) {
			this.id = id;
		}

	Object content;
		@Hidden
		public Object getContent() {
			return content;
		}
		public void setContent(Object content) {
			this.content = content;
		}
	
	String viewType;
		public String getViewType() {
			return viewType;
		}
		public void setViewType(String viewType) {
			this.viewType = viewType;
		}
	public ApplyProperties(String id, Object content){
		this.setId(id);
		this.setContent(content);
	}
}
