package org.uengine.persistence.analysis;

import org.uengine.persistence.AbstractDAOType;
import org.uengine.persistence.DAOType;
import org.uengine.persistence.dao.DAOFactory;
import org.uengine.processmanager.TransactionContext;

import org.uengine.util.UEngineUtil;
import org.uengine.util.dao.ConnectiveDAO;

public class ResourceDimensionDAOType extends AbstractDAOType {
	
	public static ResourceDimensionDAOType getInstance(TransactionContext ptc) {
		return (ResourceDimensionDAOType)getInstance(ResourceDimensionDAOType.class, ptc);
	}
	
	public ResourceDimensionDAO createDAOImpl(String sql) throws Exception {
		ResourceDimensionDAO procInsDAO = (ResourceDimensionDAO)ConnectiveDAO.createDAOImpl(getTransactionContext(), sql, ResourceDimensionDAO.class);
		return procInsDAO;
	}

	public ResourceDimensionDAO createDAOForInsert() throws Exception {
		ResourceDimensionDAO procInsDAO = createDAOImpl(null);
		procInsDAO.getImplementationObject().setAutoSQLGeneration(true);
		procInsDAO.getImplementationObject().setTableName("BPM_RSRC_DIM");
		
		return procInsDAO;
	}
	
	public boolean existAnyResourceWhereRsrc_Id(String rsrc_Id) throws Exception {
		ResourceDimensionDAO dao = createDAOImpl("select count(rsrc_id) as cnt from BPM_RSRC_DIM where RSRC_ID=?rsrc_id");
		dao.setRsrc_Id(rsrc_Id);
		dao.select();
		if(dao.next()){
			return ((Number)dao.get("CNT")).intValue() > 0;
		}else
			return false;
	}

}
