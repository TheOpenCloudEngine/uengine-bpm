package org.uengine.kernel;

import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.beanutils.PropertyUtils;
import org.uengine.processmanager.ProcessTransactionContext;
import org.uengine.processmanager.SimulatorTransactionContext;
import org.uengine.util.ActivityForLoop;
import org.uengine.util.UEngineUtil;
import org.uengine.webservices.worklist.SimulatorWorkList;
import org.uengine.webservices.worklist.WorkList;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.*;

/**
 * @author Jinyoung Jang
 */

public class DefaultProcessInstance extends AbstractProcessInstance {
    
	private static final long serialVersionUID = org.uengine.kernel.GlobalContext.SERIALIZATION_UID;
	public final static String ROOT_PROCESS = "_rootProcess";
	public final static String RETURNING_PROCESS = "_returningProcess";
	public final static String RETURNING_TRACINGTAG = "_returningTracingTag";
	public final static String RETURNING_EXECSCOPE = "_returningExecScope";
	public final static String DONT_RETURN = "_dontReturn";
	public final static String SUFFIX_PROPERTY = ":prop";
	public final static String SIMULATIONPROCESS = "isSimulationTime";

	//static in-memory repository
	//static Hashtable repository = new Hashtable();
	//static Hashtable definition = new Hashtable();
	//static Hashtable statusRepository = new Hashtable();
	//static Hashtable processDefinitions = new Hashtable();
	static int instanceCount=0;
	//	
	
	Map variables;
		public Map getVariables() {
			return variables;
		}
		public void setVariables(Map variables) {
			this.variables = variables;
		}
	
	boolean bDone = false ;
	boolean bRunning = false ;
		
	private String instanceId="";
		public String getInstanceId() {
			return instanceId;
		}
		public void setInstanceId(String value) {
			instanceId = value;
		}

	String name;
		public String getName() {
			return name;
		}
		public void setName(String value) {
			this.name = value;
		}
		
	boolean isSimulation;
		public boolean isSimulation() {
			return isSimulation;
		}
		public void setSimulation(boolean isSimulation) {
			this.isSimulation = isSimulation;
		}

    boolean subProcess;
        @Override
        public boolean isSubProcess() throws Exception {
            return this.subProcess;
        }

    boolean adhoc;
        @Override
        public boolean isAdhoc() throws Exception {
            return this.adhoc;
        }

	//TODO: make it transient to prevent a huge object
	transient ProcessDefinition processDefinition;
	
	public ProcessDefinition getProcessDefinition()  throws Exception {
		return getProcessDefinition(true);
	}
		
	public ProcessDefinition getProcessDefinition(boolean chcahed)  throws Exception {
			return processDefinition;
	}
	
	public void setProcessDefinition(ProcessDefinition value) {
		/*			if(value!=null)
						processDefinitions.put(getInstanceId(), value);*/
		processDefinition = value;
	}

	//this will accumulate errors occurred during activityinstance being used.
	transient ValidationContext validationContext = new ValidationContext();
		public ValidationContext getValidationContext() {
			return validationContext;
		}

