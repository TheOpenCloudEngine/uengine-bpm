package org.uengine.kernel;

import java.util.*;
import java.io.*;

import org.metaworks.dwr.MetaworksRemoteService;
import org.springframework.integration.channel.QueueChannel;
import org.springframework.messaging.support.GenericMessage;
import org.uengine.kernel.GlobalContext;
import org.uengine.processmanager.ProcessTransactionContext;
import org.uengine.processmanager.TransactionContext;
import org.uengine.util.*;
import org.uengine.webservices.worklist.WorkList;

/**
 * @author Jinyoung Jang
 */

public abstract class ProcessInstance implements java.io.Serializable, BeanPropertyResolver{
	private static final long serialVersionUID = org.uengine.kernel.GlobalContext.SERIALIZATION_UID;
	public final String PVKEY_EXECUTION_SCOPES = "_executionScopes";


	//warning: this visibility (public) may occur undesired operation by other components.
	//	   if a rigorously secured environment is required, change the visibility. e.g. private.
//	public static boolean USE_EJB = true;
	public static Class USE_CLASS = null;

	transient protected ProcessTransactionContext ptc = null;
	transient protected ExecutionScopeContext executionScopeContext = null;

	abstract public String getInstanceId();
	abstract public void setInstanceId(String value);

	abstract public String getName();
	abstract public void setName(String value);

	abstract public ProcessDefinition getProcessDefinition() throws Exception;
	abstract public ProcessDefinition getProcessDefinition(boolean cached) throws Exception;
//	abstract public ProcessDefinition getProcessDefinition(String definitionId);
	abstract public void setProcessDefinition(ProcessDefinition value);

	abstract public ValidationContext getValidationContext();

	abstract public UEngineException getFault(String tracingTag) throws Exception;
	abstract public RoleMapping getRoleMapping(String roleName) throws Exception;
	abstract public void putRoleMapping(RoleMapping roleMap) throws Exception;
	abstract public void putRoleMapping(String roleName, String endpoint) throws Exception;
	abstract public void putRoleMapping(String roleName, RoleMapping rp) throws Exception;

	abstract public void set(String tracingTag, String key, Serializable val) throws Exception;
	abstract public Serializable get(String tracingTag, String key) throws Exception;
	abstract public void add(String scopeByTracingTag, String key, Serializable val, int index) throws Exception;
	abstract public void set(String scopeByTracingTag, ProcessVariableValue pvv) throws Exception;
	abstract public void setAt(String scopeByTracingTag, String key, int index, Serializable val) throws Exception;
	abstract public Serializable getAt(String tracingTag, String key, int index) throws Exception;

	/**
	 * @deprecated rather use 'ProcessVariable.getMultiple(instance, scope)'
	 */
	abstract public ProcessVariableValue getMultiple(String tracingTag, String key) throws Exception;

	abstract public void setProperty(String tracingTag, String key, Serializable val) throws Exception;
	abstract public Serializable getProperty(String tracingTag, String key) throws Exception;

	abstract public String getInXML(String tracingTag, String key) throws Exception;	
	abstract public Map getAll(String tracingTag) throws Exception;	
	abstract public Map getAll() throws Exception;

	//instead of direct setting the status, we recommend use 'fireComplete' or 'fireFault' methods
	abstract protected void setStatus(String tracingTag, String status) throws Exception;	
	abstract public String getStatus(String tracingTag) throws Exception;

	abstract public void setInfo(String info) throws Exception;	
	abstract public String getInfo() throws Exception;


	abstract public void setDueDate(Calendar date) throws Exception;
	abstract public Calendar getDueDate() throws Exception;

	abstract public boolean isSubProcess() throws Exception;
	abstract public boolean isAdhocProcess() throws Exception;
	abstract public boolean isSimulation() throws Exception;

	abstract public String getMainProcessInstanceId();
	abstract public String getMainActivityTracingTag();
	abstract public String getMainExecutionScope();
	
	abstract public String getRootProcessInstanceId();
	abstract public ProcessInstance getSubProcessInstance(String absoluteTracingTag) throws Exception;
	abstract public boolean isDontReturn();
	abstract public boolean isNew();

