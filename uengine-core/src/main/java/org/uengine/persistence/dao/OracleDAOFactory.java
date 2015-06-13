/*
 * Created on 2004. 11. 3.
 */
package org.uengine.persistence.dao;

import java.sql.Time;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;

import org.uengine.persistence.processinstance.ProcessInstanceDAO;
import org.uengine.persistence.processinstance.ProcessInstanceRepositoryLocal;
import org.uengine.persistence.processvariable.ProcessVariableDAO;
import org.uengine.util.dao.ConnectiveDAO;
import org.uengine.util.dao.IDAO;


/**
 * @author Jinyoung Jang
 */
public class OracleDAOFactory extends DAOFactory{
	
	public WorkListDAO findWorkListDAOByEndpoint(Map options) throws Exception{
		return (WorkListDAO)create(
			WorkListDAO.class,
			"select * from bpm_worklist where endpoint=?endpoint"
		);
	}

	public WorkListDAO findWorkListDAOByTaskId(Map options) throws Exception{
		String tableName = "bpm_worklist";
		if(options!=null && options.containsKey("tableName"))
			tableName = (String)options.get("tableName");		
		
		return (WorkListDAO)create(
			WorkListDAO.class,
			"select * from "+tableName+" where taskId=?taskId"
		);
	}

	public WorkListDAO createWorkListDAOForInsertCall(Map options) throws Exception{
		return (WorkListDAO)create(
			WorkListDAO.class,
			"{?*taskId = call bpm_func_reserveWorkItem("+
				"?taskId, "+
				"?title, "+
				"?description, "+
				"?endpoint, "+
				"?resName, "+
				"?roleName, "+
				"?refRoleName, "+
				"?status, "+
				"?priority, "+
				"?startdate, "+
				"?enddate, "+
				"?duedate, "+
				"?instId, "+
				"?rootinstId, "+
				"?defId, "+
				"?DefName, "+
				"?trcTag, "+
				"?tool, "+
				"?dispatchOption, "+
				"?dispatchParam1, "+
				"?parameter, "+
				"?ext1, "+
				"?ext2, "+
				"?ext3, "+
				"?ext4, "+
				"?ext5, "+
				"?ext6, "+
				"?ext7, "+
				"?ext8, "+
				"?ext9, "+
				"?ext10 "+
			")}"
		);
	}
	
	public WorkListDAO createWorkListDAOForUpdate(Map options) throws Exception{
		return (WorkListDAO)create(
			WorkListDAO.class,
			"update bpm_worklist set status=?status, enddate=?enddate where taskId=?taskId"
		);
	}
	
	public KeyGeneratorDAO createKeyGenerator(String forWhat, Map options) throws Exception {
		return (KeyGeneratorDAO)create(
			KeyGeneratorDAO.class,
			"select SEQ_BPM_"+ forWhat + ".NextVal as keyNumber from dual"
		);
	}
	
	public ProcessInstanceDAO createProcessInstanceDAOForArchive() throws Exception{
		return (ProcessInstanceDAO)create(
			ProcessInstanceDAO.class,
			"{call bpm_func_archiveProc (?id)}"
		);	
	}

	public ProcessVariableDAO findProcessVariableDAOByInstanceId() throws Exception{
		return (ProcessVariableDAO)create(
			ProcessVariableDAO.class,
			"select * from bpm_procvar where instanceid = ?instanceId"
		);	
	}

	public String getDBMSProductName() throws Exception {
		return "Oracle";
	}

	public Calendar getNow() throws Exception {
		IDAO nowQuery = (IDAO)create(IDAO.class, "SELECT TO_CHAR( SYSDATE , 'YYYYMMDDHH24MISS' ) as NOW from dual");
		nowQuery.select();
		
		if(nowQuery.next()){
			Calendar now = Calendar.getInstance();
			
			String dateTimeStr = (String)nowQuery.get("NOW");
			int year = Integer.parseInt(dateTimeStr.substring(0, 4));
			int month = Integer.parseInt(dateTimeStr.substring(4, 6)) - 1;
			int date = Integer.parseInt(dateTimeStr.substring(6, 8));
			int hour = Integer.parseInt(dateTimeStr.substring(8, 10));
			int min = Integer.parseInt(dateTimeStr.substring(10, 12));
			int sec = Integer.parseInt(dateTimeStr.substring(12, 14));
			
/*			
			Timestamp date = (Timestamp)nowQuery.getImplementationObject().getRowSet().getTimestamp("NOW");
			Time time = (Time)nowQuery.getImplementationObject().getRowSet().getTime("NOW");
*/
			now.set(year, month, date, hour, min, sec);
			
			return now;
		}else{
			throw new Exception("Can't get current system date from DB.");
		}
	}

	@Override
	public String getSequenceSql(String seqName) throws Exception {
		// TODO Auto-generated method stub
		return " (select SEQ_BPM_"+ seqName + ".NextVal as keyNumber from dual) "; 
	}

}
