/*
 * Created on 2004. 12. 15.
 */
package org.uengine.util.dao;

import javax.sql.*;

import java.io.Serializable;
import java.sql.*;
import javax.naming.*;

import org.metaworks.FieldDescriptor;
import org.metaworks.Type;
import org.metaworks.inputter.RadioInput;
import org.uengine.kernel.GlobalContext;

/**
 * @author Jinyoung Jang
 */
public class JDBCConnectionFactory  implements ConnectionFactory {
	private static final long serialVersionUID = org.uengine.kernel.GlobalContext.SERIALIZATION_UID;
	
	public static void metaworksCallback_changeMetadata(Type type){
		
//		type.setFieldOrder(new String[]{
//			"DriverClassName", "URL", "User Name", "Password"	
//		});
		
	}
	
	public JDBCConnectionFactory(){
		setDriverClass(GlobalContext.JDBC_DRIVERCLASSNAME);
		setConnectionString(GlobalContext.JDBC_URL);
		setUserId(GlobalContext.JDBC_USERNAME);
		setPassword(GlobalContext.JDBC_PASSWORD);
	}
	
	public Connection getConnection() throws Exception{
		Class.forName(getDriverClass());
		Connection con = DriverManager.getConnection(getConnectionString(), getUserId(), getPassword());		
				
		return con; 
	}
	
	String connectionString;
		public String getConnectionString() {
			return connectionString;
		}
		public void setConnectionString(String value) {
			connectionString = value;
		}

	String userId;
		public String getUserId() {
			return userId;
		}
		public void setUserId(String value) {
			userId = value;
		}

	String password;
		public String getPassword() {
			return password;
		}
		public void setPassword(String value) {
			password = value;
		}

	String driverClass;
		public String getDriverClass() {
			return driverClass;
		}
		public void setDriverClass(String value) {
			driverClass = value;
		}
}
