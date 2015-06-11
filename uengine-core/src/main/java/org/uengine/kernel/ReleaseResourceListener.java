package org.uengine.kernel;

import org.uengine.processmanager.TransactionContext;

public interface ReleaseResourceListener {
	void beforeReleaseResource(TransactionContext tx) throws Exception;
}
