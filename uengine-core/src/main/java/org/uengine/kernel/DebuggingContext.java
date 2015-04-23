package org.uengine.kernel;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.uengine.processmanager.TransactionContext;

public class DebuggingContext implements NeedArrangementToSerialize{
	
	Map processDefinitionMap;
	Map processInstanceMap;
	Map processDefinitionIdForEachInstanceId;
	List executionPaths;
	String rootProcessInstanceId;

	ProcessInstance getRootProcessInstance(){
		return (ProcessInstance) processInstanceMap.get(rootProcessInstanceId);
	}
	
	public DebuggingContext (TransactionContext tc, String rootProcessInstanceId) throws Exception{
		this (tc, rootProcessInstanceId, false);
	}
		
	public DebuggingContext (TransactionContext tc, String rootProcessInstanceId, boolean includeExecutionPaths) throws Exception{
		processInstanceMap = tc.getProcessInstancesInTransaction();
		this.rootProcessInstanceId = rootProcessInstanceId;
		this.processDefinitionMap = new Hashtable();
		this.processDefinitionIdForEachInstanceId = new Hashtable();
		
		String pid;
		for(Iterator iter = processInstanceMap.keySet().iterator(); iter.hasNext();){
			pid = (String)iter.next();
			
			ProcessInstance instance = (ProcessInstance)processInstanceMap.get(pid);
			processDefinitionMap.put(instance.getProcessDefinition().getId(), instance.getProcessDefinition());
			processDefinitionIdForEachInstanceId.put(instance.getInstanceId(), instance.getProcessDefinition().getId());
		}
		
		if(includeExecutionPaths){
			executionPaths = new ArrayList();
			List executedPaths = tc.getExecutedActivityInstanceContextsInTransaction();
			
			for(int i=0; executedPaths.size() < i; i++){
				ActivityInstanceContext aic = (ActivityInstanceContext) executedPaths.get(i);

				Activity activity = aic.getActivity();
				
				activity = (Activity) activity.clone();
				activity.setParentActivity(null);
				
				executionPaths.add(activity);
			}
		}
		
	}

	public void beforeSerialization() {
		// TODO Auto-generated method stub
		
	}

	public void afterDeserialization() {
		String pid;
		for(Iterator iter = processDefinitionIdForEachInstanceId.keySet().iterator(); iter.hasNext();){
			pid = (String)iter.next();
			String defId = (String) processDefinitionIdForEachInstanceId.get(pid);
			ProcessDefinition def = (ProcessDefinition) processDefinitionMap.get(defId);
			
			ProcessInstance instance = (ProcessInstance)processInstanceMap.get(pid);
			instance.setProcessDefinition(def);
		}
	}

	public Map getProcessDefinitionIdForEachInstanceId() {
		return processDefinitionIdForEachInstanceId;
	}

	public Map getProcessDefinitionMap() {
		return processDefinitionMap;
	}

	public Map getProcessInstanceMap() {
		return processInstanceMap;
	}

	public String getRootProcessInstanceId() {
		return rootProcessInstanceId;
	}

	public List getExecutionPaths() {
		return executionPaths;
	}

	public void setExecutionPaths(List executionPaths) {
		this.executionPaths = executionPaths;
	}
	
}
