package org.uengine.processmanager;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.io.OutputStreamWriter;
import java.io.Serializable;
import java.rmi.RemoteException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import javax.ejb.EJBException;
import javax.ejb.EJBHome;
import javax.ejb.EJBObject;
import javax.ejb.Handle;
import javax.ejb.ObjectNotFoundException;
import javax.ejb.RemoveException;
import javax.ejb.SessionBean;
import javax.ejb.SessionSynchronization;
import javax.naming.NamingException;

import org.uengine.contexts.TextContext;
//import org.uengine.formmanager.trans.Html2FormView;
//import org.uengine.formmanager.trans.Html2Write;
import org.uengine.kernel.Activity;
import org.uengine.kernel.ActivityInstanceContext;
import org.uengine.kernel.ActivityReference;
import org.uengine.kernel.ComplexActivity;
import org.uengine.kernel.DefaultProcessInstance;
import org.uengine.kernel.EventHandler;
import org.uengine.kernel.EventMessagePayload;
import org.uengine.kernel.GlobalContext;
import org.uengine.kernel.HumanActivity;
import org.uengine.kernel.KeyedParameter;
import org.uengine.kernel.ProcessDefinition;
import org.uengine.kernel.ProcessDefinitionFactory;
import org.uengine.kernel.ProcessInstance;
import org.uengine.kernel.ProcessVariable;
import org.uengine.kernel.ProcessVariableValue;
import org.uengine.kernel.ResultPayload;
import org.uengine.kernel.Role;
import org.uengine.kernel.RoleMapping;
import org.uengine.kernel.SubProcessActivity;
import org.uengine.kernel.UEngineException;
import org.uengine.persistence.dao.UniqueKeyGenerator;
import org.uengine.persistence.processdefinition.ProcessDefinitionDAO;
import org.uengine.persistence.processdefinition.ProcessDefinitionDAOType;
import org.uengine.persistence.processdefinition.ProcessDefinitionRepositoryHomeLocal;
import org.uengine.persistence.processdefinition.ProcessDefinitionRepositoryLocal;
import org.uengine.persistence.processdefinition.ProductionDefinitionDAO;
import org.uengine.persistence.processdefinitionversion.ProcessDefinitionVersionRepositoryHomeLocal;
import org.uengine.persistence.processdefinitionversion.ProcessDefinitionVersionRepositoryLocal;
import org.uengine.persistence.processinstance.ProcessInstanceRepositoryHomeLocal;
import org.uengine.persistence.processinstance.ProcessInstanceRepositoryLocal;
import org.uengine.security.AclManager;
import org.uengine.security.Authority;
import org.uengine.util.ActivityForLoop;
import org.uengine.util.DeleteDir;
import org.uengine.util.FileCopy;
import org.uengine.util.UEngineUtil;
import org.uengine.util.ZipEntryMapper;
import org.uengine.util.dao.ConnectionFactory;
import org.uengine.util.dao.DefaultConnectionFactory;
import org.uengine.util.export.DefinitionArchive;
import org.uengine.util.export.UEngineArchive;

//import au.id.jericho.lib.html.FormField;
//import au.id.jericho.lib.html.FormFields;
//import au.id.jericho.lib.html.OutputDocument;
//import au.id.jericho.lib.html.Source;

/**
 * @author Jinyoung Jang
 */

public class ProcessManagerBean implements SessionBean, SessionSynchronization, ProcessManagerRemote{

	//for debug
	static long firstRequestedTime=0L;
	static boolean DEBUG = GlobalContext.getPropertyString(
			"server.invocationlog",
			"false"
		).equalsIgnoreCase("true");
		
	static boolean DEBUG_PARAMETER_OUT_WITH_XML = false;
	static String INVOCATION_LOG_FILE = "ue_invoc";
	
	private List<String> _DUMMY_LIST_ = null;
	
	static void log(String methodName, String messageStr, String sourceCodes){
		if(!DEBUG) return;
		StringBuffer message = new StringBuffer();
		StringBuffer src = new StringBuffer();
		
		message.append("\n\n#- ProcessManagerBean invocation log ------------------\n");

		long now = System.currentTimeMillis();
		if(firstRequestedTime==0L){
					
			firstRequestedTime = now;
			
		}
		
		message.append("Time: " + (now-firstRequestedTime) + "\n");
		message.append("Method: " + methodName + "\n");
		message.append("Arguments: \n");
		
		src.append("waitUntil(" + (now-firstRequestedTime) + ");\n");
		src.append("pmCall(\"" + methodName + "\", new String[]{\n");
		src.append(sourceCodes+"\n});\n");

		message.append(messageStr + "\n");		
		message.append("Invocation Code: \n");
		message.append(src);
		message.append("------------------------------------------------------#\n\n");
		
		try{
			FileWriter fw = new FileWriter(INVOCATION_LOG_FILE + firstRequestedTime + ".log", true);
			fw.write(src.toString()+"\n");
			fw.close();
		}catch(Exception e){
		}

		//System.out.println(message.toString());
	}
	
	public ProcessManagerBean(){
		//System.out.println("ProcessManagerBean::init()");
		setConnectionFactory(DefaultConnectionFactory.create());
	}

	static void logInst(String methodName, Object[] parameters){
		parameters[0] = "<instanceId>";
		log(methodName, parameters);
	}
	
	static void log(String methodName, Object[] parameters){
		StringBuffer message = new StringBuffer();
		StringBuffer src = new StringBuffer();
		
		for(int i=0; i<parameters.length; i++){
			String xml = null;
			if(parameters[i]!=null){
				if(parameters[i].equals("<instanceId>")){
					xml = "<instanceId>";
				}else{
					ByteArrayOutputStream bao = new ByteArrayOutputStream();
					try{
						GlobalContext.serialize(parameters[i], bao, String.class);	
						xml = bao.toString(GlobalContext.DATABASE_ENCODING);
					}catch(Exception e){					
					}
				}
			}
			if (message.length() > 0) message.append(", \n"); 
			message.append(xml);
			
			if (xml != null) {
				xml = xml.replaceAll("\n", "\\\\n").replaceAll("\r", "\\\\r").replaceAll("\"", "\\\\\"");
			}
			
			if (src.length() > 0) src.append(", \n");
			src.append(xml!=null ? ("\"" + xml + "\"") : "null");
		}
		//System.out.println(src.toString());
		log(methodName, message.toString(), src.toString());
	}
	//

	transient private ProcessTransactionContext transactionContext;
		public ProcessTransactionContext getTransactionContext() {
			if(transactionContext==null)
				 transactionContext = new ProcessTransactionContext(this);
			
			return transactionContext;
		}
		public void setTransactionContext(ProcessTransactionContext transactionContext) {
			this.transactionContext = transactionContext;
		}


	ConnectionFactory connectionFactory;
		public ConnectionFactory getConnectionFactory() {
			return connectionFactory;
		}
	
		public void setConnectionFactory(ConnectionFactory connectionFactory) {
			this.connectionFactory = connectionFactory;
		}
	
	boolean autoCloseConnection = true;
		public boolean isAutoCloseConnection() {
			return autoCloseConnection;
		}
		public void setAutoCloseConnection(boolean autoCloseConnection) {
			this.autoCloseConnection = autoCloseConnection;
		}	

	public javax.ejb.SessionContext ejbContext;
	public javax.naming.Context jndiContext;
	public Map genericContext;
	
	boolean isManagedTransaction = true;
		public boolean isManagedTransaction() {
			return isManagedTransaction;
		}
	
		public void setManagedTransaction(boolean isManagedTransaction) {
			this.isManagedTransaction = isManagedTransaction;
		}


	public void ejbCreate() {
		transactionContext = new ProcessTransactionContext(this);
    }
	
	private ProcessInstance getInstance(String instId) throws Exception{
		HashMap options = new HashMap();

//System.out.println("instId = " + instId);		
		
		if(transactionContext==null)
			 transactionContext = new ProcessTransactionContext(this);
		options.put("ptc", transactionContext);

		if(!UEngineUtil.isNotEmpty(instId)){
			if(transactionContext.getTemporaryInstance()!=null)
				return transactionContext.getTemporaryInstance();
			
			ProcessInstance temporaryInstance = ProcessInstance.create().getInstance(instId, options);
			transactionContext.setTemporaryInstance(temporaryInstance);
			return temporaryInstance;
		}
		
		ProcessInstance instance = ProcessInstance.create().getInstance(instId, options);

		return instance;
	}

	public String initializeProcess(String processDefinition) throws RemoteException{
//   		log("initializeProcess", new Object[]{processDefinition});
   		
		return initializeProcess(processDefinition, null);
	}

	public String initializeProcess(String processDefinition, String name) throws RemoteException{
		log("initializeProcess", new Object[]{processDefinition, name});

		try{
//System.out.println("ProcessManagerBean::initialize");
			HashMap options = new HashMap();
			
			if(transactionContext==null)
				 transactionContext = new ProcessTransactionContext(this);
		
			options.put("ptc", transactionContext);
			ProcessInstance instance = getDefinition(processDefinition).createInstance(name, options);
			
			return instance.getInstanceId();
		}catch(Exception e){
			e.printStackTrace();
			throw new RemoteException("ProcessManagerError:"+e.getMessage(), e);
		}
	}

	public void executeProcess(String instanceId) throws RemoteException{
		logInst("executeProcess", new Object[]{instanceId});
		try{
			ProcessInstance instance = getInstance(instanceId);
			
			instance.execute();
		} catch(Exception e) {
			e.printStackTrace();
			throw new RemoteException("ProcessManagerError:"+e.getMessage(), e);
		}
	}
	
	public void executeProcessByWorkitem(String instanceId, ResultPayload resultPayload) throws RemoteException{
		logInst("executeProcessByWorkitem", new Object[]{instanceId, resultPayload});
		try{
			ProcessInstance instance = getInstance(instanceId);
			ProcessDefinition definition = instance.getProcessDefinition();
			
			definition.setQueuingMechanism(instance, ProcessDefinition.QUEUINGMECH_SYNC);

			instance.execute();

			completeInitiatorWorkitem(instance, resultPayload);

			definition.setQueuingMechanism(instance, ProcessDefinition.QUEUINGMECH_JMS);
			//instance.endCaching();
						
		}catch(Exception e){
			e.printStackTrace();
			throw new RemoteException("ProcessManagerError:"+e.getMessage(), e);
		}
	}
	
	public String[] delegateWorkitem(String instanceId, String tracingTag, RoleMapping roleMapping) throws RemoteException{
		logInst("delegateWorkitem", new Object[]{instanceId, tracingTag, roleMapping});
		try{
			ProcessInstance instance = getInstance(instanceId);
			ProcessDefinition definition = instance.getProcessDefinition();
//			ProcessDefinition newPd = (ProcessDefinition)instance.getProcessDefinition().clone();
			HumanActivity humanActivity = (HumanActivity)definition.getActivity(tracingTag);
			
			//String newRoleName = "_" + humanActivity.getRole().getName();
//			String newRoleName = humanActivity.getRole().getName();
//
//			if (!newRoleName.matches("TRCTAG\\[[0-9]{1,}\\]:.*")) {
//				newRoleName = "TRCTAG[" + tracingTag + "]:" + newRoleName;
//			}
//
//			Role newRole = new Role();
//			newRole.setName(newRoleName);
//			humanActivity.setRole(newRole);
			
			//newPd.addRole(newRole);
			//newPd.registerToProcessDefinition(false, false);
			//changeProcessDefinition(instanceId, newPd);
			
			//roleMapping.setName(newRoleName);
			humanActivity.delegate(instance, roleMapping, false);
			
			//humanActivity.afterExecute(instance);
			
			return humanActivity.getTaskIds(instance);
		}catch(Exception e){
			e.printStackTrace();
			throw new RemoteException("ProcessManagerError:"+e.getMessage(), e);
		}
	}
	
	private void completeInitiatorWorkitem(ProcessInstance instance, ResultPayload resultPayload) throws Exception{
		ProcessDefinition definition = instance.getProcessDefinition();
		ActivityReference initiatorHumanActivityReference = definition.getInitiatorHumanActivityReference(instance.getProcessTransactionContext());
		
//		boolean isInitiationInSubprocess = false;
		String absoluteTracingTag = initiatorHumanActivityReference.getAbsoluteTracingTag();
		

		if(absoluteTracingTag.indexOf("@")>0){
//			isInitiationInSubprocess = true;
			String[] scopesByTracingTag = absoluteTracingTag.split("@");
			for(int i=0; i<scopesByTracingTag.length-1; i++){
				String scope = scopesByTracingTag[i];
				SubProcessActivity spAct = (SubProcessActivity)definition.getActivity(scope);
				List spInstanceIds = spAct.getSubprocessIds(instance);
				if(spInstanceIds.size() == 0){
					throw new UEngineException("Activity in the subprocess ["+ absoluteTracingTag +"] cannot be found.");
				}
				
				String spInstanceId = (String)spInstanceIds.get(0);
				
				instance = getProcessInstance(spInstanceId);
				definition = instance.getProcessDefinition();
			}
		}
		
		ActivityInstanceContext firstRunningActivityInstanceCtx = instance.getCurrentRunningActivity();
		HumanActivity humanActivity = (HumanActivity)firstRunningActivityInstanceCtx.getActivity();
		
		//HumanActivity humanActivity = (HumanActivity)initiatorHumanActivityReference.getActivity();
		
		if(humanActivity==null)
			throw new UEngineException("Inconsistent status. Couldn't find the initiator HumanActivity.");
			
		String message = humanActivity.getMessage();
		
		//add task id into payload
		String[] taskIds = humanActivity.getTaskIds(instance);

		if(!(instance instanceof DefaultProcessInstance) && (taskIds==null || taskIds.length<1)) throw new UEngineException("The first human work didn't start properly.");
	
		//TODO: kind of wierd code
		if(resultPayload!=null && taskIds!=null)
		resultPayload.setExtendedValue(
				new KeyedParameter(HumanActivity.PAYLOADKEY_TASKID, taskIds[0])
		);			
		//

		definition.fireMessage(message, instance, resultPayload);
	}

	public void executeEventByWorkitem(String mainInstanceId, String eventName, ResultPayload resultPayload) throws RemoteException{
		executeEventByWorkitem(mainInstanceId, eventName, null, resultPayload);
	}

