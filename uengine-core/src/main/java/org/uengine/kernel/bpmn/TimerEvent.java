package org.uengine.kernel.bpmn;


import org.metaworks.dwr.MetaworksRemoteService;
import org.quartz.CronTrigger;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.impl.StdSchedulerFactory;
import org.uengine.kernel.ProcessInstance;

import static org.quartz.CronScheduleBuilder.cronSchedule;
import static org.quartz.JobBuilder.newJob;
import static org.quartz.TriggerBuilder.newTrigger;
import java.util.Date;


public class TimerEvent extends Event{
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

		StdSchedulerFactory schedulerFactoryBean = MetaworksRemoteService.getComponent(StdSchedulerFactory.class);

        Scheduler sched = schedulerFactoryBean.getScheduler();

        JobDetail job = newJob(TimerEventJob.class)
                .withIdentity("job1", "group1")
                .build();

		job.getJobDataMap().put("instanceId", instance.getInstanceId());
		job.getJobDataMap().put("tracingTag", getTracingTag());

//        Date runTime = evenMinuteDate(new Date());

        CronTrigger trigger = newTrigger()
                .withIdentity(instance.getInstanceId() + "@" + getTracingTag(), "uengine")
                .withSchedule(cronSchedule(getExpression()))
                .build();

		sched.scheduleJob(job, trigger);

        if(!sched.isStarted()){
            sched.start();
        }

	}




	@Override
	public boolean onMessage(ProcessInstance instance, Object payload) throws Exception {
		return super.onMessage(instance, payload);
	}


}
