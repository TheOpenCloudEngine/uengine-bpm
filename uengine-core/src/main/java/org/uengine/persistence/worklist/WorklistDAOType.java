package org.uengine.persistence.worklist;

import java.util.Date;

import org.uengine.persistence.AbstractDAOType;
import org.uengine.persistence.dao.WorkListDAO;
import org.uengine.processmanager.TransactionContext;
import org.uengine.util.dao.ConnectiveDAO;
import org.uengine.util.dao.IDAO;


public class WorklistDAOType extends AbstractDAOType{
	
	public static WorklistDAOType getInstance(TransactionContext tc) throws Exception{
		return (WorklistDAOType)getInstance(WorklistDAOType.class, tc);
	}
	
	public WorkListDAO createDAOImpl(String sql) throws Exception {
		WorkListDAO dao = (WorkListDAO)ConnectiveDAO.createDAOImpl(getTransactionContext(), sql, WorkListDAO.class);
		return dao;
	}
	
	public int[] updateDueDateByTaskId( String[] taskIds, Date dueDate ) throws Exception {
		WorkListDAO worklistDAO = createDAOImpl(updateDueDateByTaskId_SQL);
		
		if(taskIds!=null){
			for(int i=0; i<taskIds.length; i++){
				worklistDAO.setTaskId(new Long(taskIds[i]));
				worklistDAO.setDueDate(dueDate);
				worklistDAO.addBatch();
			}
			
			return worklistDAO.updateBatch();
		}
		
		return new int[]{};
	}
	
	public static int getCurrnetRunningCount(TransactionContext tc,String defVerId,String tracingTag) throws Exception {
		IDAO dao = (IDAO)ConnectiveDAO.createDAOImpl(tc, currnetRunningCount_SQL, IDAO.class);
		dao.set("defVerId", defVerId);
		dao.set("tracingTag", tracingTag);
		dao.select();
		if(dao.next()){
			return ((Number)dao.get("CNT")).intValue();
		}
		
		return 0;
	}
	
	public String updateDueDateByTaskId_SQL = 
		"update bpm_worklist " +
		"set duedate = ?DueDate " +
		"where taskid = ?taskId";
	
	public static String currnetRunningCount_SQL = 
		"select count(1) as CNT from bpm_worklist where " +
		"defid = ?defVerId and " +
		"trctag = ?tracingTag and " +
		"(status='NEW' or status='CONFIRMED')";
	
//	public static final String updateDurationByTaskId_SQL = 
//		"update bpm_worklist " +
//		"set modifieddate=sysdate, duration = ?Duration " +
//		"where taskid = ?taskId";
//	public int[] updateDurationByTaskId( String[] taskIds, int duration ) throws Exception {
//		WorkListDAO worklistDAO = createDAOImpl("updateDurationByTaskId_SQL");
//		
//		if(taskIds!=null){
//			for(int i=0; i<taskIds.length; i++){
//				worklistDAO.setTaskId(new Long(taskIds[i]));
//				worklistDAO.setD(new Long(duration));
//				worklistDAO.addBatch();
//			}
//			
//			return worklistDAO.updateBatch();
//		}
//		
//		return new int[]{};
//	}
	
}
