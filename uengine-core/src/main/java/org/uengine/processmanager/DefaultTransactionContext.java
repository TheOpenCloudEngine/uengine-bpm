package org.uengine.processmanager;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
//import java.rmi.RemoteException;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.naming.InitialContext;
import javax.transaction.Status;
//import javax.transaction.SystemException;
//import javax.transaction.Transaction;
//import javax.transaction.TransactionManager;
import javax.transaction.UserTransaction;
//import javax.transaction.xa.XAException;
//import javax.transaction.xa.XAResource;
//import javax.transaction.xa.Xid;

//import org.apache.geronimo.transaction.manager.NamedXAResource;
//import org.apache.kandula.coordinator.CoordinationContext;
//import org.apache.kandula.coordinator.at.AT2PCStatus;
//import org.apache.kandula.coordinator.at.AbstractParticipant;
//import org.apache.kandula.coordinator.at.TransactionManagerImpl;
//import org.apache.kandula.geronimo.Bridge;
//import org.enhydra.jdbc.standard.StandardXAConnection;
import org.uengine.kernel.ActivityInstanceContext;
import org.uengine.kernel.GlobalContext;
import org.uengine.kernel.ProcessInstance;
import org.uengine.kernel.ReleaseResourceListener;
import org.uengine.kernel.TransactionListener;
import org.uengine.kernel.UEngineException;
import org.uengine.util.ForLoop;
import org.uengine.util.dao.ConnectionFactory;
import org.uengine.util.dao.ConnectiveDAO;
import org.uengine.util.dao.DataSourceConnectionFactory;
import org.uengine.util.dao.IDAO;

/**
 * @author  <a href="mailto:ghbpark@hanwha.co.kr">Sungsoo Park</a>
 * @version    $Id: DefaultTransactionContext.java,v 1.3 2012/10/23 07:11:09 pongsor Exp $
 */
public class DefaultTransactionContext implements ConnectionFactory, TransactionContext {
		
	ConnectionFactory delegatedConnectionFactory;

	UserTransaction userTransaction;

	static Hashtable connectionPool = new Hashtable();
	
	String connectionGetterStackDump = null;
		public String getConnectionGetterStackDump() {
			return connectionGetterStackDump;
		}
		public void setConnectionGetterStackDump(String connectionGetterStackDump) {
			this.connectionGetterStackDump = connectionGetterStackDump;
		}
	
	protected DefaultTransactionContext(ConnectionFactory delegatedConnectionFactory){
		this.delegatedConnectionFactory = delegatedConnectionFactory;
	}
	
	List transactionListeners = new ArrayList();
		@Override
		public void addTransactionListener(TransactionListener tl){
			transactionListeners.add(tl);
		}
		@Override
		public List getTransactionListeners(){
			return transactionListeners;
		}
		
	List releaseResourceListeners = new ArrayList();
		public void addReleaseResourceListeners(ReleaseResourceListener rrl){
			releaseResourceListeners.add(rrl);
		}
		public List getReleaseResourceListeners(){
			return releaseResourceListeners;
		}

	boolean isManagedTransaction;
		public boolean isManagedTransaction() {
			return isManagedTransaction;
		}
		public void setManagedTransaction(boolean isManagedTransaction) {
			this.isManagedTransaction = isManagedTransaction;
		}
	
	boolean autoCloseConnection = true;
		public boolean isAutoCloseConnection() {
			return autoCloseConnection;
		}
		public void setAutoCloseConnection(boolean autoCloseConnection) {
			this.autoCloseConnection = autoCloseConnection;
		}	


    transient List executedActivities = new ArrayList();
		public List getExecutedActivityInstanceContextsInTransaction() {
			return executedActivities;
		}
		public void addExecutedActivityInstanceContext(ActivityInstanceContext aic){
			if(!executedActivities.contains(aic))
				executedActivities.add(aic);
		}
			
	transient Map processInstances = new Hashtable();
		public void registerProcessInstance(ProcessInstance pi){
			processInstances.put(pi.getInstanceId(), pi);
		}
		public ProcessInstance getProcessInstanceInTransaction(String instanceId){
			return (ProcessInstance)processInstances.get(instanceId);
		}
		public Map getProcessInstancesInTransaction(){
			return processInstances;
		}
		
	transient ProcessInstance temporaryInstance;
		public ProcessInstance getTemporaryInstance() {
			return temporaryInstance;
		}
		public void setTemporaryInstance(ProcessInstance temporaryInstance) {
			this.temporaryInstance = temporaryInstance;
		}
		
