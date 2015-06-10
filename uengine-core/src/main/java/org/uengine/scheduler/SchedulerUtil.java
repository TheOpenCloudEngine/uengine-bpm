package org.uengine.scheduler;

import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Created by uengine on 2015-05-21.
 */
public class SchedulerUtil {
    public static Calendar getCalendarByCronExpression(String cronExpression) throws Exception {
        SchedulerFactory sf = new StdSchedulerFactory();
        Scheduler sched = sf.getScheduler();

        JobDetail jd = sched.getJobDetail("tempJobDetail", "tempGroupJobDetail");
        if (jd != null) {
            sched.unscheduleJob("tempJobDetail", "tempGroupJobDetail");
            sched.deleteJob("tempJobDetail", "tempGroupJobDetail");
        }

        // �뿬湲곕��꽣
        CronTrigger cronTrigger = new CronTrigger("tempCronTrigger", "tempGroupTrigger");
        cronTrigger.setCronExpression(cronExpression);
        class TempJob implements Job {
            public void execute(JobExecutionContext context) {
            }

        }
        // �뿬湲� 肄붾뱶瑜� �쐞 肄붾뱶濡� ��泥�
        Date firstRunTime = (Date) sched.scheduleJob(new JobDetail("tempJobDetail", "tempGroupJobDetail", TempJob.class), cronTrigger);

        Calendar c = new GregorianCalendar();
        c.setTime(firstRunTime);
        c.set(Calendar.MILLISECOND, 0);
        // �뿬湲곌퉴吏�

        sched.unscheduleJob("tempJobDetail", "tempGroupJobDetail");
        sched.deleteJob("tempJobDetail", "tempGroupJobDetail");

        return c;
    }
}
