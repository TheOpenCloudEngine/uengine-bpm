/*
 * Created on 2004. 9. 26.
 */
package org.uengine.webservices.worklist;

import java.io.ByteArrayOutputStream;
import java.rmi.RemoteException;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.uengine.kernel.GlobalContext;
import org.uengine.kernel.KeyedParameter;
import org.uengine.kernel.Role;
import org.uengine.persistence.dao.DAOFactory;
import org.uengine.persistence.dao.UniqueKeyGenerator;
import org.uengine.persistence.dao.WorkListDAO;
import org.uengine.persistence.worklist.WorklistDAOType;
import org.uengine.processmanager.TransactionContext;
import org.uengine.util.UEngineUtil;

/**
 * we've decided not to use entity beans for the external implementation
 * so that the customizing developers can void the learning curves and the deployment cost in J2EE development. 
 * @author Jinyoung Jang
 */

public class JDBCExtWorkList implements WorkList{
	
	public String reserveWorkItem(String userId, KeyedParameter[] parameters, TransactionContext tc)
		throws RemoteException {
		Map parameterMap = getParameterMap(parameters);
		
		return addWorkItemImpl(null, userId, parameterMap, true, tc);
	}

	public String addWorkItem(String userId, KeyedParameter[] parameters, TransactionContext tc) throws RemoteException{
		Map parameterMap = getParameterMap(parameters);

		return addWorkItemImpl(null, userId, parameterMap, false, tc);
	}
	
	public String addWorkItem(String reservedTaskId, String userId, KeyedParameter[] parameters, TransactionContext tc) throws RemoteException {
		Map parameterMap = getParameterMap(parameters);

		return addWorkItemImpl(reservedTaskId, userId, parameterMap, false, tc);
	}
		