	public void executeEventByWorkitem(String mainInstanceId, String eventName, String triggerActivityTracingTag, ResultPayload resultPayload) throws RemoteException{
		try{
			ProcessInstance mainProcessInstance = getInstance(mainInstanceId);
			
			//send message first
			ProcessDefinition mainProcessDefinition = mainProcessInstance.getProcessDefinition();
			EventMessagePayload eventMessagePayload = new EventMessagePayload();
			eventMessagePayload.setEventName(eventName);
			eventMessagePayload.setTriggerTracingTag(triggerActivityTracingTag);
			mainProcessDefinition.fireMessage("event", mainProcessInstance, eventMessagePayload);
			
			//get the initiated sub process instance
			EventHandler[] ehs = getEventHandlersInAction(mainInstanceId);
			EventHandler theEventHandler = null;
			for(int i=0; i<ehs.length; i++){
				if(ehs[i].getName().equals(eventName)){
					theEventHandler = ehs[i];
					break;
				}
			}
			
			Activity handlerActivity = theEventHandler.getHandlerActivity();
			
			HumanActivity humanActivity = null;
			
			if(handlerActivity instanceof SubProcessActivity){
				SubProcessActivity subProcessActivity = (SubProcessActivity)theEventHandler.getHandlerActivity();
				Vector idVt = subProcessActivity.getSubprocessIds(mainProcessInstance);
				
				String subInstanceId = (String)idVt.get(0);
				
				ProcessInstance subProcessInstance = getInstance(subInstanceId);

				completeInitiatorWorkitem(subProcessInstance, resultPayload);
				
				return;

			}

			if(handlerActivity instanceof HumanActivity){
				humanActivity = (HumanActivity)handlerActivity;
			}else if(handlerActivity instanceof ComplexActivity){
				ComplexActivity complexActivity = ((ComplexActivity)handlerActivity);
				ActivityReference initiatorHumanActivityReference = complexActivity.getInitiatorHumanActivityReference(mainProcessInstance .getProcessTransactionContext());
				
				humanActivity = (HumanActivity)initiatorHumanActivityReference.getActivity();
			}
			
			completeWorkitem(mainInstanceId, humanActivity.getTracingTag(), null, resultPayload);

		}catch(Exception e){	
			e.printStackTrace();
			throw new RemoteException("ProcessManagerError:"+e.getMessage(), e);
		}		
	}

	public void stopProcessInstance(String instanceId) throws RemoteException{
		logInst("stopProcessInstance", new Object[]{instanceId});
		try{
			ProcessInstance instance = getInstance(instanceId);			
			instance.stop();
		}catch(Exception e){
			e.printStackTrace();
			throw new RemoteException("ProcessManagerError:"+e.getMessage(), e);
		}		
	}

	public void setProcessVariable(String instanceId, String scope, String varKey, Serializable val) throws RemoteException{
		logInst("setProcessVariable", new Object[]{instanceId, scope, varKey, val});
		try{
			ProcessInstance instance = getInstance(instanceId);
			
			instance.set(scope, varKey, val);
		}catch(Exception e){	
			e.printStackTrace();
			throw new RemoteException("ProcessManagerError:"+e.getMessage(), e);
		}		
	}

	public Serializable getProcessVariable(String instanceId, String scope, String varKey) throws RemoteException{
		logInst("getProcessVariable", new Object[]{instanceId, scope, varKey});
		try{
			ProcessInstance instance = getInstance(instanceId);
			
			if(varKey.indexOf('.') < 0){
				ProcessDefinition definition = instance.getProcessDefinition();
				ProcessVariable variable = definition.getProcessVariable(varKey);
				
				if(variable == null) throw new UEngineException("Undeclared process variable reference : " + varKey);
				
				ProcessVariableValue theValue = definition.getProcessVariable(varKey).getMultiple(instance, scope);
				
				//ProcessVariableValue theValue = instance.getMultiple(scope, varKey);
				theValue.beforeFirst();
				if(theValue.size()==1)
					return theValue.getValue();
				else
					return theValue;
			}else{
				return instance.get(scope, varKey);
			}
			
		}catch(Exception e){
			e.printStackTrace();
			throw new RemoteException("ProcessManagerError:"+e.getMessage(), e);
		}		
	}
	
	public String getProcessVariableInXML(String instanceId, String scope, String varKey) throws RemoteException{
		logInst("getProcessVariableInXML", new Object[]{instanceId, scope, varKey});
		try{
			ProcessInstance instance = getInstance(instanceId);
			try{
				return instance.getInXML(scope, varKey);
			}catch(Exception e){
				Serializable serVal = instance.get(scope, varKey);
				return GlobalContext.serialize(serVal, String.class);
			}
		}catch(Exception e){
			e.printStackTrace();
			throw new RemoteException("ProcessManagerError:"+e.getMessage(), e);
		}		
	}
	
	public Hashtable listProcessVariableValues(String instanceId) throws RemoteException{
		logInst("listProcessVariableValues", new Object[]{instanceId});
		try{
			ProcessInstance instance = getInstance(instanceId);
			
			return (Hashtable)instance.getAll();
		}catch(Exception e){
			e.printStackTrace();
			throw new RemoteException("ProcessManagerError:"+e.getMessage(), e);
		}
	}
	
	public void putRoleMapping(String instanceId, String roleName, String endpoint) throws RemoteException{
		logInst("putRoleMapping", new Object[]{instanceId, roleName, endpoint});
		try{
			ProcessInstance instance = getInstance(instanceId);
			
			RoleMapping roleMap = RoleMapping.create();
			roleMap.setName(roleName);
			roleMap.setEndpoint(endpoint);
			roleMap.fill(instance);
			
			instance.putRoleMapping(roleMap);
		}catch(Exception e){
			e.printStackTrace();
			throw new RemoteException("ProcessManagerError:"+e.getMessage(), e);
		}
	}

	public String getRoleMapping(String instanceId, String roleName) throws RemoteException{
		logInst("getRoleMapping", new Object[]{instanceId, roleName});
		try{
			ProcessInstance instance = getInstance(instanceId);
			ProcessDefinition definition = instance.getProcessDefinition();

			RoleMapping roleMapping = definition.getRole(roleName).getMapping(instance);
			
			if(roleMapping==null) return null;
			
			return roleMapping.getEndpoint();			
		}catch(Exception e){
			e.printStackTrace();
			throw new RemoteException("ProcessManagerError:"+e.getMessage(), e);
		}
	}	
	
	public void putRoleMapping(String instanceId, RoleMapping roleMapping) throws RemoteException{
		logInst("putRoleMapping", new Object[]{instanceId, roleMapping});
		try{
			if(!UEngineUtil.isNotEmpty(roleMapping.getName()))
				throw new UEngineException("RoleMapping should have its name.");
			
			ProcessInstance instance = getInstance(instanceId);			
			instance.putRoleMapping(roleMapping);
		}catch(Exception e){
			e.printStackTrace();
			throw new RemoteException("ProcessManagerError:"+e.getMessage(), e);
		}
	}

	public RoleMapping getRoleMappingObject(String instanceId, String roleName) throws RemoteException{
		logInst("getRoleMappingObject", new Object[]{instanceId, roleName});
		try{
			ProcessInstance instance = getInstance(instanceId);
			ProcessDefinition definition = instance.getProcessDefinition();
			
			RoleMapping roleMapping = definition.getRole(roleName).getMapping(instance);
			if(roleMapping==null) return null;
			
			return roleMapping;
		}catch(Exception e){
			e.printStackTrace();
			throw new RemoteException("ProcessManagerError:"+e.getMessage(), e);
		}	
	}	
	
	public ProcessDefinitionRemote[] listProcessDefinitionRemotesLight() throws RemoteException{
		try{
			ProcessDefinitionRepositoryHomeLocal pdhr = GlobalContext.createProcessDefinitionRepositoryHomeLocal(getTransactionContext());
			ProcessDefinitionVersionRepositoryHomeLocal pdihr = GlobalContext.createProcessDefinitionVersionRepositoryHomeLocal(getTransactionContext());

			Collection definitions = pdhr.findAllProcessDefinitions();
			
			Vector processDefinitionRemotes = new Vector();			
			for(Iterator iter = definitions.iterator(); iter.hasNext();){
				ProcessDefinitionRepositoryLocal pdrl = ((ProcessDefinitionRepositoryLocal)iter.next());
				ProcessDefinitionRemote pdr = new ProcessDefinitionRemote();
				pdr.setId(pdrl.getDefId().toString());
				pdr.setFolder(pdrl.getIsFolder());
				pdr.setParentFolder(pdrl.getParentFolder().toString());
				pdr.setName(TextContext.createInstance());
				pdr.getName().setText(pdrl.getName());
				pdr.setBelongingDefinitionId(pdr.getId());
				pdr.setAlias(pdrl.getAlias());
				pdr.setVisible(pdrl.getIsVisible());
				
				pdr.objType = pdrl.getObjType();
				
				if(!org.uengine.util.UEngineUtil.isNotEmpty(pdr.objType)) {
					pdr.objType = "process";
				}

				
				String definitionGroupId = pdr.getBelongingDefinitionId();
				String objType = pdr.getObjType();

				if(!pdr.isFolder()){
					int productionVersion = pdrl.getProdVer();
					//Object[] nameAndVersion = UEngineUtil.getProcessNameAndVersion(pdr.getName());
					//String name = (String)nameAndVersion[0];
					
					Collection versions = pdihr.findAllVersions(new Long(pdr.getId()));
					for(Iterator iter2 = versions.iterator(); iter2.hasNext();){						
						ProcessDefinitionVersionRepositoryLocal pdirl = ((ProcessDefinitionVersionRepositoryLocal)iter2.next());
						
						pdr = new ProcessDefinitionRemote();
						pdr.setId(pdirl.getDefVerId().toString());
						pdr.setFolder(false);
						pdr.setParentFolder(pdrl.getParentFolder().toString());
						pdr.setName(TextContext.createInstance());	
						pdr.getName().setText(pdrl.getName());
						pdr.setVersion(pdirl.getVer().intValue());
						pdr.setBelongingDefinitionId(definitionGroupId);
						pdr.setObjType(objType);
						pdr.setAlias(pdrl.getAlias());
						pdr.setVisible(pdrl.getIsVisible());
						
						if(pdirl.getVer().intValue() == productionVersion)
							pdr.setProduction(true);

						processDefinitionRemotes.add(pdr);
					}					
				}else
					processDefinitionRemotes.add(pdr);
			}
			
			ProcessDefinitionRemote pds[] = new ProcessDefinitionRemote[processDefinitionRemotes.size()];
			processDefinitionRemotes.toArray(pds);

			return pds;
			
		}catch(Exception e){
			e.printStackTrace();
			throw new RemoteException("ProcessManagerError:"+e.getMessage(), e);
		}
	}
	
	public ProcessDefinitionRemote[] findAllVersions(String pdid ) throws RemoteException{
		try{
			ProcessDefinitionRepositoryHomeLocal pdhr = GlobalContext.createProcessDefinitionRepositoryHomeLocal(getTransactionContext());
			ProcessDefinitionVersionRepositoryHomeLocal pdihr = GlobalContext.createProcessDefinitionVersionRepositoryHomeLocal(getTransactionContext());
			
			String productionVersion = "-1";
			try{
				productionVersion = getProcessDefinitionProductionVersion(pdid);
			}catch(RemoteException e){}
			
			Vector processDefinitionRemotes = new Vector();
			ProcessDefinitionRemote pdr = null;
			
			if(pdid.startsWith("[")){
				String definitionAlias = pdid.substring(1, pdid.indexOf("]"));
				pdid = getProcessDefinitionIdByAlias(definitionAlias);
			}		
		
			Collection versions = pdihr.findAllVersions(new Long(pdid));
			for(Iterator iter2 = versions.iterator(); iter2.hasNext();){		
				ProcessDefinitionVersionRepositoryLocal pdirl = ((ProcessDefinitionVersionRepositoryLocal)iter2.next());
				
				pdr = new ProcessDefinitionRemote();
				pdr.setId(pdirl.getDefVerId().toString());
				pdr.setFolder(false);
				//pdr.setParentFolder(pdrl.getParentFolder().toString());
				pdr.setName(TextContext.createInstance());
				//pdr.getName().setText(pdrl.getName());
				pdr.setVersion(pdirl.getVer().intValue());
				pdr.setModifiedDate(pdirl.getModDate());
				//pdr.setBelongingDefinitionId(definitionGroupId);
				
				//if(pdirl.getVer() == productionVersion)
				if( productionVersion != null && productionVersion.equals(pdirl.getDefVerId().toString())){
					pdr.setProduction(true);
				}

				processDefinitionRemotes.add(pdr);
			}					
				

			ProcessDefinitionRemote pds[] = new ProcessDefinitionRemote[processDefinitionRemotes.size()];
			processDefinitionRemotes.toArray(pds);

			return pds;
			
		}catch(Exception e){
			e.printStackTrace();
			throw new RemoteException("ProcessManagerError:"+e.getMessage(), e);
		}
	}

	public void setProcessDefinitionProductionVersion(String pdvid) throws RemoteException{
		log("setProcessDefinitionProductionVersion", new Object[]{pdvid});
		try{
			if(pdvid.indexOf("@")>-1){
				pdvid = ProcessDefinition.splitDefinitionAndVersionId(pdvid)[1];
			}
			
			ProcessDefinitionVersionRepositoryHomeLocal pdvhr = GlobalContext.createProcessDefinitionVersionRepositoryHomeLocal(getTransactionContext());
			ProcessDefinitionVersionRepositoryLocal pdvlr = pdvhr.findByPrimaryKey(new Long(pdvid));
			
			ProcessDefinitionRepositoryHomeLocal pdhr = GlobalContext.createProcessDefinitionRepositoryHomeLocal(getTransactionContext());
			ProcessDefinitionRepositoryLocal pdlr = pdhr.findByPrimaryKey(pdvlr.getDefId());
			
			pdlr.setProdVer(pdvlr.getVer().intValue());
			pdlr.setProdVerId(new Long(pdvid));
			
			if(pdlr.getObjType()==null){
				ProcessDefinition definition = getProcessDefinition(pdvid);
				String shortDescription = definition.getShortDescription();
				if(shortDescription!=null){
					pdlr.setDescription(shortDescription);
				}
			}
			
		}catch(Exception e){
			e.printStackTrace();
			throw new RemoteException("ProcessManagerError:"+e.getMessage(), e);
		}
	}
	
	public String getProcessDefinitionProductionVersion(String pdid) throws RemoteException {
		log("getProcessDefinitionProductionVersion", new Object[]{pdid});
		try{
			if(pdid.startsWith("[")){
				String definitionAlias = pdid.substring(1, pdid.indexOf("]"));
				return getProcessDefinitionProductionVersionByAlias(definitionAlias);
			}
			ProcessDefinitionRepositoryHomeLocal pdhr = GlobalContext.createProcessDefinitionRepositoryHomeLocal(getTransactionContext());
			ProcessDefinitionRepositoryLocal pdlr = pdhr.findByPrimaryKey(new Long(pdid));
			
			Long productionVersionId = pdlr.getProdVerId();
			if(productionVersionId==null || ((Long)productionVersionId).longValue()==-1)
				throw new RemoteException("ProcessManagerError: There's no production. Make sure you have chosen a version of the process definition as production at least once.");
							
			return productionVersionId.toString();
		} catch(ObjectNotFoundException onfe) {
			throw new RemoteException("ProcessManagerError: No such processdefinition with id '" + pdid +"'. Please check '"+pdid+"' is a definition id not a definition version id.", onfe);
		} catch(Exception e) {
			//e.printStackTrace();
			throw new RemoteException("ProcessManagerError:"+e.getMessage(), e);
		}
	}
	
