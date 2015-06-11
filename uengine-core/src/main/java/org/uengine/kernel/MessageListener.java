package org.uengine.kernel;

public interface MessageListener {
	public boolean onMessage(ProcessInstance instance, Object payload) throws Exception;
	public String getMessage();
	public String getTracingTag();
}