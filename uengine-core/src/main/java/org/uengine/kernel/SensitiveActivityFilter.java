/*
 * Created on 2004. 10. 9.
 */
package org.uengine.kernel;


/**
 * @author Jinyoung Jang
 */
public interface SensitiveActivityFilter extends ActivityFilter{
	void onEvent(Activity activity, ProcessInstance instance, String eventName, Object payload) throws Exception;
}