	public String getProcessDefinitionProductionVersionByName(String pdName) throws RemoteException{
		log("getProcessDefinitionProductionVersionByName", new Object[]{pdName});
		try{
			ProcessDefinitionRepositoryHomeLocal pdhr = GlobalContext.createProcessDefinitionRepositoryHomeLocal(getTransactionContext());
			ProcessDefinitionRepositoryLocal pdlr = pdhr.findByName(pdName);
			
			if(pdlr==null) 
				throw new RemoteException("ProcessManagerError: There's no such process definition named '" + pdName + "'");
			
			Long productionVersionId = pdlr.getProdVerId();
			if(productionVersionId==null || ((Long)productionVersionId).longValue()==-1)
				throw new RemoteException("ProcessManagerError: There's no production. Make sure you have chosen a version of the process definition as production at least once.");
							
			return productionVersionId.toString();
		}catch(ObjectNotFoundException onfe){
			UEngineException ue = new UEngineException("No such processdefinition with name '" + pdName +"'.", onfe);
			throw new RemoteException("ProcessManagerError:" + ue.getMessage(), ue);
		}catch(Exception e){
			e.printStackTrace();
			throw new RemoteException("ProcessManagerError:"+e.getMessage(), e);
		}
	}
	
	public String getProcessDefinitionProductionVersionByAlias(String alias) throws RemoteException{
		log("getProcessDefinitionProductionVersionByAlias", new Object[]{alias});
		try{
			ProcessDefinitionRepositoryHomeLocal pdhr = GlobalContext.createProcessDefinitionRepositoryHomeLocal(getTransactionContext());
			ProcessDefinitionRepositoryLocal pdlr = pdhr.findByAlias(alias);
			
			if(pdlr==null) 
				throw new RemoteException("ProcessManagerError: There's no such process definition aliased '" + alias + "'");
			
			Long productionVersionId = pdlr.getProdVerId();
			if(productionVersionId==null || ((Long)productionVersionId).longValue()==-1)
				throw new RemoteException("ProcessManagerError: There's no production. Make sure you have chosen a version of the process definition as production at least once.");
							
			return productionVersionId.toString();
		}catch(ObjectNotFoundException onfe){
			UEngineException ue = new UEngineException("No such processdefinition with alias '" + alias +"'.", onfe);
			throw new RemoteException("ProcessManagerError:" + ue.getMessage(), ue);
		}catch(Exception e){
			e.printStackTrace();
			throw new RemoteException("ProcessManagerError:"+e.getMessage(), e);
		}
	}
	
	public String getProcessDefinitionIdByAlias(String alias) throws RemoteException{
		log("getProcessDefinitionIdByAlias", new Object[]{alias});
		try{
			ProcessDefinitionRepositoryHomeLocal pdhr = GlobalContext.createProcessDefinitionRepositoryHomeLocal(getTransactionContext());
			ProcessDefinitionRepositoryLocal pdlr = pdhr.findByAlias(alias);
			
			return pdlr.getDefId().toString();
		}catch(ObjectNotFoundException onfe){
			UEngineException ue = new UEngineException("No such processdefinition with alias '" + alias +"'.", onfe);
			throw new RemoteException("ProcessManagerError:" + ue.getMessage(), ue);
		}catch(Exception e){
			e.printStackTrace();
			throw new RemoteException("ProcessManagerError:"+e.getMessage(), e);
		}
	}
	
	public ProcessInstanceRemote[] listProcessInstanceRemotes() throws RemoteException{
//		log("listProcessInstanceRemotes", new Object[]{});
		return listProcessInstanceRemotes(null, null);
	}

	public ProcessInstanceRemote[] listProcessInstanceRemotes(String definition) throws RemoteException{
//		log("listProcessInstanceRemotes", new Object[]{definition});
		return listProcessInstanceRemotes(definition, null);
	}
	
	public ProcessInstanceRemote[] listProcessInstanceRemotes(String definition, String status) throws RemoteException{
		log("listProcessInstanceRemotes", new Object[]{definition, status});
		try{
			ProcessInstanceRepositoryHomeLocal pihr = GlobalContext.createProcessInstanceRepositoryHomeLocal();

			Collection instances;
			
			if(definition!=null){
				if(status!=null)
					instances = pihr.findByDefinitionAndStatus(new Long(definition), status);
				else
					instances = pihr.findByDefinition(new Long(definition));
			}else{
//				if(status==null)
					instances = pihr.findAllProcessInstances();
/*				else
					instances = pihr.findByStatus(status);*/
			}
				
			ProcessInstanceRemote[] pirs = new ProcessInstanceRemote[instances.size()];
				
			int i=0;
			for(Iterator iter = instances.iterator(); iter.hasNext();){
				ProcessInstanceRepositoryLocal pirl = (ProcessInstanceRepositoryLocal)iter.next();
				pirs[i] = new ProcessInstanceRemote(pirl);				
				i++;
			}
			
			return pirs; 
			
		}catch(Exception e){
			e.printStackTrace();
			throw new RemoteException("ProcessManagerError:"+e.getMessage(), e);
		}
	}
	
	public ProcessInstanceRemote[] listProcessArchiveRemotes() throws RemoteException{
		log("listProcessArchiveRemotes", new Object[]{});
		try{
			ProcessInstanceRepositoryHomeLocal pihr = GlobalContext.createProcessInstanceRepositoryHomeLocal();
			Collection instances = pihr.findAllProcessArchives();
				
			ProcessInstanceRemote[] pirs = new ProcessInstanceRemote[instances.size()];
				
			int i=0;
			for(Iterator iter = instances.iterator(); iter.hasNext();){
				ProcessInstanceRepositoryLocal pirl = (ProcessInstanceRepositoryLocal)iter.next();
				pirs[i] = new ProcessInstanceRemote(pirl);
				/*pirs[i].setId(pirl.getId().toString());
				pirs[i].setStatus(pirl.getStatus());*/
				i++;
			}
			
			return pirs; 
			
		}catch(Exception e){
			e.printStackTrace();
			throw new RemoteException("ProcessManagerError:"+e.getMessage(), e);
		}
	}
	

/*	public String[] listProcessInstanceIds() throws RemoteException{
		try{
			return ActivityInstance.getInstanceIds();
		}catch(Exception e){
			e.printStackTrace();
			throw new RemoteException("ProcessManagerError:"+e.getMessage(), e);
		}
	}
	
	public String[] listProcessInstanceIds(String definitionName) throws RemoteException{
		try{
			return ActivityInstance.getInstanceIds(definitionName);
		}catch(Exception e){
			e.printStackTrace();
			throw new RemoteException("ProcessManagerError:"+e.getMessage(), e);
		}
	}*/

	public String addProcessDefinition(String name, int version, String description, boolean isAdhoc, String strDef, String folder, String belongingPdid, String alias) throws RemoteException{
		log("addProcessDefinition", new Object[]{name, new Integer(version), description, new Boolean(isAdhoc), strDef, folder, belongingPdid});
		try{	
			HashMap options = new HashMap();
			if(alias!=null)
				options.put("alias", alias);
			
			String[] defVerIdAndDefId = ProcessDefinitionFactory.getInstance(getTransactionContext()).addDefinitionImpl(belongingPdid, null, version, name, description, isAdhoc, strDef, folder, false, options);
			return defVerIdAndDefId[1] + "@" + defVerIdAndDefId[0];
		}catch(Exception e){
			e.printStackTrace();
			throw new RemoteException("ProcessManagerError:"+e.getMessage(), e);
		}
	}
	
	public String addProcessDefinition(String name, int version, String description, boolean isAdhoc, String strDef, String folder, String belongingPdid, String alias, String objectType) throws RemoteException{
		log("addProcessDefinition", new Object[]{name, new Integer(version), description, new Boolean(isAdhoc), strDef, folder, belongingPdid});
		try{		
			HashMap options = new HashMap();
			if(objectType!=null)
				options.put("objectType", objectType);
			if(alias!=null)
				options.put("alias", alias);
			
			String[] defVerIdAndDefId = ProcessDefinitionFactory.getInstance(getTransactionContext()).addDefinitionImpl(belongingPdid, null, version, name, description, isAdhoc, strDef, folder, false, options);
			return defVerIdAndDefId[1] + "@" + defVerIdAndDefId[0];
		}catch(Exception e){
			e.printStackTrace();
			throw new RemoteException("ProcessManagerError:"+e.getMessage(), e);
		}
	}
	
	public String addProcessDefinition(String name, int version, String description, boolean isAdhoc, String strDef, String folder, String belongingPdid, String objectType, String alias, String superDefId) throws RemoteException{
		log("addProcessDefinition", new Object[]{name, new Integer(version), description, new Boolean(isAdhoc), strDef, folder, belongingPdid});
		try{	
			HashMap options = new HashMap();
			if(objectType!=null)
				options.put("objectType", objectType);
			if(alias!=null)
				options.put("alias", alias);
			if(superDefId != null)
				options.put("superDefId", superDefId);
			
			String[] defVerIdAndDefId = ProcessDefinitionFactory.getInstance(getTransactionContext()).addDefinitionImpl(belongingPdid, null, version, name, description, isAdhoc, strDef, folder, false, options);
			return defVerIdAndDefId[1] + "@" + defVerIdAndDefId[0];
		}catch(Exception e){
			e.printStackTrace();
			throw new RemoteException("ProcessManagerError:"+e.getMessage(), e);
		}
	}
	
	public String addProcessDefinition(String name, int version, String description, boolean isAdhoc, ProcessDefinition processDefinition, String folder, String belongingPdid) throws RemoteException{
		log("addProcessDefinition", new Object[]{name, new Integer(version), description, new Boolean(isAdhoc), processDefinition, folder, belongingPdid});
		try{
			String[] defVerIdAndDefId = ProcessDefinitionFactory.getInstance(getTransactionContext()).addDefinitionImpl(belongingPdid, null, version, name, description, isAdhoc, processDefinition, folder, false, null);
			return defVerIdAndDefId[1] + "@" + defVerIdAndDefId[0];
 		}catch(Exception e){
			e.printStackTrace();
			throw new RemoteException("ProcessManagerError:"+e.getMessage(), e);
		}
	}
	
	public String addFolder(String folderName, String parentFolder) throws RemoteException{
		log("addFolder", new Object[]{folderName, parentFolder});
		try{
			ProcessDefinitionRepositoryHomeLocal pdhr = GlobalContext.createProcessDefinitionRepositoryHomeLocal(getTransactionContext());
			
			//if there is no parent folder, this will occur an exception to break this try~catch block
			if(parentFolder!=null && !parentFolder.equals("-1")){
				pdhr.findByPrimaryKey(new Long(parentFolder));
			}
			if(!UEngineUtil.isNotEmpty(parentFolder)){
				parentFolder = "-1";
			}
			
			ProcessDefinitionRepositoryLocal pdr = null;
			
			try {
				pdr = pdhr.findByNameSameLevel(folderName, new Long(parentFolder), "folder");
			} catch (Exception e) {
				pdr = pdhr.create(UniqueKeyGenerator.issueProcessDefinitionKey(getTransactionContext()));
				pdr.setName(folderName);
				pdr.setParentFolder(new Long(parentFolder));
				pdr.setIsFolder(true);
				pdr.setObjType("folder");
			}
			
			return ""+pdr.getDefId();
		}catch(Exception e){
			e.printStackTrace();			
			throw new RemoteException("ProcessManagerError:"+e.getMessage(), e);
		}
	}
	
	public void moveFolder(String pdid, String parentFolder) throws RemoteException{
		log("moveFolder", new Object[]{pdid, parentFolder});
		try{
			ProcessDefinitionRepositoryHomeLocal pdhr = GlobalContext.createProcessDefinitionRepositoryHomeLocal(getTransactionContext());			
			ProcessDefinitionRepositoryLocal pdr = pdhr.findByPrimaryKey(new Long(pdid));
			
			//if there is no parent folder, this will occur an exception to break this try~catch block
			if(parentFolder!=null && !parentFolder.equals("-1")){
				pdhr.findByPrimaryKey(new Long(parentFolder));
			}
			if(!UEngineUtil.isNotEmpty(parentFolder)){
				parentFolder = "-1";
			}
			
			pdr.setParentFolder(new Long(parentFolder));
			
		}catch(Exception e){
			e.printStackTrace();
			throw new RemoteException("ProcessManagerError:"+e.getMessage(), e);
		}
	}
	
	public void setVisibleProcessDefinition(String pdid, boolean isVisible) throws RemoteException{
		log("setHiddenProcessDefinition", new Object[]{pdid, new Boolean(isVisible)});
		try{
			
			ProcessDefinitionRepositoryHomeLocal pdhr = GlobalContext.createProcessDefinitionRepositoryHomeLocal(getTransactionContext());			
			ProcessDefinitionRepositoryLocal pdr = pdhr.findByPrimaryKey(new Long(pdid));

			pdr.setIsVisible(isVisible);
			
		}catch(Exception e){
			e.printStackTrace();
			throw new RemoteException("ProcessManagerError:"+e.getMessage(), e);
		}
	}
	
	public void renameProcessDefinition(String pdid, String newName) throws RemoteException{
		log("renameProcessDefinition", new Object[]{pdid, newName});
		try{
			ProcessTransactionContext tc = getTransactionContext();
			ProcessDefinitionRepositoryHomeLocal pdhr = GlobalContext.createProcessDefinitionRepositoryHomeLocal(tc);			
			ProcessDefinitionRepositoryLocal pdr = pdhr.findByPrimaryKey(new Long(pdid));
			pdr.setName(newName);
			
			if (pdr.getProdVerId() != null) {
				//2011.1.11 add by yookjy
				//rename upd 
				String pdvid = String.valueOf(pdr.getProdVerId());
				ProcessDefinitionFactory pdf = ProcessDefinitionFactory.getInstance(getTransactionContext());
				ProcessDefinition pd = pdf.getDefinition(pdvid);
				pd.setName(newName);
	
				//rename process definition version
				ProcessDefinitionVersionRepositoryHomeLocal pdvhr = GlobalContext.createProcessDefinitionVersionRepositoryHomeLocal(tc);		
				ProcessDefinitionVersionRepositoryLocal pdvr = pdvhr.findByPrimaryKey(new Long(pdvid));
				pdvr.setDefName(newName);
						
				//restore upd file
				String path = null;
				String def = (String)pdvr.getFilePath();
				if(def.startsWith("LINK:")){
					path = def.substring("LINK:".length());
				}
				pdf.storeProcessDefinition(path, pd);
				
				//restore cached file
				pdf.compileDefinition(DEFINITION_ROOT + path, pd);		
				
				//remove cache
				pdf.removeFromCache(pdvid);
			}
		}catch(Exception e){
			e.printStackTrace();
			throw new RemoteException("ProcessManagerError:"+e.getMessage(), e);
		}
	}
	