	public RoleMapping getRoleMapping(String roleName) throws Exception{
		try{
			Object val = get("", "_roleMapping_of_" + roleName);
//System.out.println("EJBActivityInstance::getRoleMapping.class = " + val.getClass());
			if(val != null && val instanceof RoleMapping)
				return (RoleMapping)val;
			else
				return null;
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}
	
	public void putRoleMapping(RoleMapping roleMap) throws Exception{
		putRoleMapping(roleMap.getName(), roleMap);
	}

	public void putRoleMapping(String name, String endpoint) throws Exception{
		if(!UEngineUtil.isNotEmpty(endpoint)){
			putRoleMapping(name, (RoleMapping)null);
			return;
		}
		
		RoleMapping rp = RoleMapping.create();
		rp.setName(name);
		rp.setEndpoint(endpoint);
		rp.fill(this);
			
		putRoleMapping(rp);
	}

	public void putRoleMapping(String name, RoleMapping roleMap) throws Exception{
		//if(roleMap!=null)
		setSourceValue("", "_roleMapping_of_" + name, roleMap);
	}

	public UEngineException getFault(String scope) throws Exception{
		try{
			UEngineExceptionContext ctx = (UEngineExceptionContext)getProperty(scope, "_fault");
			return ctx.createException();
		}catch(Exception e){
			//e.printStackTrace();
		}
		
		return null;
	}
			
	protected void setFault(String scope, UEngineException value) throws Exception{
		try{
			setStatus(scope, Activity.STATUS_FAULT);			

			if(value==null) return;
			UEngineExceptionContext ctx = value.createContext();
			setProperty(scope, "_fault", (Serializable)ctx);
		}catch(Exception e){
			e.printStackTrace();
		}	
	}	
	
	//instead of direct invoking these methods, we recommend use 'fireComplete' or 'fireFault' methods
	public void setStatus(String scope, String status) throws Exception{
		setProperty(scope, Activity.PVKEY_STATUS, status);

//		statusRepository.put(getInstanceId()+":"+scope, status);
	}
	
	public String getStatus(String scope) throws Exception{
		Serializable status = getProperty(scope, Activity.PVKEY_STATUS);
		if(status==null)
			return Activity.STATUS_READY;
		else 
			return (String)status;
	}
	
	
//////////////////

	public DefaultProcessInstance(ProcessDefinition procDefinition, String instanceId, Map options) throws Exception{
		
		if ( getProcessTransactionContext() == null){
			if(options != null && options.get("ptc") != null ) {
				setProcessTransactionContext( (ProcessTransactionContext)options.get("ptc") );
			}else
				if(!GlobalContext.isDesignTime())
					setProcessTransactionContext(new SimulatorTransactionContext());
		}

		if(instanceId==null){
			instanceId = "Volatile_"+(instanceCount++);
		}
		
		setInstanceId(instanceId);
		
		if(procDefinition!=null && instanceId!=null){
			setName(instanceId);
			setProcessDefinition(procDefinition);
		}
		
		variables = new HashMap();
		

		if(getProcessTransactionContext()!=null)
			getProcessTransactionContext().registerProcessInstance(this);
	
		subProcess = 
			(	options!=null 
				&& options.containsKey("isSubProcess") 
				&& options.get("isSubProcess").equals("yes")
			);
		
		if(subProcess) {
			mainProcessInstanceId = (String)options.get(DefaultProcessInstance.RETURNING_PROCESS);
			mainExecutionScope = (String)options.get(DefaultProcessInstance.RETURNING_EXECSCOPE);
			mainActivityTracingTag = ((String)options.get(DefaultProcessInstance.RETURNING_TRACINGTAG));
//			processInstanceDAO.setDontReturn(((Boolean)options.get(DefaultProcessInstance.DONT_RETURN)).booleanValue());
//			processInstanceDAO.setIsEventHandler(options.containsKey("isEventHandler"));
		}

	}
	
	public DefaultProcessInstance () throws Exception{
		this(null, null, null);		
	}
	
	public void set(String scopeByTracingTag, String key, Serializable val) throws Exception{

		Serializable existingValue = getSourceValue(scopeByTracingTag, key);
		if(existingValue instanceof VariablePointer){
			((VariablePointer) existingValue).setValue(this, val);

			return;
		}

		if(val instanceof ProcessVariableValue){
			ProcessVariableValue pvv = (ProcessVariableValue)val;
			pvv.setName(key);
			set(scopeByTracingTag, pvv);
			return;
		}

		setSourceValue(scopeByTracingTag, key, val);
	}
	
	public void setSourceValue(String scopeByTracingTag, String key, Serializable val) throws Exception{
		
//		StringBuffer sb = new StringBuffer();
//		sb		
//		.append("--- ActivityInstance::set --- ")
//		.append("scope: " + scopeByTracingTag)
//		.append("key: " + key)
//		.append("val: " + val)
//		.append("instance id: " + getInstanceId());
//		jh
//		System.out.println(sb);
		val = formatValue(key, val);

		String fullKeyName = createFullKey(scopeByTracingTag, key, false);
		if(val==null)
			variables.remove(fullKeyName);
		else
			variables.put(fullKeyName, val);
		//repository.put(getInstanceId()+":"+scopeByTracingTag+":"+key, val);
	}

	private Serializable formatValue(String key, Serializable val) throws Exception {

		if(val==null) return null;

		ProcessDefinition definition = getProcessDefinition();
		ProcessVariable variable = definition.getProcessVariable(key);

		if(variable!=null && variable.getTypeClassName()!=null){
			if(variable.getTypeClassName().startsWith("java.lang.")){
				Class type = Thread.currentThread().getContextClassLoader().loadClass(variable.getTypeClassName());

				if(!val.getClass().isAssignableFrom(type)) { //need to convert

					try {
						val = (Serializable) ConvertUtils.convert(val, type);
					}catch (Exception e){

					}
				}
			}
		}

		return val;
	}

	public Serializable get(String scopeByTracingTag, String key) throws Exception{
	   String firstPart = key;

		boolean resolvePartNeeded = false;
	   if(key.indexOf('.') > 0){
			String [] wholePartPath = key.replace('.','@').split("@");
			firstPart = wholePartPath [0];

		   resolvePartNeeded = true;

	   }

		Serializable sourceValue = getSourceValue(scopeByTracingTag, firstPart);
		
		if(sourceValue instanceof IndexedProcessVariableMap){
			ProcessVariableValue pvv = getMultiple(scopeByTracingTag, key);
			sourceValue = pvv.getValue();
		}else if(sourceValue == null){
			ProcessDefinition pd = getProcessDefinition();
			if(pd != null){
				ProcessVariable pv = pd.getProcessVariable(key);
				if(pv!=null)
					return (Serializable)pv.getDefaultValue();
			}
		}

		if(resolvePartNeeded)
			sourceValue = resolveParts(sourceValue, key);


		if(sourceValue instanceof VariablePointer){
			return ((VariablePointer) sourceValue).getValue(this);
		}


		return sourceValue;
	}
	
	protected Serializable resolveParts(Serializable sourceValue, String key) throws Exception{

		try {
			int indexOfDot = key.indexOf(".");
			if (indexOfDot > 0){
				String beanPath = key.substring(indexOfDot + 1);

				if(sourceValue instanceof BeanPropertyResolver) {

					return (Serializable) ((BeanPropertyResolver) sourceValue).getBeanProperty(beanPath);
				}else{
					return (Serializable) PropertyUtils.getProperty(sourceValue, beanPath);
				}
			}
		}catch(ClassCastException e){
			throw new Exception("Process variable's property value must be serializable", e);
		}

		//resolve parts
		if(getProcessDefinition()!=null && sourceValue!=null){
			ProcessVariable pv = getProcessDefinition().getProcessVariable(key);
			if(pv!=null){
				if(key.indexOf('.') > -1 && ProcessVariablePartResolver.class.isAssignableFrom(pv.getType()) ){
					String [] wholePartPath = key.replace('.','@').split("@");
					String [] partPath = new String[wholePartPath.length-1];
					for(int i=0; i<partPath.length; i++){
						partPath[i] = wholePartPath[i+1];
					}
					
					ProcessVariablePartResolver variableDelegator = (ProcessVariablePartResolver)sourceValue;
					return (Serializable) variableDelegator.getPart(this, partPath, sourceValue);
				}
			}
		}


		
		return sourceValue;
	}

	public Serializable getSourceValue(String scopeByTracingTag, String key) throws Exception{
		return getSourceValue(scopeByTracingTag, key, false);
	}

	public Serializable getSourceValue(String scopeByTracingTag, String key, boolean isProperty) throws Exception{

		ExecutionScopeContext originalScope = getExecutionScopeContext();
		ExecutionScopeContext scope;

		Serializable value = null;

		do {
			scope = getExecutionScopeContext();

			String fullKey = createFullKey(scopeByTracingTag, key, isProperty);

			if(variables.containsKey(fullKey)){
				value = (Serializable) variables.get(fullKey);

				break;
			}else {
				if(scope!=null)
					setExecutionScope(getParentExecutionScopeOf(scope.getExecutionScope()));
			}

		}while(scope != null); //if value is not found in the scope, finding values to the parent scopes as well.

		setExecutionScopeContext(originalScope);

		return value;
	}
	
	public String getInXML(String scopeByTracingTag, String key) throws Exception{
		return getSourceValue(scopeByTracingTag, key).toString();//temporal implementation
	}
	
	public Map getAll(String scope) throws Exception{				
		return variables;
	}
	public Map getAll() throws Exception{				
		return getAll("");
	}
	
	public void addMessageListener(String message, String scope) throws Exception{		
		String keyStr = "MESSAGE_" + message;
		
		/*
		Vector messageSubscriptions = (Vector)get("", keyStr);
		if(messageSubscriptions==null)
			messageSubscriptions = new Vector();			
	
		if(!messageSubscriptions.contains(scope))
			messageSubscriptions.add(scope);*/
		
		String messageSubscriptions = (String)getProperty("", keyStr);
		String sep;
		if(!UEngineUtil.isNotEmpty(messageSubscriptions)){
			messageSubscriptions = "";
			sep="";			
		}else{
			sep = ",";
		}
	
		if(messageSubscriptions.indexOf(scope) == -1)
			messageSubscriptions += (sep+scope);
		
		setProperty("", keyStr, messageSubscriptions);
	}
	
	public Vector getMessageListeners(String message) throws Exception{
		String keyStr = "MESSAGE_" + message;

		Vector messageSubscriptions = null;

		String messageSubscriptionsString = (String)getProperty("", keyStr);
		if(messageSubscriptionsString!=null){
			messageSubscriptions = new Vector();
			
			String[] messageSubscriptionsStrings = messageSubscriptionsString.split(",");
			if(messageSubscriptionsStrings.length > 0){
				for(int i=0; i<messageSubscriptionsStrings.length; i++){
					if(org.uengine.util.UEngineUtil.isNotEmpty(messageSubscriptionsStrings[i]))
						messageSubscriptions.add(messageSubscriptionsStrings[i]);
				}
			}
		}
		
		//TODO this is a situation-specific code. Generalize sometime.
		if("event".equals(message) && (messageSubscriptions==null || messageSubscriptions.size()==0)){
			messageSubscriptions = new Vector();
			final Vector finalSubscribedScopes = messageSubscriptions;
			ActivityForLoop findingLoopForRunningScopeActivity = new ActivityForLoop(){

				public void logic(Activity activity) {
					if(activity instanceof ScopeActivity){
						try {
							if(isRunning(activity.getTracingTag()) && ((ScopeActivity)activity).getEventHandlers()!=null)
								finalSubscribedScopes.add(activity.getTracingTag());
						} catch (Exception e) {
						}
					}
				}
				
			};
			
			findingLoopForRunningScopeActivity.run(getProcessDefinition());
		}
		
		return messageSubscriptions;
	}

	public void removeMessageListener(String message, String scope) throws Exception{
/*		String keyStr = "MESSAGE_" + message;
		
		Vector messageSubscriptions = (Vector)get("", keyStr);
		if(messageSubscriptions==null) return;
		messageSubscriptions.remove(scope);
		
		set("", keyStr, messageSubscriptions);*/
		
		if(!UEngineUtil.isNotEmpty(scope)) return;
		
		String keyStr = "MESSAGE_" + message;
		
		String messageSubscriptionsString = (String)getProperty("", keyStr);
		if(messageSubscriptionsString==null)
			return;
			
		StringBuffer sb = new StringBuffer(messageSubscriptionsString);
		
		int start = sb.indexOf(scope);
		int last = start + scope.length();
		if(start>-1){
			
			if(start>0){
				start--;
			}else{				
				last++;
			}
			
			sb.delete(start, last);
		}

		//TODO: should be null?
		/*if(sb.length()==0)
			set("", keyStr, null);
		else*/
			setProperty("", keyStr, sb.toString());
	}
	
	public String[] getInstanceIds() throws Exception{
/*		ProcessInstance inst = new DefaultProcessInstance();
		
		Iterator instancesIter = processDefinitions.keySet().iterator();
		String instanceIDs [] = new String[processDefinitions.keySet().size()];
		
		int i=0;
		while(instancesIter.hasNext()){
			String instId = (String)instancesIter.next();
			instanceIDs[i++] = instId;
		}*/
		
		return new String[]{};//instanceIDs;
	}

	public String[] getInstanceIds(String definitionName) throws Exception{
		return new String[]{};
	}

	public ProcessInstance getInstance(String instanceId) throws Exception{
		return getInstance(instanceId, null);
	}
	
	public ProcessInstance getInstance(String instanceId, Map options) throws Exception{
		
		if(options.containsKey("ptc")) ptc =(ProcessTransactionContext)options.get("ptc");

		String executionScope = null;
		if(instanceId.indexOf("@") > 0){
			String[] instanceIdAndExecutionScope = instanceId.split("@");
			instanceId = instanceIdAndExecutionScope[0];

			executionScope = instanceIdAndExecutionScope[1];
		}
		
		ProcessInstance instance = ptc.getProcessInstanceInTransaction(instanceId);

		if(executionScope!=null){
			instance.setExecutionScope(executionScope);
		}

		if(instance!=null)
			return instance;
		//else{
			//return new DefaultProcessInstance(null, null, null) ;
		//}

		throw new UEngineException("There's no cached in-memory process where instanceId = " +  instanceId);
	}	

	public void remove() throws Exception {

	}

	public ProcessInstance createSnapshot() throws Exception {
		return this;
	}

	public String getInfo()  throws Exception{
		return (String)getProperty("", "_info");
	}

	public void setInfo(String info)  throws Exception{
		setProperty("", "_info", info);
	}


	public static void main(String args[]) throws Exception{
		DefaultProcessInstance instance = new DefaultProcessInstance();
		instance.addMessageListener("testmessage", "0");
		instance.addMessageListener("testmessage", "1");
		instance.addMessageListener("testmessage", "2");
		
		System.out.println(instance.getMessageListeners("testmessage"));
		
		instance.removeMessageListener("testmessage", "1");
		System.out.println(instance.getMessageListeners("testmessage"));

		instance.removeMessageListener("testmessage", "0");
		System.out.println(instance.getMessageListeners("testmessage"));

		instance.removeMessageListener("testmessage", "2");
		System.out.println(instance.getMessageListeners("testmessage"));
	}

	public void stop() throws Exception {
		stop(Activity.STATUS_STOPPED);
	}
	
	public void stop(String status) throws Exception {
		getProcessDefinition().stop(this,status);
	}

	String mainProcessInstanceId;
		public String getMainProcessInstanceId(){
			return mainProcessInstanceId;
		}
		public void setMainProcessInstanceId(String mainProcessInstanceId) {
			this.mainProcessInstanceId = mainProcessInstanceId;
		}

	String mainActivityTracingTag;
	public String getMainActivityTracingTag(){
		return mainActivityTracingTag;
	}
	
	
	protected void reloadProcessDefinition() throws Exception {};

	String rootProcessInstanceId;
	public String getRootProcessInstanceId(){
		if (this.rootProcessInstanceId == null) return getInstanceId();
		return this.rootProcessInstanceId;
	}
	
	public void add(String tracingTag, String key, Serializable val, int index) throws Exception {
		IndexedProcessVariableMap ipvm=getAsIndexedProcessVariableMap(tracingTag, key);
		ipvm.putProcessVariable(index, val);
		
		setSourceValue(tracingTag, key, ipvm);
	}
	
	public ProcessVariableValue getMultiple(String tracingTag, String key) throws Exception {
		IndexedProcessVariableMap ipvm = getAsIndexedProcessVariableMap(tracingTag, key);
		
		int maxIndex = ipvm.getMaxIndex();

		if(ipvm.size()==0)
			return null;

		ProcessVariableValue pvv = new ProcessVariableValue();

		pvv.setName(key);

		for(int i=0; i<maxIndex+1; i++){
			Serializable value = ipvm.getProcessVariableAt(i);

			if(value instanceof VariablePointer)
				value = ((VariablePointer) value).getValue(this);

			pvv.setValue(value);
			pvv.moveToAdd();
		}

		pvv.beforeFirst();
		
		if(pvv.size()==0 || (pvv.size()==1 && pvv.getValue()==null)){
			try{
				Serializable value = (Serializable)getProcessDefinition().getProcessVariable(key).getDefaultValue();
				pvv.setValue(value);

				return pvv;
			}catch(Exception e){}
		}
		
		return pvv;
	}
	
	private IndexedProcessVariableMap getAsIndexedProcessVariableMap(String tracingTag, String key) throws Exception{
		Serializable existingValue = getSourceValue(tracingTag, key);
		
		IndexedProcessVariableMap ipvm;
		if(existingValue == null){
			ipvm = new IndexedProcessVariableMap();
		}else if(existingValue instanceof IndexedProcessVariableMap){
			ipvm = (IndexedProcessVariableMap)existingValue;
		}else{
			ipvm = new IndexedProcessVariableMap();
			ipvm.putProcessVariable(0, existingValue);
		}
		
		return ipvm;
	}
	
	public boolean isDontReturn() {
		return false;
	}
	
	public ProcessInstance getSubProcessInstance(String absTracingTag) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}
	
