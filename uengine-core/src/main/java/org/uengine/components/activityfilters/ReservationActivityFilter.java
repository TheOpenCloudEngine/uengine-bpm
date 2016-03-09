package org.uengine.components.activityfilters;


import java.util.List;
import java.util.Map;

import org.uengine.kernel.Activity;
import org.uengine.kernel.GlobalContext;
import org.uengine.kernel.HumanActivity;
import org.uengine.kernel.KeyedParameter;
import org.uengine.kernel.ProcessDefinition;
import org.uengine.kernel.ProcessInstance;
import org.uengine.kernel.RoleMapping;
import org.uengine.kernel.SensitiveActivityFilter;
import org.uengine.webservices.worklist.WorkList;


public class ReservationActivityFilter implements SensitiveActivityFilter {

	private static final long serialVersionUID = org.uengine.kernel.GlobalContext.SERIALIZATION_UID;

	private void addReservedWorklist(Activity activity, ProcessInstance instance) throws Exception {
		ProcessDefinition	pd = instance.getProcessDefinition();
		List<Activity>			activities = pd.getChildActivities();
		
		for (int i = 0; i < activities.size(); i++) {
			Activity	a = (Activity)activities.get(i);
			
			if (a.getTracingTag().equals(activity.getTracingTag()))		continue;		// 자기 자신은 등록하지 않는다.
			
			// 바로 다음 Activity는 등록하지 않는다. (어차피 NEW로 등록된다.)
//			if (a.getPreviousActivities().size() == 1)		continue;
			
			if (a instanceof HumanActivity) {
				HumanActivity	ha = (HumanActivity)a;
				
				RoleMapping	rm = ha.getRole().getMapping(instance);
				
				if (rm != null) {
					// 아래는 HumanActivity의 reserveWorkItem() 메소드를 옮겨온 것이다.
					WorkList worklist = instance.getWorkList();
					
					Map kpv = ha.createParameter(instance);
					KeyedParameter[] parameters = KeyedParameter.fromMap(kpv);		

					RoleMapping roleMapping = ha.getRole().getMapping(instance, ha.getTracingTag());		
					String[] taskIds = new String[roleMapping.size()];
					
					int idx = 0;
					do{
						taskIds[idx++] = worklist.reserveWorkItem(roleMapping.getEndpoint(), parameters, instance.getProcessTransactionContext());				
					}while(roleMapping.next());
					
					StringBuffer taskId = new StringBuffer();
					if (taskIds != null) {
						for (int j = 0; j < taskIds.length; j++) {
							if (taskId.length() > 0) taskId.append(",");
							taskId.append(taskIds[j]);
						}
					}
					
					instance.setProperty(ha.getTracingTag(), HumanActivity.PVKEY_TASKID, taskId.toString());
				}
			}
		}
	}
	
	public void afterComplete(Activity activity, ProcessInstance instance)
			throws Exception {
		if (instance.isNew())		
			addReservedWorklist(activity, instance);
		
	}

	public void afterExecute(Activity activity, ProcessInstance instance)
			throws Exception {

	}

	public void beforeExecute(Activity activity, ProcessInstance instance)
			throws Exception {
		// TODO Auto-generated method stub

	}

	public void onDeploy(ProcessDefinition definition) throws Exception {
		// TODO Auto-generated method stub

	}

	/**
	 * 프로세스 중에 RoleMapping이 변경된 경우, 변경된 Role을 예정업무로 등록한다.
	 * RoleMapping이 변경되는 경우는 프로세스 디자인에서 담당자가 정해지지 않고, 진행 중에 정해지는 경우이므로
	 * 정해진 담당자를 예약 업무 담당자로 등록한다.
	 */
	public void onPropertyChange(Activity activity, ProcessInstance instance,
			String propertyName, Object changedValue) throws Exception {
		
		if (propertyName.equals("roleMapping") && !instance.isNew()) {
			RoleMapping	rm = (RoleMapping)changedValue;
			ProcessDefinition	pd = (ProcessDefinition)activity;
			String			roleName = rm.getName();
			
			// 같은 roleName을 갖는 Activity를 찾는다.
			List<Activity>	activities = pd.getChildActivities();
			for (int i = 0; i < activities.size(); i++) {
				Activity	a = (Activity)activities.get(i);
				if (a instanceof HumanActivity) {
					HumanActivity	ha = (HumanActivity)a;
					if (ha.getRole().getName().equals(roleName)) {
						// 변경된 Role 추가
						WorkList	workList = instance.getWorkList();
						String	userId = rm.getEndpoint();
						
						workList.reserveWorkItem(userId, KeyedParameter.fromMap(ha.createParameter(instance)), instance.getProcessTransactionContext());
					}
				}
			}
		}

	}

	public void onEvent(Activity activity, ProcessInstance instance, String eventName, Object payload) throws Exception {
		System.err.println("Activity : " + activity.getName() + ", event = " + eventName);
	}
}

