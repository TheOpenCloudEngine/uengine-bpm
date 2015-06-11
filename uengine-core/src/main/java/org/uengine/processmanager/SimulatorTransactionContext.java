package org.uengine.processmanager;

import java.sql.Connection;
import java.sql.SQLException;


public class SimulatorTransactionContext extends ProcessTransactionContext {

	public SimulatorTransactionContext(){
		super(null);
	}
	
	protected void beforeCommit() throws Exception {
	}

	protected void beforeRollback() throws Exception {
	}

	public Connection getConnection() throws SQLException {
		return null;
	}

	public void releaseResources() throws Exception {
	}
}
