package org.uengine.processmanager;

import java.io.InputStream;
import java.io.Serializable;
import java.rmi.RemoteException;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;
import java.util.Vector;

import org.uengine.kernel.ActivityReference;
import org.uengine.kernel.EventHandler;
import org.uengine.kernel.ProcessDefinition;
import org.uengine.kernel.ProcessInstance;
import org.uengine.kernel.ResultPayload;
import org.uengine.kernel.RoleMapping;
import org.uengine.util.export.DefinitionArchive;
import org.uengine.util.export.UEngineArchive;


/**
 * @author Jinyoung Jang
 */

public interface ProcessManagerRemote extends javax.ejb.EJBObject{

	//initialize process manager
	public String initialize(String defVerId, String instanceId, RoleMapping loggedRoleMapping) throws RemoteException;

	//process initialization, execution
	public String initializeProcess(String defVerId, String instanceName) throws RemoteException;
	public String initializeProcess(String defVerId) throws RemoteException;
	public String initializeProcessIfRequired(String defVerId, String instanceId) throws RemoteException;

	public void executeProcess(String instanceId) throws RemoteException;
	public void executeProcessByWorkitem(String instanceId, ResultPayload resultPayload) throws RemoteException;
	
	//process variable
	public void setProcessVariable(String instanceId, String scope, String varKey, Serializable val) throws RemoteException;
	public Serializable getProcessVariable(String instanceId, String scope, String varKey) throws RemoteException;
	public String getProcessVariableInXML(String instanceId, String scope, String varKey) throws RemoteException;
	public Hashtable listProcessVariableValues(String instanceId) throws RemoteException;
	
	//role
	public void putRoleMapping(String instanceId, String roleName, String endpoint) throws RemoteException;
	public void putRoleMapping(String instanceId, RoleMapping roleMapping) throws RemoteException;
	public void delegateRoleMapping(String instanceId, String roleName, String endpoint) throws RemoteException;
	public void delegateRoleMapping(String instanceId, RoleMapping roleMapping) throws RemoteException;
	public String getRoleMapping(String instanceId, String roleName) throws RemoteException;
	public RoleMapping getRoleMappingObject(String instanceId, String roleName) throws RemoteException;
	
	//messaging, triggering, event, workitem handling
	public Serializable sendMessage(String instanceId, String message, Serializable payload) throws RemoteException;
	public Serializable sendMessageXML(String instanceId, String message, String payload) throws RemoteException;
	public void completeWorkitem(String instanceId, String tracingTag, String taskId, ResultPayload payload) throws RemoteException; 
	public void saveWorkitem(String instanceId, String tracingTag, String taskId, ResultPayload payload) throws RemoteException;
	public void executeEventByWorkitem(String mainInstanceId, String eventName, ResultPayload resultPayload) throws RemoteException;
	public void executeEventByWorkitem(String mainInstanceId, String eventName, String triggerActivityTracingTag, ResultPayload resultPayload) throws RemoteException;
	public String[] delegateWorkitem(String instanceId, String tracingTag, RoleMapping roleMapping) throws RemoteException;

	//flow control
	public void flowControl(String command, String instanceId, String tracingTag) throws RemoteException;

	//process defintion
	public String addProcessDefinition(String name, int version, String description, boolean isAdhoc, String strDef, String folder, String defId, String alias) throws RemoteException;
	public String addProcessDefinition(String name, int version, String description, boolean isAdhoc, String strDef, String folder, String defId, String objectType, String alias) throws RemoteException;
	public String addProcessDefinition(String name, int version, String description, boolean isAdhoc, String strDef, String folder, String defId, String objectType, String alias, String superDefId) throws RemoteException;
	public String addProcessDefinition(String name, int version, String description, boolean isAdhoc, ProcessDefinition def, String folder, String defId) throws RemoteException;
	public void removeProcessDefinition(String defVerId) throws RemoteException;
	public void renameProcessDefinition(String pdName, String newName) throws RemoteException;
	public String getProcessDefinitionIdByAlias(String alias) throws RemoteException;
	public void setVisibleProcessDefinition(String defId, boolean isVisible) throws RemoteException;
	
	//ProcessDefinitionRemote
	public ProcessDefinitionRemote[] listProcessDefinitionRemotesLight() throws RemoteException;
	public ProcessDefinitionRemote[] findAllVersions(String defId) throws RemoteException;
	public ProcessDefinitionRemote getProcessDefinitionRemote(String defVerId) throws RemoteException;
	public ProcessDefinitionRemote getProcessDefinitionRemoteByDefinitionId(String defId) throws RemoteException;
	public ProcessDefinitionRemote getProcessDefinitionRemoteWithInstanceId(String instanceId) throws RemoteException;
	
	//Production Version
	public String getProductionVersionIdAtThatTime(String defId, Date thatTime) throws RemoteException;	
	public String getFirstProductionVersionId(String defId) throws RemoteException;
	public String getProcessDefinitionProductionVersionByAlias(String alias) throws RemoteException;
	public void setProcessDefinitionProductionVersion(String pdvid) throws RemoteException;
	public String getProcessDefinitionProductionVersion(String defId) throws RemoteException;
	public String getProcessDefinitionProductionVersionByName(String pdName) throws RemoteException;

