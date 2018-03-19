package org.uengine.kernel.bpmn;

import org.metaworks.FieldDescriptor;
import org.metaworks.Type;
import org.metaworks.inputter.RadioInput;
import org.uengine.kernel.Activity;
import org.uengine.kernel.ProcessInstance;
import org.uengine.kernel.UEngineException;
import org.uengine.kernel.ValidationContext;
import org.uengine.persistence.processinstance.ProcessInstanceDAO;
import org.uengine.persistence.processinstance.ProcessInstanceDAOType;

import java.util.Hashtable;
import java.util.Map;

/**
 * @author Jinyoung Jang
 */

public class EndEvent extends StartEvent {
	private static final long serialVersionUID = org.uengine.kernel.GlobalContext.SERIALIZATION_UID;

	boolean stopAllTokens;
		public boolean isStopAllTokens() {
			return stopAllTokens;
		}
		public void setStopAllTokens(boolean stopAllTokens) {
			this.stopAllTokens = stopAllTokens;
		}

	public EndEvent() {
		// TODO: 확인해야함
		//super("terminate");
		setName("End");
	}

	public void executeActivity(ProcessInstance instance) throws Exception{

		// TODO Auto-generated method stub
		if (instance != null && instance.isSubProcess()) {
			instance.getProcessDefinition().returnToMainProcess(instance);
		}

		if(isStopAllTokens()){
			instance.getRootProcessInstance().stop();
		}

		instance.setInfo(getName());

		fireComplete(instance);
	}
	
}