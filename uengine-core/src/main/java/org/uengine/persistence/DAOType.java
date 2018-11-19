package org.uengine.persistence;

import org.uengine.processmanager.DefaultTransactionContext;

public interface DAOType {

	public void setTransactionContext(DefaultTransactionContext tc);
}
