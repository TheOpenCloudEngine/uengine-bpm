package org.uengine.kernel;

import java.util.List;
import java.util.Vector;


/**
 * @author Jinyoung Jang
 */

public class AllActivity extends ComplexActivity{
	private static final long serialVersionUID = org.uengine.kernel.GlobalContext.SERIALIZATION_UID;

	public AllActivity(){
		super();
		setName("All");
	}
	
	protected void onEvent(String command, ProcessInstance instance, Object payload) throws Exception{
				
		//dispatching leaf childs
		if(command.equals(CHILD_COMPENSATED)){
			List<Activity> childs = getChildActivities();
			boolean stillRunning = false;
			for(int i=0; i<childs.size(); i++){
				Activity theChild = (Activity)childs.get(i);
				
				String statusOfTheChild = theChild.getStatus(instance); 
				
				if(statusOfTheChild.equals(Activity.STATUS_RUNNING) || statusOfTheChild.equals(Activity.STATUS_SUSPENDED)){
					stillRunning = true;
					break;
				}
			}
			
			if(!stillRunning){
				reset(instance);
				fireCompensate(instance);
			}
		}else				
		if(command.equals(CHILD_SKIPPED)){
			List<Activity> childs = getChildActivities();
			boolean stillRunning = false;
			for(int i=0; i<childs.size(); i++){
				Activity theChild = (Activity)childs.get(i);
				
				String statusOfTheChild = theChild.getStatus(instance); 
				
				if(statusOfTheChild.equals(Activity.STATUS_RUNNING) || statusOfTheChild.equals(Activity.STATUS_SUSPENDED)){
					stillRunning = true;
					break;
				}
			}
			
			if(!stillRunning){
				fireSkipped(instance);
			}
		}else
		if(command.equals(CHILD_DONE)){
			boolean stillRunning = false;
			List<Activity> childActivities = getChildActivities();
			for(Activity child : childActivities){
				if(!Activity.STATUS_COMPLETED.equals(child.getStatus(instance)))
					stillRunning = true;
			}
			
			if(!stillRunning){
				fireComplete(instance);
			}
		}else		
		if(command.equals(CHILD_RESUMED)){
			ComplexActivity parentActivity = (ComplexActivity)this;
			do{
				parentActivity.setStatus(instance, Activity.STATUS_RUNNING);
				parentActivity = (ComplexActivity)parentActivity.getParentActivity();
			}while(parentActivity!=null);
			
			Activity childActivity = (Activity)payload;
			int activityOrder = getChildActivities().indexOf(childActivity);
			if(activityOrder==-1) throw new UEngineException("Resuming activity is not a child of the parent activity. Some inconsistence status.");
			
			queueActivity(childActivity, instance);
		}else		
			super.onEvent(command, instance, payload);
	}

	protected void executeActivity(ProcessInstance instance) throws Exception{		
		List<Activity> childActivities = getChildActivities();
		for(Activity child : childActivities){
			if(!Activity.STATUS_RUNNING.equals(child.getStatus(instance)))
				queueActivity(child, instance);//child.executeActivity(instance);
		}
	}
	
	protected void onChanged(ProcessInstance instance) throws Exception {
		super.onChanged(instance);
		
		//TODO: may cause dirty read
		if(!instance.isRunning(getTracingTag())) return;
		List<Activity> childActivities = getChildActivities();
		for(Activity child : childActivities){
			if(child.getStatus(instance).equals(Activity.STATUS_READY)){
				queueActivity(child, instance);//child.executeActivity(instance);
			}
		}
	}
	
	public Vector getPreviousActivitiesOf(Activity child){
		return getPreviousActivities();	
	}
	
	public Vector getLastActivities(){
		List<Activity> childs = getChildActivities();
		Vector lastActs = new Vector();
		for(int i=0; i<childs.size(); i++){
			Activity act = childs.get(i);
			
			if(act instanceof ComplexActivity){
				Vector lastActsOfLastAct = ((ComplexActivity)act).getLastActivities();			
				if(lastActsOfLastAct!=null)
					lastActs.addAll(lastActsOfLastAct); 
			}
			else{
				lastActs.add(act);
			}
		}
		
		return lastActs;
	}

	protected void gatherPropagatedActivitiesOf(ProcessInstance instance, Activity child, List list) throws Exception{
		gatherPropagatedActivities(instance, list);
	}

	public void compensateOneStep(ProcessInstance instance) throws Exception{
		//Lets each child activity reset instance
		boolean allChildReset = true;
		
		List<Activity> childActivities = getChildActivities();
		for(Activity child : childActivities){
			String status = child.getStatus(instance);
			
			if(Activity.isCompensatable(status) || status.equals(Activity.STATUS_COMPLETED))
			child.compensateOneStep(instance);
			
			String statusOfChild = child.getStatus(instance);
			if(!(statusOfChild.equals(Activity.STATUS_READY))){
				allChildReset = false;
			}
		}
		
		if(allChildReset){
			super.reset(instance);
			fireCompensate(instance);
		}else{
			setStatus(instance, Activity.STATUS_RUNNING);
		}
	}
}
//