	abstract public void addMessageListener(String message, String tracingTag) throws Exception;
	abstract public Vector getMessageListeners(String message) throws Exception;
	abstract public void removeMessageListener(String message, String tracingTag) throws Exception;

	/////////////////////// factory methods ////////////////////////
	abstract public ProcessInstance getInstance(String instanceId) throws Exception;
	abstract public ProcessInstance getInstance(String instanceId, Map options) throws Exception;
	abstract public void remove() throws Exception;
	abstract public void stop() throws Exception;	
	abstract public void stop(String status) throws Exception;
	
	abstract public ProcessInstance createSnapshot() throws Exception;


	abstract public Calendar calculateDueDate(Calendar startDate, int duration);

	abstract public WorkList getWorkList();

////////////////////////hot spots /////////////////////////////////

	//event handling
	public void fireComplete(String tracingTag) throws Exception{
		getProcessDefinition().getActivity(tracingTag).fireComplete(this);
	}

	public void fireFault(String tracingTag, UEngineException fault) throws Exception{
		getProcessDefinition().getActivity(tracingTag).fireFault(this, fault);
	}
	//

	//status accessors
	public boolean isRunning(String tracingTag) throws Exception{
		String status = getStatus(tracingTag);
		return Activity.STATUS_TIMEOUT.equals(status) || Activity.STATUS_RUNNING.equals(status);
	}

	public boolean isCompleted(String tracingTag) throws Exception{
		String status = getStatus(tracingTag);
		return Activity.STATUS_COMPLETED.equals(status);
	}

	public boolean isFault(String tracingTag) throws Exception{
		return getStatus(tracingTag).equals(Activity.STATUS_FAULT);
	}

	public boolean isSuspended(String tracingTag) throws Exception{
		return getStatus(tracingTag).equals(Activity.STATUS_SUSPENDED);
	}
	//end


	protected void setFault(String tracingTag, FaultContext fc) throws Exception{
		setStatus(tracingTag, Activity.STATUS_FAULT);			

		if(fc==null) return;
		UEngineExceptionContext ctx = fc.getFault().createContext();
		setProperty(tracingTag, "_fault", (Serializable)ctx);
	}


	public void execute() throws Exception{
		getProcessDefinition().fireEventToActivityFilters(this, "instance.beforeStart", null);

		execute(""); //Activity where tracingtag="" locates the definition itself.

		getProcessDefinition().fireEventToActivityFilters(this, "instance.afterStart", null);
	}

	public void execute(String tracingTag) throws Exception{
		final Activity activity = getProcessDefinition().getActivity(tracingTag);

		if(!"".equals(tracingTag))
			addDebugInfo(activity);
		
		if(Activity.STATUS_READY.equals(activity.getStatus(this)) && activity.getStartedTime(this)!=null){ //means the activity is future activity that needed to be notified to be started later.
			activity.reserveActivity(this);
			
			return;
		}
		
		activity.beforeExecute(this);

		setStatus(tracingTag, Activity.STATUS_RUNNING);

		try{
			if(activity.isQueuingEnabled()){

				getProcessTransactionContext().addTransactionListener(new TransactionListener() {
					@Override
					public void beforeCommit(TransactionContext tx) throws Exception {

					}

					@Override
					public void beforeRollback(TransactionContext tx) throws Exception {

					}

					@Override
					public void afterCommit(TransactionContext tx) throws Exception {
						QueueChannel inputChannel = MetaworksRemoteService.getInstance().getBeanFactory().getBean("inputChannel", QueueChannel.class);
						inputChannel.send(new GenericMessage<String[]>(new String[]{activity.getTracingTag(), getInstanceId()}));
					}

					@Override
					public void afterRollback(TransactionContext tx) throws Exception {

					}
				});

			}else {
				activity.executeActivity(this);
			}
		}catch (Exception e) {
			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			e.printStackTrace(pw);
					
		    getProcessTransactionContext().addDebugInfo(sw.toString());
		    if(!"".equals(tracingTag))
		    	addDebugInfo(activity);
			
			FaultContext fc = new FaultContext();
			fc.setCauseActivity(activity);
			fc.setFault(new UEngineException(e.getMessage(),e));
			
			this.setFault(tracingTag, fc);
			throw new Exception(e);
		}

		if(!activity.isQueuingEnabled()) {
			activity.afterExecute(this);
		}
	}

