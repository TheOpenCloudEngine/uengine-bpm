package org.uengine.kernel;

import org.uengine.processmanager.ProcessTransactionContext;

public interface DeployFilter {
	public void beforeDeploy(ProcessDefinition definition, ProcessTransactionContext tc, String folder,boolean isNew) throws Exception;
	//public void afterDeploy(String folder, ProcessDefinition definition, ProcessTransactionContext tc) throws Exception;
}