	transient Map sharedContexts = new Hashtable();
		@Override
		public Object getSharedContext(String contextKey) {
			if(!sharedContexts.containsKey(contextKey)) return null;
			
			return sharedContexts.get(contextKey);
		}
		@Override
		public void setSharedContext(String contextKey, Object value) {
			if(value == null) 
				sharedContexts.remove(contextKey);
			else
				sharedContexts.put(contextKey, value);
		}		
		public IDAO createSynchronizedDAO(String tableName, String keyFieldName, Object keyFieldValue, Class daoType) throws Exception{
			return createSynchronizedDAO(tableName, keyFieldName, keyFieldValue, daoType, true);
		}
		public IDAO findSynchronizedDAO(String tableName, String keyFieldName, Object keyFieldValue, Class daoType) throws Exception{

			String sharedContextKey = tableName + "@" + keyFieldValue;
			sharedContextKey = sharedContextKey.toUpperCase();
			
			if(getSharedContext(sharedContextKey)!=null){
				IDAO cachedOne = (IDAO)getSharedContext(sharedContextKey);
				
				try{
					cachedOne.first();
				}catch(Exception e){}
				
				return cachedOne;
			}
			
			return createSynchronizedDAO(tableName, keyFieldName, keyFieldValue, daoType, false);
		}
		private IDAO createSynchronizedDAO(String tableName, String keyFieldName, Object keyFieldValue, Class daoType, final boolean isNew) throws Exception{

			final IDAO dao = ConnectiveDAO.createDAOImpl(this, null, daoType);
			dao.getImplementationObject().setTableName(tableName);
			dao.getImplementationObject().setKeyField(keyFieldName);
			dao.set(keyFieldName, keyFieldValue);

			if(!isNew){
				dao.getImplementationObject().createSelectSql();
				dao.select();
				if(!dao.next()) throw new UEngineException("No Such "+tableName+" where "+keyFieldName+" is " + keyFieldValue);
			}
			
			String sharedContextKey = tableName + "@" + keyFieldValue;
			sharedContextKey = sharedContextKey.toUpperCase();
			
			setSharedContext(sharedContextKey, dao);
			addTransactionListener(new TransactionListener(){

				public void beforeCommit(TransactionContext tx) throws Exception {
					if(isNew)
						dao.getImplementationObject().createInsertSql();
					else
						dao.getImplementationObject().createUpdateSql();
					dao.update();
				}

				public void beforeRollback(TransactionContext tx) throws Exception {
				}

				public void afterCommit(TransactionContext tx) throws Exception {
					// TODO Auto-generated method stub
					
				}

				public void afterRollback(TransactionContext tx) throws Exception {
					// TODO Auto-generated method stub
					
				}
				
			});
			
			return dao;
		}
		
	protected void beforeCommit() throws Exception{
//		for(Iterator iter = transactionListeners.iterator(); iter.hasNext();){
//			TransactionListener tl = (TransactionListener)iter.next();
//			tl.beforeCommit(this);
//		}
		
		final DefaultTransactionContext tx = this;
		
		ForLoop beforeCommitLoop = new ForLoop(){

			public void logic(Object target) {
				
				TransactionListener tl = (TransactionListener)target;
				try {
					tl.beforeCommit(tx);
				} catch (Exception e) {
					e.printStackTrace();
					throw new RuntimeException(e);
				}				
			}			
		};
		
		beforeCommitLoop.run((ArrayList)transactionListeners);
	}

	protected void afterCommit() throws Exception{
//		for(Iterator iter = transactionListeners.iterator(); iter.hasNext();){
//			TransactionListener tl = (TransactionListener)iter.next();
//			tl.afterCommit(this);
//		}
		final DefaultTransactionContext tx = this;

		ForLoop afterCommitLoop = new ForLoop(){

			public void logic(Object target) {

				TransactionListener tl = (TransactionListener)target;
				try {
					tl.afterCommit(tx);
				} catch (Exception e) {
					throw new RuntimeException(e);
				}				
			}			
		};
		
		afterCommitLoop.run((ArrayList)transactionListeners);
	}

	protected void afterRollback() throws Exception{
//		for(Iterator iter = transactionListeners.iterator(); iter.hasNext();){
//			TransactionListener tl = (TransactionListener)iter.next();
//			tl.afterRollback(this);
//		}
		
		final DefaultTransactionContext tx = this;

		ForLoop afterRollBackLoop = new ForLoop(){

			public void logic(Object target) {

				TransactionListener tl = (TransactionListener)target;
				try {
					tl.afterRollback(tx);
				} catch (Exception e) {
					throw new RuntimeException(e);
				}				
			}			
		};
		
		afterRollBackLoop.run((ArrayList)transactionListeners);
	}
	
