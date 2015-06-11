/*
 * Created on 2004. 12. 13.
 */
package org.uengine.webservices.worklist;

import java.rmi.RemoteException;
import java.util.*;

import org.uengine.kernel.KeyedParameter;
import org.uengine.processmanager.TransactionContext;

import org.uengine.webservice.*;

/**
 * @author Jinyoung Jang
 */
public class SimulatorWorkList extends DefaultWorkList{
	
	ArrayList worklist = new ArrayList();
	static int currTaskId = 0;
	
	public String addWorkItem(String userId, KeyedParameter[] parameters)
		throws RemoteException {

		return addWorkItem(null, userId, parameters);
	}

	public String addWorkItem(
		String taskId,
		String userId,
		KeyedParameter[] parameters)
		throws RemoteException {
			
		taskId = new String(""+(currTaskId++));	
		
		Hashtable workitem = new Hashtable();
		workitem.put("TASKID", taskId);	
		workitem.put("PARAMETERS", parameters);
		workitem.put("USERID", userId);
		
		worklist.add(workitem);

		return taskId;
	}

	public void cancelWorkItem(String taskID, KeyedParameter[] options, TransactionContext tc)
		throws RemoteException {
		// TODO Auto-generated method stub
		super.cancelWorkItem(taskID, options, tc);
	}

	/* (non-Javadoc)
	 * @see org.uengine.webservices.worklist.WorkList#completeWorkItem(java.lang.String, org.uengine.webservices.worklist.KeyedParameter[])
	 */
	public void completeWorkItem(String taskID, KeyedParameter[] options, TransactionContext tc)
		throws RemoteException {
		// TODO Auto-generated method stub
		super.completeWorkItem(taskID, options, tc);
	}

	/* (non-Javadoc)
	 * @see org.uengine.webservices.worklist.WorkList#reserveWorkItem(java.lang.String, org.uengine.webservices.worklist.KeyedParameter[])
	 */
	public String reserveWorkItem(String userId, KeyedParameter[] parameters)
		throws RemoteException {
		// TODO Auto-generated method stub
		return addWorkItem(userId, parameters);
	}

	/* (non-Javadoc)
	 * @see org.uengine.webservices.worklist.WorkList#updateWorkItem(java.lang.String, java.lang.String, org.uengine.webservices.worklist.KeyedParameter[])
	 */
	public void updateWorkItem(
		String taskId,
		String userId,
		KeyedParameter[] parameters, TransactionContext tc)
		throws RemoteException {
		// TODO Auto-generated method stub
		super.updateWorkItem(taskId, userId, parameters, tc);
	}

	public ArrayList getWorklist() {
		return worklist;
	}

	public void setWorklist(ArrayList list) {
		worklist = list;
	}
	
	public Collection findWorkitem(Hashtable filter, boolean isAnd){
		Collection result = new ArrayList();
		String userId = (String)filter.get("USERID");
		String status = (String)filter.get("STATUS");
		
		for(Iterator iter = getWorklist().iterator(); iter.hasNext(); ){
			Map workitem = (Map)iter.next();
			
			String compareUserId = (workitem.containsKey("USERID") ? (String)workitem.get("USERID"): "[NOVALUE]");
			String compareStatus = (workitem.containsKey("STATUS") ? (String)workitem.get("STATUS"): "[NOVALUE]");
			
			boolean isResult = false;
			
			if(compareUserId.equals(userId)){
				isResult = true;
			}
			
			if(compareStatus.equals(status)){
				if(!isAnd)
					isResult = true;
			}else{
				if(isAnd)
					isResult = false;
			}

			if(isResult)
				result.add(workitem);
		}
		
		return result;
	}

}
