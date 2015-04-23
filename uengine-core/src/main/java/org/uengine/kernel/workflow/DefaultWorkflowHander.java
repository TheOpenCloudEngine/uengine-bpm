package org.uengine.kernel.workflow;

import java.util.List;

import javax.servlet.ServletRequest;

import org.uengine.kernel.Activity;
import org.uengine.kernel.EJBProcessInstance;
import org.uengine.kernel.ProcessInstance;
import org.uengine.kernel.ProcessVariable;
import org.uengine.kernel.RoleMapping;
import org.uengine.kernel.ScopeActivity;
import org.uengine.processmanager.ProcessManagerRemote;
import org.uengine.util.UEngineUtil;

public class DefaultWorkflowHander extends WorkflowHandler{

	ProcessManagerRemote pm;
	ServletRequest sr;
	
	@Override
	public String doAction(ProcessManagerRemote pm, ServletRequest sr)throws Exception {
		this.pm=pm;
		this.sr=sr;
		
		String cmd = (String)sr.getParameter(WorkflowHandler.ARGS_CMD);
		String instanceId="";
		
		if(UEngineUtil.isNotEmpty(cmd) && CMD_ACTIVITY_COMPLETE.equals(cmd)){
			instanceId = activityComplete();
		}else if(UEngineUtil.isNotEmpty(cmd) && CMD_PROCESS_COMPENSATE.equals(cmd)){
			instanceId = processCompensate();
		}else if(UEngineUtil.isNotEmpty(cmd) && CMD_WORKITEM_ACCEPT.equals(cmd)){
			instanceId = workitemAccept();
		}else if(UEngineUtil.isNotEmpty(cmd) && CMD_SET_VARIABLE.equals(cmd)){
			instanceId = setVariables();
		}
		
		return instanceId;
	}
	
	public String processCompensate() throws Exception {
		String instanceId = (String)sr.getParameter(WorkflowHandler.ARGS_INSTANCEID);
		String tracingTag = (String)sr.getParameter(WorkflowHandler.ARGS_TRACINGTAG);
		ProcessInstance instance = pm.getProcessInstance(instanceId);
		Activity act = instance.getProcessDefinition().getActivity(tracingTag);
		Activity parentAct = act.getParentActivity();
		
		List<Activity> child = ((ScopeActivity)parentAct).getChildActivities();
		
		String targetActTT = "";
		if(child !=null && child.size() >0){
			Activity targetAct= (Activity)child.get(0);
			targetActTT = targetAct.getTracingTag();
		}
		
		pm.flowControl("compensateTo", instanceId, targetActTT);
		
		return instanceId;
	}
	
	public String setVariables() throws Exception {
		String instanceId = (String)sr.getParameter(WorkflowHandler.ARGS_INSTANCEID);
		ProcessInstance instance = pm.getProcessInstance(instanceId);
		setVariableValue(instance);
		return instanceId;
	}
	
	public String activityComplete( ) throws Exception {
		String instanceId=(String)sr.getParameter(WorkflowHandler.ARGS_INSTANCEID);
		boolean isNew = !UEngineUtil.isNotEmpty(instanceId);
		if(isNew){
			String alias = (String)sr.getParameter(WorkflowHandler.ARGS_ALIAS);
			String initiator = (String)sr.getParameter(WorkflowHandler.ARGS_INITIATOR);
			
			RoleMapping loggedRoleMapping = RoleMapping.create();
			loggedRoleMapping.setName("Initiator");
			loggedRoleMapping.setEndpoint(initiator);
	
			String defVerId = pm.getProcessDefinitionProductionVersionByAlias(alias);
			instanceId = pm.initializeProcess(defVerId);
			ProcessInstance instance = pm.getProcessInstance(instanceId);
			
			setVariableValue(instance);
			pm.putRoleMapping(instanceId, loggedRoleMapping);
			pm.executeProcessByWorkitem(instanceId,new org.uengine.kernel.ResultPayload());
		}else{
			instanceId = (String)sr.getParameter(WorkflowHandler.ARGS_INSTANCEID);
			String tracingTag = (String)sr.getParameter(WorkflowHandler.ARGS_TRACINGTAG);
			String taskId = (String)sr.getParameter(WorkflowHandler.ARGS_TASKID);
			
			ProcessInstance instance = pm.getProcessInstance(instanceId);
			
			setVariableValue(instance);
			pm.completeWorkitem(instanceId, tracingTag, taskId, new org.uengine.kernel.ResultPayload());
		}
		
		return instanceId;
	}

	@Override
	public String workitemAccept()throws Exception {
		
		String instanceId = (String)sr.getParameter(WorkflowHandler.ARGS_INSTANCEID);
		String tracingTag = (String)sr.getParameter(WorkflowHandler.ARGS_TRACINGTAG);
		String endpoint = (String)sr.getParameter(WorkflowHandler.ARGS_ENDPOINT);
		
		RoleMapping loggedRoleMapping = RoleMapping.create();
		loggedRoleMapping.setEndpoint(endpoint);
		
		String[] taskIds = pm.delegateWorkitem(instanceId, tracingTag, loggedRoleMapping);
		
		return instanceId;
	}

	private void setVariableValue(ProcessInstance pi) throws Exception {
		ProcessVariable[] pvs = pi.getProcessDefinition().getProcessVariables();
		if(pvs !=null){
			for(int i=0; i < pvs.length ; i++ ){
				String pvName = pvs[i].getName();
				String value = sr.getParameter(pvName);
				if(UEngineUtil.isNotEmpty(value)){
					pi.set("",pvName,value);
				}
			}
		}
		
		String workCode = (String)sr.getParameter(WorkflowHandler.ARGS_WORKCODE);
		if(UEngineUtil.isNotEmpty(workCode)){
			((EJBProcessInstance)pi).getProcessInstanceDAO().set("ext1", workCode);
		}
	
		String instanceName = (String)sr.getParameter(WorkflowHandler.ARGS_INSTANCENAME);
		if(UEngineUtil.isNotEmpty(instanceName)){
			pi.setName(instanceName);
		}
		
	}

	@Override
	public String activityComplete(boolean isSaveOnly) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

}
