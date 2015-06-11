package org.uengine.persistence;

import org.uengine.processmanager.TransactionContext;

public interface DAOType {

	public void setTransactionContext(TransactionContext tc);
}
