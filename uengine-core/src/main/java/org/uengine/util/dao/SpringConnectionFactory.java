package org.uengine.util.dao;


import java.sql.*;

import org.springframework.jdbc.datasource.ConnectionHolder;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.transaction.support.TransactionSynchronizationManager;

//import org.springframework.jdbc.datasource.DataSourceTransactionManager;
//import org.springframework.jdbc.datasource.DataSourceUtils;

/**
 * @author Bo-Sang kim
 */

public class SpringConnectionFactory extends DefaultConnectionFactory {

	private static final long serialVersionUID = org.uengine.kernel.GlobalContext.SERIALIZATION_UID;

	DataSourceTransactionManager transactionManager;
	public void setTransactionManager(
			DataSourceTransactionManager transactionManager) {
		this.transactionManager = transactionManager;
	}	

	public Connection getConnection() throws Exception{
		
		ConnectionHolder connHolder = (ConnectionHolder) TransactionSynchronizationManager.getResource(transactionManager.getDataSource());		
		Connection conn = connHolder.getConnection();  

		return conn; 
	}
}
