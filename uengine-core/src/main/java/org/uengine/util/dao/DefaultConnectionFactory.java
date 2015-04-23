/*
 * Created on 2004. 12. 15.
 */
package org.uengine.util.dao;

import javax.sql.*;

import java.sql.*;
import javax.naming.*;

import org.uengine.kernel.GlobalContext;

/**
 * @author Jinyoung Jang
 */
public class DefaultConnectionFactory implements ConnectionFactory {
	private static final long serialVersionUID = org.uengine.kernel.GlobalContext.SERIALIZATION_UID;
	public static Class USE_CLASS = null;
	
	protected DefaultConnectionFactory(){}

	public Connection getConnection() throws Exception{
		return this.getDataSource().getConnection(); 
	}
	
	public DataSource getDataSource() {
		DataSource ds = null;
		try {
			InitialContext ctx = new InitialContext();
			ds = (DataSource) ctx.lookup(GlobalContext.DATASOURCE_JNDI_NAME);
		} catch (Exception e) {
			e.printStackTrace();
//			BasicDataSource bds = new BasicDataSource();
//			bds.setDriverClassName(GlobalContext.JDBC_DRIVERCLASSNAME);
//			bds.setUrl(GlobalContext.JDBC_URL);
//			bds.setUsername(GlobalContext.JDBC_USERNAME);
//			bds.setPassword(GlobalContext.JDBC_PASSWORD);
//			bds.setValidationQuery(GlobalContext.JDBC_VALIDATION_QUERY);
//			bds.setMaxActive(GlobalContext.JDBC_MAX_ACTIVE);
//			bds.setMaxIdle(GlobalContext.JDBC_MAX_IDLE);
//			bds.setMaxWait(GlobalContext.JDBC_MAX_WAIT);
//			ds = bds;
		}
		return ds;
	}
	
	public static ConnectionFactory create() {
		if(USE_CLASS==null){
			try{
				USE_CLASS = Thread.currentThread().getContextClassLoader().loadClass(GlobalContext.getPropertyString("defaultconnectionfactory.class"));
			}catch(Exception e){
				USE_CLASS = DefaultConnectionFactory.class;
			}
		}
		
		try {
			return (ConnectionFactory) USE_CLASS.newInstance();
		} catch (Exception e) {
			return null;
		}
	}

}