	//2011.02.28 modified by yookjy 
	public Serializable getActivityProperty(String processDefinition, String tracingTag, String propertyName) throws RemoteException{
		log("getActivityProperty", new Object[]{processDefinition, tracingTag, propertyName});
		try{			
			if(!UEngineUtil.isNotEmpty(processDefinition)) throw new UEngineException("Check the definition id");
			
			ProcessDefinition definition = getDefinition(processDefinition);
			Activity activity = definition.getActivity(tracingTag);
			propertyName = propertyName.substring(0,1).toUpperCase() + propertyName.substring(1);//.toLowerCase();
			Serializable activityProperty = (Serializable)activity.getClass().getMethod("get" + propertyName, new Class[]{}).invoke(activity, new Object[]{});
			
			return activityProperty; 
		}catch(Exception e){
			e.printStackTrace();
			throw new RemoteException("ProcessManagerError:"+e.getMessage(), e);
		}
	}
	
	//2011.02.28 added by yookjy : dynamic change 
	public Serializable getActivityPropertyFromInstance(String instanceId, String tracingTag, String propertyName) throws RemoteException{
		log("getActivityProperty", new Object[]{instanceId, tracingTag, propertyName});
		try{
			if(!UEngineUtil.isNotEmpty(instanceId)) throw new UEngineException("Check the instance id");
			
			Serializable activityProperty = null;
			ProcessInstance instance = getInstance(instanceId);
			ProcessDefinition definition = instance.getProcessDefinition();
			
			Activity activity = null;
			String[] trcTags = tracingTag.split("@");
			/**
			 * FIXME : To expect, Process Definition, select the option to object to find the version if you need to change.
			 */
			if (trcTags.length > 1) 
			{
				SubProcessActivity subProcessActivity = null;
				for(int i=0,n=trcTags.length-1; i<n; i++) {
					if(!(definition.getActivity(trcTags[i]) instanceof SubProcessActivity)) break;
					subProcessActivity = (SubProcessActivity)definition.getActivity(trcTags[i]);
					String pdvid = subProcessActivity.getDefinitionId();
					
					if(pdvid.contains("@")){
						pdvid = pdvid.split("@")[1];
					}
					definition = getProcessDefinition(pdvid);
				}
				
				tracingTag = trcTags[trcTags.length-1];
			}		
			
			activity = definition.getActivity(tracingTag);
			
			if (!(activity instanceof SubProcessActivity)) {
				propertyName = propertyName.substring(0,1).toUpperCase() + propertyName.substring(1);//.toLowerCase();
				activityProperty = (Serializable)activity.getClass().getMethod("get" + propertyName, new Class[]{}).invoke(activity, new Object[]{});
			}
			
			return activityProperty;
		}catch(Exception e){
			e.printStackTrace();
			throw new RemoteException("ProcessManagerError:"+e.getMessage(), e);
		}
	}
	
	public String viewProcessDefinitionFlowChart(String processDefinition, Map options) throws RemoteException{
		log("viewProcessDefinitionFlowChart", new Object[]{processDefinition, options});
		try{
			ProcessDefinition definition = getDefinition(processDefinition);
			ProcessInstance instance = new DefaultProcessInstance();
			instance.setProcessTransactionContext(getTransactionContext());
			return null;
			//return ProcessDefinitionViewer.getInstance().render(getDefinition(processDefinition), null, options).toString();
		}catch(Exception e){
			e.printStackTrace();
			throw new RemoteException("ProcessManagerError:"+e.getMessage(), e);
		}
	}
	
	public String viewProcessInstanceFlowChart(String instanceId, Map options) throws RemoteException{
		logInst("viewProcessInstanceFlowChart", new Object[]{instanceId, options});
		try{
			if(instanceId == null) return "";
				//throw new RemoteException("ProcessManagerError: null process instance id");
			
			ProcessInstance instance = getInstance(instanceId);
			ProcessDefinition definition = instance.getProcessDefinition();
			
			//for performance and synchronized view - we don't need to create snapshot by the implementation of caching logic.
			ProcessInstance shotCopy = instance;//instance.createSnapshot();
			instance.setProcessDefinition(definition);
			//
			return null;
			
			//return ProcessDefinitionViewer.getInstance().render(definition, shotCopy, options).toString();
		}catch(Exception e){
			e.printStackTrace();
			throw new RemoteException("ProcessManagerError:"+e.getMessage(), e);
		}
	}
	
	public Hashtable getActivityInstanceDetails(String instanceId, String tracingTag) throws RemoteException{
		logInst("getActivityInstanceDetails", new Object[]{instanceId, tracingTag});
		try{
			ProcessInstance instance = getInstance(instanceId);
			//ProcessDefinition definition = instance.getProcessDefinition();
		
			return (Hashtable)instance.getActivityDetails(tracingTag);
		}catch(Exception e){
			e.printStackTrace();
			throw new RemoteException("ProcessManagerError:"+e.getMessage(), e);
		}
	}
	
	public Hashtable getActivityDetails(String processDefinition, String tracingTag) throws RemoteException{
		logInst("getActivityDetails", new Object[]{processDefinition, tracingTag});
		try{
			ProcessDefinition definition = getProcessDefinition(processDefinition);
		
			return (Hashtable)definition.getActivity(tracingTag).getActivityDetails(null, null);
		}catch(Exception e){
			e.printStackTrace();
			throw new RemoteException("ProcessManagerError:"+e.getMessage(), e);
		}
	}
	
	public String getActivityStatus(String instanceId, String tracingTag) throws RemoteException{
		logInst("getActivityStatus", new Object[]{instanceId, tracingTag});
		try{
			if(instanceId==null)
				return null;
			
			ProcessInstance instance = getInstance(instanceId);
		
			return instance.getStatus(tracingTag);
		}catch(Exception e){
			e.printStackTrace();
			throw new RemoteException("ProcessManagerError:"+e.getMessage(), e);
		}
	}
	
	public Serializable sendMessage(String instanceId, String message, Serializable payload) throws RemoteException{
		logInst("sendMessage", new Object[]{instanceId, message, payload});
		try{
			ProcessInstance instance = getInstance(instanceId);
			ProcessDefinition definition = instance.getProcessDefinition();
			return (Serializable)definition.fireMessage(message, instance, payload); //send message to the whole subscribers regardless of given instance id. it means even if there're instance which didn't subscribe, the instance will be ignored.

//			MessageProcessorBean.queueMessage(message, instanceId, payload);
			
			//review: 
			//return null;
		
		}catch(Exception e){
			e.printStackTrace();
			throw new RemoteException("ProcessManagerError:"+e.getMessage(), e);
		}
	}
	
	//TODO not supported now
	public Serializable sendMessageXML(String instanceId, String message, String payload) throws RemoteException{
		logInst("sendMessageXML", new Object[]{instanceId, message, payload});
		try{
			ProcessInstance instance = getInstance(instanceId);
			ProcessDefinition definition = instance.getProcessDefinition();
			
			return (Serializable)definition.fireMessageXML(message, instance, payload); //send message to the whole subscribers regardless of given instance id. it means even if there're instance which didn't subscribe, the instance will be ignored.
		
		}catch(Exception e){
			e.printStackTrace();
			throw new RemoteException("ProcessManagerError:"+e.getMessage(), e);
		}
	}
	
	public void completeWorkitem(String instanceId, String tracingTag, String taskId, ResultPayload payload) throws RemoteException{
		completeWorkitemImpl(instanceId, tracingTag, taskId, payload, false);
	}

	public void saveWorkitem(String instanceId, String tracingTag, String taskId, ResultPayload payload) throws RemoteException{
		completeWorkitemImpl(instanceId, tracingTag, taskId, payload, true);
	}

	private void completeWorkitemImpl(/*String definitionId, */String instanceId, String tracingTag, String taskId, ResultPayload payload, boolean saveOnly) throws RemoteException{
		
		logInst("completeWorkitem", new Object[]{instanceId, tracingTag, taskId, payload});
		try{
			
/*			boolean isInitiatingWorkItemCompletion = (!UEngineUtil.isNotEmpty(instanceId) && UEngineUtil.isNotEmpty(definitionId));
			if(isInitiatingWorkItemCompletion){
				instanceId = initializeProcess(definitionId);
				if(getTransactionContext().getTemporaryInstance()!=null){
					ProcessInstance instance = getInstance(instanceId);
					
				}
				
				executeProcess(instanceId);
			}
*/
			
			ProcessInstance instance = getInstance(instanceId);

			//TODO: it is not required anymore. begin with workitem.
//			if(!instance.isRunning("")
//					&& ((instance.isNew() && tracingTag.indexOf("@")>0) //first process is subprocess
//							|| !((HumanActivity)instance.getProcessDefinition().getActivity(tracingTag)).isNotificationWorkitem())
//				)
//				instance.execute();
			
			ProcessDefinition definition = instance.getProcessDefinition();
					 
			boolean isInitiationInSubprocess = false;
			String absoluteTracingTag = tracingTag;
			
			if(tracingTag.indexOf("@")>0){
				isInitiationInSubprocess = true;
				String[] scopesByTracingTag = tracingTag.split("@");
				for(int i=0; i<scopesByTracingTag.length-1; i++){
					String scope = scopesByTracingTag[i];
					SubProcessActivity spAct = (SubProcessActivity)definition.getActivity(scope);
					List spInstanceIds = spAct.getSubprocessIds(instance);
					if(spInstanceIds.size() == 0){
						throw new UEngineException("Activity in the subprocess ["+ absoluteTracingTag +"] cannot be found.");
					}
					
					String spInstanceId = (String)spInstanceIds.get(0);
					
					instance = getProcessInstance(spInstanceId);
					definition = instance.getProcessDefinition();
				}
				
				tracingTag = scopesByTracingTag[scopesByTracingTag.length-1];
			}
			
			HumanActivity humanActivity = ((HumanActivity)definition.getActivity(tracingTag));
			
			if(!instance.isRunning(humanActivity.getTracingTag()) && !humanActivity.isNotificationWorkitem()){
				throw new UEngineException("Illegal completion for workitem [" + humanActivity + ":"+ humanActivity.getStatus(instance) +"]: Already closed or illegal status."); 
			}
			
			
			if(saveOnly){
				humanActivity.saveWorkItem(instance, payload);
			}else{
				try{
					humanActivity.fireReceived(instance, payload);
				}catch(Exception e){
					humanActivity.fireFault(instance, e);
					
					throw new UEngineException(e.getMessage(), null, new UEngineException(e.getMessage(), e), instance, humanActivity);
				}
			}
		
			/*String message = humanActivity.getMessage();
						
			//add task id into payload
			KeyedParameter[] extendedValues = payload.getExtendedValues();
			int evSize=0;
			if(extendedValues!=null)
				evSize = extendedValues.length;				 
			KeyedParameter[] extendedValues_ = new KeyedParameter[evSize+1];
			System.arraycopy(extendedValues, 0, extendedValues_, 0, extendedValues.length);
			extendedValues_[evSize] = new KeyedParameter(HumanActivity.PAYLOADKEY_TASKID, taskId);			
			payload.setExtendedValues(extendedValues_);
			//		
			
			definition.fireMessage(message, instance, payload);*/
		}catch(Exception e){
			e.printStackTrace();
			throw new RemoteException("ProcessManagerError:"+e.getMessage(), e);
		}		
	}
	
	private String getProcessDefinition(String processDefinition, String encodingStyle, boolean withoutInheritance) throws RemoteException{
		log("getProcessDefinition", new Object[]{processDefinition, encodingStyle});
		try{
			ProcessDefinition def = getDefinition(processDefinition, withoutInheritance);
			
			ByteArrayOutputStream bao = new ByteArrayOutputStream();
			
			//TODO if encoding style is bean this process is not required. 
			GlobalContext.serialize(def, bao, encodingStyle);
			
			return bao.toString(GlobalContext.DATABASE_ENCODING/*"ISO-8859-1"*/);
		}catch(Exception e){
			e.printStackTrace();
			throw new RemoteException("ProcessManagerError:"+e.getMessage(), e);
		}
	}

	public String getProcessDefinition(String processDefinition, String encodingStyle) throws RemoteException{
		return getProcessDefinition(processDefinition, encodingStyle, false);
	}
	
	public String getResource(String resourceId) throws RemoteException{
		log("getResource", new Object[]{resourceId});
		try{
			InputStream ris = ProcessDefinitionFactory.getInstance(getTransactionContext()).getResourceStream(resourceId);
			
			ByteArrayOutputStream bao = new ByteArrayOutputStream();
			
			UEngineUtil.copyStream(ris, bao);
			
			return bao.toString(GlobalContext.DATABASE_ENCODING/*"ISO-8859-1"*/);
		}catch(Exception e){
			e.printStackTrace();
			throw new RemoteException("ProcessManagerError:"+e.getMessage(), e);
		}
	}

	/**
	 * This method is costly to call. Use getProcessDefinitionRemote() instead if full data is not neccessary.
	 */
	public ProcessDefinition getProcessDefinition(String processDefinition) throws RemoteException{
		log("getProcessDefinition", new Object[]{processDefinition});
		try{
			ProcessDefinition copy = (ProcessDefinition)getDefinition(processDefinition).clone();			 
			return copy;		
		}catch(Exception e){
			e.printStackTrace();
			throw new RemoteException("ProcessManagerError:"+e.getMessage(), e);
		}
	}

	private String getProcessDefinitionWithInstanceId(String instanceId, String encodingStyle, boolean withoutInheritance) throws RemoteException{
		logInst("getProcessDefinitionWithInstanceId", new Object[]{instanceId, encodingStyle});
		try{
			//TODO don't need to serialize if encoding style is "Bean".
			ProcessInstance instance = getInstance(instanceId);

			//TODO later consider that inheritance issue.
			ProcessDefinition def = instance.getProcessDefinition();	
			ByteArrayOutputStream bao = new ByteArrayOutputStream();
			GlobalContext.serialize(def, bao, encodingStyle);
			
			return bao.toString(GlobalContext.DATABASE_ENCODING/*"ISO-8859-1"*/);	
		}catch(Exception e){
			//e.printStackTrace();
			throw new RemoteException("ProcessManagerError:"+e.getMessage(), e);
		}
	}
	
