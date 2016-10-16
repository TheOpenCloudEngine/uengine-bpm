package org.uengine.kernel;

import org.uengine.processmanager.ProcessTransactionContext;
import org.uengine.processmanager.TransactionContext;
import org.uengine.util.ActivityForLoop;
import org.uengine.util.UEngineUtil;
import org.uengine.webservices.worklist.WorkList;

import java.io.*;
import java.util.*;

/**
 * Created by jjy on 2016. 10. 12..
 */
public interface ProcessInstance extends BeanPropertyResolver{

    abstract public String getInstanceId();
    abstract void setInstanceId(String value);

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
    abstract void setStatus(String tracingTag, String status) throws Exception;
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
    public void fireComplete(String tracingTag) throws Exception;

    public void fireFault(String tracingTag, UEngineException fault) throws Exception;
    //

    //status accessors
    public boolean isRunning(String tracingTag) throws Exception;

    public boolean isCompleted(String tracingTag) throws Exception;

    public boolean isFault(String tracingTag) throws Exception;

    public boolean isSuspended(String tracingTag) throws Exception;
    //end


    void setFault(String tracingTag, FaultContext fc) throws Exception;


    public void execute() throws Exception;

    public void execute(String tracingTag) throws Exception;

    public void execute(String tracingTag, boolean forceToQueue) throws Exception;

    void setEventOriginator(String eventName, Activity activity);

    public Map getActivityDetails(String tracingTag) throws Exception;

    //only for scripting users. don't use if you're developer. :)
    /**
     * @deprecated
     */
    public void set(String key, Object val) throws Exception;

    /**
     * @deprecated
     */
    public Serializable get(String key) throws Exception;
    //

    public void add(String key, Object value, int index) throws Exception;

    public ActivityInstanceContext getCurrentRunningActivity() throws Exception;

    public ActivityInstanceContext getActualInitiatorHumanActivity() throws Exception;

    public List<Activity> getCurrentRunningActivities() throws Exception;

    /**
     * occurs deep search
     * @return
     * @throws Exception
     */
    public Vector getActivitiesDeeply(final String filter) throws Exception;

    public Vector getCurrentRunningActivitiesDeeply() throws Exception;

    public Vector getEventHandlerActivity(String eventHandlerName,Vector runningActivities) throws Exception;


    public Vector getRunningOrCompletedActivities() throws Exception;

    public Vector getRunningOrCompletedActivityTracingTags() throws Exception;

    public void setStatus(String status) throws Exception;

    public String getStatus() throws Exception;

    public boolean isParticipant(RoleMapping rm) throws Exception;

    public EventHandler[] getEventHandlersInAction() throws Exception;

//	///////////////////////// static services /////////////////////////////////////


    public List<ExecutionScopeContext> getExecutionScopeContexts();

    public ExecutionScopeContext issueNewExecutionScope(Activity rootActivityForScope, Activity triggerActivity, String executionScopeName);

    public ProcessTransactionContext getProcessTransactionContext();

    public void setProcessTransactionContext(ProcessTransactionContext ptc);

    public ExecutionScopeContext getExecutionScopeContext();

    public void setExecutionScopeContext(ExecutionScopeContext executionScopeContext);

    public void setExecutionScope(String executionScope);

    public abstract ProcessInstance getMainProcessInstance() throws Exception;

    public abstract ProcessInstance getRootProcessInstance() throws Exception;

    public abstract void setDefinitionVersionId(String verId) throws Exception;

    public abstract void copyTo(ProcessInstance instance) throws Exception;

    public void addDebugInfo(Object message);

    public void addDebugInfo(String entry, Object message);

    public void addDebugInfo(Activity activity);

    public void addActivityEventInterceptor(ActivityEventInterceptor aei);

    public void removeActivityEventInterceptor(ActivityEventInterceptor aei);

    public boolean fireActivityEventInterceptor(Activity activity, String command, ProcessInstance instance, Object payload) throws Exception;


    public List<String> getActivityCompletionHistory();

    public void setActivityCompletionHistory(List<String> activityCompletionHistory);



    public String getFullInstanceId();


    public String getParentExecutionScopeOf(String executionScope);

    public ExecutionScopeContext getExecutionScopeContextTree();

    public ExecutionScopeContext getExecutionScopeContextTree(String rootExecutionScope);

    public ProcessInstance getLocalInstance();

    public boolean isRoot();
}
