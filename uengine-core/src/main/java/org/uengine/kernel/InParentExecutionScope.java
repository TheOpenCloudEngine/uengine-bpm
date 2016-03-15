package org.uengine.kernel;

/**
 * Created by jjy on 2015. 12. 18..
 */
public abstract class InParentExecutionScope {

    public abstract Object logic(ProcessInstance instance) throws Exception;

    public Object run(ProcessInstance instance) throws Exception{
        ExecutionScopeContext executionScopeContext = instance.getExecutionScopeContext();
        instance.setExecutionScope(getDesiredExecutionScope(instance));

        Object returnValue = logic(instance);

        instance.setExecutionScope(executionScopeContext!=null ? executionScopeContext.getExecutionScope() : null);

        return returnValue;

    }

    protected String getDesiredExecutionScope(ProcessInstance instance) {
        return instance.getMainExecutionScope();
    }


}