	public String getProcessDefinitionWithInstanceId(String instanceId, String encodingStyle) throws RemoteException{
		return getProcessDefinitionWithInstanceId(instanceId, encodingStyle, false);
	}
	
	public ProcessDefinitionRemote getProcessDefinitionRemote(String pdvid) throws RemoteException{
		try{
			ProcessDefinitionVersionRepositoryHomeLocal pdvrhl = GlobalContext.createProcessDefinitionVersionRepositoryHomeLocal(getTransactionContext());
			ProcessDefinitionVersionRepositoryLocal pdvrl = pdvrhl.findByPrimaryKey(new Long(pdvid));
			
			ProcessDefinitionRepositoryHomeLocal pdhr = GlobalContext.createProcessDefinitionRepositoryHomeLocal(getTransactionContext());
			ProcessDefinitionRepositoryLocal pdl = pdhr.findByPrimaryKey(pdvrl.getDefId());
			int productionVersion = pdl.getProdVer();
			
			ProcessDefinitionRemote pdr =null;
			
			try{
				ProcessDefinition pd = getDefinition(pdvid);
				if(pd !=null && pd instanceof ProcessDefinition){
					pdr = new ProcessDefinitionRemote(pd, getTransactionContext(), pdvrl);
					
					if(pdl.getObjType() != null){
						pdr.setObjType(pdl.getObjType());
					}
				}
			}catch (Exception e) {
				pdr = new ProcessDefinitionRemote(pdl, pdvrl);
			}
			
			
//			ProcessDefinitionRemote pdr = new ProcessDefinitionRemote(getDefinition(pdvid), getTransactionContext());
			//TODO: Method 'getProcessDefinitionRemoteWithInstanceId' also need such correction
			//pdr.setName(TextContext.createInstance());
			//pdr.getName().setText(pdl.getName());
			//pdr.setBelongingDefinitionId(pdl.getId().toString());
			
			if(pdvrl.getVer().intValue() == productionVersion)
				pdr.setProduction(true);
			else{
				pdr.setProduction(false);
			}
			
			return pdr;
		}catch(Exception e){
			//e.printStackTrace();
			throw new RemoteException("ProcessManagerError:"+e.getMessage(), e);
		}
	}
	
	public ProcessDefinitionRemote getProcessDefinitionRemoteByDefinitionId(String defId) throws RemoteException{
		try{
			ProcessDefinitionRepositoryHomeLocal pdhr = GlobalContext.createProcessDefinitionRepositoryHomeLocal(getTransactionContext());
			ProcessDefinitionRepositoryLocal pdrl = pdhr.findByPrimaryKey(new Long(defId));
			ProcessDefinitionRemote pdr = new ProcessDefinitionRemote(pdrl, null);

			return pdr;

		}catch(Exception e){
			//e.printStackTrace();
			throw new RemoteException("ProcessManagerError:"+e.getMessage(), e);
		}
		
	}

	public ProcessDefinitionRemote getProcessDefinitionRemoteWithInstanceId(String instanceId) throws RemoteException{
		try{
			//TODO: sometimes there are illegal invocations from web server. 
			if(instanceId==null) return null;
			
			ProcessInstance instance = getInstance(instanceId);
			ProcessDefinition definition = instance.getProcessDefinition();
			
			if(definition==null)
				throw new UEngineException("Can't find definition for this instance. Check if the definition file exists.");
				
			return new ProcessDefinitionRemote(definition, getTransactionContext());
		}catch(Exception e){
			e.printStackTrace();
			throw new RemoteException("ProcessManagerError:"+e.getMessage(), e);
		}
	}
	
	public ProcessDefinition getProcessDefinitionWithInstanceId(String instanceId) throws RemoteException{
		logInst("getProcessDefinitionWithInstanceId", new Object[]{instanceId});
		try{
			//ProcessInstanceRepositoryLocal pil = GlobalContext.createProcessInstanceRepositoryHomeLocal().findByPrimaryKey(new Long(instanceId));
			
			ProcessInstance instance = getInstance(instanceId);
			ProcessDefinition orginial = instance.getProcessDefinition();
			
			//ProcessDefinition orginial = getProcessDefinition(""+pil.getDefVerId());
				
			//Some EJB containers doesn't carry out serialization for object passing (especially in case that the caller is in the same VM).
			// So, it is required to make a copy so that the original object cannot be modified.
			return (ProcessDefinition)orginial.clone();
		}catch(Exception e){
			e.printStackTrace();
			throw new RemoteException("ProcessManagerError:"+e.getMessage(), e);
		}
	}
	
	public void flowControl(String command, String instanceId, String tracingTag) throws RemoteException{
		logInst("flowControl", new Object[]{command, instanceId, tracingTag});
		try{
			ProcessInstance instance = getInstance(instanceId);
			ProcessDefinition definition = instance.getProcessDefinition();
			
			definition.flowControl(command, instance, tracingTag);
		}catch(Exception e){
			e.printStackTrace();
			throw new RemoteException("ProcessManagerError:"+e.getMessage(), e);
		}
	}

	public void removeProcessInstance(String instanceId) throws RemoteException{
		logInst("removeProcessInstance", new Object[]{instanceId});
		try{
			ProcessInstance instance = getInstance(instanceId);
			instance.remove();		
		}catch(Exception e){
			e.printStackTrace();
			throw new RemoteException("ProcessManagerError:"+e.getMessage(), e);
		}
	}
	
	public ProcessInstance getProcessInstance(String instanceId) throws RemoteException{
		return getProcessInstance(instanceId, null);
	}

	public ProcessInstance getProcessInstance(String instanceId, String executionScope) throws RemoteException{
		logInst("getProcessInstance", new Object[]{instanceId, executionScope});
		try{
			ProcessInstance instance = getInstance(instanceId);
			
			//The request for reseting the executionscope with null value should be ignored 
			// because it's intent is the requestor doesn't know the executionScope, 
			// so existing execution scope value stored in the cached instance should be kept.
			// This logic is related to event scope mechanism malfunction, 
			//  specifically to the evenhandler activity's instance data allocation.
			if(executionScope!=null)
				instance.setExecutionScope(executionScope);
			
			//it is ok to return the original object since we have implemented caching logic.
			return instance/*.createSnapshot()*/;
		}catch(Exception e){
			e.printStackTrace();
			throw new RemoteException("ProcessManagerError:"+e.getMessage(), e);
		}
	}

	public void setProcessInstanceInfo(String instanceId, String info) throws RemoteException{
		logInst("setProcessInstanceInfo", new Object[]{instanceId, info});
		try{
			ProcessInstance instance = getInstance(instanceId);			
			instance.setInfo(info);
		}catch(Exception e){
			e.printStackTrace();
			throw new RemoteException("ProcessManagerError:"+e.getMessage(), e);
		}
	}
	
	public void setProcessInstanceStatus(String instanceId, String status) throws RemoteException{
		logInst("setProcessInstanceStatus", new Object[]{instanceId, status});
		try{
			ProcessInstance instance = getInstance(instanceId);
			instance.setStatus(status);
		}catch(Exception e){
			e.printStackTrace();
			throw new RemoteException("ProcessManagerError:"+e.getMessage(), e);
		}
	}

	public Serializable getActivityInstanceProperty(String instanceId, String tracingTag, String propertyName) throws RemoteException{
		logInst("getActivityInstanceProperty", new Object[]{instanceId, tracingTag, propertyName});
		try{			
			if(instanceId==null) return null; //guard against illegal requests

			ProcessInstance instance = getInstance(instanceId);
			ProcessDefinition definition = instance.getProcessDefinition();
			Activity activity = definition.getActivity(tracingTag);
			propertyName = propertyName.substring(0,1).toUpperCase() + propertyName.substring(1);//.toLowerCase();
			//the pattern of instance property (stateful) is 'getXXX(instance)'
			Object returnVal;
			try{ 
				returnVal = activity.getClass().getMethod("get" + propertyName, new Class[]{ProcessInstance.class}).invoke(activity, new Object[]{instance});
			}catch(NoSuchMethodException e){
				try{
					returnVal = activity.getClass().getMethod("is" + propertyName, new Class[]{ProcessInstance.class}).invoke(activity, new Object[]{instance});
				}catch(NoSuchMethodException msme){
					throw new UEngineException("No such activity property named as '" + propertyName + "'");
				}
			}

			return (Serializable)returnVal;
		}catch(Exception e){
			e.printStackTrace();
			throw new RemoteException("ProcessManagerError:"+e.getMessage(), e);
		}
	}
	
	public void setActivityInstanceProperty(String instanceId, String tracingTag, String propertyName, Serializable value) throws RemoteException{
//		logInst("setActivityInstanceProperty", new Object[]{instanceId, tracingTag, propertyName, value});
		setActivityInstanceProperty(instanceId, tracingTag, propertyName, value, null);
	}
	
	public void setActivityInstanceProperty(String instanceId, String tracingTag, String propertyName, Serializable value, Class valueType) throws RemoteException{
		logInst("setActivityInstanceProperty", new Object[]{instanceId, tracingTag, propertyName, value, valueType});
		try{			
			if(instanceId==null) return; //guard against illegal requests
			if(valueType==null){
				if(value==null)
					throw new UEngineException("Property value should be not null. If you want to apply with null value, provide valueType as well."); //TODO: change to find proper method and to allow null value
					
				valueType = value.getClass();
			}			 

			ProcessInstance instance = getInstance(instanceId);
			ProcessDefinition definition = instance.getProcessDefinition();
			
			if(definition==null)
				throw new UEngineException("Can't find the definition for instance '" + instance.getInstanceId() + "'");
				
			Activity activity = definition.getActivity(tracingTag);

			if(activity==null)
				throw new UEngineException("No such activity with tracing tag : " + tracingTag + " in definition '" + definition.getId() + "'");

			propertyName = propertyName.substring(0,1).toUpperCase() + propertyName.substring(1);//.toLowerCase();
			//the pattern of instance property (stateful) setting method is 'setXXX(instance, value)'												
			activity.getClass().getMethod("set" + propertyName, new Class[]{ProcessInstance.class, valueType}).invoke(activity, new Object[]{instance, value});
		}catch(Exception e){
			e.printStackTrace();
			throw new RemoteException("ProcessManagerError:"+e.getMessage(), e);
		}
	}
	
	public Serializable doActivityAction(String instanceId, String tracingTag, String actionName, Serializable[] parameters, Class[] parameterTypes) throws RemoteException{
		logInst("doActivityAction", new Object[]{instanceId, tracingTag, actionName, parameters, parameterTypes});
		try{			
			if(instanceId==null) return null; //guard against illegal requests
			
			if(parameters.length!=parameterTypes.length)
				throw new UEngineException("invalid parameter length");

			ProcessInstance instance = getInstance(instanceId);
			ProcessDefinition definition = instance.getProcessDefinition();
			
			if(definition==null)
				throw new UEngineException("Can't find the definition for instance '" + instance.getInstanceId() + "'");
				
			Activity activity = definition.getActivity(tracingTag);

			if(activity==null)
				throw new UEngineException("No such activity with tracing tag : " + tracingTag + " in definition '" + definition.getId() + "'");

			Class[] actualParameterTypes = new Class[parameterTypes.length+1];
			actualParameterTypes[0] = ProcessInstance.class;
			System.arraycopy(parameterTypes, 0, actualParameterTypes, 1, parameterTypes.length);

			Object[] actualParameters = new Object[parameterTypes.length+1];
			actualParameters[0] = instance;
			System.arraycopy(parameters, 0, actualParameters, 1, parameterTypes.length);
			
			return (Serializable)activity.getClass().getMethod(actionName, actualParameterTypes).invoke(activity, actualParameters);
			
		}catch(Exception e){
			e.printStackTrace();
			throw new RemoteException("ProcessManagerError:"+e.getMessage(), e);
		}
	}

	@Override
	public void exportProcessDefinitionbyDefinitionId(String defId, boolean allVersion) throws Exception {

	}

	public void removeProcessDefinition(String processDefinition) throws RemoteException{
		log("removeProcessDefinition", new Object[]{processDefinition});
		try{
			//Check there is referencing instance
			ProcessInstanceRepositoryHomeLocal pihr = GlobalContext.createProcessInstanceRepositoryHomeLocal();
			//if there is any of instance of this folder, this definition can't be removed
			Collection pis = pihr.findByDefinition(new Long(processDefinition));
			if(pis.iterator().hasNext())
				throw new UEngineException("This definition has instances");

			ProcessDefinitionFactory.getInstance(getTransactionContext()).removeDefinition(processDefinition);			
		}catch(Exception e){
			e.printStackTrace();
			throw new RemoteException("ProcessManagerError:"+e.getMessage(), e);
		}
	}
	
	public void removeFolder(String folderId) throws RemoteException{
		log("removeFolder", new Object[]{folderId});

		try{
			//Check there is child
			ProcessDefinitionRepositoryHomeLocal pdhr = GlobalContext.createProcessDefinitionRepositoryHomeLocal(getTransactionContext());
			//if there is child of this folder, this folder can't be removed
			Collection childs = pdhr.findByFolder(new Long(folderId));
			if(childs.iterator().hasNext())
				throw new UEngineException("This folder is not empty");
					
			ProcessDefinitionRepositoryLocal pdr = pdhr.findByPrimaryKey(new Long(folderId));

			pdr.setIsDeleted(true);
		}catch(Exception e){
			e.printStackTrace();
			throw new RemoteException("ProcessManagerError:"+e.getMessage(), e);
		}
	}


	
//----- standard handlers -----------
   public void ejbRemove ()
   {
	System.out.println("ProcessManagerBean::removed");
   }
   public void ejbActivate ()
   {
   	System.out.println("ProcessManagerBean::activated");
   }
   public void ejbPassivate ()
   {
	System.out.println("ProcessManagerBean::passivated");
   }
   
   public void setSessionContext (javax.ejb.SessionContext cntx)
   {
	System.out.println("ProcessManagerBean::got sessionContext");

		
      ejbContext = cntx;
      
	  try
	  {
		 jndiContext = new javax.naming.InitialContext ();
	  }
	  catch(NamingException ne)
	  {
		 throw new EJBException (ne);
	  }
   }
//----------------------------------

	private ProcessDefinition getDefinition(String pdvid, boolean withoutInheritance) throws Exception{
		
		if(pdvid.startsWith("[")){
			String definitionAlias = pdvid.substring(1, pdvid.indexOf("]"));
			pdvid = getProcessDefinitionProductionVersionByAlias(definitionAlias);
		}		
		
		ProcessDefinition pd = ProcessDefinitionFactory.getInstance(getTransactionContext()).getDefinition(pdvid, true, withoutInheritance);
		
/*		if(pd.isAdhoc())
			pd = ProcessDefinitionFactory.getDefinition(name, false);*/
		
		return pd;
	}
	
