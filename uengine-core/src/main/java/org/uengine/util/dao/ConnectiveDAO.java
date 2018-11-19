package org.uengine.util.dao;

import java.lang.reflect.*;
import java.sql.Connection;

import org.uengine.processmanager.DefaultTransactionContext;

/**
 * Generic DAO
 *
 * @author <a href="mailto:ghbpark@hanwha.co.kr">Sungsoo Park</a>, Jinyoung Jang
 * @version $Id: ConnectiveDAO.java,v 1.1 2012/02/13 05:29:13 sleepphoenix4 Exp $
 */
public class ConnectiveDAO extends AbstractGenericDAO {
	private static final long serialVersionUID = org.uengine.kernel.GlobalContext.SERIALIZATION_UID;

	protected ConnectiveDAO(final ConnectionFactory cf, boolean isConnective, String sqlStmt, Class daoClass)	throws Exception {
		super(cf, isConnective, sqlStmt, daoClass);
	}
	
	/**
	 * DefaultTransactionContext�� �Բ� DAO ��
	 * 
	 * @param tc 		Ʈ����� ���ؽ�Ʈ (see DefaultTransactionContext, ProcessTransactionContext)
	 * @param sqlStmt 	DB ��
	 * @param daoClass	����Ÿ�� �������̽� (IDAO or IDAO ��ӹ޾� ������ DAO �������̽�)
	 * @see org.uengine.contexts.DefaultTransactionContext
	 * @see org.uengine.contexts.UEngineSession
	 */
	public static IDAO createDAOImpl(ConnectionFactory cf, String sqlStmt, Class daoClass) throws Exception{		

		return (IDAO)Proxy.newProxyInstance(
			daoClass.getClassLoader(),
			new Class[]{daoClass},
			new ConnectiveDAO(cf, true, sqlStmt, daoClass)
		);		
	}	
	
	public static IDAO createDAOImpl(DefaultTransactionContext tc, String sqlStmt, Class daoClass) throws Exception{

		return (IDAO)Proxy.newProxyInstance(
			daoClass.getClassLoader(),
			new Class[]{daoClass},
			new ConnectiveDAO(tc, true, sqlStmt, daoClass)
		);		
	}	

	public static IDAO createDAOImpl(final String dataSourceJndiName, final DefaultTransactionContext tc, String sqlStmt, Class daoClass) throws Exception{
		
		ConnectionFactory staticConnectionFactory = new ConnectionFactory(){

			public Connection getConnection() throws Exception {
				// TODO Auto-generated method stub
				return tc.createManagedExternalConnection(dataSourceJndiName);
			}
			
		};
		
		return (IDAO)Proxy.newProxyInstance(
			daoClass.getClassLoader(),
			new Class[]{daoClass},
			new ConnectiveDAO(
				staticConnectionFactory, 
				true, 
				sqlStmt, 
				daoClass
			)
		);		
	}	

}	