package org.uengine.kernel;

import org.uengine.processmanager.DefaultTransactionContext;

public interface ReleaseResourceListener {
	void beforeReleaseResource(DefaultTransactionContext tx) throws Exception;
}
