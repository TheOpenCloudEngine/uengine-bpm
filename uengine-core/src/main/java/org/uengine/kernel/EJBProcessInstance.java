package org.uengine.kernel;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

import org.uengine.contexts.ComplexType;
import org.uengine.contexts.IFileContent;
import org.uengine.persistence.dao.UniqueKeyGenerator;
import org.uengine.persistence.processinstance.ProcessInstanceDAO;
import org.uengine.persistence.processinstance.ProcessInstanceDAOType;
import org.uengine.persistence.processvariable.ProcessVariableDAO;
import org.uengine.persistence.processvariable.ProcessVariableDAOType;
import org.uengine.persistence.rolemapping.RoleMappingDAO;
import org.uengine.persistence.rolemapping.RoleMappingDAOType;
import org.uengine.processmanager.ProcessTransactionContext;
import org.uengine.processmanager.SimulatorTransactionContext;
import org.uengine.processmanager.TransactionContext;
import org.uengine.util.MeasuringContext;
import org.uengine.util.UEngineUtil;
import org.uengine.webservices.worklist.WorkList;
import org.uengine.webservices.worklist.WorkListServiceLocator;
//

/**
 * @author Jinyoung Jang
 */

public class EJBProcessInstance extends DefaultProcessInstance implements TransactionListener {
	
	private static final long serialVersionUID = org.uengine.kernel.GlobalContext.SERIALIZATION_UID;

	protected final static int TYPE_NULL 	= -2;
	protected final static int TYPE_ANY 	= -1;
	protected final static int TYPE_STRING 	= 1;
	protected final static int TYPE_INTEGER = 2;
	protected final static int TYPE_LONG 	= 3;
	protected final static int TYPE_BOOLEAN = 4;
	protected final static int TYPE_DATE 	= 5;
	protected final static int TYPE_CALENDAR= 6;
//	protected final static int TYPE_FORMCONTEXT= 7;

	protected final static int TYPE_FILECONTENT= 9;

	//dirty field list for synchronizing when endCaching()
	Map modifiedKeyMap;
	Map modifiedRoleMappings;
	Map cachedRoleMappings;
	//
	
	//for caching
	boolean caching;
		public boolean isCaching() {
			return caching;
		}
		public void setCaching(boolean b) {
			caching = b;
		}
	
	boolean processVariablesAreCached = false;
	boolean roleMappingsAreCached = false;
	boolean isNew = false;
	//
	
//	ProcessInstanceRepositoryLocal processInstanceRepositoryLocal;
//	public ProcessInstanceRepositoryLocal getProcessInstanceRepositoryLocal() throws Exception{
//		if(processInstanceRepositoryLocal == null){
//			ProcessInstanceRepositoryHomeLocal pirh = GlobalContext.createProcessInstanceRepositoryHomeLocal();
//			processInstanceRepositoryLocal = pirh.findByPrimaryKey(new Long(getInstanceId()));
//		}
//		
//		return processInstanceRepositoryLocal;			
//	}

	ProcessInstanceDAO processInstanceDAO;
	public ProcessInstanceDAO getProcessInstanceDAO() throws Exception{
		if(processInstanceDAO == null){
			ProcessInstanceDAOType pidt = ProcessInstanceDAOType.getInstance(getProcessTransactionContext());
			processInstanceDAO = pidt.findByPrimaryKey(new Long(getInstanceId()));
			processInstanceDAO.getImplementationObject().setTableName("BPM_PROCINST");
			processInstanceDAO.getImplementationObject().setKeyField("INSTID");
			processInstanceDAO.getImplementationObject().createUpdateSql();
		}
		
		return processInstanceDAO;
	}

	ProcessVariableDAOType pvdf;
	public ProcessVariableDAOType getProcessVariableDAOFacade() {
		if(pvdf==null){
			pvdf = ProcessVariableDAOType.getInstance(ptc);
		}
		
		return pvdf;
	}
		
	public EJBProcessInstance (ProcessDefinition procDef, String name, Map options) throws Exception{
		setCaching(true);
		
		//isNew field is for recognizing the instance is just initiated in the transaction. 
		// when the argument procDef is provided, it means also initiation stage. 
		if(procDef != null)
			isNew = true;
		
		//creates an instance for the use of factory logic if definition is null
		if(procDef==null) return;			
		
		if ( (getProcessTransactionContext()==null || (getProcessTransactionContext() instanceof SimulatorTransactionContext)) && options != null && options.get("ptc") != null ) {
			super.setProcessTransactionContext( (ProcessTransactionContext)options.get("ptc") );
		}
		
		getProcessTransactionContext().addTransactionListener(this);
		
		Long instanceId = UniqueKeyGenerator.issueProcessInstanceKey(getProcessTransactionContext());
		
		ProcessInstanceDAOType pidt = ProcessInstanceDAOType.getInstance(getProcessTransactionContext());
		processInstanceDAO = pidt.createDAOImpl(null);
				
		processInstanceDAO.setStatus(Activity.STATUS_READY);
		processInstanceDAO.setDefName(procDef.getName());
		processInstanceDAO.setInstId(instanceId);
		processInstanceDAO.setDefModDate(procDef.getModifiedDate().getTime());
		
		setInstanceId(""+processInstanceDAO.getInstId());
	
		if(!UEngineUtil.isNotEmpty(name))
			name = procDef.getName() + instanceId;
			
		processInstanceDAO.setName(name);
		setName(name);
		
		Date now = GlobalContext.getNow(getProcessTransactionContext()).getTime();
		processInstanceDAO.setStartedDate(now);
		
		boolean isSubProcess = 
			(	options!=null 
				&& options.containsKey("isSubProcess") 
				&& options.get("isSubProcess").equals("yes")
			);
			
		if(isSubProcess){
			processInstanceDAO.setIsSubProcess(true);			
			processInstanceDAO.setMainInstId(new Long((String)options.get(DefaultProcessInstance.RETURNING_PROCESS)));
			processInstanceDAO.setMainActTrcTag((String)options.get(DefaultProcessInstance.RETURNING_TRACINGTAG));
			processInstanceDAO.setMainExecScope((String)options.get(DefaultProcessInstance.RETURNING_EXECSCOPE));
			processInstanceDAO.setDontReturn(((Boolean)options.get(DefaultProcessInstance.DONT_RETURN)).booleanValue());
			processInstanceDAO.setIsEventHandler(options.containsKey("isEventHandler"));
		}
		
		if(options.containsKey(DefaultProcessInstance.ROOT_PROCESS)){
			processInstanceDAO.setRootInstId(new Long((String)options.get(DefaultProcessInstance.ROOT_PROCESS)));
		}else{
			processInstanceDAO.setRootInstId(instanceId);
		}

		processInstanceDAO.setDefVerId(procDef.getId());
		processInstanceDAO.setDefId(procDef.getBelongingDefinitionId());
		
		getProcessTransactionContext().registerProcessInstance(this);
	}