	private ProcessDefinition getDefinition(String pdvid) throws Exception{
		return getDefinition(pdvid, false);
	}

	public void changeProcessDefinition(String instanceId, String definition) throws RemoteException{
		logInst("changeProcessDefinition", new Object[]{instanceId, definition});
		changeProcessDefinitionImpl(instanceId, definition);
	}
	public void changeProcessDefinition(String instanceId, ProcessDefinition definition) throws RemoteException{
		logInst("changeProcessDefinition", new Object[]{instanceId, definition});
		changeProcessDefinitionImpl(instanceId, definition);
	}


	private void changeProcessDefinitionImpl(String instanceId, Object definition) throws RemoteException{
		try{
			ProcessInstance instance = getInstance(instanceId);
			
			ProcessDefinition processDefinition = null;
			if(definition instanceof String){
				ByteArrayInputStream is = new ByteArrayInputStream(((String)definition).getBytes("UTF-8"));
				processDefinition = (ProcessDefinition) ProcessDefinitionFactory.getActivity(is);
			}else{
				processDefinition = (ProcessDefinition)definition;
			}
	
			if(processDefinition==instance.getProcessDefinition()) 
				throw new UEngineException("Dynamic change exception: Changed definition is the original one so it can't be changed. That means your code didn't clone the definition for changing it. Also It implies the damage of cached definition.");
			
			instance.setProcessDefinition(processDefinition);

		}catch(Exception e){
			throw new RemoteException("ProcessManagerError:"+e.getMessage(), e);
		}
	}
	
//	private void changeProcessDefinitionImpl(String instanceId, Object definition) throws RemoteException{
//		try{
//			//verify whether the instance flow has been changed and the changed definition is inconsistence with instance changes.
//			
//
//			//TODO: if bad performance, bpm_procinst should have a field signaling isAdhocInstance
////			ProcessInstanceRepositoryLocal pil = GlobalContext.createProcessInstanceRepositoryHomeLocal().findByPrimaryKey(new Long(instanceId));
//			ProcessInstance instance = getInstance(instanceId);
//			ProcessDefinition orginalDefinition = instance.getProcessDefinition();
//								 
//			ProcessDefinitionVersionRepositoryLocal pdvl = GlobalContext.createProcessDefinitionVersionRepositoryHomeLocal(getTransactionContext()).findByPrimaryKey(new Long(orginalDefinition.getId()));
//			
//			//if the process has not yet been overriden, make new one. Or, overwrite.
//			boolean bOverwrite = (pdvl.getVer().intValue()==-1);
//			String pvId = (bOverwrite ? ""+orginalDefinition.getId() : null);
//			
//			Map options = new Hashtable();
//			options.put("instanceId", instanceId);
//			options.put("associatedInstance", instance); // lets "addDefinitionImpl()" change this instance's definition as well
//			
//			ProcessDefinitionFactory definitionFactory = ProcessDefinitionFactory.getInstance(getTransactionContext());
//			
//			String defVerId[] = definitionFactory.addDefinitionImpl(""+pdvl.getDefId(), pvId, -1, null, null, true, definition, null, bOverwrite, options);
//			//if new one, modify the old link to indicate new one.
//			if(!bOverwrite){
///*				ProcessInstanceRepositoryHomeLocal pihr = GlobalContext.createProcessInstanceRepositoryHomeLocal();
//				ProcessInstanceRepositoryLocal piRemote = pihr.findByPrimaryKey(new Long(instanceId));
//*/				
//				instance.setDefinitionVersionId(defVerId[0]);
//			}
//			
//			/* : no need by "options.put("associatedInstance", instance);" above.
//			//may be bad performance 
//			//instance = getInstance(instanceId);//issue new instance so that the definition can be changed			
//			//instance.setProcessDefinition(definitionFactory.getDefinition(defVerId[0]));
//			*/
//			
//			//NOTE:			 DO NOT FIRE CHANGE EVENT ANYMORE
//			/*
//			ProcessDefinition pd = instance.getProcessDefinition();
//			
//			pd.fireChanged(instance);*/ 
//			
//		}catch(Exception e){
//			e.printStackTrace();
//			throw new RemoteException("ProcessManagerError:"+e.getMessage(), e);
//		}
//	}
//	
	
	public void setFault(String instanceId, String tracingTag, Exception fault) throws RemoteException{
		try{
			ProcessInstance instance = getInstance(instanceId);
	
			ProcessDefinition definition = instance.getProcessDefinition();
			Activity activity = definition.getActivity(tracingTag);
			activity.fireFault(instance, fault);
			
		}catch(Exception e){
			e.printStackTrace();
			throw new RemoteException("ProcessManagerError:"+e.getMessage(), e);
		}
	}
	
	public String getProductionVersionIdAtThatTime(String defId, Date thatTime) throws RemoteException {
		ProcessDefinitionDAOType procDefDF = ProcessDefinitionDAOType.getInstance(null);
		ProductionDefinitionDAO productionDefDAO;
		
		try {
			productionDefDAO = procDefDF.getProductionDefinitionAtThatTime(Long.parseLong(defId), thatTime);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RemoteException("ProcessManagerError:"+e.getMessage(), e);
		}
		
		return productionDefDAO.getDefVerId().toString();
	}
	
	public String getFirstProductionVersionId(String defId) throws RemoteException {
		try{
			ProcessDefinitionDAOType procDefDF = ProcessDefinitionDAOType.getInstance(getTransactionContext());
			ProductionDefinitionDAO productionDefDAO = procDefDF.getFirstProductionDefinition(Long.parseLong(defId));
			return productionDefDAO.getDefVerId().toString();
		} catch (Exception e) {
			e.printStackTrace();
			throw new RemoteException("ProcessManagerError:"+e.getMessage(), e);
		}
		
	}
	
	  protected Connection getConnection () throws SQLException
	   {
		  //return null;
		  
	      try
	      {
	    	  System.out.print("Getting connection....");
	    	  Connection conn = getConnectionFactory().getConnection();
	    	  System.out.println("Got connection!");
	    	  
	    	  return conn;
	    	  //return DbcpConnectionFactory.create().getConnection();
	      }
	      catch(Exception ne)
	      {
	         throw new EJBException (ne);
	      }
	   }
	  
	  
	public void applyChanges() throws RemoteException {
		try{
			if(transactionContext==null) return;
			transactionContext.commit(); //means one unit transaction
			//transactionContext = new TransactionContext(this);
			//System.out.println("!!! pm.applyChanges() !!!");
		} catch (Exception e) {
			e.printStackTrace();
			throw new RemoteException("ProcessManagerError:"+e.getMessage(), e);
		}
	}


	
	public void cancelChanges() throws RemoteException{
		try{
			if(transactionContext==null) return;
			
			transactionContext.rollback(); //means one unit transaction
			transactionContext = new ProcessTransactionContext(this);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RemoteException("ProcessManagerError:"+e.getMessage(), e);
		}
	}
	
	public EventHandler[] getEventHandlersInAction(String instanceId) throws RemoteException{
		try{
			ProcessInstance instance = getInstance(instanceId);
			return instance.getEventHandlersInAction();
		} catch (Exception e) {
			e.printStackTrace();
			throw new RemoteException("ProcessManagerError:"+e.getMessage(), e);
		}
	}

//////////// standard EJB Object methods ////////////////////

	public EJBHome getEJBHome() throws RemoteException {
		// TODO Auto-generated method stub
		return null;
	}

	public Handle getHandle() throws RemoteException {
		// TODO Auto-generated method stub
		return null;
	}

	public Object getPrimaryKey() throws RemoteException {
		// TODO Auto-generated method stub
		return null;
	}

	public boolean isIdentical(EJBObject arg0) throws RemoteException {
		// TODO Auto-generated method stub
		return false;
	}

	public void remove() throws RemoteException, RemoveException {
		try{
			if(transactionContext==null) return;
			
			transactionContext.releaseResources(true);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RemoteException("ProcessManagerError:"+e.getMessage(), e);
		}
	}

	public void afterBegin() throws EJBException, RemoteException {
		// TODO Auto-generated method stub
	}

	public void afterCompletion(boolean arg0) throws EJBException, RemoteException {		
	}

	public void beforeCompletion() throws EJBException, RemoteException {
		try{
			if(transactionContext!=null)
				transactionContext.releaseResources();
		} catch (Exception e) {
			e.printStackTrace();
			throw new RemoteException("ProcessManagerError:"+e.getMessage(), e);
		}
	}

	public Map getGenericContext() {
		return genericContext;
	}

	public void setGenericContext(Map genericContext) throws RemoteException {
		try{
			this.genericContext = genericContext;
		} catch (Exception e) {
			throw new RemoteException("ProcessManagerError:"+e.getMessage(), e);
		}
	}

	public ActivityReference getInitiatorHumanActivityReference(String pdvid) throws RemoteException {
		try{
			ProcessDefinition pd = (ProcessDefinition)getDefinition(pdvid);
			return pd.getInitiatorHumanActivityReference(getTransactionContext());
		}catch(Exception e){
			throw new RemoteException("ProcessManagerError:"+e.getMessage(), e);
		}
	}

	public String initializeProcessIfRequired(String processDefinition, String instanceId) throws RemoteException {
		if(!UEngineUtil.isNotEmpty(instanceId))
			instanceId = initializeProcess(processDefinition);
		
		return instanceId;
	}

	public void setLoggedRoleMapping(RoleMapping loggedRoleMapping) throws RemoteException {
		if(genericContext==null) genericContext = new HashMap();
		
		genericContext.put(HumanActivity.GENERICCONTEXT_CURR_LOGGED_ROLEMAPPING, loggedRoleMapping);
		//genericContext.put("request", request);

	}

	public String initialize(String processDefinition, String instanceId, RoleMapping loggedRoleMapping) throws RemoteException {
		setLoggedRoleMapping(loggedRoleMapping);
		return initializeProcessIfRequired(processDefinition, instanceId);
	}

	public void delegateRoleMapping(String instanceId, String roleName, String endpoint) throws RemoteException {
		putRoleMapping(instanceId, roleName, endpoint);
		RoleMapping roleMapping = getRoleMappingObject(instanceId, roleName);
		delegateForRoleMapping(instanceId, roleMapping);
	}

	public void delegateRoleMapping(String instanceId, RoleMapping roleMapping) throws RemoteException {
		putRoleMapping(instanceId, roleMapping);
		delegateForRoleMapping(instanceId, roleMapping);
	}
	
	private void delegateForRoleMapping(String instanceId, RoleMapping roleMapping) throws RemoteException{
		try{
			ProcessInstance instance = getInstance(instanceId);
			List<Activity> runningActs = instance.getCurrentRunningActivities();
			for(int i=0; i<runningActs.size(); i++){
				if(runningActs.get(i) instanceof HumanActivity){
					HumanActivity humanActivity = (HumanActivity)runningActs.get(i);
					
					if(humanActivity.getRole().getName().equals(roleMapping.getName()))
						humanActivity.delegate(instance, roleMapping);
				}
			}
		}catch(Exception e){
			throw new RemoteException("ProcessManagerError:"+e.getMessage(), e);
		}
	}
	
//	public void exportProcessDefinitionbyVersionId(String defVerId) throws RemoteException {
//		ProcessDefinitionRemote mainPdr = getProcessDefinitionRemote(defVerId);
//		ArrayList zipEntryMapperList= new ArrayList();
//
//		InputStream in;
//		String filePathRoot = "temp" + File.separatorChar;
//		File isExistFolder = new File(filePathRoot);
//		if(!isExistFolder.exists()){
//			isExistFolder.mkdir();
//		}
//		String filePath = filePathRoot + mainPdr.getName().toString() + ".zip";
//
//		try {
//			Vector subDefinition = getSubDefinitionsDeeply(getDefinition(mainPdr.getId()));
//			subDefinition.add(mainPdr.getId());
//
//			UEngineArchive ua = new UEngineArchive();
//			for(int i=0; i < subDefinition.size() ;i++){
//				String subDefVerId = (String)subDefinition.get(i);
//				ProcessDefinitionRemote pdr = getProcessDefinitionRemote(subDefVerId);
//
//				in = ProcessDefinitionFactory.getInstance(getTransactionContext()).getResourceStream(pdr.getId());
//				String objType="";
//				if(pdr.getObjType()==null){
//					if(pdr.isFolder()) 	objType="folder";
//					else	objType="process";
//				}else{
//					objType = pdr.getObjType();
//				}
//				boolean isRoot=false;
//				if(mainPdr.getId().equals(subDefVerId)){
//					isRoot=true;
//				}
//
//				String defName = pdr.getName().toString();
//				ZipEntryMapper entryMapper = new ZipEntryMapper(defName,subDefVerId,pdr.getAlias(), objType,pdr.getVersion(),in);
//				zipEntryMapperList.add(entryMapper);
//
//				ua.setDefinitionList(defName, pdr.getAlias(), pdr.getBelongingDefinitionId(), subDefVerId, objType, "","", pdr.getParentFolder(), pdr.getId(), isRoot);
//			}
//
//			writeZip(filePath, zipEntryMapperList, ua);
//
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//
//		//return filePath;
//	}
//
	public Vector getSubDefinitionsDeeply(ProcessDefinition pd) throws Exception{
		final Vector definitions = new Vector();
		final ProcessManagerBean pmb=this; 
				
		ActivityForLoop forLoop = new ActivityForLoop(){
			public void logic(Activity act){
				try{
					if(act instanceof SubProcessActivity){
						SubProcessActivity spAct = (SubProcessActivity)act;
						String defVerId = spAct.getDefinitionVersionId("",null);
						definitions.add(defVerId);
						
						pmb.getSubDefinitionsDeeply(pmb.getDefinition(defVerId));
					}
				}catch(Exception e){
					throw new RuntimeException(e);
				}
			}
		};
		forLoop.run(pd);

		return definitions;
	}
	
//	public void writeZip(String filePath, ArrayList zipEntryMapperList,UEngineArchive ua) throws IOException {
//		net.sf.jazzlib.ZipOutputStream zipOut = new net.sf.jazzlib.ZipOutputStream(new FileOutputStream(filePath));
//		int zipEntryMapperLength = zipEntryMapperList.size();
//
//		for(int idx=0; idx<zipEntryMapperLength; idx++) {
//
//			ZipEntryMapper zipEntryMapper = (ZipEntryMapper)zipEntryMapperList.get(idx);
//
//			if(zipEntryMapper.getEntryType().equals(ZipEntryMapper.TYPE_FOLDER)) {
//				zipOut.putNextEntry(new net.sf.jazzlib.ZipEntry(zipEntryMapper.getEntryName() + File.separatorChar));
//
//			} else {
//				String entryName = zipEntryMapper.getEntryAlias() + ZipEntryMapper.ENTRY_SEPARATOR
//					             + zipEntryMapper.getEntryType();
//
//				ua.setProcessDefinitions(zipEntryMapper.getEntryId(), entryName);
//
//				zipOut.putNextEntry(new net.sf.jazzlib.ZipEntry(entryName));
//
//				InputStream zipIn = (InputStream)zipEntryMapper.getStream();
//
//				if(zipIn!=null){
//					byte [] buf = new byte[1024];
//					int len;
//					while ((len = zipIn.read(buf)) > 0) {
//						zipOut.write(buf, 0, len);
//					}
//					zipIn.close();
//		      	}
//			}
//	      	zipOut.closeEntry();
//		}
//
//		//meta-inf
//		zipOut.putNextEntry(new net.sf.jazzlib.ZipEntry("META-INF" + File.separatorChar));
//		zipOut.closeEntry();
//
//		zipOut.putNextEntry(new net.sf.jazzlib.ZipEntry("META-INF" + File.separatorChar+ "manifest.xml"));
//		try {
//			GlobalContext.serialize(ua, zipOut, String.class);
//			zipOut.closeEntry();
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//
//		zipOut.close();
//	}
//
//	public DefinitionArchive[] importDefinitionArchiveList(InputStream is) throws RemoteException {
//		net.sf.jazzlib.ZipInputStream zipIn = new net.sf.jazzlib.ZipInputStream(is);
//		net.sf.jazzlib.ZipEntry zipEntry;
//
//	    DefinitionArchive[] das = null;
//	    try {
//	    	while((zipEntry = zipIn.getNextEntry()) != null) {
//
//		    	ByteArrayOutputStream baos = new ByteArrayOutputStream();
//				byte [] b = new byte[1024];
//				int len = 0;
//				while ( (len=zipIn.read(b))!= -1 ) {
//				    baos.write(b,0,len);
//				}
//
//				String definitionDoc = baos.toString("UTF-8");
//				baos.close();
//
//		    	System.out.println("++++++++++++++++++++++++++++++++++++++++++++");
//		    	System.out.println("zipEntry.getName() : " + zipEntry.getName());
//		    	System.out.println("++++++++++++++++++++++++++++++++++++++++++++");
//
//				if(definitionDoc instanceof String){
//					try{
//						Object obj = GlobalContext.deserialize(definitionDoc.toString(), Object.class);
//
//						if(obj instanceof UEngineArchive){
//							ArrayList definitionAcchiveList=((UEngineArchive)obj).getDefinitionList();
//
//							if(definitionAcchiveList.size()>0){
//								das = new DefinitionArchive[definitionAcchiveList.size()];
//								for (int i = 0; i < definitionAcchiveList.size(); i++) {
//									das[i] = (DefinitionArchive)definitionAcchiveList.get(i);
//								}
//							}
//							break;
//						}
//					}catch (Exception e) {
//
//					}
//				}
//	    	} // end while
//
//			zipIn.close();
//	    } catch (Exception e) {
//	    	e.printStackTrace();
//	    }
//
//	    return das;
//	}
		
