package org.uengine.contexts;

import java.io.Serializable;

import org.uengine.kernel.GlobalContext;


public class SoundUrlContext implements Serializable {
	private static final long serialVersionUID = GlobalContext.SERIALIZATION_UID;
	
	private String url;
	private String charset;
	private boolean isPost;
	private KeyValueContext[] parameters;
	
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getCharset() {
		return charset;
	}
	public void setCharset(String charset) {
		this.charset = charset;
	}
	public boolean isPost() {
		return isPost;
	}
	public void setPost(boolean isPost) {
		this.isPost = isPost;
	}
	public KeyValueContext[] getParameters() {
		return parameters;
	}
	public void setParameters(KeyValueContext[] parameters) {
		this.parameters = parameters;
	}
}