	public Map getActivityDetails(String tracingTag) throws Exception{
		return getProcessDefinition().getActivity(tracingTag).getActivityDetails(this, null);
	}

	//only for scripting users. don't use if you're developer. :)
	/**
	 * @deprecated
	 */
	public void set(String key, Object val) throws Exception{
		int dotPos = key.indexOf('.');
		String pvNamePartOnly = null;
		if(dotPos > -1){
			pvNamePartOnly  = key.substring(0, dotPos);
		}

		if(getProcessDefinition().getProcessVariable(key)==null){
			getValidationContext().addWarning("[ActivityInstance::set] unregistered process variable '" + pvNamePartOnly + "' is used.");
		}

		set("", key, (Serializable)val);
	}

	/**
	 * @deprecated
	 */
	public Serializable get(String key) throws Exception{
		int dotPos = key.indexOf('.');
		String pvNamePartOnly = null;
		if(dotPos > -1){
			pvNamePartOnly  = key.substring(0, dotPos);
		}else{
			pvNamePartOnly  = key;
		}

		if(getProcessDefinition().getProcessVariable(pvNamePartOnly)==null){
			getValidationContext().addWarning("[ActivityInstance::get] unrecognized process variable '" + pvNamePartOnly + "' is referenced.");
		}

		return (Serializable)get("", key);
	}
	//

	public void add(String key, Object value, int index) throws Exception{
		int dotPos = key.indexOf('.');
		String pvNamePartOnly = null;
		if(dotPos > -1){
			pvNamePartOnly  = key.substring(0, dotPos);
		}

		if(getProcessDefinition().getProcessVariable(key)==null){
			getValidationContext().addWarning("[ActivityInstance::add] unregistered process variable '" + pvNamePartOnly + "' is used.");
		}

		add("", key, (Serializable)value, index);
	}

	public ActivityInstanceContext getCurrentRunningActivity() throws Exception{
		final ProcessInstance finalThis = this;

		ActivityForLoop forLoop = new ActivityForLoop(){
			public void logic(Activity act){
				try{
					if(act instanceof SubProcessActivity && act.getStatus(finalThis).equals(Activity.STATUS_RUNNING)){
						SubProcessActivity spAct = (SubProcessActivity)act;
						List spInstanceIds = spAct.getSubprocessIds(finalThis);
						if(spInstanceIds.size() == 0){
							throw new UEngineException("Activity in the running subprocess cannot be found.");
						}

						String spInstanceId = (String)spInstanceIds.get(0);

						ProcessInstance instance = (ProcessInstance)getInstance(spInstanceId);
						stop(instance.getCurrentRunningActivity());
					}else					
						if(!(act instanceof ComplexActivity) && act.getStatus(finalThis).equals(Activity.STATUS_RUNNING)){
							stop(new ActivityInstanceContext(act, finalThis));
						}
				}catch(Exception e){
					throw new RuntimeException(e);
				}
			}
		};
		forLoop.run(getProcessDefinition());

		return (ActivityInstanceContext)forLoop.getReturnValue();
	}

	public ActivityInstanceContext getActualInitiatorHumanActivity() throws Exception{
		final ProcessInstance finalThis = this;

		ActivityForLoop forLoop = new ActivityForLoop(){
			public void logic(Activity act){
				try{
					if((act instanceof HumanActivity) && act.getStatus(finalThis).equals(Activity.STATUS_COMPLETED)){
						stop(new ActivityInstanceContext(act, finalThis));
					}
				}catch(Exception e){
					throw new RuntimeException(e);
				}
			}
		};
		forLoop.run(getProcessDefinition());

		return (ActivityInstanceContext)forLoop.getReturnValue();
	}

	public Vector getCurrentRunningActivities() throws Exception{
		final ProcessInstance finalThis = this;
		final Vector runningActivities = new Vector();

		ActivityForLoop forLoop = new ActivityForLoop(){
			public void logic(Activity act){
				try{
					if(!(act instanceof ComplexActivity) && 
						(act.getStatus(finalThis).equals(Activity.STATUS_RUNNING)||act.getStatus(finalThis).equals(Activity.STATUS_TIMEOUT))){
						runningActivities.add(act);
					}
				}catch(Exception e){
					throw new RuntimeException(e);
				}
			}
		};
		forLoop.run(getProcessDefinition());

		return runningActivities;				
	}

