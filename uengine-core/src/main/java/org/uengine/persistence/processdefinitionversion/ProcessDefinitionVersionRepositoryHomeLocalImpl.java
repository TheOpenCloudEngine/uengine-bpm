package org.uengine.persistence.processdefinitionversion;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import javax.ejb.CreateException;
import javax.ejb.EJBException;
import javax.ejb.FinderException;
import javax.ejb.RemoveException;

import org.uengine.kernel.TransactionListener;
import org.uengine.kernel.UEngineException;
import org.uengine.persistence.processdefinition.ProcessDefinitionDAO;
import org.uengine.persistence.processdefinition.ProcessDefinitionRepositoryLocal;
import org.uengine.processmanager.ProcessTransactionContext;
import org.uengine.processmanager.TransactionContext;
import org.uengine.util.dao.ConnectiveDAO;
import org.uengine.util.dao.DAO2ArrayListAdapter;

public class ProcessDefinitionVersionRepositoryHomeLocalImpl implements ProcessDefinitionVersionRepositoryHomeLocal{

	
	TransactionContext tc;
	
	public ProcessDefinitionVersionRepositoryHomeLocalImpl(TransactionContext tc){
		this.tc = tc;
	}
	
	public ProcessDefinitionVersionRepositoryLocal create(Long id) throws CreateException {
		final ProcessDefinitionVersionDAO pd;
		try {
			pd = (ProcessDefinitionVersionDAO) tc.createSynchronizedDAO("BPM_PROCDEFVER", "DefVerId", id, ProcessDefinitionVersionDAO.class);
			
/*			pd = (ProcessDefinitionVersionDAO)ConnectiveDAO.createDAOImpl(tc, null, ProcessDefinitionVersionDAO.class);
			pd.getImplementationObject().setTableName("BPM_PROCDEFVER");
			pd.setDefVerId(id);
			tc.addTransactionListener(new TransactionListener(){

				public void beforeCommit(TransactionContext tx) throws Exception {
					pd.getImplementationObject().createInsertSql();
					pd.insert();
				}

				public void beforeRollback(TransactionContext tx) throws Exception {
					// TODO Auto-generated method stub
					
				}

				public void afterCommit(TransactionContext tx) throws Exception {
					// TODO Auto-generated method stub
					
				}

				public void afterRollback(TransactionContext tx) throws Exception {
					// TODO Auto-generated method stub
					
				}
				
			});
*/			return pd;
		} catch (Exception e) {
			throw new CreateException(e.getMessage());
		}
	}

	public ProcessDefinitionVersionRepositoryLocal findByPrimaryKey(Long id) throws FinderException {
		final ProcessDefinitionVersionDAO pd;
		try {
			pd = (ProcessDefinitionVersionDAO) tc.findSynchronizedDAO("BPM_PROCDEFVER", "DefVerId", id, ProcessDefinitionVersionDAO.class);
			
/*			pd = (ProcessDefinitionVersionDAO)ConnectiveDAO.createDAOImpl(tc, "select * from bpm_procdefver where defverid=?defverid", ProcessDefinitionVersionDAO.class);
			pd.setDefVerId(id);
			pd.getImplementationObject().setKeyField("DEFVERID");

			pd.select();
			if(!pd.next()) throw new UEngineException("No Such Process Definition Version where defVerId = " + id);

			tc.addTransactionListener(new TransactionListener(){

				public void beforeCommit(TransactionContext tx) throws Exception {
					pd.getImplementationObject().setTableName("bpm_procdefver");
					pd.getImplementationObject().createUpdateSql();
					pd.update();
				}

				public void beforeRollback(TransactionContext tx) throws Exception {
					// TODO Auto-generated method stub
					
				}

				public void afterCommit(TransactionContext tx) throws Exception {
					// TODO Auto-generated method stub
					
				}

				public void afterRollback(TransactionContext tx) throws Exception {
					// TODO Auto-generated method stub
					
				}
				
			});
*/			return pd;
		} catch (Exception e) {
			throw new FinderException(e.getMessage());
		}
	}
	
	public Collection findAllVersions(Long id) throws FinderException {
		final ProcessDefinitionVersionDAO pd;
		try {
			pd = (ProcessDefinitionVersionDAO)ConnectiveDAO.createDAOImpl(tc, "select * from bpm_procdefver where defid=?defid order by defverid", ProcessDefinitionVersionDAO.class);
			pd.setDefId(id);
			pd.select();
			
			ArrayList proxyList = new DAO2ArrayListAdapter(pd);
			
			return proxyList;

		} catch (Exception e) {
			e.printStackTrace();
			throw new FinderException(e.getMessage());
		}
	}

	public Collection findByDefinitionAndVersion(Long id, Long version) throws FinderException {
		final ProcessDefinitionVersionDAO pd;
		try {
			pd = (ProcessDefinitionVersionDAO)ConnectiveDAO.createDAOImpl(tc, "select * from bpm_procdefver where defid=?defid and ver = ?ver", ProcessDefinitionVersionDAO.class);
			pd.setDefId(id);
			pd.setVer(version);
			pd.select();
			
			ArrayList proxyList = new DAO2ArrayListAdapter(pd);
			
			pd.getImplementationObject().setTableName("BPM_PROCDEFVER");
			pd.getImplementationObject().setKeyField("DEFVERID");
			tc.addTransactionListener(new TransactionListener(){

				public void beforeCommit(TransactionContext tx) throws Exception {
					pd.getImplementationObject().createUpdateSql();
					pd.update();
				}

				public void beforeRollback(TransactionContext tx) throws Exception {
					// TODO Auto-generated method stub
					
				}

				public void afterCommit(TransactionContext tx) throws Exception {
					// TODO Auto-generated method stub
					
				}

				public void afterRollback(TransactionContext tx) throws Exception {
					// TODO Auto-generated method stub
					
				}
				
			});
			
			return proxyList;

		} catch (Exception e) {
			throw new FinderException(e.getMessage());
		}
	}

	public void remove(Object arg0) throws RemoveException, EJBException {
		// TODO Auto-generated method stub
		
	}

}
