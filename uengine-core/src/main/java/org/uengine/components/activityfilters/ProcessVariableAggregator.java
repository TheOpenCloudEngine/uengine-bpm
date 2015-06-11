/*
 * Created on 2005. 5. 4.
 */
package org.uengine.components.activityfilters;

import java.io.Serializable;

import org.uengine.kernel.Activity;
import org.uengine.kernel.ActivityFilter;
import org.uengine.kernel.GlobalContext;
import org.uengine.kernel.ProcessDefinition;
import org.uengine.kernel.ProcessInstance;
import org.uengine.util.dao.*;

/**
 * @author Jinyoung Jang
 */
public class ProcessVariableAggregator implements ActivityFilter, Serializable{
	private static final long serialVersionUID = org.uengine.kernel.GlobalContext.SERIALIZATION_UID;
	
	public void afterExecute(Activity activity, ProcessInstance instance)
		throws Exception {
	}
				
	public void afterComplete(Activity activity, ProcessInstance instance)
		throws Exception {
/*
		//TODO: [for performance] for performance, time checking for non-human activity is omitted
		if(!(activity instanceof ProcessDefinition)) return;
		
		HumanActivity humanActivity = (HumanActivity)activity;
		long elapsedTime = humanActivity.getElapsedTimeAsLong(instance);
		Long procDefId = new Long(humanActivity.getProcessDefinition().getBelongingDefinitionId());

		boolean isNew = false;
		long totalTime=0;
		long occurrence=0;

		Long[] totalTimeAndOccurrence = getTotalTimeAndOccurrence(humanActivity);
		if(totalTimeAndOccurrence==null){
			isNew = true;
		}else{
			totalTime=totalTimeAndOccurrence[0].longValue();
			occurrence=totalTimeAndOccurrence[1].longValue();
		}
		
		occurrence++;
		totalTime = totalTime + elapsedTime;
		
		String sql = (isNew?
		"insert into BPM_AUDIT_TIME(ProcDefId, TracingTag, TotalTime, Occurrence) values (?ProcDefId, ?TracingTag, ?TotalTime, ?Occurrence)":
		"update BPM_AUDIT_TIME set Occurrence=?Occurrence, TotalTime=?TotalTime where (ProcDefId = ?ProcDefId and TracingTag = ?TracingTag)"
		);
		 
		BPM_AUDIT_TIME newProbability = (BPM_AUDIT_TIME)GenericDAO.createDAOImpl(
			new ConnectionFactory(),
			sql,
			BPM_AUDIT_TIME.class
		);
		
		newProbability.setProcDefId(procDefId);
		newProbability.setTracingTag(humanActivity.getTracingTag());
		newProbability.setTotalTime(new Long(totalTime));
		newProbability.setOccurrence(new Long(occurrence));
		
		newProbability.insert();*/		
	}

	public void beforeExecute(Activity activity, ProcessInstance instance)
		throws Exception {
	}

	public void onDeploy(ProcessDefinition definition) throws Exception {
	}
	
/*	public static Long[] getTotalTimeAndOccurrence(Activity activity) throws Exception{
		Long procDefId = new Long(activity.getProcessDefinition().getBelongingDefinitionId());
		
		BPM_AUDIT_VARAGGR oldTime = (BPM_AUDIT_VARAGGR)GenericDAO.createDAOImpl(
			new ConnectionFactory(),
			"select TotalTime, Occurrence from BPM_AUDIT_TIME where ProcDefId=?ProcDefId and tracingTag=?tracingTag",
		BPM_AUDIT_VARAGGR.class
		);
		
		oldTime.setProcDefId(procDefId);
		oldTime.setTracingTag(activity.getTracingTag());
		
		oldTime.select();
		
		long occurrence=0;
		long totalTime=0;
		
		if(oldTime.next()){
			occurrence = (oldTime.getOccurrence() !=null ? oldTime.getOccurrence().longValue() : 0);
			totalTime = (oldTime.getTotalTime() !=null ? oldTime.getTotalTime().longValue() : 0);	
		}else{
			return null;
		}
		
		return new Long[]{new Long(totalTime), new Long(occurrence)};
	}*/
	
	interface BPM_AUDIT_VARAGGR extends IDAO{
		Number getProcDefId();
		void setProcDefId(Number versionId);
		
		String getVariableName();
		void setVariableName(String varName);
		
		String getVariableValue();
		void setVariableValue(String value);
		
		Number getOccurrence();
		void setOccurrence(Number occurrence);
	}//spacer is ProcDefId, VariableName, VariableValue

	public void onPropertyChange(Activity activity, ProcessInstance instance, String propertyName, Object changedValue) throws Exception {
		// TODO Auto-generated method stub
		
	}


}