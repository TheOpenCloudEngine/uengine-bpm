package org.uengine.persistence.processinstance;

import org.uengine.kernel.GlobalContext;
import org.uengine.persistence.AbstractDAOType;
import org.uengine.persistence.DAOType;
import org.uengine.persistence.dao.DAOFactory;
import org.uengine.processmanager.TransactionContext;

import org.uengine.util.UEngineUtil;
import org.uengine.util.dao.ConnectiveDAO;

public class ProcessInstanceDAOType extends AbstractDAOType {
	
	public static ProcessInstanceDAOType getInstance(TransactionContext ptc) {
		return (ProcessInstanceDAOType)getInstance(ProcessInstanceDAOType.class, ptc);
	}
	
	public ProcessInstanceDAO createDAOImpl(String sql) throws Exception {
		ProcessInstanceDAO procInsDAO = (ProcessInstanceDAO)ConnectiveDAO.createDAOImpl(getTransactionContext(), sql, ProcessInstanceDAO.class);
		return procInsDAO;
	}

	public ProcessInstanceDAO createDAOForInsert() throws Exception {
		ProcessInstanceDAO procInsDAO = createDAOImpl(null);
		procInsDAO.getImplementationObject().createInsertSql();
		procInsDAO.getImplementationObject().setTableName("BPM_PROCINST");
		return procInsDAO;
	}

	public String findByPrimaryKey_SQL =
		"SELECT " + 
//		"InstID, Name, DefID, DefName, DefVerId," +
//		"StartedDate, FinishedDate, dueDate, Status, Info, IsAdhoc, IsArchive, IsSubprocess, RootInstId," +
//		"DefPath, mainInstId, mainActTrcTag, defmoddate, ext1, ext2 " +
		"* " + 
		"FROM BPM_PROCINST " +
		"WHERE InstID = ?InstID"
	;
	
	public String findByDefVerId_SQL =
		"SELECT " + 
		"* " + 
		"FROM BPM_PROCINST " +
		"WHERE DefVerID = ?DefVerID"
	;
	
	public String getSiblingProcessInstances_SQL =
		"select instid from bpm_procinst where rootinstid = ?rootinstid";
	
	public String getSiblingProcessInstancesIncludeRunAndForgetSubProcess_SQL =
		"select instid from bpm_procinst where rootinstid = ?rootinstid and (dontreturn is null or dontreturn ='0')";
	
	public String removeProcessInstances_SQL =
		"update bpm_procinst set isdeleted=1, finisheddate=?finisheddate where rootinstid = ?rootinstid";
	

	public ProcessInstanceDAO getSiblingProcessInstances(Long rootInstanceId, boolean isIncludeRunAndForgetSubProcess) throws Exception {
		ProcessInstanceDAO processInstance = null;
		if (isIncludeRunAndForgetSubProcess) {
			processInstance = createDAOImpl(getSiblingProcessInstances_SQL);	
		} else {
			processInstance = createDAOImpl(getSiblingProcessInstancesIncludeRunAndForgetSubProcess_SQL);
		}
		processInstance.setRootInstId(rootInstanceId);
		processInstance.select();
		return processInstance;
	}
	
	public ProcessInstanceDAO removeProcessInstance(Long rootInstanceId) throws Exception {
		ProcessInstanceDAO processInstance = createDAOImpl(removeProcessInstances_SQL);
		processInstance.setRootInstId(rootInstanceId);
		processInstance.setFinishedDate(GlobalContext.getNow(getTransactionContext()).getTime());
		processInstance.update();
		return processInstance;
	}
	
	public ProcessInstanceDAO findByPrimaryKey (Long instanceId) throws Exception {
		ProcessInstanceDAO procInsDAO = createDAOImpl(findByPrimaryKey_SQL);
		procInsDAO.setInstId(instanceId);
		procInsDAO.select();
		if ( procInsDAO.size() > 0 ) procInsDAO.next();
		else throw new Exception("Process Instance where ID=["+instanceId+"] is not found.");
		
		return procInsDAO;
	}

	public ProcessInstanceDAO findByDefinitionVersion (String defVerId) throws Exception {
		ProcessInstanceDAO procInsDAO = createDAOImpl(findByDefVerId_SQL);
		procInsDAO.setDefVerId(defVerId);
		procInsDAO.select();
		
		return procInsDAO;
	}

	public void archiveInstance(Long instanceId, String archivePath) throws Exception{
		//Move ProcessInstance to ProcessArchive
		ProcessInstanceDAO storedProcedure_ArchiveProcess = (ProcessInstanceDAO)(ProcessInstanceDAO)ConnectiveDAO.createDAOImpl(
			getTransactionContext(),
			"{call bpm_proc_archiveProcess(?instId, ?defPath)}",
			ProcessInstanceDAO.class
		);
		storedProcedure_ArchiveProcess.setInstId(instanceId);
		storedProcedure_ArchiveProcess.setDefPath(archivePath);
		storedProcedure_ArchiveProcess.call();
	}

}
