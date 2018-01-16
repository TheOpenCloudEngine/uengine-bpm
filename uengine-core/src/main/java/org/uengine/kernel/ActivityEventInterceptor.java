package org.uengine.kernel;

import java.io.Serializable;

public interface ActivityEventInterceptor extends Serializable {
	public boolean interceptEvent(Activity activity, String command, ProcessInstance instance, Object payload) throws Exception;
}