	public Calendar calculateDueDate(Calendar startDate, int duration){
		startDate.setTimeInMillis(startDate.getTimeInMillis() + (long)duration * 86400000L);
		
		return startDate;
	}
	
	public void setDueDate(Calendar date) throws Exception {
		setProperty("", HumanActivity.PVKEY_DUEDATE, date);
	}
	
	public void setDueDate(Date date) throws Exception {
		Calendar cal = new GregorianCalendar();
		cal.setTime(date);
		setProperty("", HumanActivity.PVKEY_DUEDATE, cal);
	}
	
	public Calendar getDueDate() throws Exception {
		return (Calendar)getProperty("", HumanActivity.PVKEY_DUEDATE);
	}
	
	public void set(String scopeByTracingTag, ProcessVariableValue pvv) throws Exception {
		if(pvv==null) throw new UEngineException("Not a valid ProcessVariableValue (null).");
		if(pvv.getName()==null) throw new UEngineException("Not a valid ProcessVariableValue (should have name).");

		Serializable existingValue = getSourceValue(scopeByTracingTag, pvv.getName());
		if(existingValue instanceof VariablePointer){
			pvv.beforeFirst();
			((VariablePointer) existingValue).setValue(this, pvv.getValue()); //only one item will be stored.

			return;
		}

		setSourceValue(scopeByTracingTag, pvv.getName(), new IndexedProcessVariableMap());
		pvv.beforeFirst();
		int i=0;
		do{
			add(pvv.getName(), pvv.getValue(), i++);			
		}while(pvv.next());				
	}

