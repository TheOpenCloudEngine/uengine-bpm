package org.uengine.queue.faultqueue;

/**
 * @author Jinyoung Jang
 */

import org.uengine.kernel.UEngineException;


public class FaultParameter implements java.io.Serializable{
	String instanceId;
	String tracingTag;

	/**
	 * 
	 * @uml.property name="fault"
	 * @uml.associationEnd 
	 * @uml.property name="fault" multiplicity="(1 1)"
	 */
	UEngineException fault;

	
	public FaultParameter(String instanceId, String tracingTag, UEngineException fault){
		setInstanceId(instanceId);
		setTracingTag(tracingTag);
		setFault(fault);
	}

	/**
	 * @return
	 * 
	 * @uml.property name="fault"
	 */
	public UEngineException getFault() {
		return fault;
	}

	/**
	 * @return
	 * 
	 * @uml.property name="instanceId"
	 */
	public String getInstanceId() {
		return instanceId;
	}

	/**
	 * @return
	 * 
	 * @uml.property name="tracingTag"
	 */
	public String getTracingTag() {
		return tracingTag;
	}

	/**
	 * @param exception
	 * 
	 * @uml.property name="fault"
	 */
	public void setFault(UEngineException exception) {
		fault = exception;
	}

	/**
	 * @param string
	 * 
	 * @uml.property name="instanceId"
	 */
	public void setInstanceId(String string) {
		instanceId = string;
	}

	/**
	 * @param string
	 * 
	 * @uml.property name="tracingTag"
	 */
	public void setTracingTag(String string) {
		tracingTag = string;
	}

}
