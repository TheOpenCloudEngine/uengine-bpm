package org.uengine.kernel;

import org.uengine.kernel.bpmn.Event;

public class ReceiveMessageEvent extends Event implements MessageListener {
	public ReceiveMessageEvent(){
		super();
		if( this.getName() == null ){
			setName(this.getClass().getSimpleName());
		}
	}
	@Override
	protected void executeActivity(ProcessInstance instance) throws Exception {
		//start listens...
	}

	public boolean onMessage(ProcessInstance instance, Object payload) throws Exception {
		fireComplete(instance);
		return true;
	}

	public String getMessage() {
		return this.getName();
	}
}
