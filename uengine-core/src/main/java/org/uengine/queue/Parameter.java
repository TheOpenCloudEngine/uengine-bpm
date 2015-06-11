package org.uengine.queue;

/**
 * @author Jinyoung Jang
 */

public class Parameter implements java.io.Serializable{

	String endpoint;

	/**
	 * 
	 * @uml.property name="endpoint"
	 */
	public String getEndpoint() {
		return endpoint;
	}

	/**
	 * 
	 * @uml.property name="endpoint"
	 */
	public void setEndpoint(String value) {
		endpoint = value;
	}

	
	Object [] parameters;

	/**
	 * 
	 * @uml.property name="parameters"
	 */
	public Object[] getParameters() {
		return parameters;
	}

	/**
	 * 
	 * @uml.property name="parameters"
	 */
	public void setParameters(Object[] value) {
		parameters = value;
	}

	
}