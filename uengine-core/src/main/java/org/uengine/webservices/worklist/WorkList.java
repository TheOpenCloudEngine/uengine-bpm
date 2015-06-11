/*
 * Created on 2004. 10. 14.
 */
package org.uengine.webservices.worklist;

import java.rmi.RemoteException;

import org.uengine.kernel.KeyedParameter;
import org.uengine.processmanager.TransactionContext;

import org.uengine.webservice.*;

/**
 * @author Jinyoung Jang
 */
public interface WorkList {
	public String reserveWorkItem(String userId, KeyedParameter[] parameters, TransactionContext tc) throws RemoteException;
	public String addWorkItem(String taskId, String userId, KeyedParameter[] parameters, TransactionContext tc) throws RemoteException;
	public String addWorkItem(String userId, KeyedParameter[] parameters, TransactionContext tc) throws RemoteException;

	public void updateWorkItem(String taskId, String userId, KeyedParameter[] parameters, TransactionContext tc) throws RemoteException;
	public void cancelWorkItem(String taskID, KeyedParameter[] options, TransactionContext tc) throws RemoteException;	
	public void completeWorkItem(String taskID, KeyedParameter[] options, TransactionContext tc) throws RemoteException;
	
	//status of workitem maybe..
	//	state					invocation pattern
	//------------------------ -----------------------------
	// 1. RESERVED				reserveWorkItem(<userid>, <parameters>);
	// 2. NEW (OPEN)			If reserved workitem --> addWorkItem(<reservedTaskId>, <userid>, <parameters>);
	//							If new workitem --> just addWorkItem(<userid>, <parameter>);	
	// 3. CONFIRMED				engine should not implcitly change the workitem to this status. the worklist system should do this. 
	// 4. COMPLETED (CLOSED)	completeWorkItem(...);
	// 5. CANCELLED				cancelWorkItem(...);
	// 6. SKIPPED or REWINDED	worklist doesn't need to know this info. use CANCELLED instead.
	// 7. RETURNED				
	
	    
}
