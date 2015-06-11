package org.uengine.kernel;

import org.uengine.processmanager.ProcessTransactionContext;

public interface ObjectDeployFilter {
	public void beforeDeploy(String definition, String definitionId, String definitionVersionId, ProcessTransactionContext tc, String folder,boolean isNew) throws Exception;
}
