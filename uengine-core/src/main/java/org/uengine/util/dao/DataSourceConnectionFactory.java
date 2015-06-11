/*
 * Created on 2004. 12. 15.
 */
package org.uengine.util.dao;

import javax.sql.*;
import javax.transaction.UserTransaction;

import java.io.Serializable;
import java.sql.*;
import javax.naming.*;

import org.uengine.kernel.UEngineException;

/**
 * @author Jinyoung Jang
 */
public class DataSourceConnectionFactory implements ConnectionFactory {
	private static final long serialVersionUID = org.uengine.kernel.GlobalContext.SERIALIZATION_UID;
	public Connection getConnection() throws Exception{
		InitialContext ctx = null;
		ctx = new InitialContext();
		
		if(getDataSourceJndiName()==null)
			throw (new UEngineException("Data Source JNDI name is null. Check whether the JNDI name is null."));
		
		DataSource ds = (javax.sql.DataSource) ctx.lookup(getDataSourceJndiName());
		Connection conn = ds.getConnection();
		
		return conn; 
	}
	
	String dataSourceJndiName;
	
	public String getDataSourceJndiName() {
		return dataSourceJndiName;
	}

	public void setDataSourceJndiName(String string) {
		dataSourceJndiName = string;
	}

}
