package org.uengine.webservice;

/**
 * This interface unifies the usage of web service stubs
 * @author Jinyoung Jang
 */

public interface ServiceProvider{

	public Object getStub(String endpoint) throws Exception;
	public Object invokeService(String endpoint, String operationName, Object [] parameters) throws Exception;
}