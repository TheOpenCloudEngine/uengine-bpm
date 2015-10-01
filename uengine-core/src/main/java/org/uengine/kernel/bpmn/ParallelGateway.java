package org.uengine.kernel.bpmn;

import org.uengine.kernel.Activity;
import org.uengine.kernel.ProcessInstance;
import org.uengine.kernel.ValidationContext;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class ParallelGateway extends Gateway {

	public ParallelGateway() {
		//super("Parallel");
	}


	@Override
	public List<Activity> getPossibleNextActivities(ProcessInstance instance, String scope) throws Exception {

		//TODO: 모든 outgoing activity 들

		return super.getPossibleNextActivities(instance, scope);
	}

	@Override
	protected boolean isCompletedAllOfPreviousActivities(ProcessInstance instance) throws Exception {
		
		// this is before execute
//		System.out.println("---- parent activity's tracing tag: " + getParentActivity().getTracingTag());
		// why this previousActivities() is always 1?
//		System.out.println("---- previous activities number: " + getPreviousActivities().size());
		
		
		
		SequenceFlow sequenceFlow = null;
		for (Iterator<SequenceFlow> it = getIncomingSequenceFlows().iterator(); it.hasNext(); ) {
			sequenceFlow = it.next();
			String status = (sequenceFlow.getSourceActivity()).getStatus(instance);
			if (!status.equals(Activity.STATUS_COMPLETED)) {
				return false;
			}
		}
		
		return true;
	}


	@Override
	public ValidationContext validate(Map options) {

		//And 임에도 불구하고, outgoing transition 에 조건이  있다면, warning 을 추가...    super.validate(options).add("....");

		return super.validate(options);
	}
}
