/*
 * Created on 2004. 12. 13.
 */
package org.uengine.kernel;

import java.awt.event.ActionEvent;


/**
 * @author Jinyoung Jang
 */
public class UEngineEvent extends ActionEvent {

	ProcessInstance instance;
	Object payload;

	public UEngineEvent(String command, ProcessInstance instance, Object payload){
		super(instance, 0, command);
		setInstance(instance);
		setPayload(payload);
	}
	
	public ProcessInstance getInstance() {
		return instance;
	}

	public Object getPayload() {
		return payload;
	}

	public void setInstance(ProcessInstance instance) {
		this.instance = instance;
	}

	public void setPayload(Object object) {
		payload = object;
	}

}
