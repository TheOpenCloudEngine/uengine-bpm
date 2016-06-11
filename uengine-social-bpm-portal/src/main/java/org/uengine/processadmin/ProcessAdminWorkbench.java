package org.uengine.processadmin;

import org.metaworks.annotation.ServiceMethod;
import org.quartz.CronTrigger;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.impl.StdSchedulerFactory;
import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.uengine.modeling.resource.Workbench;


import static org.quartz.CronScheduleBuilder.cronSchedule;
import static org.quartz.DateBuilder.evenMinuteDate;
import static org.quartz.JobBuilder.newJob;
import static org.quartz.TriggerBuilder.newTrigger;
import java.util.Date;

/**
 * Created by jangjinyoung on 15. 7. 12..
 */
public class ProcessAdminWorkbench extends Workbench {
    public ProcessAdminWorkbench() {
        super(new ProcessAdminResourceNavigator());

        try {
            setEditorPanel(new RecentEditedResourcesPanel(getResourceNavigator()));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


//    @Autowired
//    public StdSchedulerFactory schedulerFactoryBean;

//    @ServiceMethod(inContextMenu = true)
//    public void startJob() throws SchedulerException {
//        Scheduler sched = schedulerFactoryBean.getScheduler();
//
//        HelloJob helloJob = new HelloJob();
//
//        JobDetail job = newJob(HelloJob.class)
//                .withIdentity("job1", "group1")
//                .build();
//
//        Date runTime = evenMinuteDate(new Date());
//
////        Trigger trigger = newTrigger()
////                .withIdentity("trigger1", "group1")
////                .startAt(runTime)
////                .build();
//
//        CronTrigger trigger = newTrigger()
//                .withIdentity("trigger1", "group1")
//                .withSchedule(cronSchedule("0/10 * * * * ?"))
//                .build();
//
//        try {
//            sched.scheduleJob(job, trigger);
//        } catch (SchedulerException e) {
//            e.printStackTrace();
//        }
//
//        if(!sched.isStarted()){
//            sched.start();
//        }
//    }

}





