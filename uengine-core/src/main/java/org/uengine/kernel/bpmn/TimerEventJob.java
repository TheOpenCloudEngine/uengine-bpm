package org.uengine.kernel.bpmn;

import org.metaworks.dwr.MetaworksRemoteService;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.uengine.processmanager.ProcessManagerRemote;

import java.rmi.RemoteException;

/**
 * Created by jinyoung jang on 2015. 8. 6..
 */
@Component
public class TimerEventJob implements Job {
    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        TimerEventJob timerEventJob = MetaworksRemoteService.getComponent(TimerEventJob.class);

        try {
            timerEventJob.fireEvent(jobExecutionContext.getJobDetail().getJobDataMap());
        } catch (Exception e) {
            e.printStackTrace();

            throw new JobExecutionException(e);
        }

    }

    @Autowired
    ProcessManagerRemote processManagerRemote;

    @Transactional
    public void fireEvent(JobDataMap jobDataMap) throws Exception {
        String instanceId = jobDataMap.getString("instanceId");
        String tracingTag = jobDataMap.getString("tracingTag");
        String executionScope = jobDataMap.getString("executionScope");


        System.out.println("triggered for [instId: " + instanceId + "] and [execScope: " + executionScope + "] and [trcTag: "+ tracingTag + "]");

        processManagerRemote.sendMessage(
                instanceId + (executionScope != null ? "@" + executionScope : ""),
                "onTime", //it is important! --> org.uengine.five.overriding.ProcessManagerBean.sendMessage(..) will check this as key
                tracingTag
        );

    }
}
