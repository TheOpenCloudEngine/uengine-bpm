package org.uengine.kernel.bpmn;

import org.metaworks.annotation.Range;
import org.uengine.kernel.DefaultActivity;
import org.uengine.kernel.MessageListener;
import org.uengine.kernel.ProcessInstance;


public class TimerEvent extends Event{

	@Override
	protected void executeActivity(ProcessInstance instance) throws Exception {
//		//TODO: quartz job 에 등록
//		job.register("timer_" + getTracingTag(), instanceId);
//
//		// quartz 에서는 해당 Timer Event 를 호출한다:
//		instance.getProcessDefinition().fireMessage("timer", instance, data.getTracingTag());
	}

	@Override
	public String getMessage() {
		return "timer";
	}
}
