package org.uengine.kernel.test;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.metaworks.dao.ConnectionFactory;
import org.metaworks.dao.TransactionContext;
import org.metaworks.spring.SpringConnectionFactory;
import org.quartz.*;
import org.quartz.Calendar;
import org.quartz.impl.StdSchedulerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.uengine.kernel.*;
import org.uengine.kernel.bpmn.SequenceFlow;
import org.uengine.kernel.bpmn.TimerEvent;
import org.uengine.scheduler.SchedulerItem;
import org.uengine.scheduler.WaitJob;

import javax.sql.DataSource;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.*;

public class WaitJobTest extends UEngineTest implements Job {

    public static java.util.Calendar modifyCal = null;
    ProcessDefinition subDefinition;
    ProcessDefinition processDefinition;
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss");
    public static int triggerTotalCallCnt = 0;
    public static int runTotalCallCnt = 0;

    public static ConnectionFactory connectionFactory;
    /**
     * build a graph as follows:
     * <p>
     * <p>
     * 1 -> 2 -> 3 --------> 4
     *          (+)
     *           |
     *           +-> 5 (timer -> onMessage()) -> sub ( 1 -> 2 -> 3 )
     *
     * 20초 동안 10초 마다 실행 되는 트리거를 돌리고 트리거는 5초 마다 돈다.
     * 즉 4번 돌아서 2번 실행 되어야 한다.
     *
     * @throws Exception
     */
    public void prepareDefinition() throws Exception {

        subDefinition = new ProcessDefinition();

        for (int i = 1; i < 4; i++) {
            Activity a1 = new DefaultActivity();

            a1.setTracingTag("a" + i);
            subDefinition.addChildActivity(a1);

        }

        {
            SequenceFlow t1 = new SequenceFlow();
            t1.setSourceRef("a1");
            t1.setTargetRef("a2");

            subDefinition.addSequenceFlow(t1);
        }
        {
            SequenceFlow t1 = new SequenceFlow();
            t1.setSourceRef("a2");
            t1.setTargetRef("a3");

            subDefinition.addSequenceFlow(t1);
        }
        subDefinition.afterDeserialization();
        ProcessInstance.USE_CLASS = DefaultProcessInstance.class;

        processDefinition = new ProcessDefinition();

        processDefinition.setProcessVariables(new ProcessVariable[]{
//                ProcessVariable.forName("var1"),
        });


        for (int i = 1; i < 8; i++) {
            Activity a1 = new DefaultActivity();

            if (i == 2 || i == 3) {
                ReceiveActivity rcv = new ReceiveActivity();
                rcv.setMessage("receive");

                a1 = rcv;

            }

            if (i == 5) {
                TimerEvent event = new TimerEvent();
                event.setName("a5");
                event.setAttachedToRef("a3");

                event.setDuration(TimerEvent.WAITING_TYPE_UNTIL);
                event.setExpression("20 0 0 0");

                a1 = event;
            }

            a1.setTracingTag("a" + i);
            processDefinition.addChildActivity(a1);

        }

        {
            SequenceFlow t1 = new SequenceFlow();
            t1.setSourceRef("a1");
            t1.setTargetRef("a2");

            processDefinition.addSequenceFlow(t1);
        }
        {
            SequenceFlow t1 = new SequenceFlow();
            t1.setSourceRef("a2");
            t1.setTargetRef("a3");

            processDefinition.addSequenceFlow(t1);
        }

        {
            SequenceFlow t1 = new SequenceFlow();
            t1.setSourceRef("a3");
            t1.setTargetRef("a4");

            processDefinition.addSequenceFlow(t1);
        }


        processDefinition.afterDeserialization();


    }