	protected void beforeRollback() throws Exception{
//		for(Iterator iter = transactionListeners.iterator(); iter.hasNext();){
//			TransactionListener tl = (TransactionListener)iter.next();
//			tl.beforeRollback(this);
//		}
		
		final DefaultTransactionContext tx = this;

		ForLoop beforeRollBackLoop = new ForLoop(){

			public void logic(Object target) {

				TransactionListener tl = (TransactionListener)target;
				try {
					tl.beforeRollback(tx);
				} catch (Exception e) {
					throw new RuntimeException(e);
				}				
			}			
		};
		
		beforeRollBackLoop.run((ArrayList)transactionListeners);
		
	}
	
	@Override
	public void commit() throws Exception {
		try {
			beforeCommit();
						
			if(connection != null && (getConnectionFactory()==null || !isManagedTransaction())){
				
				//added for ws-at: 
				//	when the transaction is associated with WebServices transaction, won't commit by the connection's signature rather use wstm's one.
//				if(wsCoordinationContext!=null){
//					wstm.commit();
//				}else
					connection.commit();
		
			}
			
			if(userTransaction != null && GlobalContext.useAutoUserTransactionDemarcation){
				userTransaction.commit();
			}
			
		} catch (Exception e) {
			throw e;
		} finally {
			releaseResources();
			afterCommit();
		}
	}
	
