package org.uengine.persistence;

import org.uengine.persistence.dao.DAOFactory;
import org.uengine.processmanager.DefaultTransactionContext;

public class AbstractDAOType implements DAOType{
	
	DefaultTransactionContext transactionContext;
		public void setTransactionContext(DefaultTransactionContext tc){
			transactionContext = tc;
		}
		public DefaultTransactionContext getTransactionContext() {
			return transactionContext;
		}

	protected static DAOType getInstance(Class clsType, DefaultTransactionContext ptc){
		
		//try own dbms' strategy first. 
		try {
			Class actualDAOTypeCls = 
				DAOFactory.getInstance(ptc).getDAOTypeClass(clsType);
			
			DAOType pidt = (DAOType)actualDAOTypeCls.newInstance();
			pidt.setTransactionContext(ptc);
			
			return pidt;
			
		} catch (Exception e) {
			try{
				DAOType pidt = (DAOType)clsType.newInstance();
				pidt.setTransactionContext(ptc);
				return pidt;
			}catch(Exception ex){
				throw new RuntimeException(ex);
			}

		}		
		
	}
	
}