    public void testWaitJob() throws Exception {
        DataSource dataSource = null;
        ApplicationContext xml = new ClassPathXmlApplicationContext("spring-config.xml");
        dataSource = (DataSource) xml.getBean("dataSource");

        JobDetail job = new JobDetail();
        job.setName("WaitJobTest");

        job.setJobClass(WaitJobTest.class);
        SpringConnectionFactory f = new SpringConnectionFactory();
        f.setDataSource(dataSource);

        job.getJobDataMap().put("connectionFactory", f);

        CronTrigger trigger = new CronTrigger();
        trigger.setName("WaitJobTestTrigger");
        trigger.setCronExpression("0/5 * * * * ?");

        Scheduler scheduler = new StdSchedulerFactory().getScheduler();
        scheduler.start();
        scheduler.scheduleJob(job, trigger);
        try {
            Thread.sleep(20000);
            assertEquals("triggerTotalCallCnt", triggerTotalCallCnt, 4);
            assertEquals("runTotalCallCnt", runTotalCallCnt, 2);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }



    public void execute(JobExecutionContext context) {
        try {
            prepareDefinition();
        } catch (Exception e) {
            e.printStackTrace();
        }

        long time = System.currentTimeMillis();

        WaitJobTest.triggerTotalCallCnt++;

        System.out.println("WaitJob trigger 1 (10 second): current time = " + sdf.format(time));
        TransactionContext tx = null;
        ProcessInstance instance = null;
        ProcessInstance subInstance = null;
        try {
            connectionFactory = (SpringConnectionFactory) context.getJobDetail().getJobDataMap().get("connectionFactory");

            tx = new TransactionContext(); //once a TransactionContext is created, it would be cached by ThreadLocal.set, so, we need to remove this after the request processing.
            tx.setManagedTransaction(true);
            tx.setAutoCloseConnection(true);

            if (connectionFactory != null)
                tx.setConnectionFactory(connectionFactory);


            java.util.Calendar now = java.util.Calendar.getInstance();

            List<SchedulerItem> schedulerItems = new ArrayList<SchedulerItem>(); //this.getAllSchedule();

            //Test code
            SchedulerItem s = new SchedulerItem();
            s.setExpression("0/10 * * * * ?");
            s.setIdx(0);
            s.setInstanceId("5");
            s.setTracingTag("a5");
            if (WaitJobTest.modifyCal == null) {
                s.setStartDate(new Timestamp(now.getTimeInMillis()));
            } else {
                s.setStartDate(new Timestamp(WaitJobTest.modifyCal.getTimeInMillis()));
            }
            s.setNewInstance(true);

            schedulerItems.add(s);
            for (final SchedulerItem item : schedulerItems) {
                if (!(item.getStartDate().getTime() <= now.getTimeInMillis())) {
                    System.out.println("modifyCal time = " + sdf.format(WaitJobTest.modifyCal.getTimeInMillis()));
                    System.out.println("NEXT TIME@@@");
                    continue;
                }

//                CodiClassLoader clForSession = CodiClassLoader.createClassLoader(null, item.getGlobalCom());
//                Thread.currentThread().setContextClassLoader(clForSession);


                try {
                    instance = processDefinition.createInstance();
                    subInstance = subDefinition.createInstance();
//                    instance = pm.getProcessInstance("Volatile_" + item.getInstanceId());
                } catch (Exception e) {
                    e.printStackTrace();
                }

                boolean isError = true;
                if (instance != null) {
                    Activity act = instance.getProcessDefinition().getActivity(item.getTracingTag());

                    if (act != null && act instanceof TimerEvent) {
                        TimerEvent wa = (TimerEvent) act;

                        String status = wa.getStatus(instance);

                        //TODO :
                        if (Activity.STATUS_RUNNING.equals(status) || Activity.STATUS_TIMEOUT.equals(status) || Activity.STATUS_READY.equals(status)) {

                            boolean isUpdate = false;

                            if (item.getExpression() != null) {


                                WaitJobTest.modifyCal = SchedulerUtil.getCalendarByCronExpression(item.getExpression());
                                System.out.println("modifyCal time = " + sdf.format(WaitJobTest.modifyCal.getTimeInMillis()));


                                if (WaitJobTest.modifyCal.getTimeInMillis() >= now.getTimeInMillis()) {
                                    // 다음 스케쥴이 존재한다면 지우지 않고, update를 해준다.
                                    isUpdate = true;
                                }
                            }
                            String instId = item.getInstanceId();
                            if (item.isNewInstance() && isUpdate) {
//                                instId = pm.initializeProcess(instance.getProcessDefinition().getId());
//                                RoleMapping rm = instance.getRoleMapping("Initiator");
//                                rm.setName("Initiator");
//                                pm.putRoleMapping(instId, rm);
//
//                                Session codiSession = new Session();
//                                IEmployee emp = new Employee();
//                                emp.setEmpCode(rm.getEndpoint());
//                                emp.select();
//                                codiSession.setUser(emp.getUser());
//                                codiSession.setEmployee(emp);
//
//                                RoleMappingPanel roleMappingPanel = new RoleMappingPanel(pm, instance.getProcessDefinition().getId(), codiSession);
//                                roleMappingPanel.putRoleMappings(pm, instId);
//                                pm.executeProcess(instId);
                                instance.execute();
                                subInstance.execute();
                                assertExecutionPathEquals("Running Before Event", new String[]{
                                        "a1", "a2", "a3"
                                }, subInstance);
                                WaitJobTest.runTotalCallCnt++;
                            } else if (isUpdate) {
                                updateSchedule(item.getIdx(), modifyCal);
                            } else {
                                deleteSchedule(item.getIdx());
                            }

                            isError = false;

//                            if(wa.getInstanceStop() != null && wa.getInstanceStop().equals("STOP_INSTANCE")){
//                                WaitActivity waitActivity = new WaitActivity();
//                                waitActivity.stopInstance(instance);
//                            }

                            wa.fireComplete(instance);
                        } else if (Activity.STATUS_FAULT.equals(status)
                                || Activity.STATUS_READY.equals(status)
                                || Activity.STATUS_STOPPED.equals(status) || Activity.STATUS_CANCELLED.equals(status)) {
                            continue;
                        }
                    }
                }

                if (isError) {
                    deleteSchedule(item.getIdx());
                }
            }


        } catch (Exception e) {
            e.printStackTrace();

            if (tx != null) {
                try {
                    tx.rollback();
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
            }
        } finally {
            if (tx != null) {
                try {
                    tx.releaseResources();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

    }

//    public List<SchedulerItem> getAllSchedule() {
//
//        Connection conn = null;
//        Statement stmt = null;
//        ResultSet rs = null;
//        List<SchedulerItem> schedulerItems = new ArrayList<SchedulerItem>();
//
//        try {
//            conn = connectionFactory.getConnection();
//            stmt = conn.createStatement();
//            StringBuilder sql = new StringBuilder();
//            sql.append("SELECT ");
//            sql.append("	schedule_table.SCHE_IDX, ");
//            sql.append("	schedule_table.INSTID, ");
//            sql.append("	schedule_table.TRCTAG, ");
//            sql.append("	schedule_table.STARTDATE, ");
//            sql.append("	schedule_table.expression, ");
//            sql.append("	schedule_table.newInstance, ");
//            sql.append("	bpm_procinst.INITCOMCD, ");
//            sql.append("	bpm_procinst.STATUS, ");
//            sql.append("	bpm_procinst.ISDELETED ");
//            sql.append("FROM schedule_table JOIN bpm_procinst ON schedule_table.INSTID = bpm_procinst.INSTID ");
//            sql.append("WHERE ");
//            sql.append("	bpm_procinst.ISDELETED = 0 ");
//            sql.append("	AND bpm_procinst.STATUS = 'Running' ");
//
//            rs = stmt.executeQuery(sql.toString());
//
//            while (rs.next()) {
//                SchedulerItem item = new SchedulerItem();
//
//                item.setIdx(rs.getInt("SCHE_IDX"));
//                item.setInstanceId(rs.getString("INSTID"));
//                item.setTracingTag(rs.getString("TRCTAG"));
//                item.setStartDate(rs.getTimestamp("STARTDATE"));
//                item.setExpression(rs.getString("expression"));
//                item.setNewInstance(rs.getInt("newInstance") == 1 ? true : false);
//                item.setGlobalCom(rs.getString("INITCOMCD"));
//                schedulerItems.add(item);
//            }
//
//        } catch (Exception e){
////        	e.printStackTrace();
//        } finally {
//            if (rs != null) {
//                try {
//                    rs.close();
//                } catch (Exception e) { }
//            }
//            if (stmt != null) {
//                try {
//                    stmt.close();
//                } catch (Exception e) { }
//            }
//            if (conn != null) {
//                try {
//                    conn.close();
//                } catch (Exception e) { }
//            }
//        }
//
//        return schedulerItems;
//    }

    /**
     * Delete schedule in DB.
     */
    public void updateSchedule(int idx, java.util.Calendar modifyCal) throws Exception {
        // delete schedule information from DB.
        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            conn = connectionFactory.getConnection();
            StringBuilder sql = new StringBuilder();
            sql.append(" UPDATE schedule_table SET STARTDATE=?  WHERE SCHE_IDX=").append(idx);

            pstmt = conn.prepareStatement(sql.toString());
            pstmt.setTimestamp(1, new Timestamp(modifyCal.getTimeInMillis()));
            pstmt.executeUpdate();

//            conn.commit();
        } catch (Exception e) {
            if (conn != null) try {
                conn.rollback();
            } catch (Exception e1) {
            }
            throw e;
        } finally {
            if (pstmt != null) {
                try {
                    pstmt.close();
                } catch (Exception e) {
                }
            }
            if (conn != null) {
                try {
                    conn.close();
                } catch (Exception e) {
                }
            }
        }
    }

    /**
     * Delete schedule in DB.
     */
    public void deleteSchedule(int idx) throws Exception {
        // delete schedule information from DB.
        Connection conn = null;
        Statement stmt = null;

        try {
            conn = connectionFactory.getConnection();

            stmt = conn.createStatement();

            StringBuilder sql = new StringBuilder();
            sql.append(" DELETE FROM schedule_table WHERE SCHE_IDX=").append(idx);

            stmt.executeUpdate(sql.toString());

//            conn.commit();
        } catch (Exception e) {
            if (conn != null) try {
                conn.rollback();
            } catch (Exception e1) {
            }
            throw e;
        } finally {
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (Exception e) {
                }
            }
            if (conn != null) {
                try {
                    conn.close();
                } catch (Exception e) {
                }
            }
        }
    }
}
