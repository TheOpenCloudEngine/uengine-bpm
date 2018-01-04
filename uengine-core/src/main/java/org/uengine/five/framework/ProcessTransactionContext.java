package org.uengine.five.framework;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import org.uengine.kernel.ActivityInstanceContext;
import org.uengine.kernel.ProcessInstance;
import org.uengine.util.ForLoop;

/**
 * Created by uengine on 2017. 11. 10..
 */
public class ProcessTransactionContext {

    static ThreadLocal<ProcessTransactionContext> local = new ThreadLocal<ProcessTransactionContext>();
    private boolean commitable;

    protected ProcessTransactionContext() {
        if (local.get() != null && local.get().isCommitable()) {
            throw new RuntimeException("There's uncommitted transactionContext remains.");
        }
        setCommitable(true);
        local.set(this);
    }

    public static ProcessTransactionContext getThreadLocalInstance() {
        ProcessTransactionContext tc = local.get();
        if (tc == null) {
            // if the local instance is obtained without @ProcessTransactional demarcation
            tc = new ProcessTransactionContext();
            tc.setCommitable(false);
        }
        return tc;
    }

    List<ProcessTransactionListener> transactionListeners = new ArrayList<ProcessTransactionListener>();
    public void addTransactionListener(ProcessTransactionListener tl) {
        transactionListeners.add(tl);
    }
    public List<ProcessTransactionListener> getTransactionListeners() {
        return transactionListeners;
    }

    transient List<ActivityInstanceContext> executedActivities = new ArrayList<ActivityInstanceContext>();
    public void addExecutedActivityInstanceContext(ActivityInstanceContext aic) {
        if (!executedActivities.contains(aic)) {
            executedActivities.add(aic);
        }
    }
    public List<ActivityInstanceContext> getExecutedActivityInstanceContextsInTransaction() {
        return executedActivities;
    }

    transient Map<String, ProcessInstance> processInstances = new Hashtable<String, ProcessInstance>();
    public void registerProcessInstance(ProcessInstance pi) {
        processInstances.put(pi.getInstanceId(), pi);
    }
    public ProcessInstance getProcessInstanceInTransaction(String instanceId) {
        return processInstances.get(instanceId);
    }

    public Map<String, ProcessInstance> getProcessInstancesInTransaction() {
        return processInstances;
    }

    transient Map<String, Object> sharedContexts = new Hashtable<String, Object>();
    public void setSharedContext(String contextKey, Object value) {
        if (value == null) {
            sharedContexts.remove(contextKey);
        } else {
            sharedContexts.put(contextKey, value);
        }
    }
    public Object getSharedContext(String contextKey) {
        if (!sharedContexts.containsKey(contextKey)) {
            return null;
        }
        return sharedContexts.get(contextKey);
    }


    protected void beforeCommit() throws Exception {
        ForLoop beforeCommitLoop = new ForLoop() {
            public void logic(Object target) {
                ProcessTransactionListener tl = (ProcessTransactionListener) target;
                try {
                    tl.beforeCommit(ProcessTransactionContext.this);
                } catch (Exception e) {
                    e.printStackTrace();
                    throw new RuntimeException(e);
                }
            }
        };

        beforeCommitLoop.run((ArrayList)transactionListeners);
    }

    public void commit() throws Exception {
        if (!isCommitable()) {
            throw new Exception("This transaction is uncommitable. If you want to commit this, mark with @ProcessTransactional at the beginning of the service method.");
        }
        beforeCommit();
        remove();
    }

    public void rollback() throws Exception {
        remove();
    }

    public void remove() throws Exception {
        local.set(null);
    }

    public void setCommitable(boolean commitable) {
        this.commitable = commitable;
    }

    public boolean isCommitable() {
        return commitable;
    }
}