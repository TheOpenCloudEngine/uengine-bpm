/*
 * Created on 2004. 11. 3.
 */
package org.uengine.persistence.dao;

import java.util.Calendar;
import java.util.Map;

import org.uengine.persistence.DAOType;
import org.uengine.processmanager.ProcessTransactionContext;
import org.uengine.util.UEngineUtil;
import org.uengine.util.dao.*;
import org.uengine.kernel.GlobalContext;
import org.uengine.kernel.UEngineException;

/**
 * @author Jinyoung Jang
 * @author Jong-uk Jeong
 */

public abstract class DAOFactory{
	public static String USE_CLASS_NAME;
	
	ConnectionFactory connectionFactory;
		public ConnectionFactory getConnectionFactory() {
			return connectionFactory;
		}
		public void setConnectionFactory(ConnectionFactory factory) {
			connectionFactory = factory;
		}

	abstract public WorkListDAO findWorkListDAOByEndpoint(Map options) throws Exception;
	abstract public WorkListDAO findWorkListDAOByTaskId(Map options) throws Exception;
	abstract public WorkListDAO createWorkListDAOForInsertCall(Map options) throws Exception;
	abstract public WorkListDAO createWorkListDAOForUpdate(Map options) throws Exception;
	abstract public KeyGeneratorDAO createKeyGenerator(String forWhat, Map options) throws Exception;
	abstract public Calendar getNow() throws Exception;

	abstract public String getSequenceSql(String seqName) throws Exception;
	
	abstract public String getDBMSProductName() throws Exception;

	public static DAOFactory getInstance(ConnectionFactory tc){
//		if(tc == null)
//			throw new IllegalArgumentException("ConnectionFactory or DefaultTransactionContext is null. ConnectionFactory should be provided to create the DAOFactory.");

		DAOFactory daoFactory;
		try{
			USE_CLASS_NAME = GlobalContext.getPropertyString("daofactory.class");
			daoFactory = (DAOFactory)Thread.currentThread().getContextClassLoader().loadClass(USE_CLASS_NAME).newInstance();
		}catch(Exception e){
			throw new RuntimeException("No DAO Factory (database product setting) is specified. Please set the entry 'daofactory.class' in the uengine.properteis", e);
		}
		
		daoFactory.setConnectionFactory(tc);
		
		return daoFactory;
	}
	
	/**
	 * @deprecated DAOFactory should be provided with ConnectionFactory.
	 */

	public static DAOFactory getInstance(){
		return getInstance(null);
	}
	
	protected Object create(Class whatKind, String sqlStmt) throws Exception{
		return ConnectiveDAO.createDAOImpl(getConnectionFactory(), sqlStmt, whatKind);
	}
	
	public Class getDAOTypeClass(Class clsType) throws UEngineException{
		Class actualDAOTypeCls;
		try {
			actualDAOTypeCls = Thread.currentThread().getContextClassLoader().loadClass(
					clsType.getPackage().getName() + 
					"." +
					getDBMSProductName() + 
					UEngineUtil.getClassNameOnly(clsType)
				);
			
			return actualDAOTypeCls;

		} catch (Exception e) {
			throw new UEngineException("Failed to find proper DAO type: ", e);
		}			
			
	}
	
}
