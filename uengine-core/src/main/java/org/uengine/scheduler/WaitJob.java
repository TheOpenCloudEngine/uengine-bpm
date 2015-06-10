package org.uengine.scheduler;

import org.metaworks.dao.ConnectionFactory;
import org.metaworks.dao.TransactionContext;
import org.metaworks.spring.SpringConnectionFactory;
import org.quartz.Job;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.uengine.kernel.Activity;
import org.uengine.kernel.ProcessInstance;
import org.uengine.kernel.bpmn.TimerEvent;

import java.sql.*;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by uengine on 2015-05-21.
 */
public class WaitJob implements Job {
    public static ConnectionFactory connectionFactory;

    public void execute(JobExecutionContext context) {

        TransactionContext tx = null;

        try{
            connectionFactory = (SpringConnectionFactory) context.getJobDetail().getJobDataMap().get("connectionFactory");

            tx = new TransactionContext(); //once a TransactionContext is created, it would be cached by ThreadLocal.set, so, we need to remove this after the request processing.
            tx.setManagedTransaction(true);
            tx.setAutoCloseConnection(true);

            if(connectionFactory!=null)
                tx.setConnectionFactory(connectionFactory);

            MetaworksUEngineSpringConnectionAdapter connectionAdapter = new MetaworksUEngineSpringConnectionAdapter();

            CodiProcessManagerBean pm = new CodiProcessManagerBean();
            pm.setConnectionFactory(connectionAdapter);
            pm.setAutoCloseConnection(false);
            pm.setManagedTransaction(true);

            Calendar now = Calendar.getInstance();

            List<SchedulerItem> schedulerItems = this.getAllSchedule();

            for (final SchedulerItem item : schedulerItems) {
                if (!(item.getStartDate().getTime() <= now.getTimeInMillis())) {
                    continue;
                }

//                CodiClassLoader clForSession = CodiClassLoader.createClassLoader(null, item.getGlobalCom());
//                Thread.currentThread().setContextClassLoader(clForSession);

                ProcessInstance instance = null;

                try {
                    instance = pm.getProcessInstance(item.getInstanceId());
                } catch (Exception e) {
                    e.printStackTrace();
                }

                boolean isError = true;
                if (instance != null) {
                    Activity act = instance.getProcessDefinition().getActivity(item.getTracingTag());
					/*
					 *WaitActivity에서 인스턴스 종료 옵션 선택시
					 *
					 */
                    /*if (act != null && act instanceof WaitActivity) {
                        WaitActivity wa = (WaitActivity)act;

                        String status = wa.getStatus(instance);
                        if (Activity.STATUS_RUNNING.equals(status) || Activity.STATUS_TIMEOUT.equals(status)) {
                            tx.addTransactionListener(new TransactionListener() {

                                public void beforeRollback(TransactionContext tx) throws Exception {
                                }

                                public void beforeCommit(TransactionContext tx) throws Exception {
                                    deleteSchedule(item.getIdx());
                                }

                                public void afterRollback(TransactionContext tx) throws Exception {
                                }

                                public void afterCommit(TransactionContext tx) throws Exception {
                                }
                            });

                            isError = false;

                            if(wa.getInstanceStop() != null && wa.getInstanceStop().equals("STOP_INSTANCE")){
                                wa.stopInstance(instance);
                            }

                            wa.fireComplete(instance);
                        } else if (Activity.STATUS_FAULT.equals(status)
                                || Activity.STATUS_READY.equals(status)
                                || Activity.STATUS_STOPPED.equals(status) || Activity.STATUS_CANCELLED.equals(status) ) {

                            continue;

                        }
                    }else */if( act != null && act instanceof TimerEvent ){
                        TimerEvent wa = (TimerEvent)act;

                        String status = wa.getStatus(instance);
                        if (Activity.STATUS_RUNNING.equals(status) || Activity.STATUS_TIMEOUT.equals(status)) {

                            boolean isUpdate = false;
                            Calendar modifyCal = null;
                            if( item.getExpression() != null ){


//                                modifyCal = SchedulerUtil.getCalendarByCronExpression(item.getExpression());



                                if (modifyCal.getTimeInMillis() >= now.getTimeInMillis()) {
                                    // 다음 스케쥴이 존재한다면 지우지 않고, update를 해준다.
                                    isUpdate = true;
                                }
                            }
                            String instId = item.getInstanceId();
                            if(item.isNewInstance() && isUpdate){
                                instId = pm.initializeProcess(instance.getProcessDefinition().getId());
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
                                pm.executeProcess(instId);
                            }else if( isUpdate ){
                                updateSchedule(item.getIdx(), modifyCal);
                            }else{
                                deleteSchedule(item.getIdx());
                            }

                            isError = false;

//                            if(wa.getInstanceStop() != null && wa.getInstanceStop().equals("STOP_INSTANCE")){
//                                WaitActivity waitActivity = new WaitActivity();
//                                waitActivity.stopInstance(instance);
//                            }

                            wa.fireComplete(instance);
                        }else if (Activity.STATUS_FAULT.equals(status)
                                || Activity.STATUS_READY.equals(status)
                                || Activity.STATUS_STOPPED.equals(status) || Activity.STATUS_CANCELLED.equals(status) ) {
                            continue;
                        }
                    }
                }

                if (isError) {
                    deleteSchedule(item.getIdx());
                }
            }

            pm.applyChanges();
            tx.commit();

        }catch(Exception e){
            e.printStackTrace();

            if(tx != null){
                try {
                    tx.rollback();
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
            }
        }finally{
            if(tx != null){
                try {
                    tx.releaseResources();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

    }

    public List<SchedulerItem> getAllSchedule() {

        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;
        List<SchedulerItem> schedulerItems = new ArrayList<SchedulerItem>();

        try {
            conn = connectionFactory.getConnection();
            stmt = conn.createStatement();
            StringBuilder sql = new StringBuilder();
            sql.append("SELECT ");
            sql.append("	schedule_table.SCHE_IDX, ");
            sql.append("	schedule_table.INSTID, ");
            sql.append("	schedule_table.TRCTAG, ");
            sql.append("	schedule_table.STARTDATE, ");
            sql.append("	schedule_table.expression, ");
            sql.append("	schedule_table.newInstance, ");
            sql.append("	bpm_procinst.INITCOMCD, ");
            sql.append("	bpm_procinst.STATUS, ");
            sql.append("	bpm_procinst.ISDELETED ");
            sql.append("FROM schedule_table JOIN bpm_procinst ON schedule_table.INSTID = bpm_procinst.INSTID ");
            sql.append("WHERE ");
            sql.append("	bpm_procinst.ISDELETED = 0 ");
            sql.append("	AND bpm_procinst.STATUS = 'Running' ");

            rs = stmt.executeQuery(sql.toString());

            while (rs.next()) {
                SchedulerItem item = new SchedulerItem();

                item.setIdx(rs.getInt("SCHE_IDX"));
                item.setInstanceId(rs.getString("INSTID"));
                item.setTracingTag(rs.getString("TRCTAG"));
                item.setStartDate(rs.getTimestamp("STARTDATE"));
                item.setExpression(rs.getString("expression"));
                item.setNewInstance(rs.getInt("newInstance") == 1 ? true : false);
                item.setGlobalCom(rs.getString("INITCOMCD"));
                schedulerItems.add(item);
            }

        } catch (Exception e){
//        	e.printStackTrace();
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (Exception e) { }
            }
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (Exception e) { }
            }
            if (conn != null) {
                try {
                    conn.close();
                } catch (Exception e) { }
            }
        }

        return schedulerItems;
    }

    /**
     * Delete schedule in DB.
     *
     */
    public void updateSchedule(int idx, Calendar modifyCal) throws Exception {
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
            if (conn != null) try { conn.rollback(); } catch (Exception e1) { }
            throw e;
        } finally {
            if (pstmt != null) {
                try {
                    pstmt.close();
                } catch (Exception e) { }
            }
            if (conn != null) {
                try {
                    conn.close();
                } catch (Exception e) { }
            }
        }
    }
    /**
     * Delete schedule in DB.
     *
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
            if (conn != null) try { conn.rollback(); } catch (Exception e1) { }
            throw e;
        } finally {
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (Exception e) { }
            }
            if (conn != null) {
                try {
                    conn.close();
                } catch (Exception e) { }
            }
        }
    }

}
