package org.uengine.kernel;

/**
 * Created by jjy on 2015. 12. 18..
 */
public abstract class InExecutionScope extends InParentExecutionScope{

    String executionScope;

    public InExecutionScope(String executionScope){
        this.executionScope = executionScope;
    }

    @Override
    protected String getDesiredExecutionScope(ProcessInstance instance, String currentExecutionScope) {
        return executionScope;
    }
}
