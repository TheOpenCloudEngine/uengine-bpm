package org.uengine.persistence.analysis;

import org.uengine.persistence.AbstractDAOType;
import org.uengine.persistence.DAOType;
import org.uengine.persistence.dao.DAOFactory;
import org.uengine.processmanager.TransactionContext;

import org.uengine.util.UEngineUtil;
import org.uengine.util.dao.ConnectiveDAO;

public class PerformanceFactDAOType extends AbstractDAOType {
	
	public static PerformanceFactDAOType getInstance(TransactionContext ptc) {
		return (PerformanceFactDAOType)getInstance(PerformanceFactDAOType.class, ptc);
	}
	
	public PerformanceFactDAO createDAOImpl(String sql) throws Exception {
		PerformanceFactDAO dao = (PerformanceFactDAO)ConnectiveDAO.createDAOImpl(getTransactionContext(), sql, PerformanceFactDAO.class);
		return dao;
	}

	public PerformanceFactDAO createDAOForInsert() throws Exception {
		PerformanceFactDAO dao = createDAOImpl(null);
		dao.getImplementationObject().setAutoSQLGeneration(true);
		dao.getImplementationObject().setTableName("BPM_PRFM_FACT_2006");
		
		return dao;
	}

}
