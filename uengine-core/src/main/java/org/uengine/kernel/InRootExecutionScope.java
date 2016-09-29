package org.uengine.kernel;

/**
 * Created by jjy on 2015. 12. 18..
 */
public abstract class InRootExecutionScope extends InParentExecutionScope{

    @Override
    protected String getDesiredExecutionScope(ProcessInstance instance, String currentExecutionScope) {
        return null;
    }
}