	/**
	 * occurs deep search
	 * @return
	 * @throws Exception
	 */
	public Vector getActivitiesDeeply(final String filter) throws Exception{
		final ProcessInstance finalThis = this;
		final Vector runningActivities = new Vector();

		ActivityForLoop forLoop = new ActivityForLoop(){
			public void logic(Activity act){
				try{
					ProcessInstance instancehere = finalThis;
					
					if(
							act instanceof SubProcessActivity
							&& (
									act.getStatus(instancehere).equals(Activity.STATUS_COMPLETED)
									|| act.getStatus(instancehere).equals(Activity.STATUS_RUNNING)
							)
					) {
						SubProcessActivity spAct = (SubProcessActivity)act;
						List<String> spInstanceIds = spAct.getSubprocessIds(instancehere);
						
						if(spInstanceIds.size() == 0){
							throw new UEngineException("Activity in the running subprocess cannot be found.");
						} else {
							for(String spInstanceId : spInstanceIds) {
								ProcessInstance instance = (ProcessInstance) getInstance(spInstanceId);
								if (instancehere == instance) {
									continue;
								} else {
									runningActivities.addAll(instance.getActivitiesDeeply(filter));
								}
							}
						}
						
					} else if (
							!(act instanceof ComplexActivity)
							&& (filter == null || filter.indexOf(act.getStatus(instancehere)) > -1)
					) {
							runningActivities.add(new ActivityInstanceContext(act,instancehere));
					}
				}catch(Exception e){
					throw new RuntimeException(e);
				}
			}
		};
		
		forLoop.run(getProcessDefinition());

		return runningActivities;
	}

	public Vector getCurrentRunningActivitiesDeeply() throws Exception{
		return getActivitiesDeeply(Activity.STATUS_RUNNING);
	}

	public Vector getEventHandlerActivity(String eventHandlerName,Vector runningActivities) throws Exception{
		
		EventHandler[] ehs = this.getEventHandlersInAction();
		for(int i=0; i<ehs.length; i++){
			if(!eventHandlerName.equals(ehs[i].getName())) continue;
				
			Activity act = ehs[i].getHandlerActivity();
			try{
				//if(act instanceof ScopeActivity )
				if(act instanceof SubProcessActivity ){
					SubProcessActivity spAct = (SubProcessActivity)act;
					List spInstanceIds = spAct.getSubprocessIds(this);
					if(spInstanceIds.size() == 0){
						return runningActivities;
					}
					
					String spInstanceId = (String)spInstanceIds.get(0);
					
					ProcessInstance SubInstance = (ProcessInstance)getInstance(spInstanceId);
					runningActivities.addAll(SubInstance.getCurrentRunningActivitiesDeeply());
					SubInstance.getEventHandlerActivity(eventHandlerName,runningActivities);
				}else					
				if(!(act instanceof ComplexActivity) ){
					runningActivities.add(new ActivityInstanceContext(act,this));
				}
			}catch(Exception e){
				throw new RuntimeException(e);
			}
		}
		
		return runningActivities;
	}
	
	

	public Vector getRunningOrCompletedActivities() throws Exception{
		final ProcessInstance finalThis = this;
		final Vector findingActivities = new Vector();

		ActivityForLoop forLoop = new ActivityForLoop(){
			public void logic(Activity act){
				try{
					if(!(act instanceof ComplexActivity)){
						String status = act.getStatus(finalThis);

						if(status.equals(Activity.STATUS_RUNNING) || status.equals(Activity.STATUS_COMPLETED)){
							findingActivities.add(act);
						}
					}
				}catch(Exception e){
					throw new RuntimeException(e);
				}
			}
		};
		forLoop.run(getProcessDefinition());

		return findingActivities;		
	}

	public Vector getRunningOrCompletedActivityTracingTags() throws Exception{
		Vector activities = getRunningOrCompletedActivities();
		Vector tracingTags = new Vector();
		for(int i=0; i<activities.size(); i++){
			tracingTags.add(((Activity)activities.elementAt(i)).getTracingTag());
		}

		return tracingTags;
	}

