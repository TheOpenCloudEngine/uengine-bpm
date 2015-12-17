package org.uengine.kernel.bpmn;


import org.metaworks.dwr.MetaworksRemoteService;
import org.quartz.CronTrigger;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.impl.StdSchedulerFactory;
import org.uengine.kernel.ProcessInstance;

import static org.quartz.CronScheduleBuilder.cronSchedule;
import static org.quartz.JobBuilder.newJob;
import static org.quartz.JobKey.jobKey;
import static org.quartz.TriggerBuilder.newTrigger;


public class TimerEvent extends Event{

	private static final String SCHEDULER_GROUP_ID = "uengine";

	public TimerEvent() {
		setName("Timer");
	}
	private static final long serialVersionUID = org.uengine.kernel.GlobalContext.SERIALIZATION_UID;


	String expression;
		public String getExpression() {
			return expression;
		}
		public void setExpression(String expression) {
			this.expression = expression;
		}


	@Override
	protected void executeActivity(ProcessInstance instance) throws Exception {

		//TODO enable please.
//		StdSchedulerFactory schedulerFactoryBean = MetaworksRemoteService.getComponent(StdSchedulerFactory.class);
//
//        Scheduler sched = schedulerFactoryBean.getScheduler();
//
//		String jobId = createJobId(instance);
//
//		JobDetail job = newJob(TimerEventJob.class)
//                .withIdentity(jobId, SCHEDULER_GROUP_ID)
//                .build();
//
//		job.getJobDataMap().put("instanceId", instance.getInstanceId());
//
//		if(instance.getExecutionScopeContext()!=null)
//			job.getJobDataMap().put("executionScope", instance.getExecutionScopeContext().getExecutionScope());
//
//		job.getJobDataMap().put("tracingTag", getTracingTag());
//
////        Date runTime = evenMinuteDate(new Date());
//
//		StringBuffer cronExpression = evaluateContent(instance, getExpression());
//
//		CronTrigger trigger = newTrigger()
//				.withIdentity(jobId, SCHEDULER_GROUP_ID)
//				.withSchedule(cronSchedule(cronExpression.toString()))
//				.build();
//
//		sched.scheduleJob(job, trigger);
//
//		if (!sched.isStarted()) {
//			sched.start();
//		}

	}

	private String createJobId(ProcessInstance instance) {
		return instance.getInstanceId()
                    + (instance.getExecutionScopeContext()!=null ? "@" + instance.getExecutionScopeContext().getExecutionScope() : "")
                    + "@" + getTracingTag();
	}

	@Override
	protected void afterComplete(ProcessInstance instance) throws Exception {
		super.afterComplete(instance);

		//if non-boundary event, after executing the event, the schedule must be dropped,
		// if it is a boundry event, the lifecycle of the event must be associated with the attached activity's lifecycle.
		if(getAttachedToRef()==null){
			unschedule(instance);
		}
	}

	@Override
	public boolean onMessage(ProcessInstance instance, Object payload) throws Exception {
		return super.onMessage(instance, payload);
	}

	@Override
	public void afterUnregistered(ProcessInstance instance) throws Exception{
		super.afterUnregistered(instance);

		unschedule(instance);

	}

	private void unschedule(ProcessInstance instance) {
		StdSchedulerFactory schedulerFactoryBean = MetaworksRemoteService.getComponent(StdSchedulerFactory.class);

		try {
			Scheduler sched = schedulerFactoryBean.getScheduler();

			String jobId = createJobId(instance);

			// Unschedule a particular trigger from the job (a job may have more than one trigger).
			// Reference following code in the case.
			//			scheduler.unscheduleJob(triggerKey("trigger1", "group1"));

			//Deleting a Job and Unscheduling all of its Triggers
			sched.deleteJob(jobKey(jobId, SCHEDULER_GROUP_ID));

		} catch (SchedulerException e) {
			new RuntimeException(e);
		}
	}
}
