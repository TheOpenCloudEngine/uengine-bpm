/*
 * Created on 2004. 12. 19.
 */
package org.uengine.components.activityfilters;

import java.io.Serializable;

import org.uengine.kernel.Activity;
import org.uengine.kernel.ActivityFilter;
import org.uengine.kernel.GlobalContext;
import org.uengine.kernel.HumanActivity;
import org.uengine.kernel.ProcessDefinition;
import org.uengine.kernel.ProcessInstance;
import org.uengine.util.dao.ConnectionFactory;
import org.uengine.util.dao.DefaultConnectionFactory;
import org.uengine.util.dao.GenericDAO;
import org.uengine.util.dao.IDAO;


/**
 * @author Jinyoung Jang
 */
public class TimeDeterminationFilter implements ActivityFilter, Serializable{
	private static final long serialVersionUID = org.uengine.kernel.GlobalContext.SERIALIZATION_UID;
	
	public void afterExecute(Activity activity, ProcessInstance instance)
		throws Exception {
	}
				
	public void afterComplete(Activity activity, ProcessInstance instance)
		throws Exception {

		//TODO: [for performance] for performance, time checking for non-human activity is omitted
		if(!(activity instanceof HumanActivity)) return;
		
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
			DefaultConnectionFactory.create(),
			sql,
			BPM_AUDIT_TIME.class
		);
		
		newProbability.setProcDefId(procDefId);
		newProbability.setTracingTag(humanActivity.getTracingTag());
		newProbability.setTotalTime(new Long(totalTime));
		newProbability.setOccurrence(new Long(occurrence));
		
		newProbability.insert();		
	}

	public void beforeExecute(Activity activity, ProcessInstance instance)
		throws Exception {
	}

	public void onDeploy(ProcessDefinition definition) throws Exception {
	}
	
	public static Long[] getTotalTimeAndOccurrence(Activity activity) throws Exception{
		Long procDefId = new Long(activity.getProcessDefinition().getBelongingDefinitionId());
		
		BPM_AUDIT_TIME oldTime = (BPM_AUDIT_TIME)GenericDAO.createDAOImpl(
			DefaultConnectionFactory.create(),
			"select TotalTime, Occurrence from BPM_AUDIT_TIME where ProcDefId=?ProcDefId and tracingTag=?tracingTag",
			BPM_AUDIT_TIME.class
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
		
		//close CachedRowSet 
		oldTime.releaseResource();
		
		return new Long[]{new Long(totalTime), new Long(occurrence)};
	}
	
	interface BPM_AUDIT_TIME extends IDAO{
		Number getProcDefId();
		void setProcDefId(Number versionId);
		
		String getTracingTag();
		void setTracingTag(String tracingTag);
		
		Number getTotalTime();
		void setTotalTime(Number pathNo);
		
		Number getOccurrence();
		void setOccurrence(Number occurrence);
	}
/*
CREATE TABLE BPM_AUDIT_TIME(
  PROCDEFID NUMBER (19)     NOT NULL,
  TRACINGTAG   VARCHAR2 (200)  NOT NULL,
  OCCURRENCE   NUMBER (19),
  TOTALTIME   NUMBER (19),
  CONSTRAINT PK_BPM_AUDIT_TIME
  PRIMARY KEY ( PROCDEFID,TRACINGTAG)
)
*/

	public void onPropertyChange(Activity activity, ProcessInstance instance, String propertyName, Object changedValue) throws Exception {
		// TODO Auto-generated method stub
		
	}

}