	protected String addWorkItemImpl(String reservedTaskId, String userId, Map parameterMap, boolean isReservation, TransactionContext tc) throws RemoteException {
			
		try{

			DAOFactory daoFactory = DAOFactory.getInstance(tc);
			
			Calendar startDate;

			if(parameterMap.containsKey("startedTime")){
				startDate = Calendar.getInstance();
				startDate.setTimeInMillis(Long.parseLong((String) parameterMap.get("startedTime")));
			}else{
				startDate = daoFactory.getNow();
			}

			Calendar dueDate = null;
			try{			
				String dueDateInMSStr = ""+parameterMap.get(KeyedParameter.DUEDATE);
				long dueDateInMS = Long.parseLong(dueDateInMSStr);
				dueDate = Calendar.getInstance();
				dueDate.setTimeInMillis(dueDateInMS);
			}catch(Exception e){
			}
			
			if(dueDate==null){
				int duration = 0;
				{
					try{
						String durationStr = ""+parameterMap.get(KeyedParameter.DURATION);
						duration = Integer.parseInt(durationStr);
					}catch(Exception e){
					}
				}
				
				if(duration>0){
					dueDate = Calendar.getInstance();
					dueDate.setTimeInMillis(startDate.getTimeInMillis() + (long)duration * 86400000L);			
					int dayOfMonth = dueDate.get(Calendar.DAY_OF_MONTH);
					int year = dueDate.get(Calendar.YEAR);
					int month = dueDate.get(Calendar.MONTH);
				}
			}
			
			Number priority = new Integer(1);
			{
				try{
					String priorityStr = ""+parameterMap.get(KeyedParameter.PRIORITY);
					priority = new Integer(priorityStr);
				}catch(Exception e){
				}
			}
			
			final WorkListDAO wl;
			
			Long taskId; 
			if(reservedTaskId!=null){
				taskId = new Long(reservedTaskId);
				wl = (WorkListDAO) tc.findSynchronizedDAO("BPM_WORKLIST", "taskId", taskId, WorkListDAO.class);
			}
			else{
				taskId = UniqueKeyGenerator.issueWorkItemKey(tc);
				wl = (WorkListDAO) tc.createSynchronizedDAO("BPM_WORKLIST", "taskId", taskId, WorkListDAO.class);
			}

			if(dueDate!=null)
				wl.setDueDate(dueDate.getTime());
			else
				wl.setDueDate(null);

			String definitionName = (String)parameterMap.get("definitionName");
			String instanceName = (String)parameterMap.get("instanceName");
			String instanceId = (String)parameterMap.get(KeyedParameter.INSTANCEID);
			
/*			if(definitionName!=null){
				wl.setProcessDefinitionName(definitionName);
			}
			if(instanceName!=null){
				wl.setProcessInstanceName(instanceName);
			}
*/	
			
			
			wl.setPriority(priority);
			wl.setTool(""+parameterMap.get(KeyedParameter.TOOL));
			wl.setEndpoint(userId);

			//modified
			Timestamp startedTime;
//			if(tc.getProcessManager().getGenericContext().containsKey("initiationTime")){
//				String startedTimeInStr = (String)tc.getProcessManager().getGenericContext().get("initiationTime");
//				startedTime = new Timestamp(Long.parseLong(startedTimeInStr));
//			}else
				startedTime = new Timestamp(startDate.getTimeInMillis());
			wl.setStartDate(startedTime);
			//end
			
			wl.setTitle(""+parameterMap.get(KeyedParameter.TITLE));
			wl.setTrcTag(""+parameterMap.get(KeyedParameter.TRACINGTAG));
			wl.setInstId(new Long(""+parameterMap.get(KeyedParameter.INSTANCEID)));
			wl.setRootInstId(new Long(""+parameterMap.get(KeyedParameter.ROOTINSTANCEID)));
			wl.setDefId(""+parameterMap.get(KeyedParameter.PROCESSDEFINITION));
			wl.setDefName(""+parameterMap.get(KeyedParameter.PROCESSDEFINITIONNAME));
			wl.setRoleName(""+parameterMap.get("roleName"));
			wl.setRefRoleName(""+parameterMap.get("referenceRoleName"));
			wl.setResName(""+parameterMap.get("resourceName"));
			
			int i=1;
			while(parameterMap.containsKey("dispatchParam" + i)){
				wl.set("DispatchParam" +i, ""+parameterMap.get("dispatchParam" + i));
				i++;
			}
			
			if(parameterMap.containsKey("executionScope")){
				wl.setExecScope((String)parameterMap.get("executionScope"));
			}
						
			if(parameterMap.containsKey("extValue1")){
				wl.set("EXT1", parameterMap.get("extValue1"));
			}
			if(parameterMap.containsKey("extValue2")){
				wl.set("EXT2", parameterMap.get("extValue2"));
			}
			if(parameterMap.containsKey("extValue3")){
				wl.set("EXT3", parameterMap.get("extValue3"));
			}
			if(parameterMap.containsKey("extValue4")){
				wl.set("EXT4", parameterMap.get("extValue4"));
			}
			if(parameterMap.containsKey("extValue5")){
				wl.set("EXT5", parameterMap.get("extValue5"));
			}
			if(parameterMap.containsKey("extValue6")){
				wl.set("EXT6", parameterMap.get("extValue6"));
			}
			if(parameterMap.containsKey("extValue7")){
				wl.set("EXT7", parameterMap.get("extValue7"));
			}
			if(parameterMap.containsKey("extValue8")){
				wl.set("EXT8", parameterMap.get("extValue8"));
			}
			if(parameterMap.containsKey("extValue9")){
				wl.set("EXT9", parameterMap.get("extValue9"));
			}
			if(parameterMap.containsKey("extValue10")){
				wl.set("EXT10", parameterMap.get("extValue10"));
			}
			
			//dispatching option//////
			try{
				int dispatchingOption = Integer.parseInt((String)parameterMap.get(KeyedParameter.DISPATCHINGOPTION));	
				wl.setDispatchOption(dispatchingOption);
			}catch(Exception e){
				wl.setDispatchOption(Role.DISPATCHINGOPTION_ALL);
			}
			//parameterMap.remove(KeyedParameter.DISPATCHINGOPTION);
			//
			
			setExtendedFields(wl, parameterMap);
			
			//status//////
			String defaultStatus = parameterMap.containsKey(KeyedParameter.DEFAULT_STATUS) ? 
					""+parameterMap.get(KeyedParameter.DEFAULT_STATUS)
					:
					(isReservation ? DefaultWorkList.WORKITEM_STATUS_RESERVED : DefaultWorkList.WORKITEM_STATUS_NEW);			
			wl.setStatus(defaultStatus);
			//
			
			//parameter map////// don't use parameter map anymore
			//if(!DAOFactory.getInstance(tc).getDBMSProductName().startsWith("DB2")){
			//	ByteArrayOutputStream bao = new ByteArrayOutputStream();
			//	GlobalContext.serialize(parameterMap, bao, String.class);
			//	wl.setParameter(bao.toString(GlobalContext.DATABASE_ENCODING));
			//}
			//

			return ""+taskId;				
			
		}catch(Exception e){
			
System.out.println("====================HARD-TO-FIND-ERR: pi=" + parameterMap.get(KeyedParameter.INSTANCEID) + "  tracingTag=" + parameterMap.get(KeyedParameter.TRACINGTAG));
			
			throw new RemoteException("ExtWorkList", e);
		}				
	}
	
