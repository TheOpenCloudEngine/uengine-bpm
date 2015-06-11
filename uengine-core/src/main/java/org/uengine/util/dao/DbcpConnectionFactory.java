package org.uengine.util.dao;

import javax.sql.*;

import java.sql.*;
import javax.naming.*;

import org.uengine.kernel.GlobalContext;

/**
 * @author Jong-uk Jeong
 */
@Deprecated
public class DbcpConnectionFactory extends DefaultConnectionFactory {
	private static final long serialVersionUID = org.uengine.kernel.GlobalContext.SERIALIZATION_UID;
	
	@Deprecated	
	public Connection getConnection() throws Exception{
		return this.getDataSource().getConnection();
	}
	
	@Deprecated
	public DataSource getDataSource() {
		DataSource ds = null;
		try {
			Context initContext = new InitialContext();
			Context envContext = (Context)initContext.lookup("java:/comp/env");
			ds = (DataSource)envContext.lookup(GlobalContext.DATASOURCE_JNDI_NAME);
		} catch (NamingException e) {
			e.printStackTrace();
		}
		return ds;
	}
}
