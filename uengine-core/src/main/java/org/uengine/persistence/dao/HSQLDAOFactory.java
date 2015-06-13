/*
 * Created on 2004. 12. 14.
 */
package org.uengine.persistence.dao;

import org.uengine.util.dao.*;

import java.util.*;
import java.lang.reflect.*;


/**
 * @author Jinyoung Jang
 */
public class HSQLDAOFactory extends OracleDAOFactory{
		
	public WorkListDAO createWorkListDAOForInsertCall(Map options) throws Exception{
		return (WorkListDAO)Proxy.newProxyInstance(
			WorkListDAO.class.getClassLoader(),
			new Class[]{WorkListDAO.class},
			new ConnectiveDAO(
				getConnectionFactory(), 
				true,
				null, //"insert into bpm_worklist(taskId, title, description, endpoint, resName, roleName, refRoleName, status, priority, startdate, enddate, duedate, instId, rootInstId, defId, DefName, trcTag, tool, dispatchOption, dispatchParam1, parameter)	values(?taskId, ?title, ?description, ?endpoint, ?resName, ?roleName, ?refRoleName, ?status, ?priority, ?startdate, ?enddate, ?duedate, ?instId, ?rootInstId, ?defId, ?DefName, ?trcTag, ?tool, ?dispatchOption, ?dispatchParam1, ?parameter)",
				WorkListDAO.class
			){
				
				public int call() throws Exception{					
					Number var_taskId = (Number)get("TASKID");
					
					if(var_taskId==null){
						IDAO valueDAO = ConnectiveDAO.createDAOImpl(
							getConnectionFactory(),
							"select NEXT VALUE FOR SEQ_BPM_WORKITEM as TASKID from DUAL",
							IDAO.class
						);
						valueDAO.select();
						valueDAO.next();
						var_taskId = (Number)valueDAO.get("TASKID");
						set("TASKID", var_taskId);
					}else{
						WorkListDAO existingWorklist = (WorkListDAO)ConnectiveDAO.createDAOImpl(
							getConnectionFactory(),
							"delete from BPM_WORKLIST where TASKID=?taskId",
							WorkListDAO.class
						);
						existingWorklist.setTaskId(var_taskId);
						existingWorklist.update();
					}
					
					setTableName("bpm_worklist");
					createInsertSql();
				
					return insert();
				}
			}
		);
	}
	
//	select bpm_func_reserveWorkItem(100, 'title','de', 'e','s',1,null,null,null,'','','','') from DUAL;
/*	public static Long bpm_func_reserveWorkItem(
		Long var_taskId,
		String var_title,
		String var_description,
		String var_endpoint,
		String var_status,
		int var_priority,
		Date var_startdate,
		Date var_enddate,
		Date var_duedate,
		String var_processInstance,
		String var_processDefinition,
		String var_tracingTag,
		String var_tool,
		String var_parameter
	) throws Exception{
		
		if(var_taskId==null){
			IDAO valueDAO = GenericDAO.createDAOImpl(
				DAOFactory.getConnectionFactory(),
				"select NEXT VALUE FOR SEQ_BPM_WORKITEM as TASKID from DUAL",
				IDAO.class
			);
			valueDAO.select();
			valueDAO.next();
			var_taskId = (Long)valueDAO.get("TASKID");
		}
				
		WorkListDAO worklist = (WorkListDAO)GenericDAO.createDAOImpl(
			DAOFactory.getConnectionFactory(),
			"insert into bpm_worklist(taskId, title, description, endpoint, status, priority, startdate, enddate, duedate, processInstance, processDefinition, tracingTag, tool, parameter)	values(?taskId, ?title, ?description, ?endpoint, ?status, ?priority, ?startdate, ?enddate, ?duedate, ?processInstance, ?processDefinition, ?tracingTag, ?tool, ?parameter)",
			WorkListDAO.class
		);

		worklist.setTaskId(var_taskId);
		worklist.setTitle(var_title);
		worklist.setDescription(var_description);
		worklist.setEndpoint(var_endpoint);
		worklist.setStatus(var_status);
		worklist.setPriority(new Long(var_priority));
		worklist.setStartDate(var_startdate);
		worklist.setEndDate(var_enddate);
		worklist.setDueDate(var_duedate);
		worklist.setProcessInstance(new Long(var_processInstance));
		worklist.setProcessDefinition(new Long(var_processDefinition));
		worklist.setTracingTag(var_tracingTag);
		worklist.setTool(var_tool);
		worklist.setParameter(var_parameter);

		worklist.insert();

		return var_taskId;
	}
*/
	public KeyGeneratorDAO createKeyGenerator(String forWhat, Map options) throws Exception {
		return (KeyGeneratorDAO)create(
			KeyGeneratorDAO.class,
			"SELECT NEXT VALUE FOR SEQ_BPM_"+forWhat+" AS keyNumber FROM dual"
		);
	}
	
	public Calendar getNow() throws Exception {
		String sql = "select CURRENT_TIMESTAMP as NOW from dual";
		
		IDAO nowQuery = (IDAO)create(IDAO.class, sql);
		nowQuery.select();
		
		if(nowQuery.next()){
			Calendar now = Calendar.getInstance();
			now.setTime((Date)nowQuery.get("NOW"));
			//now.setTimeZone(TimeZone.getDefault());
			
			return now;
		}else{
			throw new Exception("Can't get current system date from DB.");
		}
	}
	
	@Override
	public String getSequenceSql(String seqName) throws Exception {
		// TODO Auto-generated method stub
		return " (select NEXT VALUE FOR SEQ_BPM_" + seqName + " from DUAL) "; 
	}

	public String getDBMSProductName() throws Exception {
		return "HSQL";
	}
	
}