	public void setStatus(String status) throws Exception{
		setStatus("", status);
	}

	public String getStatus() throws Exception{
		return getStatus("");	
	}

	public boolean isParticipant(RoleMapping rm) throws Exception{
		Role [] roleDefined = getProcessDefinition().getRoles();
		for(int i=0; i<roleDefined.length; i++){
			if(roleDefined[i].getMapping(this).equals(rm)) return true;
		}

		return false;
	}

	public EventHandler[] getEventHandlersInAction() throws Exception{
		ArrayList eventHandlers = new ArrayList();
		Vector mls = getMessageListeners("event");

		ProcessDefinition definition = getProcessDefinition();
		if(mls!=null)
			for(int i=0; i<mls.size(); i++){
				ScopeActivity scopeAct = (ScopeActivity)definition.getActivity((String)mls.get(i));
				EventHandler[] ehs = scopeAct.getEventHandlers();

				if(ehs!=null)
					for(int j=0; j<ehs.length; j++)
						eventHandlers.add(ehs[j]);
			}

		EventHandler[] eventHandlersInArray = new EventHandler[eventHandlers.size()];
		eventHandlers.toArray(eventHandlersInArray);

		return eventHandlersInArray;
	}


//	///////////////////////// static services /////////////////////////////////////	

	public static ProcessInstance create() throws Exception{
		return create(null, null, null);
	}

	public static ProcessInstance create(ProcessDefinition def) throws Exception{
		return create(def, null, null);
	}

	public static ProcessInstance create(ProcessDefinition def, String name, Map options) throws Exception{

		if(def!=null && def.isVolatile()){
			//ProcessInstance instance = new DefaultProcessInstance();

			return new DefaultProcessInstance(def, name, options);
		}

		if(USE_CLASS==null){
			try{
				USE_CLASS = Thread.currentThread().getContextClassLoader().loadClass(GlobalContext.getPropertyString("processinstance.class"));
			}catch(Exception e){
				USE_CLASS = EJBProcessInstance.class;
			}
		}

		return (ProcessInstance) USE_CLASS.getConstructor(new Class[]{ProcessDefinition.class, String.class, Map.class}).newInstance(new Object[]{def, name, options});			
	}

	public ArrayList getExecutionScopeContexts(){
		try {
			setExecutionScopeContext(null); //this lets the caller exactly obtain the executionScopeContext.

			ArrayList executionScopeList = ((ArrayList)getProperty("", PVKEY_EXECUTION_SCOPES));
			if(executionScopeList!=null)
				return executionScopeList;
		} catch (Exception e) {
			e.printStackTrace();
		}

		return new ArrayList();
	}

	public ExecutionScopeContext issueNewExecutionScope(Activity rootActivityForScope, Activity triggerActivity, String executionScopeName){
		ArrayList executionScopeList = getExecutionScopeContexts();
		int existingExecutionScopeSeq = executionScopeList.size();

		ExecutionScopeContext esc = new ExecutionScopeContext();
		esc.setRootActivityInTheScope(rootActivityForScope);
		esc.setExecutionScope(""+existingExecutionScopeSeq);
		esc.setName(executionScopeName);
		if(triggerActivity!=null)
			esc.setTriggerActivityTracingTag(triggerActivity.getTracingTag());

		executionScopeList.add(esc);

		try {
			setProperty("", PVKEY_EXECUTION_SCOPES, executionScopeList);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}

		return esc;
	}

	public ProcessTransactionContext getProcessTransactionContext() {
		return ptc;
	}
	public void setProcessTransactionContext(ProcessTransactionContext ptc) {
		this.ptc = ptc;
	}

	public ExecutionScopeContext getExecutionScopeContext() {
		if(executionScopeContext!=null && executionScopeContext.getRootActivityInTheScope()==null && executionScopeContext.getRootActivityTracingTag()!=null){
			try {
				executionScopeContext.setRootActivityInTheScope(getProcessDefinition().getActivity(executionScopeContext.getRootActivityTracingTag()));
			} catch (Exception e) {
			}
		}
		return executionScopeContext;
	}
	public void setExecutionScopeContext(ExecutionScopeContext executionScopeContext) {
		this.executionScopeContext = executionScopeContext;
	}	
	public void setExecutionScope(String executionScope) {
		if(executionScope==null){
			setExecutionScopeContext(null);
			return;
		}

		List executionScopes = getExecutionScopeContexts();
		for(int i=0; i<executionScopes.size(); i++){
			ExecutionScopeContext esc = (ExecutionScopeContext)executionScopes.get(i);
			if(executionScope.equals(esc.getExecutionScope())){
				setExecutionScopeContext(esc);
				return;
			}
		}

		throw new RuntimeException(new UEngineException("There's no execution scope named '" + executionScope + "'"));
	}

