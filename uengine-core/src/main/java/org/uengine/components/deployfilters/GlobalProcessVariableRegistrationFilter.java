package org.uengine.components.deployfilters;

import org.uengine.kernel.DeployFilter;
import org.uengine.kernel.ProcessDefinition;
import org.uengine.processmanager.ProcessTransactionContext;

/**
 * Created by jjy on 2016. 10. 14..
 */
public class GlobalProcessVariableRegistrationFilter implements DeployFilter{
    @Override
    public void beforeDeploy(ProcessDefinition definition, ProcessTransactionContext tc, String folder, boolean isNew) throws Exception {

    }
}
