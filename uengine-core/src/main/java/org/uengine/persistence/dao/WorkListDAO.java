/*
 * Created on 2004. 11. 3.
 */
package org.uengine.persistence.dao;

import org.uengine.util.dao.*;
import java.util.Date;

/**
 * @author Jinyoung Jang
 */


/* schema for Oracle *************************************
 
  		drop table bpm_worklist;
		create table bpm_worklist(
			taskId number,
			title varchar2(200),
			description varchar2(500),
			endpoint varchar2(200),
			status varchar2(100),
			priority int,
			startdate date, -- available if reserved WI
			enddate date,
			duedate date,
			processInstance varchar2(200),
			processDefinition varchar2(200),
			tracingTag varchar2(100),
			tool varchar2(200),
			parameter varchar2(4000), -- keyed values
			constraint pk_worklist primary key(taskId)
		);
		
		drop sequence bpm_seq_workitem;
		create sequence bpm_seq_workitem;
		
		create or replace function bpm_func_reserveWorkItem(
			var_title bpm_worklist.title%Type,
			var_description bpm_worklist.description%Type,
			var_endpoint bpm_worklist.endpoint%Type,
			var_status bpm_worklist.status%Type,
			var_priority bpm_worklist.priority%Type,
			var_startdate bpm_worklist.startdate%Type,
			var_enddate bpm_worklist.enddate%Type,
			var_duedate bpm_worklist.duedate%Type,
			var_processInstance bpm_worklist.processInstance%Type,
			var_processDefinition bpm_worklist.processDefinition%Type,
			var_tracingTag bpm_worklist.tracingTag%Type,
			var_tool bpm_worklist.tool%Type,
			var_parameter bpm_worklist.parameter%Type
		)
		return number
		IS
			var_taskId number;
		begin
			select bpm_seq_workitem.nextVal into var_taskId from dual;

			insert into bpm_worklist(taskId, title, description, endpoint, status, priority, startdate, enddate, duedate, processInstance, processDefinition, tracingTag, tool, parameter)
			values(var_taskId, var_title, var_description, var_endpoint, var_status, var_priority, var_startdate, var_enddate, var_duedate, var_processInstance, var_processDefinition, var_tracingTag, var_tool, var_parameter);

			return var_taskId;
		end;
 
 
 * schema for HSQL **********************************
 
		create table bpm_worklist(
			taskId int,
			title varchar(200),
			description varchar(500),
			endpoint varchar(200),
			status varchar(100),
			priority int,
			startdate date,
			enddate date,
			duedate date,
			processInstance varchar(200),
			processDefinition varchar(200),
			tracingTag varchar(100),
			tool varchar(200),
			parameter varchar,
			constraint pk_worklist primary key(taskId)
		);

--		CREATE ALIAS BPM_FUNC_RESERVEWORKITEM FOR "org.uengine.persistence.dao.HSQLDAOFactory.bpm_func_reserveWorkItem";
		CREATE SEQUENCE BPM_SEQ_WORKITEM
		CREATE TABLE DUAL(
			id varchar
		);
		insert into DUAL values ('value');
		
		select NEXT VALUE FOR BPM_SEQ_WORKITEM from DUAL
		
***************************************************/



public interface WorkListDAO extends IDAO{

	public Number getTaskId();
	public void setTaskId(Number id);

	public String getTitle();
	public void setTitle(String title);
	
	public String getDescription();
	public void setDescription(String description);

	public String getEndpoint();
	public void setEndpoint(String userId);

	public String getRoleName();
	public void setRoleName(String roleName);

	public String getRefRoleName();
	public void setRefRoleName(String roleName);

	public String getResName();
	public void setResName(String resourceName);

	public Number getInstId();
	public void setInstId(Number processId);

	public String getDefId();
	public void setDefId(String definition);

	public String getDefName();
	public void setDefName(String definition);

	public String getTrcTag();
	public void setTrcTag(String tracingTag);

	public String getTool();
	public void setTool(String tool);

	public String getParameter();
	public void setParameter(String parameters);

	public Number getPriority();
	public void setPriority(Number priority);

	public Date getStartDate();
	public void setStartDate(Date date);

	public Date getEndDate();
	public void setEndDate(Date date);

	public Date getSaveDate();
	public void setSaveDate(Date date);

	public Date getDueDate();
	public void setDueDate(Date date);

	//status : RESERVED, REFERENCE, NEW, CONFIRMED, SUSPENDED, COMPLETED 
	public String getStatus();
	public void setStatus(String status);
	
	public int getDispatchOption();
	public void setDispatchOption(int val);
	
	public String getDispatchParam1();
	public void setDispatchParam1(String val);
	
	public String getPrevUserName();
	public String setPrevUserName(String name);

	public Number getRootInstId();
	public Number setRootInstId(Number value);
	
	public Date getReadDate();
	public Date setReadDate(Date value);
	
	public String getActType();
	public String setActType(String actType);

	public String getAbsTrcTag();
	public String setAbsTrcTag(String value);
	
	public Boolean getIsDelegated();
	public Boolean setIsDelegated(Boolean value);

	public Boolean getIsUrget();
	public Boolean setIsUrget(Boolean value);
	
	public String getExecScope();
	public void setExecScope(String execScope);
	
	public String getExt1();
	public String setExt1(String value);
	
	public String getExt2();
	public String setExt2(String value);
	
	public String getExt3();
	public String setExt3(String value);
	
	public String getExt4();
	public String setExt4(String value);
	
	public String getExt5();
	public String setExt5(String value);
	
	public String getExt6();
	public String setExt6(String value);
	
	public String getExt7();
	public String setExt7(String value);
	
	public String getExt8();
	public String setExt8(String value);
	
	public String getExt9();
	public String setExt9(String value);
	
	public String getExt10();
	public String setExt10(String value);

}
