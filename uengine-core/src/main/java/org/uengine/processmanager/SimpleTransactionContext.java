package org.uengine.processmanager;

import java.sql.Connection;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.uengine.util.dao.ConnectionFactory;
import org.uengine.util.dao.DefaultConnectionFactory;


public class SimpleTransactionContext extends TransactionContext{
	
	String dataSourceJNDIName;
		public String getDataSourceJNDIName() {
			return dataSourceJNDIName;
		}
		public void setDataSourceJNDIName(String dataSourceJNDIName) {
			this.dataSourceJNDIName = dataSourceJNDIName;
		}
	
	public SimpleTransactionContext(){
		super(null);
	}

	public SimpleTransactionContext(String dataSourceJNDIName){
		this();
		setDataSourceJNDIName(dataSourceJNDIName);
	}
	
	//TODO : 임시 코드
	public DataSource getDataSource() {
		return ((DefaultConnectionFactory)getConnectionFactory()).getDataSource();
	}
	
	private DefaultConnectionFactory connectionFactory;
	
	public ConnectionFactory getConnectionFactory() {
		if (connectionFactory == null) {
			connectionFactory = (DefaultConnectionFactory) DefaultConnectionFactory.create();
		}
		return connectionFactory;
	}
	
	public Connection getConnection() throws SQLException {
		if(connection == null || connection.isClosed()){
			try {
				connection = getConnectionFactory().getConnection();
						
			} catch (Exception e) {
				// TODO Auto-generated catch block
				throw new RuntimeException(e);
			}
			
			connection.setAutoCommit(false);
			
		}
		
		return connection;
	}

	
}
