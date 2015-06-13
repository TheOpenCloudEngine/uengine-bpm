package org.uengine.persistence;

import org.uengine.persistence.dao.DAOFactory;
import org.uengine.processmanager.TransactionContext;
import org.uengine.util.UEngineUtil;

public class AbstractDAOType implements DAOType{
	
	TransactionContext transactionContext;
		public void setTransactionContext(TransactionContext tc){
			transactionContext = tc;
		}
		public TransactionContext getTransactionContext() {
			return transactionContext;
		}

	protected static DAOType getInstance(Class clsType, TransactionContext ptc){
		
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
