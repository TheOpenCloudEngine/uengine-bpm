package org.uengine.kernel;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * Created by jjy on 2016. 10. 12..
 */
public class ExecutionScopedProcessInstance implements InvocationHandler{

    public static ProcessInstance newInstance(ProcessInstance processInstance, String desiredExecutionScope) {
        return (ProcessInstance) java.lang.reflect.Proxy.newProxyInstance(
                ProcessInstance.class.getClassLoader(),
                new Class[]{ProcessInstance.class},
                new ExecutionScopedProcessInstance(processInstance, desiredExecutionScope));
    }

    ProcessInstance originalProcessInstance;
        public ProcessInstance getOriginalProcessInstance() {
            return originalProcessInstance;
        }

    String executionScope;
        public String getExecutionScope() {
            return executionScope;
        }


    protected ExecutionScopedProcessInstance(ProcessInstance processInstance, String executionScope){
        originalProcessInstance = processInstance;
        this.executionScope = executionScope;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

        //setExecutionScope means this proxy should be changed to keep the context.
        if("setExecutionScope".equals(method.getName())){
            executionScope = (String) args[0];

            return null;
        }

        String originalExecutionScope = originalProcessInstance.getExecutionScopeContext()!=null ? originalProcessInstance.getExecutionScopeContext().getExecutionScope() : null;

        originalProcessInstance.setExecutionScope(executionScope);

        Object returnVal;
        if("getLocalInstance".equals(method.getName())){
            returnVal = originalProcessInstance;
        }else{
            returnVal = method.invoke(originalProcessInstance, args);
        }

        originalProcessInstance.setExecutionScope(originalExecutionScope);

        return returnVal;
    }
}
