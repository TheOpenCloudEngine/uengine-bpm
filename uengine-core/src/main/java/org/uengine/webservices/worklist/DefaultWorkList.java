/*
 * Created on 2004. 9. 26.
 */
package org.uengine.webservices.worklist;

import java.util.*;
import java.rmi.RemoteException;

import org.uengine.kernel.KeyedParameter;
import org.uengine.processmanager.TransactionContext;

import org.uengine.webservice.*;

/**
 * @author Jinyoung Jang
 */

public class DefaultWorkList implements WorkList{

	public final static String WORKITEM_STATUS_DRAFT	= "DRAFT";
	public final static String WORKITEM_STATUS_NEW 		= "NEW";
	public final static String WORKITEM_STATUS_CONFIRMED= "CONFIRMED";
	public final static String WORKITEM_STATUS_RESERVED	= "RESERVED";
	public final static String WORKITEM_STATUS_REFERENCE= "REFERENCE";
	public final static String WORKITEM_STATUS_COMPLETED= "COMPLETED";
	public final static String WORKITEM_STATUS_CANCELLED= "CANCELLED";
	public final static String WORKITEM_STATUS_SUSPENDED= "SUSPENDED";
	//When user delegate a workitem, the workitem's status whould be changed to this status and another workitem.	
	public final static String WORKITEM_STATUS_DELEGATED= "DELEGATED"; 
	final static String adminID = "liferay.com.1";
	final static String adminPW = "test";
	static Properties exceptList = new Properties();
	static{
		exceptList.setProperty(KeyedParameter.INSTRUCTION, "");
	}


	public String addWorkItem(
		String taskId,
		String userId,
		KeyedParameter[] parameters, TransactionContext tc)
		throws RemoteException {

		return addWorkItem(userId, parameters, tc);
	}

	public String addWorkItem(String userId, KeyedParameter[] parameters, TransactionContext tc) throws RemoteException{
		return "";
	}
	
	public void cancelWorkItem(String taskID, KeyedParameter[] options, TransactionContext tc) throws RemoteException{
	}
	
	public void completeWorkItem(String taskID, KeyedParameter[] options, TransactionContext tc) throws RemoteException{
	}
    
	public static String getWIHAddress(Properties parameterMap) throws Exception{
		String tool = parameterMap.getProperty(KeyedParameter.TOOL, "defaultHandler");
		String url = parameterMap.getProperty(KeyedParameter.URL, "/html/uengine-web/wih/" + tool);
		String message = parameterMap.getProperty(KeyedParameter.MESSAGE, "");
		String tracingTag = parameterMap.getProperty(KeyedParameter.TRACINGTAG, "");
		String processDefinition = parameterMap.getProperty(KeyedParameter.PROCESSDEFINITION, "");
		String instanceId = parameterMap.getProperty(KeyedParameter.INSTANCEID, "");
				
		StringBuffer address = new StringBuffer();

		address.append(url);
					 
		address.append(address.indexOf("?") > -1 ? "&" : "?"); 

		String sep = "";
		for(Enumeration enumeration = parameterMap.keys(); enumeration.hasMoreElements(); ){
			String key = (String)enumeration.nextElement();
			if(exceptList.contains(key)) continue;
			
			address.append(sep + key + "=" + parameterMap.getProperty(key).replace(' ','+'));
			sep = "&";
		}
/*		address.append("message=" + message + "&instanceId=" + instanceId);
		address.append("&tracingTag=" + tracingTag);
		address.append("&processDefinition=" + processDefinition.replace(' ', '+'));
*/		
		return address.toString();
	}

	
	public String reserveWorkItem(String userId, KeyedParameter[] parameters, TransactionContext tc)
		throws RemoteException {
		return addWorkItem(userId, parameters, tc);
	}

	public void updateWorkItem(
		String taskId,
		String userId,
		KeyedParameter[] parameters, TransactionContext tc)
		throws RemoteException {

		//not supported
	}


}
	