	public abstract ProcessInstance getMainProcessInstance() throws Exception;

	public abstract ProcessInstance getRootProcessInstance() throws Exception;

	public abstract void setDefinitionVersionId(String verId) throws Exception;

	public abstract void copyTo(ProcessInstance instance) throws Exception;
	
	public void addDebugInfo(Object message){
		getProcessTransactionContext().addDebugInfo(message+"\n");
	}
	
	public void addDebugInfo(String entry, Object message){
		addDebugInfo(" * "+entry+" = " +message);
	}
	
	public void addDebugInfo(Activity activity){
		addDebugInfo("\n\n-- Execute "+UEngineUtil.getClassNameOnly(activity.getClass())+" \t---------------------------------------\n");
		
		addDebugInfo("name", activity);
		addDebugInfo("instance", getInstanceId());
		addDebugInfo("definition", GlobalContext.WEB_CONTEXT_ROOT + "/processmanager/activityChangeForm.jsp?instId="+getInstanceId()+"&tracingTag="+activity.getTracingTag()+"&defVerId="+activity.getProcessDefinition().getId());
		
		
/*		if(GlobalContext.logLevelIsDebug && !(activity instanceof ProcessDefinition))
			try {
				Activity clonedAct = (Activity) activity.clone();
				clonedAct.setParentActivity(null);
				
				addDebugInfo(" -- definition -----------");
				addDebugInfo(GlobalContext.serialize(clonedAct, String.class));
				addDebugInfo(" -------------------------\n");
			} catch (Exception e) {
			}*/
		
		try {
			if (GlobalContext.logLevelIsDebug) {
				PrintStream ps = null;
				try {
					File f = new File(GlobalContext.FILE_SYSTEM_PATH + "/log/" + getInstanceId() + "/");
					f.mkdirs();
					
					ps = new PrintStream(
							new FileOutputStream(GlobalContext.FILE_SYSTEM_PATH + "/log/" /*+ UEngineUtil.getCalendarDir() + "/" */+ getInstanceId() + "/" + activity.getTracingTag() + ".log.xml")
					);
					ps.print(getProcessTransactionContext().getDebugInfo());

				} catch (FileNotFoundException e) {
					e.printStackTrace();
				} finally {
					if (ps != null) {
						try {
							ps.close();
						} catch (Exception e) {
						}
					}
				}
			}
		} catch (Exception e) {
		}

	}	
	
	transient ArrayList eventInterceptors;
		public void addActivityEventInterceptor(ActivityEventInterceptor aei){
			if(eventInterceptors == null) eventInterceptors = new ArrayList();
			eventInterceptors.add(aei);
		}
		public void removeActivityEventInterceptor(ActivityEventInterceptor aei){
			if(eventInterceptors != null) eventInterceptors.remove(aei);
		}
		public boolean fireActivityEventInterceptor(Activity activity, String command, ProcessInstance instance, Object payload) throws Exception{
	
			if(eventInterceptors==null) return false;
			
			boolean eventHasBeenIntercepted = false;
			for(int i=0; i<eventInterceptors.size(); i++){
				ActivityEventInterceptor eventInterceptor = (ActivityEventInterceptor) eventInterceptors.get(i);
				if(eventInterceptor.interceptEvent(activity, command, instance, payload)){
					eventHasBeenIntercepted = true;
				}
			}
			
			return eventHasBeenIntercepted;
		}


	List<String> activityCompletionHistory = new ArrayList<String>();
		public List<String> getActivityCompletionHistory() {
			return activityCompletionHistory;
		}

		public void setActivityCompletionHistory(List<String> activityCompletionHistory) {
			this.activityCompletionHistory = activityCompletionHistory;
		}


}