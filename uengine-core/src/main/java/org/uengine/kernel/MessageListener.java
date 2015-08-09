package org.uengine.kernel;

public interface MessageListener {
	public boolean onMessage(ProcessInstance instance, Object payload) throws Exception;
	public String getMessage();
	public String getTracingTag();

	public void beforeRegistered(ProcessInstance instance) throws Exception;
	public void afterRegistered(ProcessInstance instance) throws Exception;

	public void afterUnregistered(ProcessInstance instance) throws Exception;
}