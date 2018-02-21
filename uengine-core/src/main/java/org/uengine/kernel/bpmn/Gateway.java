package org.uengine.kernel.bpmn;

import org.uengine.kernel.*;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Gateway extends Activity {

	public Gateway() {
		setName("Exclusive");
	}
	
	public Gateway(String name) {
		super(name);
	}

	@Override
	protected void executeActivity(ProcessInstance instance) throws Exception {
	    
	    addLoopBackCount(instance);
	    
		if(isCompletedAllOfPreviousActivities(instance)) {
			fireComplete(instance);
		}
	}

	//split logic
	@Override
	public List<Activity> getPossibleNextActivities(ProcessInstance instance, String scope) throws Exception {
		List<Activity> activities = new ArrayList<Activity>();
		/* TODO gate 에서 무조건 추가하는 건가요?? otherwise 도??
		Transition transition = null;
		for (Iterator<Transition> it = getOutgoingTransitions().iterator(); it.hasNext();) {
			transition = it.next();
			if (transition.isMet(instance, scope)) {
				activities.add(transition.getTargetActivity());
			}
		}
		return activities;
		*/

//			System.out.println("outgoingTransitions: " + getOutgoingTransitions().size());
		boolean otherwiseFlag = false;
		Activity otherwiseActivity = null;
		for (Iterator<SequenceFlow> it = getOutgoingSequenceFlows().iterator(); it.hasNext(); ) {
			SequenceFlow ts = (SequenceFlow)it.next();
			if( ts.getCondition() != null){
				Condition condition = ts.getCondition();
				if( condition.isMet(instance, scope) ){
					if( condition instanceof Or){
						Condition[] condis =  ((Or) condition).getConditions();
						if( condis.length > 0 && condis[0] instanceof Otherwise){
							// 순서가 없다보니 Otherwise가 먼저와서 무조건 true 가 발생하는 경우가 생김
							// Otherwise가 먼저 올 경우는 일단 스킵했다가 다시 해준다.
							otherwiseFlag = true;
							otherwiseActivity = ts.getTargetActivity();
							continue;
						}
					}
					else if (condition instanceof Otherwise) {
						otherwiseFlag = true;
						otherwiseActivity = ts.getTargetActivity();
						continue;
					}
					activities.add(ts.getTargetActivity());
				}
			}else{
				activities.add(ts.getTargetActivity());
			}
		}
		if( otherwiseFlag && activities.isEmpty()){
			activities.add(otherwiseActivity);
		}
		return activities;
	}

	//join logic
	protected boolean isCompletedAllOfPreviousActivities(ProcessInstance instance) throws Exception{
		return !hasTokenInPreviousActivities(instance, this);	
	}
	
	public static boolean hasTokenInPreviousActivities(ProcessInstance instance, Activity activity) throws Exception {
		List<Activity> blockMembers = BlockFinder.getBlockMembers(activity);

		//debug here under condition 'getTracingTag().equals("a7")'
		if(blockMembers.size() > 1) {

			List<Activity> possibleBlockMembers = BlockFinder.getPossibleBlockMembers(blockMembers, instance);
			for (Activity blockMember : possibleBlockMembers) {
				String status = blockMember.getStatus(instance);

				if (Activity.STATUS_READY.equals(status) || Activity.STATUS_RUNNING.equals(status) || Activity.STATUS_SUSPENDED.equals(status)) { //with catching event incoming sequence flows, old implemntation was problematic (
					//debug here under condition 'getTracingTag().equals("a7")' --------- when the a11 and 12 never executed
					return true;
				}
			}
		}

		//debug here under condition 'getTracingTag().equals("a7")' --------- when the a11 and 12 ran twice
		return false;
	}




}