	@Override
	public void setAt(String scopeByTracingTag, String key, int index, Serializable val) throws Exception {
		Object sourceValue = getSourceValue(scopeByTracingTag, key);

		IndexedProcessVariableMap indexedProcessVariableMap;

		if(sourceValue instanceof IndexedProcessVariableMap){
			indexedProcessVariableMap = ((IndexedProcessVariableMap) sourceValue);
		}else{
			indexedProcessVariableMap = new IndexedProcessVariableMap();
			indexedProcessVariableMap.putProcessVariable(0, sourceValue);
		}

		sourceValue = (indexedProcessVariableMap.getProcessVariableAt(index));
		if(sourceValue instanceof VariablePointer){
			((VariablePointer) sourceValue).setValue(this, val);
		}else {

			indexedProcessVariableMap.putProcessVariable(index, val);
			setSourceValue(scopeByTracingTag, key, indexedProcessVariableMap);
		}
	}

	@Override
	public Serializable getAt(String tracingTag, String key, int index) throws Exception {

		Object sourceValue = getSourceValue(tracingTag, key);

		IndexedProcessVariableMap indexedProcessVariableMap;

		if(sourceValue instanceof IndexedProcessVariableMap){
			indexedProcessVariableMap = ((IndexedProcessVariableMap) sourceValue);
		}else{
			indexedProcessVariableMap = new IndexedProcessVariableMap();
			indexedProcessVariableMap.putProcessVariable(0, sourceValue);
		}

		return indexedProcessVariableMap.getProcessVariableAt(index);
	}

