/*
 * Created on 2004. 12. 19.
 */
package org.uengine.components.activityfilters;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Calendar;

import org.uengine.kernel.Activity;
import org.uengine.kernel.ActivityFilter;
import org.uengine.kernel.GlobalContext;
import org.uengine.kernel.HumanActivity;
import org.uengine.kernel.ProcessDefinition;
import org.uengine.kernel.ProcessInstance;
import org.uengine.kernel.RoleMapping;
import org.uengine.persistence.analysis.PerformanceFactDAO;
import org.uengine.persistence.analysis.PerformanceFactDAOType;
import org.uengine.persistence.analysis.ResourceDimensionDAO;
import org.uengine.persistence.analysis.ResourceDimensionDAOType;
import org.uengine.processmanager.SimpleTransactionContext;
import org.uengine.processmanager.TransactionContext;
import org.uengine.webservices.worklist.SimulatorWorkList;


/**
 * @author Jinyoung Jang
 */

public class OlapEtlFilter implements ActivityFilter, Serializable{
	private static final long serialVersionUID = org.uengine.kernel.GlobalContext.SERIALIZATION_UID;
	private static final String DW_DSN = GlobalContext.getPropertyString("org.uengine.components.activityfilters.olapetlfilter.datasource.name");
	
	public void afterExecute(Activity activity, ProcessInstance instance)
		throws Exception {
		
		if(activity instanceof HumanActivity){
			HumanActivity humanActivity = (HumanActivity)activity;
			
			RoleMapping theMapping = humanActivity.getRole().getMapping(instance);
			
			if(theMapping !=null){
				theMapping.beforeFirst();
				do{
					TransactionContext tc;
					
					if(DW_DSN!=null){
						tc = new SimpleTransactionContext(DW_DSN);
					}else{
						tc = instance.getProcessTransactionContext();
					}
					
					ResourceDimensionDAOType resourceDimensionDAOType = ResourceDimensionDAOType.getInstance(tc);
	
					if(resourceDimensionDAOType.existAnyResourceWhereRsrc_Id(theMapping.getEndpoint())) continue;
					
					if(theMapping.getGroupName()==null)
						theMapping.fill(instance);
	
					ResourceDimensionDAO resourceDimension = resourceDimensionDAOType.createDAOForInsert();{
						resourceDimension.setRsrc_Id(theMapping.getEndpoint());
						resourceDimension.setRsrc_Name(theMapping.getResourceName());
						resourceDimension.setGender(theMapping.isMale());
						resourceDimension.setDept_Name(theMapping.getGroupName());
						resourceDimension.setDept_Id(theMapping.getGroupId());
						if(theMapping.getBirthday()!=null)
							resourceDimension.setBirthDay(new Long(1900 + theMapping.getBirthday().getYear()));
					}
					resourceDimension.insert();
					
					if(tc instanceof SimpleTransactionContext)
						tc.releaseResources();
					
				}while(theMapping.next());
				
				theMapping.beforeFirst();
			}
		}
		
	}

	public void afterComplete(Activity activity, ProcessInstance instance)
		throws Exception {
		
		if(instance.getWorkList() instanceof SimulatorWorkList) return;
		
		if(activity instanceof HumanActivity){
			
			HumanActivity humanActivity = (HumanActivity)activity;
			
			RoleMapping theMapping = humanActivity.getRole().getMapping(instance);
	
			theMapping.beforeFirst();

			TransactionContext tc;
			
			if(DW_DSN!=null){
				tc = new SimpleTransactionContext(DW_DSN);
			}else{
				tc = instance.getProcessTransactionContext();
			}

			PerformanceFactDAOType performanceFactDAOType = PerformanceFactDAOType.getInstance(tc);
			PerformanceFactDAO performanceFact = performanceFactDAOType.createDAOForInsert();{
				performanceFact.setACT_ID(humanActivity.getTracingTag());
				performanceFact.setINST_ID(new Long(instance.getInstanceId()));
				performanceFact.setROOTINST_ID(new Long(instance.getRootProcessInstanceId()));
				performanceFact.setACT_NAME(humanActivity.getName());
				performanceFact.setCOST(new Long(humanActivity.getCost()));
				performanceFact.setDEF_ID(new Long(humanActivity.getProcessDefinition().getBelongingDefinitionId()));
				performanceFact.setDEF_NAME(humanActivity.getProcessDefinition().getName());
				performanceFact.setPRSNGTIME(new Long(humanActivity.getElapsedTimeAsLong(instance) / 60000));
				performanceFact.setRsrc_Id(theMapping.getEndpoint());
				performanceFact.setMODTIME(new Timestamp(GlobalContext.getNow(instance.getProcessTransactionContext()).getTimeInMillis()));
				
				
				long timeId = 0;{
					java.util.Calendar startedTime = humanActivity.getOpenTime(instance);
					if(startedTime == null) startedTime = humanActivity.getStartedTime(instance);
					
					int year = startedTime.get(Calendar.YEAR);
					int month = startedTime.get(Calendar.MONTH) + 1;
					int day = startedTime.get(Calendar.DATE);
					timeId += year * 10000;
					timeId += month * 100;
					timeId += day;
				}
				
				performanceFact.setTIME_ID(new Long(timeId));
				
				int DEADLNHT = 0;{
					java.util.Calendar startedTime = humanActivity.getEndTime(instance);
					java.util.Calendar dueDate = humanActivity.getDueDate(instance);
					if(startedTime.compareTo(dueDate) <= 0){
						DEADLNHT = 0;
					}else{
						DEADLNHT = 1;
					}
				}
		
				performanceFact.setDEADLNHT( new Integer(DEADLNHT));
			}
			performanceFact.insert();
			
			if(tc instanceof SimpleTransactionContext)
				tc.releaseResources();
		}
		
	}

	public void beforeExecute(Activity activity, ProcessInstance instance)
		throws Exception {
	}

	public void onDeploy(ProcessDefinition definition) throws Exception {
	}

	public void onPropertyChange(Activity activity, ProcessInstance instance, String propertyName, Object changedValue) throws Exception {
		// TODO Auto-generated method stub
		
	}
	
}