	private static String TEMP_DIRECTORY;
	private static String DEFINITION_ROOT;
	
	static {
		TEMP_DIRECTORY = GlobalContext.getPropertyString(
			"server.definition.path",
			"." + File.separatorChar + "uengine" + File.separatorChar + "definition" + File.separatorChar
		);
		
		if(!TEMP_DIRECTORY.endsWith("/") && !TEMP_DIRECTORY.endsWith("\\")){
			TEMP_DIRECTORY = TEMP_DIRECTORY + "/";
		}
		DEFINITION_ROOT = TEMP_DIRECTORY;
		TEMP_DIRECTORY = TEMP_DIRECTORY + "temp" + File.separatorChar + "download" + File.separatorChar;
		
		File f = new File(TEMP_DIRECTORY);
		if (!f.exists()) {
			f.mkdirs();
		}
	}
		
//	public void exportProcessDefinitionbyDefinitionId(String defId, boolean ExportAllVersion) throws Exception {
//		try{
//			UEngineArchive ua = new UEngineArchive();
//			Hashtable options = new Hashtable();
//
//			ProcessDefinitionRemote pdr = getProcessDefinitionRemoteByDefinitionId(defId);
//			String defName = pdr.getName().getText();
//
//			ua.setDefinitionList(defName, "", pdr.getBelongingDefinitionId(), String.valueOf(pdr.getVersion()), ZipEntryMapper.TYPE_FOLDER, defName, "", "-1", pdr.getId(), true);
//
//			options.put(UEngineArchive.UENGINE_ARCHIVE, (UEngineArchive)ua);
//			options.put(UEngineArchive.SUB_PROC, new Hashtable());
//
//			String rootDirectory = TEMP_DIRECTORY + defName + File.separatorChar;
//			File f = new File(rootDirectory);
//			if (f.exists()) {
//				DeleteDir.deleteDir(rootDirectory);
//			} else {
//				f.mkdirs();
//			}
//
//			f = new File(TEMP_DIRECTORY + defName+".zip");
//			if (f.exists()) {
//				f.delete();
//			}
//
//			_DUMMY_LIST_ = new ArrayList<String>();
//
//			setDefinitionsForExport(defId, listProcessDefinitionRemotesLight(), defName + File.separatorChar, options, ExportAllVersion);
//			setSubProcessesForExport(defName, options);
//			setFormsForExport(defName,options);
//
//			FileOutputStream fos = new FileOutputStream(TEMP_DIRECTORY + defName + "\\errlog.txt");
//			OutputStreamWriter osw = new OutputStreamWriter(fos);
//			osw.write(_DUMMY_LIST_.toString());
//			osw.close();
//			fos.close();
//
//			CompressZip cz = new CompressZip();
//			cz.zip(TEMP_DIRECTORY, defName+".zip", TEMP_DIRECTORY, ua);
//
//			DeleteDir.deleteDir(rootDirectory);
//		}catch(Exception e){
//			e.printStackTrace();
//			throw new RemoteException("ProcessManagerError:"+e.getMessage(), e);
//		}
//	}
	

		
	private Hashtable setDefinitionsForExport(String processDefinition, 
			                                  ProcessDefinitionRemote[] pdrs, 
			                                  String parentDirectory, 
			                                  Hashtable options, 
			                                  boolean ExportAllVersion) throws Exception {
		for (int i=0; i<pdrs.length; i++) {
			ProcessDefinitionRemote pdr = pdrs[i];
			
			if(!pdr.getParentFolder().equals(processDefinition)) continue;
			
			UEngineArchive ua = (UEngineArchive)options.get(UEngineArchive.UENGINE_ARCHIVE);
			
			if (pdr.isFolder()) { //folder
				ua.setDefinitionList(pdr.getName().getText(), "", pdr.getBelongingDefinitionId(), String.valueOf(pdr.getVersion()), ZipEntryMapper.TYPE_FOLDER, pdr.getName().getText(), "", pdr.getParentFolder(), pdr.getId(), false);
				options.put(UEngineArchive.UENGINE_ARCHIVE, (UEngineArchive)ua);
				String temp = parentDirectory;
				parentDirectory = parentDirectory + pdr.getName() + File.separatorChar;
				File f = new File( TEMP_DIRECTORY + parentDirectory );
				f.mkdirs();

				setDefinitionsForExport(pdr.getId(), pdrs, parentDirectory, options, ExportAllVersion);
				
				parentDirectory = temp;
			} else if (pdr.isProduction() ) { //process or form
				ua.setDefinitionList(pdr.getName().getText(), pdr.getAlias(), pdr.getBelongingDefinitionId(), String.valueOf(pdr.getVersion()), pdr.getObjType(), pdr.getName().getText()+ZipEntryMapper.ENTRY_SEPARATOR+pdr.getAlias()+ZipEntryMapper.ENTRY_SEPARATOR+pdr.getId()+ZipEntryMapper.ENTRY_SEPARATOR+pdr.getObjType(), pdr.getDescription() == null ? "" : pdr.getDescription().getText(), pdr.getParentFolder(), pdr.getId(), false);
				options.put(UEngineArchive.UENGINE_ARCHIVE, (UEngineArchive)ua);
				getFilePathNFileCopy(pdr, parentDirectory);
				
				if (pdr.getObjType().equals("process")) {
					//sub process
					try {
						Vector tempSubDefinition = getSubDefinitionsDeeply(getProcessDefinition(pdr.getId()));
						Hashtable subDefinition = (Hashtable)options.get(UEngineArchive.SUB_PROC);
						for (int z=0; z<tempSubDefinition.size(); z++ ) {
							String tempDefId = (String)tempSubDefinition.get(z);
							if(!subDefinition.containsKey(tempDefId))
								subDefinition.put(tempDefId,tempDefId);
						}
						options.put(UEngineArchive.SUB_PROC, subDefinition);
					} catch (Exception re) {
						System.out.println("setDefinitionsForExport(process) : " +  pdr.getId());
						_DUMMY_LIST_.add(pdr.getId()+ "(" + pdr.getName() + "," + pdr.getObjType() + "," + pdr.getAlias() + ") ");
					}
				}
			}
		}
		
		return options;
	}
		
	private Hashtable setSubProcessesForExport(String rootDirectory, Hashtable options) throws Exception  {
		
		UEngineArchive ua = (UEngineArchive)options.get(UEngineArchive.UENGINE_ARCHIVE);
		Hashtable subDefinition = (Hashtable)options.get(UEngineArchive.SUB_PROC);
		
		Enumeration keys = subDefinition.keys();
		while (keys.hasMoreElements()) {
			String key = (String) keys.nextElement();
			String subDefVerId = (String) subDefinition.get(key);
			
			ProcessDefinitionRemote pdr = getProcessDefinitionRemote(subDefVerId);
			pdr.setObjType("process");
			
			if ( !ua.containKeys(pdr.getBelongingDefinitionId())) {
	            //_SubProc folder create
				ua.setDefinitionList("_SubProc","", "_SubProc", "", ZipEntryMapper.TYPE_FOLDER ,"_SubProc" ,"", ua.getMainProcessDefinition().getBelongingId(), "", false);
				
				//add sub-process
				String fileName = ZipEntryMapper.ENTRY_SEPARATOR+pdr.getName().getText()+ZipEntryMapper.ENTRY_SEPARATOR+pdr.getAlias()+ZipEntryMapper.ENTRY_SEPARATOR+pdr.getId()+ZipEntryMapper.ENTRY_SEPARATOR+pdr.getObjType();
				String desc = pdr.getDescription() == null ? "" : pdr.getDescription().getText();
				ua.setDefinitionList(pdr.getName().getText(), pdr.getAlias(), pdr.getBelongingDefinitionId(), String.valueOf(pdr.getVersion()), pdr.getObjType(),fileName ,desc, "_SubProc" , pdr.getId(), false);
				
				getFilePathNFileCopy(pdr, rootDirectory + File.separatorChar + "_SubProc" + File.separatorChar, false);
			}
		}
			
		options.put(UEngineArchive.UENGINE_ARCHIVE, (UEngineArchive)ua);
		
		return options;
	}
	
	private void getFilePathNFileCopy(ProcessDefinitionRemote pdr, String parentDirectory) throws Exception {
		getFilePathNFileCopy(pdr, parentDirectory, false);
	}
	
	private void getFilePathNFileCopy(ProcessDefinitionRemote pdr, String parentDirectory, boolean isSub) throws Exception {
		ProcessManagerBean pmb = this;
		ProcessTransactionContext tc = pmb.getTransactionContext();
		ProcessDefinitionVersionRepositoryHomeLocal pdvhr = GlobalContext.createProcessDefinitionVersionRepositoryHomeLocal(tc);
		ProcessDefinitionVersionRepositoryLocal pdvr;

		pdvr = pdvhr.findByPrimaryKey(new Long(pdr.getId()));
		String def = (String) pdvr.getFilePath();
		if (def.startsWith("LINK:")) {
			File f = new File(TEMP_DIRECTORY + parentDirectory);
			if (!f.exists())
				f.mkdirs();
			String resourceLocation = def.substring("LINK:".length());
			String filePath = null;
			if (isSub == false) {
				filePath = TEMP_DIRECTORY + parentDirectory + (pdr.getName().getText() + ZipEntryMapper.ENTRY_SEPARATOR + pdr.getAlias() 
						+ ZipEntryMapper.ENTRY_SEPARATOR + pdr.getId() + ZipEntryMapper.ENTRY_SEPARATOR + pdr.getObjType());
			} else if (isSub == true) {
				filePath = TEMP_DIRECTORY + parentDirectory + ("sub" + ZipEntryMapper.ENTRY_SEPARATOR + pdr.getName().getText() 
						+ ZipEntryMapper.ENTRY_SEPARATOR + pdr.getAlias() + ZipEntryMapper.ENTRY_SEPARATOR + pdr.getId() 
						+ ZipEntryMapper.ENTRY_SEPARATOR + pdr.getObjType());
			}
			FileCopy fc = new FileCopy(DEFINITION_ROOT + resourceLocation, filePath);
			fc.start();
		}
	}
	
//	public Hashtable importProcessAliasCheck(InputStream is) throws Exception {
//		Hashtable result = new Hashtable();
//
//		Hashtable inputStreamList = expandFiles(is);
//		String key = "manifest.xml";
//		String manifestXml = (String) inputStreamList.get(key);
//		UEngineArchive ua = (UEngineArchive) GlobalContext.deserialize(manifestXml);
//		ProcessDefinitionRemote[] pds = listProcessDefinitionRemotesLight();
//		boolean[] duplication = new boolean[ua.getDefinitionList().size()];
//
//		for (int i = 0; i < ua.getDefinitionList().size(); i++) {
//			DefinitionArchive da = (DefinitionArchive) ua.getDefinitionList().get(i);
//			ProcessDefinitionRemote pdr = null;
//			for (int j = 0; j < pds.length; j++) {
//				pdr = pds[j];
//				if (da.getAlias().equals(pdr.getAlias()) && UEngineUtil.isNotEmpty(da.getAlias())) {
//					duplication[i] = true;
//					break;
//				}
//			}
//		}
//
//		result.put((String) "duplicationProcessList", duplication);
//		result.put((String) "processDefinitionArchive", ua);
//
//		return result;
//	}
//
//	public Vector importProcessDefinition(String parentFolder,
//										  InputStream loadedZipFile,
//										  UEngineArchive editedUa,
//										  String[] command ) throws Exception {
//		try{
//			Hashtable newDefIdList = new Hashtable();
//			Hashtable newDefVerIdList = new Hashtable();
//			Hashtable newAliasList = new Hashtable();
//
//			Hashtable inputStreamList = expandFiles(loadedZipFile);
//
//			UEngineArchive ua = (UEngineArchive)GlobalContext.deserialize((String)inputStreamList.get("manifest.xml"));
//
//			//step1 : import root-folder
//			String newRootFolderName = editedUa.getMainProcessDefinition().getName();
//			String newRootFolderId = addFolder(newRootFolderName, parentFolder);
//
//			String oldRootFolderId = ua.getMainProcessDefinition().getBelongingId();
//			newDefIdList.put(oldRootFolderId, newRootFolderId);
//
//			//step2 : import child-folder
//			importFolder(newRootFolderId, oldRootFolderId, ua,editedUa, newDefIdList);
//
//			//step3 : import definitions
//			importDefinitions(inputStreamList, command, ua, editedUa, newDefIdList,newDefVerIdList,newAliasList);
//
//			//step4 : relate sub-process and form
//			relateDefinitionsForImport(ua, editedUa, newDefIdList, newDefVerIdList, newAliasList);
//
//			Vector defIdList = new Vector();
//			Enumeration keys = newDefIdList.keys();
//			while (keys.hasMoreElements()) {
//				String key = (String) keys.nextElement(); //Old DefId
//				String toFindDefid = (String) newDefIdList.get(key); //New DefId
//				defIdList.add(toFindDefid);
//			}
//			return defIdList;
//
//		}catch(Exception e){
//			e.printStackTrace();
//			throw new RemoteException("ProcessManagerError:"+e.getMessage(), e);
//		}
//	}

