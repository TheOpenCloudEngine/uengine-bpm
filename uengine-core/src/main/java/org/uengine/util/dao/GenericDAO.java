package org.uengine.util.dao;

import java.lang.reflect.*;

import java.sql.*;


/**
 * Generic DAO
 *
 * @author <a href="mailto:ghbpark@hanwha.co.kr">Sungsoo Park</a>, Jinyoung Jang
 * @version $Id: GenericDAO.java,v 1.1 2012/02/13 05:29:13 sleepphoenix4 Exp $
 */
public class GenericDAO extends AbstractGenericDAO {
	private static final long serialVersionUID = org.uengine.kernel.GlobalContext.SERIALIZATION_UID;


	protected GenericDAO(Class daoClass) throws Exception{
		super(daoClass, false);
	}	

	protected GenericDAO(final String jndiName, String sqlStmt, Class daoClass) throws Exception {
		super(jndiName, false, sqlStmt, daoClass);
	}
//	
//	protected GenericDAO(final Connection conn, String sqlStmt, Class daoClass)	throws Exception {
//		super(conn, false, sqlStmt, daoClass);
//	}		

	protected GenericDAO(ConnectionFactory con, String sqlStmt, Class daoClass) throws Exception {
		super(con, false, sqlStmt, daoClass);
	}
	

	



	
	public static IDAO createDAOImpl(String jndiName, String sqlStmt, Class daoClass) throws Exception{		
		return (IDAO)Proxy.newProxyInstance(
			daoClass.getClassLoader(),
			new Class[]{daoClass},
			new GenericDAO(jndiName, sqlStmt, daoClass)
		);		
	}

//	public static IDAO createDAOImpl(Connection conn, String sqlStmt, Class daoClass) throws Exception{		
//		return (IDAO)Proxy.newProxyInstance(
//			daoClass.getClassLoader(),
//			new Class[]{daoClass},
//			new GenericDAO(conn, sqlStmt, daoClass)
//		);		
//	}
	

	public static IDAO createDAOImpl(ConnectionFactory conn, String sqlStmt, Class daoClass) throws Exception{		
		return (IDAO)Proxy.newProxyInstance(
			daoClass.getClassLoader(),
			new Class[]{daoClass},
			new GenericDAO(conn, sqlStmt, daoClass)
		);		
	}

	public static IDAO createDAOImpl(ConnectionFactory conn, String sqlStmt) throws Exception{		
		return (IDAO)Proxy.newProxyInstance(
			IDAO.class.getClassLoader(),
			new Class[]{IDAO.class},
			new GenericDAO(conn, sqlStmt, IDAO.class)
		);
	}

	public static IDAO createDAOImpl(String sqlStmt, Class daoClass) throws Exception{		
		return (IDAO)Proxy.newProxyInstance(
			daoClass.getClassLoader(),
			new Class[]{daoClass},
			new GenericDAO(DefaultConnectionFactory.create(), sqlStmt, daoClass)
		);
	}

	public static IDAO createDAOImpl() throws Exception{		
		return (IDAO)Proxy.newProxyInstance(
			IDAO.class.getClassLoader(),
			new Class[]{IDAO.class},
			new GenericDAO(IDAO.class)
		);
	}

	public static IDAO createDAOImpl(Class daoClass) throws Exception{		
		return (IDAO)Proxy.newProxyInstance(
			IDAO.class.getClassLoader(),
			new Class[]{daoClass},
			new GenericDAO(daoClass)
		);
	}

	public static IDAO createDAOImpl(String sqlStmt) throws Exception{		
		return (IDAO)Proxy.newProxyInstance(
			IDAO.class.getClassLoader(),
			new Class[]{IDAO.class},
			new GenericDAO(DefaultConnectionFactory.create(), sqlStmt, IDAO.class)
		);
	}



}