	public void setProperty(String tracingTag, String key, Serializable val) throws Exception {
		String fullKey = createFullKey(tracingTag, key, true);

		if(val==null) variables.remove(fullKey);
		else
			variables.put(fullKey, val);
	}
	
	public Serializable getProperty(String tracingTag, String key) throws Exception {
//		String fullKey = createFullKey(tracingTag, key, true);
//		return (Serializable)variables.get(fullKey);

		return getSourceValue(tracingTag, key, true);
	}


	protected String createFullKey(String tracingTag, String key, boolean isProperty){
		return createFullKey(tracingTag, key, isProperty, -1);
	}

	protected String createFullKey(String tracingTag, String key, boolean isProperty, int index){

		return tracingTag
				+ (getExecutionScopeContext() != null ? ":" + getExecutionScopeContext().getExecutionScope() : "")
				+ ":" + key
				+ (isProperty ? SUFFIX_PROPERTY : "")
				+ (index > 0 ? "[" + index +"]" : "")
				;
	}
	
	protected boolean isProperty(String fullKey){
		return fullKey.endsWith(SUFFIX_PROPERTY);
	}
	
	public ProcessInstance getMainProcessInstance() throws Exception {
		throw new UEngineException("You can't access main process in in-memory process.");				
	}
	
	public ProcessInstance getRootProcessInstance() throws Exception {		
		throw new UEngineException("You can't access root process in in-memory process.");				
	}
	

