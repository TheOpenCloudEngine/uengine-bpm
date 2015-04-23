package org.uengine.persistence.processdefinition;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import javax.ejb.CreateException;
import javax.ejb.EJBException;
import javax.ejb.FinderException;
import javax.ejb.RemoveException;

import org.uengine.kernel.TransactionListener;
import org.uengine.kernel.UEngineException;
import org.uengine.processmanager.ProcessTransactionContext;
import org.uengine.processmanager.TransactionContext;
import org.uengine.util.dao.ConnectiveDAO;
import org.uengine.util.dao.DAO2ArrayListAdapter;

public class ProcessDefinitionRepositoryHomeLocalImpl implements ProcessDefinitionRepositoryHomeLocal{

	TransactionContext tc;
	
	public ProcessDefinitionRepositoryHomeLocalImpl(TransactionContext tc){
		this.tc = tc;
	}
	
	public ProcessDefinitionRepositoryLocal create(Long id) throws CreateException {
		final ProcessDefinitionDAO pd;
		try {
			pd = (ProcessDefinitionDAO) tc.createSynchronizedDAO("BPM_PROCDEF", "DefId", id, ProcessDefinitionDAO.class);
			
/*			pd = (ProcessDefinitionDAO)ConnectiveDAO.createDAOImpl(tc, null, ProcessDefinitionDAO.class);
			pd.getImplementationObject().setTableName("BPM_PROCDEF");
			pd.setDefId(id);
			
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
				
			});*/
			return pd;
		} catch (Exception e) {
			throw new CreateException(e.getMessage());
		}
	}

	public ProcessDefinitionRepositoryLocal findByPrimaryKey(Long id) throws FinderException {
		final ProcessDefinitionDAO pd;
		try {
			pd = (ProcessDefinitionDAO) tc.findSynchronizedDAO("BPM_PROCDEF", "DefId", id, ProcessDefinitionDAO.class);

			
			/*			pd = (ProcessDefinitionDAO)ConnectiveDAO.createDAOImpl(tc, "select * from bpm_procdef where defid=?defid", ProcessDefinitionDAO.class);
			pd.setDefId(id);
			pd.getImplementationObject().setKeyField("DEFID");

			pd.select();
			if(!pd.next()) throw new UEngineException("No Such process definition where id is " + id);
			tc.addTransactionListener(new TransactionListener(){

				public void beforeCommit(TransactionContext tx) throws Exception {
					pd.getImplementationObject().setTableName("bpm_procdef");
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
				
			});*/
			return pd;
		} catch (Exception e) {
			throw new FinderException(e.getMessage());
		}
	}

	public Collection findAllProcessDefinitions() throws FinderException {
		final ProcessDefinitionDAO pd;
		try {
			pd = (ProcessDefinitionDAO)ConnectiveDAO.createDAOImpl(tc, "select * from bpm_procdef where isdeleted = 0", ProcessDefinitionDAO.class);
			pd.select();
			
			ArrayList proxyList = new DAO2ArrayListAdapter(pd);
			
			return proxyList;

		} catch (Exception e) {
			throw new FinderException(e.getMessage());
		}
	}

	public Collection findByFolder(Long folderId) throws FinderException {
		// TODO Auto-generated method stub
		return new ArrayList();
	}
	
	public ProcessDefinitionRepositoryLocal findByName(String name) throws FinderException {
		final ProcessDefinitionDAO pd;
		try {
			pd = (ProcessDefinitionDAO)ConnectiveDAO.createDAOImpl(tc, "select * from bpm_procdef where name=?name and ISDELETED=0", ProcessDefinitionDAO.class);
			pd.setName(name);
			pd.getImplementationObject().setKeyField("DEFID");

			pd.select();
			if(!pd.next()) throw new UEngineException("No Such Process Definition named '" + name +"'.");
			tc.addTransactionListener(new TransactionListener(){

				public void beforeCommit(TransactionContext tx) throws Exception {
					pd.getImplementationObject().setTableName("bpm_procdef");
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
			return pd;
		} catch (Exception e) {
			throw new FinderException(e.getMessage());
		}
	}
	
	public ProcessDefinitionRepositoryLocal findByNameSameLevel(String name, Long parent, String objType) throws FinderException {
		final ProcessDefinitionDAO pd;
		try {
			pd = (ProcessDefinitionDAO)ConnectiveDAO.createDAOImpl(tc, "select * from bpm_procdef where name=?name and parentfolder=?parentfolder and objtype=?objtype and ISDELETED=0", ProcessDefinitionDAO.class);
			pd.setName(name);
			pd.setParentFolder(parent);
			pd.setObjType(objType);
			pd.getImplementationObject().setKeyField("DEFID");

			pd.select();
			if(!pd.next()) throw new UEngineException("No Such Process Definition named '" + name +"'.");
			tc.addTransactionListener(new TransactionListener(){

				public void beforeCommit(TransactionContext tx) throws Exception {
					pd.getImplementationObject().setTableName("bpm_procdef");
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
			return pd;
		} catch (Exception e) {
			throw new FinderException(e.getMessage());
		}
	}

	public ProcessDefinitionRepositoryLocal findByAlias(String alias) throws FinderException {
		final ProcessDefinitionDAO pd;
		try {
			pd = (ProcessDefinitionDAO)ConnectiveDAO.createDAOImpl(tc, "select * from bpm_procdef where alias=?alias and isdeleted = 0", ProcessDefinitionDAO.class);
			pd.setAlias(alias);
			pd.getImplementationObject().setKeyField("DEFID");

			pd.select();
			if(!pd.next()) throw new UEngineException("No Such Process Definition aliased '" + alias +"'.");
			tc.addTransactionListener(new TransactionListener(){

				public void beforeCommit(TransactionContext tx) throws Exception {
					pd.getImplementationObject().setTableName("bpm_procdef");
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
			return pd;
		} catch (Exception e) {
			throw new FinderException(e.getMessage());
		}
	}

	public void remove(Object arg0) throws RemoveException, EJBException {
		// TODO Auto-generated method stub
	}
	

}
