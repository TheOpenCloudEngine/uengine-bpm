/*
 * Created on 2004. 10. 9.
 */
package org.uengine.kernel;


/**
 * @author Jinyoung Jang
 */
public interface ActivityFilter extends java.io.Serializable{
	//run-time
	void beforeExecute(Activity activity, ProcessInstance instance) throws Exception;
	void afterExecute(Activity activity, ProcessInstance instance) throws Exception;
	void afterComplete(Activity activity, ProcessInstance instance) throws Exception;
	void onPropertyChange(Activity activity, ProcessInstance instance, String propertyName, Object changedValue) throws Exception;
	
	//deploy time
	void onDeploy(ProcessDefinition definition) throws Exception;
}
