package org.uengine.kernel.bpmn;


import org.uengine.kernel.ProcessInstance;
import org.uengine.kernel.bpmn.Gateway;

public class ExclusiveGateway extends Gateway {
	
	public ExclusiveGateway() {
		//super("Xor");
	}

	@Override
	protected boolean isCompletedAllOfPreviousActivities(ProcessInstance instance) throws Exception {
		//XOR Gateway case,
		//complete this activity, when one previous activity is completed
		return true;
	}

}