	public EJBProcessInstance () throws Exception{
		setInstanceId(null);
	}
	
	public String getMainActivityTracingTag(){
		try {
			return getProcessInstanceDAO().getMainActTrcTag();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public ProcessInstance getInstance(String instanceId) throws Exception{
		return this.getInstance(instanceId, null);
	}
	
	//TODO hotspot
	public ProcessInstance getInstance(String instanceId, Map options) throws Exception{

		if(!UEngineUtil.isNotEmpty(instanceId)){
			throw new UEngineException("Check your parameter! Instance Id is null");
			
/*			EJBProcessInstance tempInst =  new EJBProcessInstance();
			tempInst.setCaching(true);
			tempInst.setInstanceId( null);
			if (options != null && options.get("ptc") != null ) {
				tempInst.setProcessTransactionContext( (TransactionContext)options.get("ptc") );
			}
			
			return tempInst;
*/		}
		
		ProcessTransactionContext ptc = getProcessTransactionContext();
		if (options != null && options.containsKey("ptc")) {
			ptc = (ProcessTransactionContext)options.get("ptc");
		}
		
		if(ptc==null)
			throw new UEngineException("TransactionContext should be provided.");
		
		setProcessTransactionContext(ptc);

		if(isCaching()){
			ProcessInstance instance = ptc.getProcessInstanceInTransaction(instanceId);
			if(instance!=null)
				return instance;
		}
		
		try{
addDebugInfo("EJBProcessInstance: instanceId is set by " + instanceId);


			setInstanceId(instanceId);
			boolean isArchive = getProcessInstanceDAO().getIsArchive();
			
			if(GlobalContext.logLevelIsDebug){
				addDebugInfo("Definition administration url",GlobalContext.WEB_CONTEXT_ROOT + "/processmanager/viewProcessFlowChart.jsp?processDefinition=" + getProcessInstanceDAO().getDefId() + "&processDefinitionVersionID=" + getProcessInstanceDAO().getDefVerId());
				addDebugInfo("Process Designer launch url",GlobalContext.WEB_CONTEXT_ROOT + "/processmanager/ProcessDesigner.jnlp?defVerId=" + getProcessInstanceDAO().getDefVerId() + "&defId=" + getProcessInstanceDAO().getDefId());
				addDebugInfo("Instance administration url", GlobalContext.WEB_CONTEXT_ROOT + "/processmanager/viewProcessInformation.jsp?instanceId=" + getInstanceId());
			}
			
			
			//if the instance is archived, the instance would be loaded from a serialized XML file.
			if(isArchive){
				return FileProcessArchive.load(instanceId, ptc);
			}
//			ProcessInstance inst = create();
//			inst.setInstanceId(instanceId);
//		
//			return inst;
			getProcessTransactionContext().addTransactionListener(this);
			ptc.registerProcessInstance(this);
			
			//if the instance has been retreived by instanceId, it tells us it is not newly initiated status.
			isNew = false;
			
			return this;
			
		//The case that there's no such instance record in bpm_procinst. Perhaps the instance is archived.
		}catch(javax.ejb.ObjectNotFoundException onfe){
			
			//try to find in file system
			try{
				return FileProcessArchive.load(instanceId, ptc);
			}catch(Exception e){
				throw new UEngineException("No such process instance.");
			}			
		}
	}
	
	public void applyChanges() throws Exception{
		MeasuringContext mc = new MeasuringContext("EJBProcessInstance:applyChanges()");
		
		ProcessInstanceDAO procInsDAO = getProcessInstanceDAO();
		procInsDAO.getImplementationObject().setTableName("BPM_PROCINST");
		procInsDAO.getImplementationObject().setKeyField("INSTID");
		procInsDAO.setModDate(GlobalContext.getNow(getProcessTransactionContext()).getTime());
		
		if(isNew){
			procInsDAO.getImplementationObject().createInsertSql();
			
			isNew = false;
		}else{
			procInsDAO.getImplementationObject().createUpdateSql();
		}
		
		
		//TODO Checking for dirty field is needed
		procInsDAO.update();

		mc.printElapsedTime(this);

		if(modifiedKeyMap!=null){
			//System.out.println("modifiedKeyMap.size() ===========================> " + modifiedKeyMap.size());
//changed to let the variable/property values persist so that the history of data change can be stored
			if(modifiedKeyMap.size() > 0)
				getProcessVariableDAOFacade().deleteValue(getInstanceId(), modifiedKeyMap.keySet().iterator());

			ProcessVariableDAO pvd = getProcessVariableDAOFacade().createProcessVariableDAOForBatchInsert();
			for(Iterator iterator = modifiedKeyMap.keySet().iterator(); iterator.hasNext();){
				String fullKey = (String)iterator.next();
				
				String[] scopeAndKey = (String[])modifiedKeyMap.get(fullKey);
				String scope = scopeAndKey[0];
				String key = scopeAndKey[1];
				
				boolean isProperty = isProperty(fullKey);
				
				Serializable cachedValue = (isProperty ? this.getProperty(scope, key) : this.getSourceValue(scope, key));
				if(cachedValue instanceof IndexedProcessVariableMap){
					ProcessVariableValue multipleValue = (ProcessVariableValue)getMultiple(scope, key);
					multipleValue.beforeFirst();
					int i=0;
					do{
						setImpl(scope, key, multipleValue.getValue(), i, true, false, true, pvd, false);
						i++;
					}while(multipleValue.next());
				}
				else
					setImpl(scope, key, cachedValue, 0, false, false, true, pvd, isProperty);
			}
			
			pvd.updateBatch();
		}

		mc.printElapsedTime(this);

		setCaching(false);
		
		if(modifiedRoleMappings!=null){
			RoleMappingDAOType rmDAOFacade = RoleMappingDAOType.getInstance(ptc);

			if(modifiedRoleMappings.size() > 0)				
				rmDAOFacade.removeRoleMappings(getInstanceId(), modifiedRoleMappings.keySet().iterator());
								
			RoleMappingDAO roleMappingDAO = rmDAOFacade.createDAOForInsertRoleMappingBatch();
			
			for(Iterator iterator = modifiedRoleMappings.keySet().iterator(); iterator.hasNext();){
				String roleName = (String)iterator.next();
				RoleMapping rm = (RoleMapping)cachedRoleMappings.get(roleName);
				
//(new Exception("[EJBProcessInstance:putRoleMappingImpl] instanceId = " + instanceId + "; RoleName = " + roleName)).printStackTrace();

				putRoleMappingImpl(roleName, rm, true, roleMappingDAO);
			}
			
			roleMappingDAO.updateBatch();
		}
		
		mc.printElapsedTime(this);

	}
	

	protected int getDataType(Object value){
		if(value==null) return TYPE_NULL; //null
		
		if(value instanceof String) 	return TYPE_STRING;
		if(value instanceof Integer) 	return TYPE_INTEGER;
		if(value instanceof Long) 		return TYPE_LONG;
		if(value instanceof Boolean)	return TYPE_BOOLEAN;
		if(value instanceof Date) 		return TYPE_DATE;
		if(value instanceof Calendar) 	return TYPE_CALENDAR;
		if(value instanceof IFileContent)	return TYPE_FILECONTENT;
		
		return TYPE_ANY;//means any type (xml serialization)
	}

	public void set(String scopeByTracingTag, String key, Serializable val) throws Exception{
		
		if(val instanceof ProcessVariableValue){
			ProcessVariableValue pvv = (ProcessVariableValue)val;
			pvv.setName(key);
			set(scopeByTracingTag, pvv);
			
			getProcessDefinition().firePropertyChangeEventToActivityFilters(this, "variable", pvv);

			return;
		}
		
		setImpl(scopeByTracingTag, key, val, 0, false, isCaching(), false, null, false);

		ProcessVariableValue pvv = new ProcessVariableValue();
		pvv.setName(key);
		pvv.setValue(val);
		getProcessDefinition().firePropertyChangeEventToActivityFilters(this, "variable", pvv);
	}

	public void setProperty(String scopeByTracingTag, String key, Serializable val) throws Exception{
		
		//If the activity where the scopeByTracingTag is under an execution scope, the property space should be devided.
		ExecutionScopeContext esc = getExecutionScopeContext();
		if(esc!=null){
			Activity activity = getProcessDefinition().getActivity(scopeByTracingTag);
		
			if(activity == esc.getRootActivityInTheScope() || esc.getRootActivityInTheScope().isAncestorOf(activity)){
				scopeByTracingTag = scopeByTracingTag + "." + esc.getExecutionScope();
			}
		}
		
		setImpl(scopeByTracingTag, key, val, 0, false, isCaching(), false, null, true);
	}
	
	public void add(String scopeByTracingTag, String key, Serializable val, int index) throws Exception{
		if(isCaching()){
			super.add(scopeByTracingTag, key, val, index);
			
			if(modifiedKeyMap==null)
				modifiedKeyMap = new Hashtable();
			
			modifiedKeyMap.put(createFullKey(scopeByTracingTag, key, false), new String[]{scopeByTracingTag, key});

		}else

			setImpl(scopeByTracingTag, key, val, index, true, isCaching(), false, null, false);
	}

	private void setImpl(String scopeByTracingTag, String key, Serializable val, int index, boolean append, boolean toCache, boolean isBatch, ProcessVariableDAO pvd, boolean isProperty) throws Exception{
		
		if(!isProperty && getProcessDefinition()!=null){
			ProcessVariable pv = getProcessDefinition().getProcessVariable(key);

			if(pv==null) throw new UEngineException("[ProcessInstance.set] Process [" + getProcessDefinition() + "] tries to set value for an UNDECLARED PROCESS VARIABLE [" + key + "]. Check whether the spell is correct.");

			addDebugInfo(" --- [Set Variable] --------------------\n  * name : "+pv.getName()+"\n  * value : " + (GlobalContext.logLevelIsDebug ? "\n"+GlobalContext.serialize(val, String.class) : val +"'"));
			addDebugInfo(" ---------------------------------------");
		
			if(pv!=null && pv.shouldAccessValueInSpecializedWay()){
				pv.set(this, scopeByTracingTag, val);
				return;
			}
			
			
			if(key.indexOf('.') > -1){
				
				//case 1 :  the variable type is kind of ProcessVariablePartResolver that already has the protocol
				if( ProcessVariablePartResolver.class.isAssignableFrom(pv.getType()) ){
					String [] wholePartPath = key.replace('.','@').split("@");
					/*String [] partPath = new String[wholePartPath.length-1];
					for(int i=0; i<partPath.length; i++){
						partPath[i] = wholePartPath[i+1];
					}*/
	
					Object sourceValue = get("", wholePartPath[0]);
	
					ProcessVariablePartResolver variableDelegator = (ProcessVariablePartResolver)sourceValue;
					variableDelegator.setPart(this, wholePartPath, val);
					
					return;
				}
				
				//case 2 :  the variable type is kind of complex type that needs bean property setting
				if(ComplexType.class.isAssignableFrom(pv.getType())){
					String [] wholePartPath = key.replace('.','@').split("@");
					String majorKey = wholePartPath[0];
					Object sourceValue = get("", majorKey);
					Class theType = ((ComplexType)pv.getDefaultValue()).getTypeClass();
					
					if(sourceValue == null || sourceValue instanceof ComplexType){
						sourceValue = theType.newInstance();
					}
					
					String propKeyOnly = key.substring(key.indexOf(".")+1);
					
					UEngineUtil.setBeanPropertyValue(sourceValue, propKeyOnly, val);
					
					val = (Serializable) sourceValue;
					key = majorKey;

				}

			}
			
			/**
			 * if the process variable is volatile, never stores it in persisting storage.
			 */
			if(pv.isVolatile()) toCache = true;
		}

		if(toCache){
//System.out.println("The value in the cach is used: set ================================");
			
			if(val instanceof ProcessVariableValue){
				throw new UEngineException("Object ProcessVariableValue cannot be set by method 'set(String scope, String variableName, Serializable value)' but 'set(String scope, ProcessVariableValue value)' rather.");
			}
			
			if(isProperty)
				super.setProperty(scopeByTracingTag, key, val);
			else
				super.set(scopeByTracingTag, key, val);
			
			//check the values became dirty so that they can be updated into database later
			if(modifiedKeyMap==null)
				modifiedKeyMap = new Hashtable();
			
			modifiedKeyMap.put(createFullKey(scopeByTracingTag, key, isProperty), new String[]{scopeByTracingTag/*, new Boolean(isInserted)*/, key});

			return;
		}		
		
		int dataType =getDataType(val);

		if(dataType==TYPE_ANY){
			//try{
			//TODO: type-sensitive serialization is enabled now. You may let this disabled for the performance issue						
			ProcessVariable pd = getProcessDefinition()	
					.getProcessVariable(key);
			
											
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			
			GlobalContext.serialize(val, bos, pd);
									//encoding
			String xml = bos.toString(GlobalContext.DATABASE_ENCODING);
						
			//TODO: [serious] value should be very short if you use oracle.. how to?
			//if(xml.length() > GlobalContext.DATABASE_MAXSTRLENGTH)
			//	xml = xml.substring(GlobalContext.DATABASE_MAXSTRLENGTH);

			val = xml;
//
//			}catch(Exception e){
//				e.printStackTrace();
//			}	
		}		
		
		String fullKey = createFullKey(scopeByTracingTag, key, isProperty);
		
		if(isBatch){			
			getProcessVariableDAOFacade().insertValueBatch(pvd, getInstanceId(), scopeByTracingTag, key, isProperty, fullKey, val, dataType, index);
		}else{
			if(!append){
				getProcessVariableDAOFacade().updateValue(getInstanceId(), scopeByTracingTag, key, isProperty, fullKey, val, dataType); 
			}else{
				getProcessVariableDAOFacade().insertValue(getInstanceId(), scopeByTracingTag, key, isProperty, fullKey, val, dataType, index); 
			}
		}
		
	}


	
	private void beginCaching(String scopeByTracingTag, String key, boolean isProperty) throws Exception{
		if(!processVariablesAreCached && !variables.containsKey(createFullKey(scopeByTracingTag, key, isProperty))){
			try{
				if(!isNew){
					
					ProcessVariable[] vars = getProcessDefinition().getProcessVariables();
					if(vars!=null)
					for(int i=0; i<vars.length; i++){
						ProcessVariable var = vars[i];
						if(var.getDefaultValue() instanceof ComplexType){
							//modified by yookjy 2011.03.24
							ComplexType v = (ComplexType)var.getDefaultValue();
							if (v.getTypeId() != null && !"".equals(v.getTypeId())) {
								v.getTypeClass(getProcessTransactionContext().getProcessManager());
							}
//							((ComplexType)var.getDefaultValue()).getTypeClass(getProcessTransactionContext().getProcessManager());
						}
					}
					
					DefaultProcessInstance shotProcessInstance = getProcessVariableDAOFacade().getAllVariablesAsDefaultProcessInstance(getInstanceId());
					shotProcessInstance.variables.putAll(variables);
					variables = shotProcessInstance.variables;

					if(GlobalContext.logLevelIsDebug){
						addDebugInfo("");

						if(vars!=null)
						for(int i=0; i<vars.length; i++){
							ProcessVariable var = vars[i];
							Serializable data = super.get("", var.getName());
							String dataInStr = (String)GlobalContext.serialize(data, String.class);
							
							addDebugInfo("Initial Variable '" + var.getName() + "'", dataInStr);
						}
						
						addDebugInfo("");

						Role[] roles = getProcessDefinition().getRoles();
						if(roles!=null)
							for(int i=0; i<roles.length; i++){
								Role role = roles[i];
								RoleMapping rm = getRoleMapping(role.getName());
								String dataInStr = (String)GlobalContext.serialize(rm, String.class);
								
								addDebugInfo("Initial RoleMapping '" + role.getName() + "'", dataInStr);
							}
							
						
						
					}else
						addDebugInfo("Initial Variable Values", variables);

				}
				
				
				processVariablesAreCached = true;
			}catch(Exception e){
				throw new UEngineException("Error when caching process instance data: "+e.getMessage(), e);
			}
		}
	}

	public Serializable get(String scopeByTracingTag, String key) throws Exception{
		return getImpl(scopeByTracingTag, key, false);
	}

	public Serializable getProperty(String scopeByTracingTag, String key) throws Exception {
		//If the activity where the scopeByTracingTag is under an execution scope, the property space should be devided.
		ExecutionScopeContext esc = getExecutionScopeContext();
		if(esc!=null){
			if(scopeByTracingTag.indexOf('.')==-1){			
		
				Activity activity = getProcessDefinition().getActivity(scopeByTracingTag);
			
				if(activity == esc.getRootActivityInTheScope() || esc.getRootActivityInTheScope().isAncestorOf(activity)){
					scopeByTracingTag = scopeByTracingTag + "." + esc.getExecutionScope();
				}
			}
		}

		return getImpl(scopeByTracingTag, key, true);
	}
	

   public Serializable getImpl(String scopeByTracingTag, String key, boolean isProperty)
    throws Exception
	{
		   try{
	   
		   String firstPart = key;
		   if(key.indexOf('.') > 0){
				String [] wholePartPath = key.replace('.','@').split("@");
				firstPart = wholePartPath [0];
		   }
		   
			if(getProcessDefinition()!=null){
				ProcessVariable pv = getProcessDefinition().getProcessVariable(firstPart);
				if(pv!=null && pv.shouldAccessValueInSpecializedWay()){
					return pv.get(this, scopeByTracingTag);
				}
			}
	
			Serializable sourceValue = null;
			
			if(isCaching()){
				beginCaching(scopeByTracingTag, firstPart, isProperty);
	
	//System.out.println("The value in the cache is used================================");
				if(isProperty)
					return super.getProperty(scopeByTracingTag, firstPart);
				else
					sourceValue = super.get(scopeByTracingTag, firstPart);
			}
		   
			if(sourceValue==null)
				sourceValue = getProcessVariableDAOFacade().get(getInstanceId(), scopeByTracingTag, firstPart); 
			
			if(sourceValue == null){
				ProcessDefinition pd = getProcessDefinition();
				if(pd != null){
					ProcessVariable pv = pd.getProcessVariable(firstPart);
					if(pv!=null)
						sourceValue = (Serializable)pv.getDefaultValue();
				}
			}
			
	
			
			return resolveParts(sourceValue, key);
	   }catch(Exception e){
		   throw new UEngineException("Error to get variable [" + key + "]: " + e.getMessage(), e);
	   }
	}
   

   public ProcessVariableValue getMultiple(String scopeByTracingTag, String key)
   throws Exception
	{
	   if(key.indexOf(".") > -1){
		   Serializable orgValue = get(scopeByTracingTag, key);
		   if(orgValue instanceof ProcessVariableValue){
			   return (ProcessVariableValue)orgValue;
		   }else{
			   ProcessVariableValue pvv = new ProcessVariableValue();
			   
			   if(orgValue instanceof BeanPropertyResolver){
				   key = key.substring(key.indexOf("."));
				   
				   ((BeanPropertyResolver)orgValue).getBeanProperty(key);
			   }else if (orgValue instanceof ProcessVariablePartResolver){
					orgValue = resolveParts(orgValue, key);
			   }else{
				   key = key.substring(key.indexOf(".")+1);
				   orgValue = (Serializable) UEngineUtil.beanPropertyValueObject(orgValue, key, false, null);
			   }
			   
			   pvv.setValue(orgValue);
			   pvv.beforeFirst();
			   
			   return pvv;
		   }
	   }
	   
		if(isCaching()){
			if(!processVariablesAreCached && !variables.containsKey(createFullKey(scopeByTracingTag, key, false))){
				try{
					if(!isNew){
						variables.putAll(getAll()); // there may be some extra values 
					}
					
					processVariablesAreCached = true;
				}catch(Exception e){
					throw new UEngineException("Error when caching process instance data: "+e.getMessage(), e);
				}
			}

			return super.getMultiple(scopeByTracingTag, key);
		}
		
		return getProcessVariableDAOFacade().getAsProcessVariableValue(getInstanceId(), scopeByTracingTag, key);	
	}

	public Map getAll(String scope) throws Exception {
		return getProcessVariableDAOFacade().getAll(getInstanceId()); 
	}
	
	public String getStatus(String scope) throws Exception{		
		return getProcessDefinition().getActivity(scope).getStatus(this);		
	}
	
	protected void setStatus(String scope, String status) throws Exception{
		super.setStatus(scope, status);
		
		ProcessInstanceDAO piDAO = null;
		//forward status of pi to processinstance
		if(scope.equals("")){
			//remove if this instance doesn't need to be archived				
			if(status.equals(Activity.STATUS_COMPLETED) && !getProcessDefinition().isArchive())
				remove();	
			else{
				piDAO = getProcessInstanceDAO();
				piDAO.setStatus(status);
			}
			
			//when the instance is completed or stopped.
			if(status.equals(Activity.STATUS_COMPLETED) || status.equals(Activity.STATUS_STOPPED)){
				if(piDAO==null){					
					piDAO = getProcessInstanceDAO();
				}
					
				piDAO.setFinishedDate(GlobalContext.getNow(getProcessTransactionContext()).getTime());
				
				archive();
			}
		}

	}
	
	public void archive() throws Exception{
		if("false".equals(GlobalContext.getPropertyString("server.archive.completed.processes", "false"))) return;
		
		FileProcessArchive processArchive = new FileProcessArchive();{					
			Map pvMap = getAll();
			processArchive.setInstanceId(getInstanceId());
			processArchive.variables = pvMap;
		
			ProcessDefinition definition = getProcessDefinition();
			
			if(definition==null) throw new UEngineException("There's no definition found for the instance ("+ getInstanceId() +")"); 
			
			Role[] roles = definition.getRoles();
			for(int i=0; i<roles.length; i++){
				RoleMapping rm = getRoleMapping(roles[i].getName());
				if(rm!=null)
					processArchive.putRoleMapping(rm);			
			}
			
			processArchive.setProcessDefinition(getProcessDefinition());			
		}
		
		final String archivePath = processArchive.save(getProcessTransactionContext());	
		//should be invoked after applyChanges() of this ProcessInstance
		getProcessTransactionContext().addTransactionListener(new TransactionListener(){

			public void beforeCommit(TransactionContext tx) throws Exception{
				ProcessInstanceDAOType pidt = ProcessInstanceDAOType.getInstance(getProcessTransactionContext());
				pidt.archiveInstance(new Long(getInstanceId()), archivePath);
			}

			public void beforeRollback(TransactionContext tx) throws Exception{
				// TODO Auto-generated method stub
				
			}

			public void afterCommit(TransactionContext tx) throws Exception {
				// TODO Auto-generated method stub
				
			}

			public void afterRollback(TransactionContext tx) throws Exception {
				// TODO Auto-generated method stub
				
			}
			
		});
	
	}

	public void setProcessDefinition(ProcessDefinition value) {
		if(value!=processDefinition){
			value.clearWithOutLastRevisionInfo();
			String generatedPath = UEngineUtil.getCalendarDir();
			generatedPath = generatedPath + "/" + getInstanceId() + ".upd";
			
			try {
				ProcessDefinitionFactory.getInstance(getProcessTransactionContext()).storeProcessDefinition(generatedPath, value);
				getProcessInstanceDAO().setDefPath(generatedPath);
				getProcessInstanceDAO().setIsAdhoc(true);
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}
		
		super.setProcessDefinition(value);
	}
	
	public ProcessDefinition getProcessDefinition() throws Exception {
		//TODO if pd can't be cached since it is an ad-hoc, this may cause some decrease of performance
		if(processDefinition==null){
			if(getInstanceId()==null) return null;
//			try{
				ProcessDefinition procDef;
				
				if(isAdhocProcess()){
					procDef = ProcessDefinitionFactory.getInstance(getProcessTransactionContext()).getDefinitionWithPath(getProcessInstanceDAO().getDefPath());
				}else
					procDef = ProcessDefinitionFactory.getInstance(getProcessTransactionContext()).getDefinition(getProcessInstanceDAO().getDefVerId().toString());
				
/*				if(procDef.isAdhoc()){
					//TODO reload if adhoc.. bad performance?
					procDef = ProcessDefinitionFactory.getDefinition(piRemote.getDefinition(), true);
				}*/
				/*	return procDef; //probihit caching
				}else{
					setProcessDefinition(procDef);
				}*/
				
				//if(!procDef.isAdhoc()) //TODO: check-me-if-error: maintain same definition during same transation
				processDefinition = procDef;
				//else
				//	return procDef;
									
//			}catch(javax.ejb.ObjectNotFoundException onfe){
//System.out.println("EJBActivityInstance::getProcessDefinition(): can't find process instance!");
//				
//			}catch(Exception e){
//				e.printStackTrace();
//				setProcessDefinition(null);
//			}
		}
		
		return processDefinition;
	}		
	
	

	public void remove() throws Exception{
/*		ProcessInstanceDAO piDAO = getProcessInstanceDAO();
		piDAO.setIsDeleted(true);
		if(piDAO.getFinishedDate()==null){
			piDAO.setFinishedDate(GlobalContext.getNow(getProcessTransactionContext()).getTime());
		}
*/
		ProcessInstanceDAOType.getInstance(getProcessTransactionContext()).removeProcessInstance(new Long(getRootProcessInstanceId()));
	}
	
	public ProcessInstance createSnapshot() throws Exception {
		final DefaultProcessInstance shotProcessInstance = getProcessVariableDAOFacade().getAllVariablesAsDefaultProcessInstance(getInstanceId());
		shotProcessInstance.setInstanceId(getInstanceId());
		
		Role[] roles = getProcessDefinition().getRoles();
		for(int i=0; i<roles.length; i++){
			RoleMapping rm = getRoleMapping(roles[i].getName());
			if(rm!=null){
				rm.setName(roles[i].getName());
				shotProcessInstance.putRoleMapping(rm);	
			}
		}
		
		shotProcessInstance.setProcessTransactionContext(getProcessTransactionContext());
		
		return shotProcessInstance;
	}


////////////// private methods ////////////////////
	
	public String getInfo() throws Exception{
		
		return getProcessInstanceDAO().getInfo();
	}

	public void setInfo(String info) throws Exception{
		getProcessInstanceDAO().setInfo(info);
	}


	public String getName() {
		if(name==null){
			try{
				name = getProcessInstanceDAO().getName();			
			}catch(Exception e){
			}
		}
		
		return name;
	}
	
	public boolean isSubProcess() throws Exception {
		return getProcessInstanceDAO().getIsSubProcess();			
	}

	Boolean isSubProcess = null;
	public boolean isAdhocProcess() throws Exception {
		if(isSubProcess==null){
			try{
				ProcessInstanceDAO piDAO = getProcessInstanceDAO();
				isSubProcess = Boolean.valueOf(piDAO.getIsAdhoc());										
			}catch(Exception e){
				isSubProcess = Boolean.valueOf(false);
			}
		}
		
		return isSubProcess.booleanValue();
	}

	public Vector getRunningOrCompletedActivityTracingTags() throws Exception{
		Map statusMap = getAll();
		Vector finding = new Vector();
		
		Set keys = statusMap.keySet();
		for(Iterator iter = keys.iterator(); iter.hasNext();){
			String key = (String)iter.next();
			if(key.endsWith("_status")){
				String val = (String)statusMap.get(key);
				if(!val.equals(Activity.STATUS_READY)){
					//TODO: It is required to add a field for scope (tracingtag) into BPM_PROCVAR
					String tracingTag = key.substring(0, key.length() - "_status".length() - 1);					
					finding.add(tracingTag);
				}
			}
		}
	
		return finding;
	}


	public void setProcessTransactionContext(ProcessTransactionContext ptc) {
		this.ptc = ptc;
	}

	public String getMainProcessInstanceId() {
		try{
			String mainProcessInstanceIdInStr = (getProcessInstanceDAO().getMainInstId() != null ? getProcessInstanceDAO().getMainInstId().toString() : null);
			return (mainProcessInstanceIdInStr.equals("-1") ? null : mainProcessInstanceIdInStr);
			
		}catch(Exception e){
			return null;
		}
	}

	public String getRootProcessInstanceId() {
		try{
			return (getProcessInstanceDAO().getRootInstId() != null ? getProcessInstanceDAO().getRootInstId().toString() : null);
		}catch(Exception e){
			return null;
		}
	}

	public boolean isDontReturn() {
		try{
			return (getProcessInstanceDAO().getDontReturn());
		}catch(Exception e){
			return false;
		}
	}

	public ProcessInstance getSubProcessInstance(String absoluteTracingTag) throws Exception {
		ProcessDefinition definition = getProcessDefinition();
		ProcessInstance findingProcessInstance = this;
		
		if(absoluteTracingTag.indexOf("@")>0){
			String[] scopesByTracingTag = absoluteTracingTag.split("@");
			for(int i=0; i<scopesByTracingTag.length-1; i++){
				String scope = scopesByTracingTag[i];
				SubProcessActivity spAct = (SubProcessActivity)definition.getActivity(scope);
				List spInstanceIds = spAct.getSubprocessIds(findingProcessInstance);
				if(spInstanceIds.size() == 0){
					throw new UEngineException("Activity in the subprocess ["+ absoluteTracingTag +"] cannot be found.");
				}
				
				String spInstanceId = (String)spInstanceIds.get(0);
				
				
				findingProcessInstance = ProcessInstance.create().getInstance(spInstanceId);
				definition = findingProcessInstance.getProcessDefinition();
			}
			
			absoluteTracingTag = scopesByTracingTag[scopesByTracingTag.length-1];
		}
		
		return findingProcessInstance;
	}
	
	public Calendar calculateDueDate(Calendar startDate, int duration) {
		startDate.setTimeInMillis(startDate.getTimeInMillis() + (long)duration * 86400000L);
		return startDate;
	}
	
	public void beforeCommit(TransactionContext tx) throws Exception{
		applyChanges();
	}
	
	public void beforeRollback(TransactionContext tx) throws Exception{
	}
	
	public ProcessInstance getMainProcessInstance() throws Exception {
		if(getMainProcessInstanceId() == null) return null;
		
		return getProcessTransactionContext().getProcessManager().getProcessInstance(getMainProcessInstanceId());
	}
	
	public ProcessInstance getRootProcessInstance() throws Exception {
		if(getRootProcessInstanceId() == null) return null;
		
		return getProcessTransactionContext().getProcessManager().getProcessInstance(getRootProcessInstanceId());
	}

	
	public RoleMapping getRoleMapping(String roleName) throws Exception{
		if(isCaching() && cachedRoleMappings!=null && cachedRoleMappings.containsKey(roleName)){
			return (RoleMapping)((RoleMapping)cachedRoleMappings.get(roleName)).clone();
		}
		
		if(isCaching() && modifiedRoleMappings!=null && modifiedRoleMappings.containsKey(roleName)) return null;
		
		RoleMappingDAOType rmDAOFacade = RoleMappingDAOType.getInstance(ptc);
		RoleMappingDAO roleMappingDAO = rmDAOFacade.findByInstanceIdAndRoleName(new Long(getInstanceId()), roleName);
		
		if ( roleMappingDAO.size() < 1 ) return null;
		
		RoleMapping making;
		
		if(roleMappingDAO.size()==1){
			making = rmDAOFacade.createRoleMapping(roleMappingDAO); //(RoleMapping)GlobalContext.deserialize(roleMappingDAO.getValue(), RoleMapping.class);
		}else{
			making = RoleMapping.create();
			making.setName(roleName);
			
			int i = roleMappingDAO.size();
			do{
				//RoleMapping mapping = (RoleMapping)GlobalContext.deserialize(roleMappingDAO.getValue(), RoleMapping.class);
				RoleMapping mapping = rmDAOFacade.createRoleMapping(roleMappingDAO);
				making.replaceCurrentRoleMapping(mapping);	
				
				if(making.getCursor() == 0){
					making.setDispatchingOption(mapping.getDispatchingOption());
					making.setDispatchingParameters(mapping.getDispatchingParameters());
				}
				
				if ((--i)>0) making.moveToAdd();	
			}while(roleMappingDAO.next());
		}
		
		making.beforeFirst();
		
		if(isCaching()){
			if(cachedRoleMappings == null)
				cachedRoleMappings = new HashMap();
		
			cachedRoleMappings.put(roleName, making);
		}
		
		return making;
	}
	
	public void putRoleMapping(String roleName, RoleMapping roleMapping) throws Exception{		
		putRoleMappingImpl(roleName, roleMapping, false, null);
	}
		
	public void putRoleMappingImpl(String roleName, RoleMapping roleMapping, boolean isBatch, RoleMappingDAO roleMappingDAO) throws Exception{		
		addDebugInfo("   --- [Set Role] --------------------\n    * name : "+roleName+"\n    * value : " + (GlobalContext.logLevelIsDebug ? GlobalContext.serialize(roleMapping, String.class) : roleMapping.getEndpoint() +"'"));
		addDebugInfo("   -----------------------------------");
		
		if(isCaching()){
			if(modifiedRoleMappings == null)
				modifiedRoleMappings = new HashMap();

			if(cachedRoleMappings == null)
				cachedRoleMappings = new HashMap();
			

			modifiedRoleMappings.put(roleName, roleName);

			if(roleMapping==null){
				cachedRoleMappings.remove(roleName);
			}else{
				roleMapping.setName(roleName);
				cachedRoleMappings.put(roleName, roleMapping);
			}
			
			getProcessDefinition().firePropertyChangeEventToActivityFilters(this, "roleMapping", roleMapping);

			return;
		}		

		RoleMappingDAOType rmDAOType = RoleMappingDAOType.getInstance(ptc);
		
		int count = 0;

		if(!isBatch){
			rmDAOType.removeRoleMapping(getInstanceId(), roleName);
			roleMappingDAO = rmDAOType.createDAOForInsertRoleMappingBatch();
		}

		if(roleMapping==null){
			return; //to clear the rolemapping, simply skip inserting to database
		}

		roleMapping.beforeFirst();

		do{
			RoleMapping currMapping = roleMapping.getCurrentRoleMapping();
			if(currMapping==null)
				continue;
			
			currMapping.makeSingle();
			
			if(roleMapping.getCurrentRoleMapping().resourceName == null){
				roleMapping.fill(this);
			}
			
			boolean verifyOK = true;
			switch ( currMapping.getAssignType() ) {
			case Role.ASSIGNTYPE_USER:
				if(currMapping.getEndpoint() == null ){
					if(currMapping.size()-1 != currMapping.getCursor())
						throw new UEngineException("One of rolemapping value has null endpoint.");
					
					verifyOK = false;					
				}
				break;
			}
			
			if(verifyOK){
				rmDAOType.insertRoleMappingBatch(roleMappingDAO, new Long(getInstanceId()), new Long(getRootProcessInstanceId()), roleName, currMapping);
				count++;
			}
			
		}while(roleMapping.next());
		roleMapping.beforeFirst();
		
		if(count>0 && !isBatch)
			roleMappingDAO.updateBatch();


	}

	public boolean isParticipant(RoleMapping rm) throws Exception{
		RoleMappingDAOType rmDAOType = RoleMappingDAOType.getInstance(ptc);
		return rmDAOType.findByRootInstanceIdAndEndpoint(new Long(getRootProcessInstanceId()), rm.getEndpoint()).size() > 0;
	}
	
	public void setDueDate(Calendar date) throws Exception {
		super.setDueDate(date);
		getProcessInstanceDAO().setDueDate(date.getTime());
	}
	
	public void setDueDate(Date date) throws Exception {
		Calendar cal = new GregorianCalendar();
		cal.setTime(date);
		this.setDueDate(cal);
	}
	
	public void setName(String value) {
		// TODO Auto-generated method stub
		super.setName(value);
		try {
			getProcessInstanceDAO().setName(value);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	public void setDefinitionVersionId(String verId) throws Exception {
		getProcessInstanceDAO().setDefVerId(verId);
	}
	
	public boolean isNew() {
		return isNew;
	}
	
	public void stop() throws Exception {
		stop(Activity.STATUS_STOPPED);
	}

	public void stop(String status) throws Exception {
		super.stop(status);
		getProcessInstanceDAO().setStatus(status);
	}
	
	public WorkList getWorkList() {
		return (new WorkListServiceLocator()).getWorkList();
	}
	public void afterCommit(TransactionContext tx) throws Exception {
		
		

	}
	public void afterRollback(TransactionContext tx) throws Exception {
		// TODO Auto-generated method stub
		
	}

	public String getMainExecutionScope() {
		try {
			return getProcessInstanceDAO().getMainExecScope();
		} catch (Exception e) {
			return null;
		}
	}


}