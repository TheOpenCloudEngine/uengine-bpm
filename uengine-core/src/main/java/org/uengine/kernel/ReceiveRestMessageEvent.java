package org.uengine.kernel;

public class ReceiveRestMessageEvent extends ReceiveMessageEvent {
	
	String url;
		public String getUrl() {
			return url;
		}
		public void setUrl(String url) {
			this.url = url;
		}
}
