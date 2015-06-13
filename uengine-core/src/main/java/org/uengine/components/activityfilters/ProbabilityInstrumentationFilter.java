/*
 * Created on 2004. 12. 19.
 */
package org.uengine.components.activityfilters;

import java.io.Serializable;

import org.uengine.kernel.Activity;
import org.uengine.kernel.ActivityFilter;
import org.uengine.kernel.GlobalContext;
import org.uengine.kernel.ProcessDefinition;
import org.uengine.kernel.ProcessInstance;
import org.uengine.kernel.SwitchActivity;
import org.uengine.util.dao.ConnectionFactory;
import org.uengine.util.dao.DefaultConnectionFactory;
import org.uengine.util.dao.GenericDAO;
import org.uengine.util.dao.IDAO;


/**
 * @author Jinyoung Jang
 */
public class ProbabilityInstrumentationFilter implements ActivityFilter, Serializable{
	private static final long serialVersionUID = org.uengine.kernel.GlobalContext.SERIALIZATION_UID;
	
	public void afterExecute(Activity activity, ProcessInstance instance)
		throws Exception {
	}
				
	public void afterComplete(Activity activity, ProcessInstance instance)
		throws Exception {

		if(!(activity instanceof SwitchActivity)) return;
		
		SwitchActivity switchActivity = (SwitchActivity)activity;
		int selectionPath = switchActivity.getCurrentStep(instance);
		Long procDefId = new Long(switchActivity.getProcessDefinition().getBelongingDefinitionId());
		Long pathNo = new Long(selectionPath);

		boolean isNew = false;
		long occurrence=0;
		long total=0;

		Long[] occurrenceAndTotal = getOccurrenceAndTotal(switchActivity, selectionPath);
		if(occurrenceAndTotal==null){
			isNew = true;
		}else{
			occurrence=occurrenceAndTotal[0].longValue();
			total=occurrenceAndTotal[1].longValue();
		}
		
		occurrence++;
		
		String sql = (isNew?
		"insert into BPM_AUDIT_PRBLTY(ProcDefId, TracingTag, PathNo, Occurrence) values (?ProcDefId, ?TracingTag, ?PathNo, ?Occurrence)":
		"update BPM_AUDIT_PRBLTY set Occurrence=?Occurrence where (ProcDefId = ?ProcDefId and TracingTag = ?TracingTag and PathNo = ?PathNo)"
		);
		 
		BPM_AUDIT_PRBLTY newProbability = (BPM_AUDIT_PRBLTY)GenericDAO.createDAOImpl(
			DefaultConnectionFactory.create(),
			sql,
			BPM_AUDIT_PRBLTY.class
		);
		
		newProbability.setProcDefId(procDefId);
		newProbability.setTracingTag(switchActivity.getTracingTag());
		newProbability.setPathNo(pathNo);
		newProbability.setOccurrence(new Long(occurrence));
		
		newProbability.insert();		
	}

	public void beforeExecute(Activity activity, ProcessInstance instance)
		throws Exception {
	}

	public void onDeploy(ProcessDefinition definition) throws Exception {
	}
	
	public static Long[] getOccurrenceAndTotal(SwitchActivity switchActivity, int selectionPath) throws Exception{
		Long procDefId = new Long(switchActivity.getProcessDefinition().getBelongingDefinitionId());
		Long pathNo = new Long(selectionPath);
		
		BPM_AUDIT_PRBLTY oldProbability = (BPM_AUDIT_PRBLTY)GenericDAO.createDAOImpl(
			DefaultConnectionFactory.create(),
			"select Occurrence, (select sum(occurrence) from BPM_AUDIT_PRBLTY where ProcDefId=?ProcDefId and tracingTag=?tracingTag) as total from BPM_AUDIT_PRBLTY where pathNo=?pathNo and ProcDefId=?ProcDefId and tracingTag=?tracingTag",
			BPM_AUDIT_PRBLTY.class
		);
		
		oldProbability.setProcDefId(procDefId);
		oldProbability.setTracingTag(switchActivity.getTracingTag());
		oldProbability.setPathNo(pathNo);
		
		oldProbability.select();
		
		long occurrence=0;
		long total=0;
		
		if(oldProbability.next()){
			occurrence = (oldProbability.getOccurrence() !=null ? oldProbability.getOccurrence().longValue() : 0);
			total = (oldProbability.getTotal() !=null ? oldProbability.getTotal().longValue() : 0);	
		}else{
			return null;
		}
		
		//close CachedRowSet 
		oldProbability.releaseResource();
		
		return new Long[]{new Long(occurrence), new Long(total)};
	}
	
	interface BPM_AUDIT_PRBLTY extends IDAO{
		Number getProcDefId();
		void setProcDefId(Number versionId);
		
		String getTracingTag();
		void setTracingTag(String tracingTag);
		
		Number getPathNo();
		void setPathNo(Number pathNo);
		
		Number getOccurrence();
		void setOccurrence(Number occurrence);
		
		Number getTotal();
		void setTotal(Number total);
	}
/*
CREATE TABLE BPM_AUDIT_PRBLTY(
  PROCDEFID NUMBER (19)     NOT NULL,
  TRACINGTAG   VARCHAR2 (200)  NOT NULL,
  PATHNO       NUMBER (3),
  OCCURRENCE   NUMBER (19),
  CONSTRAINT PK_BPM_AUDIT_PRBLTY
  PRIMARY KEY ( PROCDEFID,TRACINGTAG,PATHNO)
)
*/

	public void onPropertyChange(Activity activity, ProcessInstance instance, String propertyName, Object changedValue) throws Exception {
		// TODO Auto-generated method stub
		
	}

}