	private void importFolder(String newRootFolderId,
			                  String oldRootFolderId, 
			                  UEngineArchive ua,
			                  UEngineArchive edited_ua, 
			                  Hashtable newDefIdList) throws Exception {
				
		for (int i = 0; i < ua.getDefinitionList().size(); i++) {
			DefinitionArchive da = (DefinitionArchive) ua.getDefinitionList().get(i);
			DefinitionArchive fixedDa = (DefinitionArchive) edited_ua.getDefinitionList().get(i);

			if (da.isRoot() == false && da.getObjectType().equals("folder") && da.getParentFolder().equals(oldRootFolderId)) {
				String newFolderId = addFolder(fixedDa.getName(), newRootFolderId);
			
				newDefIdList.put(da.getBelongingId(),newFolderId);
				
				importFolder(newFolderId, da.getBelongingId(), ua,edited_ua,newDefIdList);
			}
		}
	}
	
	private void importDefinitions(Hashtable inputStreamList,
            					   String[] command ,
            					   UEngineArchive ua,
			                       UEngineArchive edited_ua, 
			                       Hashtable newDefIdList,
			                       Hashtable newDefVerIdList,
			                       Hashtable newAliasList) throws Exception {

		for (int i = 0; i < ua.getDefinitionList().size(); i++) {
			DefinitionArchive da = (DefinitionArchive) ua.getDefinitionList().get(i);
			DefinitionArchive fixedDa = (DefinitionArchive) edited_ua.getDefinitionList().get(i);
			String[] deployedDefinitionInformation = null;

			if (!da.isRoot()&& fixedDa.getArchiveFileName().equals(da.getArchiveFileName()) && !"folder".equals(da.getObjectType())) {
				
				String key = da.getName() + "." + da.getAlias() + "." + da.getId() + "." + da.getObjectType();
				String definitionDoc = (String) inputStreamList.get(key);

				if (definitionDoc != null) {
					Map objectOptions = new HashMap();
					objectOptions.put("alias", fixedDa.getAlias());
					objectOptions.put("objectType", da.getObjectType());
					
					if ("new".equals(command[i])) {
						String parentFolder = (String) newDefIdList.get(da.getParentFolder());
	
						deployedDefinitionInformation = ProcessDefinitionFactory.getInstance(getTransactionContext()).addDefinitionImpl(
								null, null, 1, fixedDa.getName(), null, false, definitionDoc, parentFolder, false, objectOptions);
						
					} else if ("update".equals(command[i])) {
						String parentFolder = null;
						ProcessDefinitionRemote[] pdrs = listProcessDefinitionRemotesLight();
						for (int j = 0; j < pdrs.length; j++) {
							ProcessDefinitionRemote tempPdr = pdrs[i];
							if ((!tempPdr.isFolder) && ((tempPdr.getAlias()).equals(fixedDa.getAlias())) && (fixedDa.getAlias() != null && !fixedDa.getAlias().equals(""))) {
								parentFolder = tempPdr.getParentFolder();
							}
						}

						String pdId = getProcessDefinitionIdByAlias(fixedDa.getAlias());
						ProcessDefinitionRemote[] findLastVersion = findAllVersions(pdId);
						
						int versionId = 0;
						for (int j = 0; j < findLastVersion.length; j++) {
							int compareVersionId = findLastVersion[j].getVersion();
							if (versionId < compareVersionId) {
								versionId = compareVersionId;
							}
						}

						deployedDefinitionInformation = ProcessDefinitionFactory.getInstance(getTransactionContext()).addDefinitionImpl(
								pdId, null, versionId+1, fixedDa.getName(), null, false, definitionDoc, parentFolder, false, objectOptions);
					}
					
					String pdvId = deployedDefinitionInformation[0];
					newDefIdList.put(da.getBelongingId(),deployedDefinitionInformation[1]);
					newDefVerIdList.put(da.getId(),pdvId);
					newAliasList.put(da.getAlias(), fixedDa.getAlias());
					
					setProcessDefinitionProductionVersion(pdvId);

				}
			}
		}
	}
	

	
	private void replaceMacro(String HTML_PATH) throws Exception {
		File htmlFile = new File(HTML_PATH);
		BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream(htmlFile), "UTF-8"));
		StringBuffer daoHeader = new StringBuffer();
		StringBuffer contents = new StringBuffer();
		String line = "";

		while ((line = in.readLine()) != null) {
			if (line.indexOf("<form") > -1) {
				line = deleteTag(line, "<form");
			}
			if (line.indexOf("</form>") > -1) {
				line = deleteTag(line, "</form>");
			}
			contents.append(line);
			contents.append("\r\n");
		}
		UEngineUtil.saveContents(htmlFile.getAbsolutePath(), daoHeader.toString() + contents.toString());
	}
	
	private String deleteTag(String src, String key) throws Exception {
		String retHtml = "";

		int beg = 0;
		int end = 0;
		int keysize = key.length();
		boolean bcontinue = true;

		if (key.equals("<form")) {
			end = src.indexOf(key);
			retHtml = src.substring(beg, end);
			beg = src.indexOf(">", end) + 1;
			retHtml = " " + retHtml + " " + src.substring(beg);
		} else if (key.equals("</form>")) {
			end = src.indexOf(key);
			retHtml = src.substring(beg, end);
			retHtml = " " + retHtml + src.substring(end + 7);
		}

		return retHtml;
	}

//	private Hashtable expandFiles(InputStream is) throws Exception {
//		net.sf.jazzlib.ZipInputStream zipIn = new net.sf.jazzlib.ZipInputStream(is);
//		net.sf.jazzlib.ZipEntry zipEntry = zipIn.getNextEntry();
//		Hashtable inputStreamList = new Hashtable();
//		while (zipEntry != null) {
//			if (!zipEntry.isDirectory()) {
//				ByteArrayOutputStream baos = new ByteArrayOutputStream();
//				byte[] b = new byte[1024];
//				int len = 0;
//				while ((len = zipIn.read(b)) != -1) {
//					baos.write(b, 0, len);
//				}
//				String definitionDoc = baos.toString("UTF-8");
//
//				String zipEntryName = zipEntry.getName();
//				zipEntryName = zipEntryName.replace("//", "/");
//				zipEntryName = zipEntryName.replace("\\\\", "/");
//				zipEntryName = zipEntryName.replace("\\", "/");
//
//				String key = (String) zipEntryName.substring(zipEntryName.lastIndexOf("/") + 1, zipEntryName.length());
//				inputStreamList.put((String) key, (String) definitionDoc);
//				baos.close();
//
//				zipEntry = zipIn.getNextEntry();
//			} else {
//				zipEntry = zipIn.getNextEntry();
//			}
//		}
//		zipIn.close();
//		return inputStreamList;
//	}

	public HashMap getInitiationParameterMap(String pvdid) throws RemoteException {
		try {
			ActivityReference initiatorHumanActivityReference = getInitiatorHumanActivityReference(pvdid);
			
			if(initiatorHumanActivityReference!=null){			
				String initiatorHumanActivityTracingTag = initiatorHumanActivityReference.getAbsoluteTracingTag();
				HumanActivity initiatorHumanActivity = (HumanActivity)initiatorHumanActivityReference.getActivity();
/*				String tool = initiatorHumanActivity.getTool();
				String url = "../wih/" + tool + "/index.jsp";
*/				
				Map parameterMap = initiatorHumanActivity.getParameterMap();

				HashMap hashMapParameterMap = new HashMap();
				
				hashMapParameterMap.put("absoluteTracingTag", initiatorHumanActivityTracingTag);
				hashMapParameterMap.putAll(parameterMap);
				
				return hashMapParameterMap;
			}else
				return null;
			
		}catch(Exception e){
			throw new RemoteException("ProcessManagerError:"+e.getMessage(), e);
		}
	}

	public String getProcessDefinitionWithInstanceIdWithoutInheritance(
			String instanceId, String encodingStyle) throws RemoteException {
		// TODO Auto-generated method stub
		return getProcessDefinitionWithInstanceId(
				instanceId, encodingStyle, true);
	}

	public String getProcessDefinitionWithoutInheritance(String defVerId,
			String encodingStyle) throws RemoteException {
		// TODO Auto-generated method stub
		return getProcessDefinition(defVerId,
				encodingStyle, true);
	}

//    @Override
//    public void importProcessDefinitionGraciously(String parentFolder, String itemId, String itemFilePath, String loggedUserGlobalCom) throws Exception {
//	// TODO get MarketItem's zip file by itemId
//	InputStream is = null;
//	try {
//	    is = new BufferedInputStream(new FileInputStream(itemFilePath));
//	} catch (FileNotFoundException e) {
//	    e.printStackTrace();
//	    throw e;
//	}
//
//	// TODO import zip file to ProcessCodi
//	// step 1 : unzip
//	// step 2 : rename alias
//	Hashtable result = processAliasReviseForPurchase(is, loggedUserGlobalCom + "_purchased_" + itemId + "_");
//	//Hashtable result = importProcessAliasCheck(is);
//
//	// step 3 : import
//	UEngineArchive editedUa = (UEngineArchive) result.get("editedUa");
//	String[] command = (String[]) result.get("command");
//	try {
//	    is = new BufferedInputStream(new FileInputStream(itemFilePath));
//
//	    Vector newDefId = importProcessDefinition("-1", is, editedUa, command);
//
//	    AclManager dau = AclManager.getInstance(getTransactionContext());
//	    for (int i = 0; i < newDefId.size(); i++) {
//		String defId = (String) newDefId.get(i);
//
//		dau.setPermission(Integer.parseInt(defId), Authority.createAuthorityList(loggedUserGlobalCom, AclManager.ACL_FIELD_COM, loggedUserGlobalCom, new String[] { AclManager.PERMISSION_MANAGEMENT + "" }));
//
//		ProcessDefinitionDAO procDef = (ProcessDefinitionDAO) getTransactionContext().findSynchronizedDAO("BPM_PROCDEF", "DEFID", defId, ProcessDefinitionDAO.class);
//		procDef.setComCode(loggedUserGlobalCom);
//	    }
//	} catch (FileNotFoundException e) {
//	    e.printStackTrace();
//	    throw e;
//	}
//    }

//    private Hashtable processAliasReviseForPurchase(InputStream is, String aliasHeader) throws Exception {
//	Hashtable result = new Hashtable();
//	try {
//	    Hashtable inputStreamList;
//
//	    inputStreamList = expandFiles(is);
//
//	    String key = "manifest.xml";
//	    String manifestXml = (String) inputStreamList.get(key);
//	    UEngineArchive ua = (UEngineArchive) GlobalContext.deserialize(manifestXml);
//	    ProcessDefinitionRemote[] pds = listProcessDefinitionRemotesLight();
//	    String[] command = new String[ua.getDefinitionList().size()];
//
//	    for (int i = 0; i < command.length; i++) {
//		command[i] = "new";
//	    }
//
//	    for (int i = 0; i < ua.getDefinitionList().size(); i++) {
//		DefinitionArchive da = (DefinitionArchive) ua.getDefinitionList().get(i);
//		if(!"folder".equals(da.getObjectType())) {
//		    da.setAlias(aliasHeader + da.getAlias());
//		}
//		ProcessDefinitionRemote pdr = null;
//		for (int j = 0; j < pds.length; j++) {
//		    pdr = pds[j];
//		    if (da.getAlias().equals(pdr.getAlias()) && UEngineUtil.isNotEmpty(da.getAlias())) {
//			command[i] = "update";
//			break;
//		    }
//		}
//	    }
//
//	    result.put((String) "command", command);
//	    result.put((String) "editedUa", ua);
//	    return result;
//	} catch (Exception e) {
//	    throw e;
//	}
//    }

    @Override
    public String exportProcessDefinitionForAddToMarket(String defId, String loggedUserGlobalCom) throws Exception {
	exportProcessDefinitionbyDefinitionId(defId, false);
	String fileName = "";
	if (!defId.equals("-1")) {
	    fileName = getProcessDefinitionRemoteByDefinitionId(defId).getName().getText();
	} else {
	    fileName = "root";
	}

	String TEMP_DIRECTORY = GlobalContext.getPropertyString("server.definition.path", "." + File.separatorChar + "uengine" + File.separatorChar + "definition" + File.separatorChar);

	if (!TEMP_DIRECTORY.endsWith("/") && !TEMP_DIRECTORY.endsWith("\\")) {
	    TEMP_DIRECTORY = TEMP_DIRECTORY + "/";
	}
	TEMP_DIRECTORY = TEMP_DIRECTORY + "temp" + File.separatorChar + "download" + File.separatorChar;
	String filePath = TEMP_DIRECTORY + fileName + ".zip";

	return filePath;
    }

	@Override
	public void setChanged() {
		//do nothing.  ProcessManagerDirtyAdvice will check this.
	}

	@Override
	public void exportProcessDefinitionbyVersionId(String defVerId) throws Exception {

	}

	@Override
	public Vector importProcessDefinition(String parentFolder, InputStream loadedZipFile, UEngineArchive editedUa, String[] command) throws Exception {
		return null;
	}

	@Override
	public Hashtable importProcessAliasCheck(InputStream is) throws Exception {
		return null;
	}

	@Override
	public DefinitionArchive[] importDefinitionArchiveList(InputStream is) throws RemoteException {
		return new DefinitionArchive[0];
	}

	@Override
	public void importProcessDefinitionGraciously(String parentFolder, String itemId, String itemFilePath, String loggedUserGlobalCom) throws Exception {

	}
}