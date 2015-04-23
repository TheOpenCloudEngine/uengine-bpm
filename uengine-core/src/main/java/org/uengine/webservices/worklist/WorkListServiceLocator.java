/*
 * Created on 2004. 10. 14.
 */
package org.uengine.webservices.worklist;

import java.rmi.RemoteException;

import org.uengine.kernel.GlobalContext;
import org.uengine.kernel.KeyedParameter;
import org.uengine.processmanager.TransactionContext;
/**
 * @author Jinyoung Jang
 */
public class WorkListServiceLocator {
	public static WorkList USE_OBJECT;
	public static String USE_CLASS_NAME;		
	
	public WorkList getWorkList(){
//TODO now we're using the local object because of the performance problem
//		return new DefaultWorkList(); //worklist of liferay portal (Task Portlet)
//		return new EJBExtWorkList();

		if(GlobalContext.isDesignTime())
			return new WorkList(){

				public String reserveWorkItem(String userId, KeyedParameter[] parameters, TransactionContext tc) throws RemoteException {
					// TODO Auto-generated method stub
					return null;
				}

				public String addWorkItem(String taskId, String userId, KeyedParameter[] parameters, TransactionContext tc) throws RemoteException {
					// TODO Auto-generated method stub
					return null;
				}

				public String addWorkItem(String userId, KeyedParameter[] parameters, TransactionContext tc) throws RemoteException {
					// TODO Auto-generated method stub
					return null;
				}

				public void updateWorkItem(String taskId, String userId, KeyedParameter[] parameters, TransactionContext tc) throws RemoteException {
					// TODO Auto-generated method stub
					
				}

				public void cancelWorkItem(String taskID, KeyedParameter[] options, TransactionContext tc) throws RemoteException {
					// TODO Auto-generated method stub
					
				}

				public void completeWorkItem(String taskID, KeyedParameter[] options, TransactionContext tc) throws RemoteException {
					// TODO Auto-generated method stub
					
				}
			
		};
		
			
		if(USE_OBJECT==null)
		try{
			USE_CLASS_NAME = GlobalContext.getPropertyString("worklistservice.class");
			USE_OBJECT = (WorkList)Thread.currentThread().getContextClassLoader().loadClass(USE_CLASS_NAME).newInstance();		
		}catch(Exception e){
//			USE_OBJECT = new JDBCExtWorkList();
			try{
				USE_CLASS_NAME = GlobalContext.getPropertyString("extworklistservice.class");
				USE_OBJECT = (WorkList)Thread.currentThread().getContextClassLoader().loadClass(USE_CLASS_NAME).newInstance();		
			}catch(Exception ee){
				USE_OBJECT = new JDBCExtWorkList();
			}
		}

		return USE_OBJECT; 
	}
}
