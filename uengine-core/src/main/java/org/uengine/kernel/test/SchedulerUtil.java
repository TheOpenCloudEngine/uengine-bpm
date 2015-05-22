package org.uengine.kernel.test;

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

        // 여기부터
        CronTrigger cronTrigger = new CronTrigger("tempCronTrigger", "tempGroupTrigger");
        cronTrigger.setCronExpression(cronExpression);
        class TempJob implements Job {
            public void execute(JobExecutionContext context) {
            }

        }
        // 여기 코드를 위 코드로 대체
        Date firstRunTime = (Date) sched.scheduleJob(new JobDetail("tempJobDetail", "tempGroupJobDetail", TempJob.class), cronTrigger);

        Calendar c = new GregorianCalendar();
        c.setTime(firstRunTime);
        c.set(Calendar.MILLISECOND, 0);
        // 여기까지

        sched.unscheduleJob("tempJobDetail", "tempGroupJobDetail");
        sched.deleteJob("tempJobDetail", "tempGroupJobDetail");

        return c;
    }
}
