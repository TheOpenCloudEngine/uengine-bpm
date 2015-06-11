package org.uengine.kernel;

import org.uengine.processmanager.TransactionContext;


public interface TransactionListener {
	void beforeCommit(TransactionContext tx) throws Exception;
	void beforeRollback(TransactionContext tx) throws Exception;
	void afterCommit(TransactionContext tx) throws Exception;
	void afterRollback(TransactionContext tx) throws Exception;
}