	//getProcessDefinition
	public ProcessDefinition getProcessDefinition(String defVerId) throws RemoteException;
	public String getProcessDefinition(String defVerId, String encodingStyle) throws RemoteException;
	public ProcessDefinition getProcessDefinitionWithInstanceId(String instanceId) throws RemoteException;
	public String getProcessDefinitionWithInstanceId(String instanceId, String encodingStyle) throws RemoteException;
	
	//public ProcessDefinition getProcessDefinitionWithoutInheritance(String defVerId) throws RemoteException;
	public String getProcessDefinitionWithoutInheritance(String defVerId, String encodingStyle) throws RemoteException;
	//public ProcessDefinition getProcessDefinitionWithInstanceIdWithoutInheritance(String instanceId) throws RemoteException;
	public String getProcessDefinitionWithInstanceIdWithoutInheritance(String instanceId, String encodingStyle) throws RemoteException;

	public String getResource(String resourceId) throws RemoteException;
    
	//ProcessInstanceRemote
	public ProcessInstanceRemote[] listProcessInstanceRemotes() throws RemoteException;
	public ProcessInstanceRemote[] listProcessInstanceRemotes(String defId) throws RemoteException;
	public ProcessInstanceRemote[] listProcessInstanceRemotes(String defId, String status) throws RemoteException;
	public ProcessInstanceRemote[] listProcessArchiveRemotes() throws RemoteException;
	
	//process instance
	public void removeProcessInstance(String instanceId) throws RemoteException;
	public void stopProcessInstance(String instanceId) throws RemoteException;	
	public ProcessInstance getProcessInstance(String instanceId) throws RemoteException;
	public void setProcessInstanceInfo(String instanceId, String info) throws RemoteException;
	public void setProcessInstanceStatus(String instanceId, String status) throws RemoteException;
	
	//activity
	public Serializable getActivityProperty(String defVerId, String tracingTag, String propertyName) throws RemoteException;
	public Serializable getActivityPropertyFromInstance(String defVerId, String tracingTag, String propertyName) throws RemoteException;
	public Hashtable getActivityDetails(String defVerId, String tracingTag) throws RemoteException;
	public String getActivityStatus(String instanceId, String tracingTag) throws RemoteException;
	public HashMap getInitiationParameterMap(String pvdid) throws RemoteException;
	
	//activityInstance
	public Hashtable getActivityInstanceDetails(String instanceId, String tracingTag) throws RemoteException;
	public Serializable getActivityInstanceProperty(String instanceId, String tracingTag, String propertyName) throws RemoteException;
	public void setActivityInstanceProperty(String instanceId, String tracingTag, String propertyName, Serializable value) throws RemoteException;
	public void setActivityInstanceProperty(String instanceId, String tracingTag, String propertyName, Serializable value, Class valueType) throws RemoteException;
	public Serializable doActivityAction(String instanceId, String tracingTag, String actionName, Serializable[] parameters, Class[] parameterTypes) throws RemoteException;
		
	//import and export
	public void exportProcessDefinitionbyDefinitionId(String defId, boolean allVersion) throws Exception;
    public void exportProcessDefinitionbyVersionId(String defVerId) throws Exception;
    public Vector importProcessDefinition(String parentFolder, InputStream loadedZipFile, UEngineArchive editedUa, String[] command ) throws Exception;
    public Hashtable importProcessAliasCheck(InputStream is) throws Exception;
	public DefinitionArchive[] importDefinitionArchiveList(InputStream is) throws RemoteException;
	
	//flow chart
	public String viewProcessDefinitionFlowChart(String defVerId, Map options) throws RemoteException;
	public String viewProcessInstanceFlowChart(String instanceId, Map options) throws RemoteException;

	//folder
	public void removeFolder(String folderName) throws RemoteException;
	public String addFolder(String folderName, String parentFolder) throws RemoteException;
	public void moveFolder(String defId, String parentFolder) throws RemoteException;
	
	//dynamic change
	public void changeProcessDefinition(String instanceId, String definitionInXML) throws RemoteException;
	public void changeProcessDefinition(String instanceId, ProcessDefinition definition) throws RemoteException;
	
	//test
	public void setFault(String instanceId, String tracingTag, Exception fault) throws RemoteException;
	
	//signaling all the changes can be applied
	public void applyChanges() throws RemoteException;
	public void cancelChanges() throws RemoteException;

	//user can pass some parameters into the kernel
	public void setGenericContext(Map genericContext) throws RemoteException;
	public void setLoggedRoleMapping(RoleMapping roleMapping) throws RemoteException;
	public EventHandler[] getEventHandlersInAction(String instanceId) throws RemoteException;
	public ActivityReference getInitiatorHumanActivityReference(String pdvid) throws RemoteException;
	
	//for Process Market
	
	public void importProcessDefinitionGraciously(String parentFolder, String itemId, String itemFilePath, String loggedUserGlobalCom) throws Exception;
	public String exportProcessDefinitionForAddToMarket(String defId, String loggedUserGlobalCom) throws Exception;

}	