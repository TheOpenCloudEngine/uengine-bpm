package org.uengine.kernel;

public interface ActivityEventInterceptor {
	public boolean interceptEvent(Activity activity, String command, ProcessInstance instance, Object payload) throws Exception;
}