	public void cancelWorkItem(String taskID, KeyedParameter[] options, TransactionContext tc)
		throws RemoteException {
			
		try{
			
			WorkListDAO wl = (WorkListDAO) tc.findSynchronizedDAO("BPM_WORKLIST", "taskId", new Long(taskID), WorkListDAO.class);
			wl.setStatus(DefaultWorkList.WORKITEM_STATUS_CANCELLED);
			for(int i=0; i<options.length; i++){
				KeyedParameter parameter = options[i];
				if("status".equals(parameter.getKey()) && UEngineUtil.isNotEmpty((String)parameter.getValue())){
					wl.setStatus(((String)parameter.getValue()).toUpperCase());
				}
			}
			
			
		}catch(Exception e){
			throw new RemoteException("ExtWorkList", e);
		}
	}

	public void completeWorkItem(String taskID, KeyedParameter[] options, TransactionContext tc)
		throws RemoteException {

		try{
			DAOFactory daoFactory = DAOFactory.getInstance(tc);
			Calendar now = daoFactory.getNow();

			WorkListDAO wl = (WorkListDAO) tc.findSynchronizedDAO("BPM_WORKLIST", "taskId", new Long(taskID), WorkListDAO.class);
			wl.setStatus(DefaultWorkList.WORKITEM_STATUS_COMPLETED);
			wl.setEndDate(new Timestamp(now.getTimeInMillis()));
			
		}catch(Exception e){
			throw new RemoteException("ExtWorkList", e);
		}
	}

	public void updateWorkItem(
		String taskId,
		String userId,
		KeyedParameter[] parameters,
		TransactionContext tc)
		throws RemoteException {
		
		try{
			
//			WorklistDAOType wlType = WorklistDAOType.getInstance(tc);
//			WorkListDAO wlDAO = wlType.createDAOImpl(null);
//			wlDAO.getImplementationObject().setTableName("bpm_worklist");
//			wlDAO.getImplementationObject().setKeyField("taskId");
//			wlDAO.getImplementationObject().setAutoSQLGeneration(true);
//			wlDAO.setTaskId(new Long(taskId));

			WorkListDAO wlDAO = (WorkListDAO) tc.findSynchronizedDAO("BPM_WORKLIST", "taskId", new Long(taskId), WorkListDAO.class);

			if(userId!=null)
				wlDAO.setEndpoint(userId);
			
			for(int i=0; i<parameters.length; i++){
				KeyedParameter parameter = parameters[i];
				
				if(KeyedParameter.DISPATCHINGOPTION.equals(parameter.getKey())){
					wlDAO.setDispatchOption(Integer.parseInt(""+parameter.getValue()));
				}else
				if("dispatchParam1".equals(parameter.getKey())){
					wlDAO.setDispatchParam1(""+parameter.getValue());
				}else 
                if(KeyedParameter.DUEDATE.equals(parameter.getKey())){
					wlDAO.setDueDate((Date)parameter.getValue());
                }else 
                if(KeyedParameter.DEFAULT_STATUS.equals(parameter.getKey())){
					wlDAO.setStatus((String)parameter.getValue());
                }else 
                if("endDate".equals(parameter.getKey())){
    				wlDAO.setEndDate((Date)parameter.getValue());
                }else 
                if("saveDate".equals(parameter.getKey())){ //임시저장 시간 저장 (11.23)
        			wlDAO.setSaveDate((Date)parameter.getValue());
                }
			}
			
			//wlDAO.update();		
			
		}catch(Exception e){
			throw new RemoteException("ExtWorkList", e);
		}		
	}
	
	private Map getParameterMap(KeyedParameter[] parameters){
		Map parameterMap = new HashMap();
	
		for(int i=0; i<parameters.length; i++){
			parameterMap.put(parameters[i].getKey(), parameters[i].getValue());
		}
		
		return parameterMap;
	}

	//customizable methods//////
	protected void setExtendedFields(WorkListDAO wl, Map parameterMap) throws Exception{
	}

/*	protected WorkListDAO createWorkListDAOForInsertCall(Map parameterMap, TransactionContext tc) throws Exception{
		return DAOFactory.getInstance(tc).createWorkListDAOForInsertCall(null);
	}
	protected WorkListDAO createWorkListDAOForUpdate(Map parameterMap, TransactionContext tc) throws Exception{ 
		return DAOFactory.getInstance(tc).createWorkListDAOForUpdate(null);
	}
*/	//end//////

}
	