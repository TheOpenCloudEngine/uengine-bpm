package org.uengine.kernel.bpmn;


import org.uengine.kernel.Activity;
import org.uengine.kernel.ProcessInstance;

import java.util.ArrayList;
import java.util.List;

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

	@Override
	public List<Activity> getPossibleNextActivities(ProcessInstance instance, String scope) throws Exception {

		//return only the first (only-one) matching out-sequence flow

		List<Activity> inclusiveNextActivities = super.getPossibleNextActivities(instance, scope);

		List<Activity> exclusiveNextActivities = new ArrayList<>();

		if(inclusiveNextActivities==null || inclusiveNextActivities.size()==0)
			return exclusiveNextActivities;

		Activity theOnlyOneActivity = null;
		int minPriority = 10000;

		for(Activity nextActivity : inclusiveNextActivities){
			SequenceFlow theSequenceFlow = null;

			for(SequenceFlow sequenceFlow : getOutgoingSequenceFlows()){
				if(nextActivity.getTracingTag().equals(sequenceFlow.getTargetRef())){
					theSequenceFlow = sequenceFlow;
					break;
				}
			}

			if(theSequenceFlow==null) continue;

			if(theSequenceFlow.getPriority() < minPriority){
				minPriority = theSequenceFlow.getPriority();
				theOnlyOneActivity = nextActivity;
			}
		}

		if(theOnlyOneActivity!=null)
			exclusiveNextActivities.add(theOnlyOneActivity);

		return exclusiveNextActivities;
	}
}