	public void setDefinitionVersionId(String verId) throws Exception {
	}

	public boolean isNew() {
		return true;
	}
	
	public void copyTo(ProcessInstance instance) throws Exception {
		for(Iterator iter = variables.keySet().iterator(); iter.hasNext();){
			String key = (String) iter.next();
			
		}
	}
	public WorkList getWorkList() {
		return new SimulatorWorkList();
	}
	
	public void setBeanProperty(String key, Object value) {
		try {
			if(key.startsWith("[instance].dummy")) return;
			
			HashMap objects = new HashMap();
			objects.put("instance", this);
			objects.put("definition", getProcessDefinition());
			objects.put("activities", new ActivityBeanResolver(this));  

			if(key.startsWith("[roles].")){
				String[] rolesAndRoleName = key.replace('.','@').split("@");
				if(rolesAndRoleName.length>1 ){
					String roleName = rolesAndRoleName[1];

					if(value instanceof String){
						putRoleMapping(roleName, (String)value);
					}else if(value instanceof RoleMapping){
						putRoleMapping(roleName, ((RoleMapping)value));
					}else if(value instanceof ProcessVariableValue){
						ProcessVariableValue pvvOfEP = (ProcessVariableValue)value;
						pvvOfEP.beforeFirst();
						RoleMapping rm = RoleMapping.create();

						do {
							String ep = null;
							if (pvvOfEP.getValue() instanceof RoleMapping) {
								RoleMapping rmTemp = ((RoleMapping) pvvOfEP.getValue());
								rmTemp.beforeFirst();
								do{
									rm.setEndpoint(rmTemp.getEndpoint());
									rm.moveToAdd();
								}while(rmTemp.next());
							} else{
								ep = "" + pvvOfEP.getValue();
								rm.setEndpoint(ep);
								rm.moveToAdd();
							}

						} while (pvvOfEP.next());
						
						putRoleMapping(roleName, rm);
					}
				}
			}else if(key.startsWith("[activities].")){
				String objectIndex = key.substring(1, key.indexOf("]."));
				String propertyName = key.substring(key.indexOf("].")+2);
				Object object = objects.get(objectIndex);
				
				UEngineUtil.setBeanProperty(object, propertyName, value,this, object instanceof ProcessInstance);
			}else if(key.startsWith("[")){
				String objectIndex = key.substring(1, key.indexOf("]."));
				String propertyName = key.substring(key.indexOf("].")+2);
				Object object = objects.get(objectIndex);
				
				UEngineUtil.setBeanProperty(object, propertyName, value, object instanceof ProcessInstance);
			}else
				set("", key, (Serializable) value);
			
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	public Object getBeanProperty(String key) {

		try {
			
			HashMap objects = new HashMap();
			objects.put("instance", this);
			objects.put("definition", getProcessDefinition());
			objects.put("activities", new ActivityBeanResolver(this));  
	
			if(key.startsWith("[roles].")){
				String[] rolesAndRoleName = key.replace('.','@').split("@");
				if(rolesAndRoleName.length>1 ){
					return getRoleMapping(rolesAndRoleName[1]);
				}
			}else if(key.startsWith("[activities].")){
				String objectIndex = key.substring(1, key.indexOf("]."));
				String propertyName = key.substring(key.indexOf("].")+2);
				Object object = objects.get(objectIndex);
				
				boolean ignoreBeanPropertyResolver = object instanceof ProcessInstance;
				
				return UEngineUtil.getBeanProperty(object, propertyName,this,ignoreBeanPropertyResolver);
			}else if(key.startsWith("[")){
				String objectIndex = key.substring(1, key.indexOf("]."));
				String propertyName = key.substring(key.indexOf("].")+2);
				Object object = objects.get(objectIndex);
				
				boolean ignoreBeanPropertyResolver = object instanceof ProcessInstance;
				
				return UEngineUtil.getBeanProperty(object, propertyName, ignoreBeanPropertyResolver);
			}
			ProcessVariableValue pvv = getMultiple("", key);
			if(pvv.size() == 1) return pvv.getValue();
			else return pvv;
			
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	String mainExecutionScope;
	public String getMainExecutionScope() {
		// TODO Auto-generated method stub
		return mainExecutionScope;
	}

//	public String getParentExecutionScopeOf(String executionScope){
//		return super.getParentExecutionScopeOf(executionScope); //TODO
//	}

}