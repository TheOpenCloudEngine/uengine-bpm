package org.uengine.processmanager;

import java.sql.Connection;

import javax.naming.InitialContext;
import org.uengine.kernel.GlobalContext;
import org.uengine.kernel.UEngineException;
import org.uengine.util.dao.ConnectionFactory;

/**
 * @author Jinyoung Jang
 */
public class ProcessManagerFactoryBean {

	InitialContext context;
	ProcessManagerHomeRemote pmh;
	ProcessManagerFactoryBean processManagerFactoryDelegator;
	
	public ProcessManagerFactoryBean(){
		
		boolean useEJB = GlobalContext.useEJB;
		
//		String processManagerFactoryDelegatorClassName = GlobalContext.getPropertyString("processManagerFactoryDelegator");
//		if(processManagerFactoryDelegatorClassName!=null){
//			try{
//				processManagerFactoryDelegator = (ProcessManagerFactoryBean) Thread.currentThread().getContextClassLoader().loadClass(processManagerFactoryDelegatorClassName).newInstance();
//				return;
//			}catch(Exception e){
//				
//			}
//		}
		
		if(useEJB){			
			try{
				context = new InitialContext();
				pmh = (ProcessManagerHomeRemote)context.lookup("ProcessManagerHomeRemote");
				
			}catch(Exception e){
				System.out.println("can't locate ejb:");
				e.printStackTrace();
			}
		}
	}
	
	public ProcessManagerRemote getProcessManager(boolean uengineManageTheTransaction, ConnectionFactory connFactory) throws Exception{
		boolean useEJB = GlobalContext.useEJB;
		boolean readOnly = !uengineManageTheTransaction;
		
		String processManagerFactoryDelegatorClassName = GlobalContext.getPropertyString("processManagerFactoryDelegator");
		if(processManagerFactoryDelegatorClassName!=null){
			processManagerFactoryDelegator = (ProcessManagerFactoryBean) Thread.currentThread().getContextClassLoader().loadClass(processManagerFactoryDelegatorClassName).newInstance();
			return processManagerFactoryDelegator.getProcessManager();
		}else
		if(useEJB){
			ProcessManagerRemote pm = pmh.create();
			return pm;
		}else{
			
			ProcessManagerBean pmb = new ProcessManagerBean();
			
			if(connFactory!=null){
				pmb.setConnectionFactory(connFactory);
			}
			
			pmb.jndiContext = new InitialContext();
			pmb.setManagedTransaction(readOnly || GlobalContext.useManagedTransaction); //don't use transaction demarcation when the manager is read-only purpose.
			
			return pmb;
		}
	}

	public ProcessManagerRemote getProcessManager(boolean uengineManageTheTransaction, final Connection conn) throws Exception{
		if(conn == null) throw new UEngineException("Null connection is provided.");
		
		return getProcessManager(uengineManageTheTransaction, new ConnectionFactory(){

			public Connection getConnection() throws Exception {
				return conn;
			}
			
		});
	}

	public ProcessManagerRemote getProcessManager() throws Exception{
		return getProcessManager(true, (ConnectionFactory)null);
	}

	public ProcessManagerRemote getProcessManagerForReadOnly() throws Exception{
		return getProcessManager(false, (ConnectionFactory)null);
	}

	public ProcessManagerRemote getProcessManager(final Connection conn) throws Exception{
		if(conn == null) throw new UEngineException("Null connection is provided.");

		return getProcessManager(false, new ConnectionFactory(){

			public Connection getConnection() throws Exception {
				return conn;
			}
			
		});
	}

	public ProcessManagerRemote getProcessManager(ConnectionFactory connFactory) throws Exception{
		return getProcessManager(false, connFactory);
	}

}