package org.uengine.kernel.workflow;


import javax.servlet.ServletRequest;

import org.uengine.kernel.GlobalContext;
import org.uengine.processmanager.ProcessManagerRemote;

public abstract class WorkflowHandler {

	final public static String CMD_EXCUTE_PROCESS="excute_process";
	final public static String CMD_ACTIVITY_COMPLETE="activity_complete";
	final public static String CMD_PROCESS_COMPENSATE="process_compensate";
	final public static String CMD_WORKITEM_ACCEPT="workitem_accept";
	final public static String CMD_SET_VARIABLE="setProcessVariable";
	final public static String CMD_ACTIVITY_SAVE="activity_save";
		
	final public static String ARGS_CMD="cmd";
	final public static String ARGS_ALIAS="alias";
	final public static String ARGS_INITIATOR="initiator";
	final public static String ARGS_INSTANCEID="instanceId";
	final public static String ARGS_TRACINGTAG="tracingTag";
	final public static String ARGS_TASKID="taskId";       
	final public static String ARGS_ENDPOINT="endpoint";     
	final public static String ARGS_STATUS="status";  
	final public static String ARGS_ISAPPROVE="isApprove";
	final public static String ARGS_FORMCODE="formCode";
	final public static String ARGS_WORKCODE="workCode";
	final public static String ARGS_INSTANCENAME="instanceName";
	final public static String ARGS_FLAG="flag";
	
	public abstract String doAction(ProcessManagerRemote pm,ServletRequest sr) throws Exception;
	public abstract String activityComplete(boolean isSaveOnly) throws Exception;
	public abstract String setVariables() throws Exception;
	public abstract String processCompensate() throws Exception;
	public abstract String workitemAccept() throws Exception;
	
	public static Class USE_CLASS = null;
	
	public static WorkflowHandler getInstance(){
		if(USE_CLASS==null){
			try {
				USE_CLASS = Thread.currentThread().getContextClassLoader().loadClass(GlobalContext.getPropertyString("workflowhandler.class"));
			} catch(Exception e) {
				USE_CLASS = WorkflowHandler.class;
			}
		}
		
		try {
			return (WorkflowHandler) USE_CLASS.newInstance();
		} catch (Exception e) {
			return null;
		}
	}

}
