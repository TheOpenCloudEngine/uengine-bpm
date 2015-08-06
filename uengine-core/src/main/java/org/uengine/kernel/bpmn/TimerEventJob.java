package org.uengine.kernel.bpmn;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

/**
 * Created by jinyoung jang on 2015. 8. 6..
 */
public class TimerEventJob implements Job {
    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        System.out.println(jobExecutionContext.getJobDetail().getJobDataMap().getString("instanceId"));

    }
}