	@Override
	public void rollback() throws Exception{
		try {
			beforeRollback();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		try {
			if(connection!=null && (getConnectionFactory()==null || !isManagedTransaction())){
//				//added for ws-at:
//				//	when the transaction is associated with WebServices transaction, won't rollback by the connection's signature rather use wstm's one.
//				if(wsCoordinationContext!=null){
//					wstm.rollback();
//
//					try {
//						txm.getTransaction().delistResource(xaconn.getXAResource(), XAResource.TMSUSPEND);
//					}catch(Exception e1){}
//
//				}else
					connection.rollback();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		try{
			if(userTransaction != null && GlobalContext.useAutoUserTransactionDemarcation){
				userTransaction.rollback();
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		
		try {
			releaseResources();
			afterRollback();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void releaseResources() throws Exception{
		releaseResources(false);
	}

	@Override
	public void addDebugInfo(String s) {

	}

	protected void releaseResources(boolean fromRemoveCall) throws Exception{
		
		final DefaultTransactionContext tx = this;

		ForLoop beforeRealeaseLoop = new ForLoop(){

			public void logic(Object target) {

				ReleaseResourceListener tl = (ReleaseResourceListener)target;
				try {
					tl.beforeReleaseResource(tx);
				} catch (Exception e) {
					throw new RuntimeException(e);
				}				
			}			
		};
		
		beforeRealeaseLoop.run((ArrayList)releaseResourceListeners);
		
		
		if(connection != null && !connection.isClosed()){
			if(fromRemoveCall && !isManagedTransaction()){ 
//				(new UEngineException("This thread tries to release resources that is not applied(committed) or cancelled(rolled-back).")).printStackTrace();
				
				
			}
 
			if(!isManagedTransaction()){
//				if(wsCoordinationContext!=null){
//					//added for ws-at
//					try {
//						if(getConnectionFactory() instanceof ClosableConnectionFactory){
//							((ClosableConnectionFactory)getConnectionFactory()).close();
//						}
//					} catch (Exception e) {
//						e.printStackTrace();
//					}
//
//					try {
//						txm.commit();
//					} catch (Exception e) {
//					}
//					//end
//				}else{
					try{
						if(isAutoCloseConnection())
							connection.close();
					}catch(Exception e){
						e.printStackTrace();
					}
//				}
			}

		}

		if("true".equals(GlobalContext.CONNECTION_LEAKAGE_DETECT))
			connectionPool.remove(this);
		
		connection = null;

	}
	
	public static void reportConnectionHasNotBeenReleased(){
		Set clonedSet = new HashSet();
		clonedSet.addAll(connectionPool.keySet());
		Iterator keyIter = clonedSet.iterator();
		
		while(keyIter.hasNext()) {
			DefaultTransactionContext key = (DefaultTransactionContext)keyIter.next();
			//Connection connection =(Connection)connectionPool.get(key);
			System.out.println(key.getConnectionGetterStackDump());
		}
	}

//	//added for ws-at
//	TransactionManagerImpl wstm = TransactionManagerImpl.getInstance();
//	CoordinationContext wsCoordinationContext;
//	TransactionManager txm;
//	StandardXAConnection xaconn;
//	//end
	
	Connection connection;
	public Connection getConnection() throws Exception {
		if(connection == null || connection.isClosed()){
			if(connectionGetterStackDump==null)
				try {
					StringWriter sw = new StringWriter();
					PrintWriter pw = new PrintWriter(sw);
					(new Exception("Connection leakage code has been detected. uEngine closes the connection:")).printStackTrace(pw);
					connectionGetterStackDump = sw.toString();
					
					if("true".equals(GlobalContext.CONNECTION_LEAKAGE_DETECT)){
						connectionPool.put(this, this);
					}
					//System.out.println("@@@@@@@@@@@@@@@@@@@" + connectionGetterStackDump);
					sw.close();
					pw.close();
				} catch (IOException e) {
				}

				
			if(GlobalContext.useAutoUserTransactionDemarcation){
				try{
					InitialContext jndiContext = new InitialContext();
					userTransaction = (UserTransaction) jndiContext.lookup(GlobalContext.USERTRANSACTION_JNDI_NAME);
					
					if(userTransaction.getStatus() != Status.STATUS_ACTIVE)
						userTransaction.begin();
				}catch(Exception e){
					throw new RuntimeException(e);
				}
			}
			
//			//added for ws-at
//			if(getConnectionFactory() instanceof StandardXAConnection){
//				xaconn = (StandardXAConnection)getConnectionFactory();
//
//				//how can it be guarrantted wstm and txm can be correlated?
//				wsCoordinationContext = wstm.begin();
//				txm = Bridge.getInstance().getTM();
//
//				txm.begin();
//				Transaction tx = txm.suspend();
//				txm.resume(tx);
//				((StandardXAConnection)getConnectionFactory()).setTransactionManager(txm);
//				wstm.getTransaction().enlistParticipant(true, new XA_Internal(tx));
//
//			}
			
			connection = getConnectionFactory().getConnection();
			
			if(!isManagedTransaction()){
				connection.setAutoCommit(false);
			}/*else
				connection.setAutoCommit(true);
			*/
			
		}
		
		return connection;
	}
	
	public ConnectionFactory getConnectionFactory(){
		return delegatedConnectionFactory;
	}	
	
	public void setConnectionFactory(ConnectionFactory cf) {
		this.delegatedConnectionFactory = cf;
	}
	
	public Connection createManagedExternalConnection(DataSourceConnectionFactory connFactory) throws Exception{
		String dataSourceJndiName = connFactory.getDataSourceJndiName();
		final String sharedContextName = "ConnectionFromDatasource:" + dataSourceJndiName;
		
		boolean uEngineShouldManageTheTransaction = !isManagedTransaction();

		Connection con = null;
		
		if(uEngineShouldManageTheTransaction){
			con = (Connection)getSharedContext(sharedContextName);
		}
		
		if(con == null){
			con = connFactory.getConnection();
			
			if(uEngineShouldManageTheTransaction){
				con.setAutoCommit(false);
					
				setSharedContext(sharedContextName, con);
	
				addTransactionListener(
					new TransactionListener(){
	
						public void beforeCommit(TransactionContext tx) throws Exception {
							Connection con = (Connection)tx.getSharedContext(sharedContextName);
							try{
								con.commit();
							}catch(Exception e){
								throw new UEngineException("Error when to delayed transaction commit for external datasource", e);
							}finally{
								con.close();
							}
						}
	
						public void beforeRollback(TransactionContext tx) throws Exception {
							Connection con = (Connection)tx.getSharedContext(sharedContextName);
							try{
								con.rollback();
							}catch(Exception e){
								throw new UEngineException("Error when to delayed transaction rollback for external datasource" , e);
							}finally{
								con.close();
							}
						}

						public void afterCommit(TransactionContext tx) throws Exception {
							// TODO Auto-generated method stub
							
						}

						public void afterRollback(TransactionContext tx) throws Exception {
							// TODO Auto-generated method stub
							
						}
			
					}
				);	
			}
		}
		

		return con;
	}

	public Connection createManagedExternalConnection(String dataSourceJndiName) throws Exception{
		
		DataSourceConnectionFactory connFactory = new DataSourceConnectionFactory();
		connFactory.setDataSourceJndiName(dataSourceJndiName);
		return createManagedExternalConnection(connFactory);

	}

	protected void finalize() throws Throwable {
		if( connection != null && !connection.isClosed() ){			
			if(connectionGetterStackDump!=null){
				System.out.println(connectionGetterStackDump);
			}
		}
		
		releaseResources();
		super.finalize();
	}


//	public class XA_Internal extends AbstractParticipant implements NamedXAResource {
//
//		private int timeout = Integer.MAX_VALUE;
//
//		private String id;
//
//		private Transaction tx;
//
//		private Bridge bridge1 = Bridge.getInstance();
//
//		private org.apache.geronimo.transaction.manager.TransactionManagerImpl gerotm = (org.apache.geronimo.transaction.manager.TransactionManagerImpl) bridge1.getTM();
//
//		private boolean bridged = false;
//
//		public XA_Internal(Transaction tx) throws RemoteException {
//			this.tx = tx;
//			try {
//				tx.enlistResource(this);
//			} catch (Exception e) {
//				throw new RuntimeException(e);
//			}
//		}
//
//		public void commit(Xid arg0, boolean arg1) throws XAException {
////			System.err.println("<<commit:"+arg0);
//			if (bridged) {
//				forget();
//				try {
//					getCoordinator().committedOperation(null);
//				} catch (Exception e) {
//				}
//			}
//		}
//
//		public void end(Xid arg0, int arg1) throws XAException {
////			System.err.println(">>end:"+arg0);
//		}
//
//		public void forget(Xid arg0) throws XAException {
////			System.err.println(">>forget:"+arg0);
//		}
//
//		public String getName() {
//			return getClass().getName();
//		}
//
//		public int getTransactionTimeout() throws XAException {
//			return timeout;
//		}
//
//		public boolean isSameRM(XAResource rm) throws XAException {
//			return this == rm;
//		}
//
//		public int prepare(Xid arg0) throws XAException {
////			System.err.println(">>prepare:"+arg0);
//			if (bridged) {
//				forget();
//				try {
//					getCoordinator().abortedOperation(null);
//				} catch (Exception e) {
//				}
//				throw new XAException();
//			} else
//				return XAResource.XA_RDONLY;
//		}
//
//		public Xid[] recover(int arg0) throws XAException {
//			System.err.println(">>recover:"+arg0);
//			return null;
//		}
//
//		public void rollback(Xid arg0) throws XAException {
////			System.err.println(">>rollback:"+arg0);
//			if (bridged) {
//				forget();
//				try {
//					getCoordinator().abortedOperation(null);
//				} catch (Exception e) {
//				}
//			}
//		}
//
//		public boolean setTransactionTimeout(int timeout) throws XAException {
//			this.timeout = timeout;
//			return true;
//		}
//
//		public void start(Xid arg0, int arg1) throws XAException {
////			System.err.println(">>start:"+arg0);
//		}
//
//		public int prepare() throws XAException {
//			System.err.println("<<prepare:");
//			return gerotm.prepare(tx);
//		}
//
//		public void commit() throws XAException {
//			System.err.println("<<commit:");
//			gerotm.commit(tx, false);
//		}
//
//		public void rollback() throws XAException {
//			System.err.println("<<rollback:");
//			gerotm.rollback(tx);
//		}
//
//		public void forget()  {
//			System.err.println("<<forget:");
////			gerotm.forget(tx);
//			if (bridged) {
//				bridge1.forget(id);
//				bridged = false;
//			}
//		}
//
//		public int getStatus() {
//			try {
//				if (this.tx == null)
//					return Status.STATUS_NO_TRANSACTION;
//
//				switch (tx.getStatus()) {
//				case Status.STATUS_ACTIVE:
//				case Status.STATUS_MARKED_ROLLBACK:
//					return AT2PCStatus.ACTIVE;
//
//				case Status.STATUS_PREPARING:
//					return AT2PCStatus.PREPARING;
//
//				case Status.STATUS_ROLLING_BACK:
//				case Status.STATUS_ROLLEDBACK:
//					return AT2PCStatus.ABORTING;
//
//				case Status.STATUS_PREPARED:
//					return AT2PCStatus.PREPARED;
//
//				case Status.STATUS_COMMITTING:
//				case Status.STATUS_COMMITTED:
//					return AT2PCStatus.COMMITTING;
//
//				case Status.STATUS_NO_TRANSACTION:
//					return AT2PCStatus.NONE;
//
//				case Status.STATUS_UNKNOWN:
//				default:
//					throw new RuntimeException();
//				}
//			} catch (SystemException e) {
//				e.printStackTrace();
//				throw new RuntimeException(e);
//			}
//		}
//
//	}


	
}


