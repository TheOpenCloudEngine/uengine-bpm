package org.uengine.kernel;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * @author Jinyoung Jang
 */

public class SwitchActivity extends ComplexActivity{
	private static final long serialVersionUID = org.uengine.kernel.GlobalContext.SERIALIZATION_UID;
	private static final String PROP_FINISHEDPATHCOUNT = "_finChilds";
	private static final String PROP_ALLPATHCOUNT = "_allExcChilds";

	Condition[] conditions;
		public Condition[] getConditions() {
			return conditions;
		}
		public void setConditions(Condition[] value) {
//			System.out.println("SwitchActivity::setConditions ");
//			for (int i = 0; i < value.length; i++) {
//				System.out.println("	condition " + i + " = " + value[i]);
//			}
			conditions = value;
			
			firePropertyChangeEvent(new java.beans.PropertyChangeEvent(this, "conditions", conditions, conditions));
		}

	Activity defaultActivity;
		public Activity getDefaultActivity() {
			List<Activity> childs = getChildActivities();
			
			if(childs.size()==1)
				return null;
			
			// 01.16 김형국 - 디자이너에서 더블클릭시 child가 없기때문에.. 우선 추가함 TODO 삭제
			if(childs.size()==0)
				return null;
			defaultActivity = childs.get(childs.size() - 1);

			return defaultActivity;
		}
		
	boolean operateAsIfThenElse;
		public boolean isOperateAsIfThenElse() {
			return operateAsIfThenElse;
		}
		public void setOperateAsIfThenElse(boolean operateAsIfThenElse) {
			this.operateAsIfThenElse = operateAsIfThenElse;
		}
		
	public SwitchActivity(){
		super();
		setName("");
	}
	
	public void setFinishedPathCount(ProcessInstance instance, int val) throws Exception{
		instance.setProperty(getTracingTag(), PROP_FINISHEDPATHCOUNT, new Integer(val));
	}
	public int getFinishedPathCount(ProcessInstance instance) throws Exception{
		 Serializable value = instance.getProperty(getTracingTag(), PROP_FINISHEDPATHCOUNT);
		 
		 if(value == null) return 0;
		 if(!(value instanceof Integer)) throw new UEngineException("Internal Error:Property value should be Integer");
		 return ((Integer)value).intValue();
	}

	private Integer Serializable(Integer integer) {
		// TODO Auto-generated method stub
		return null;
	}
	public void setAllPathCount(ProcessInstance instance, int val) throws Exception{
		instance.setProperty(getTracingTag(), PROP_ALLPATHCOUNT, new Integer(val));
	}
	public int getAllPathCount(ProcessInstance instance) throws Exception{
		 Serializable value = instance.getProperty(getTracingTag(), PROP_ALLPATHCOUNT);
		 
		 if(value == null) return 0;
		 if(!(value instanceof Integer)) throw new UEngineException("Internal Error: Property value should be Integer");
		 return ((Integer)value).intValue();
	}
	
	public SwitchActivity(Condition[] conditions, Activity[] childActivities){
		super();
		ArrayList<Activity> chidActivity = new ArrayList<Activity>();
		if( childActivities != null ){
			for(int i =0; i < childActivities.length; i++){
				chidActivity.add(childActivities[i]);
			}
		}
		setChildActivities(chidActivity);
		this.conditions = conditions;
	}

	protected void executeActivity(ProcessInstance instance) throws Exception{
		
		boolean bMatchEvenOnlyOnce = false;
				
		int i=0;
		List queuingActivities = new ArrayList();
		List<Activity> childActivities = getChildActivities();
		for(Activity child : childActivities){
//			if(!enum.hasMoreElements()) break;
			
System.out.println("	conditions[i]: "+conditions[i]);
			if(conditions[i]==null)
				throw new UEngineException("Condition must be specified.", "Some of conditions need to evaluate the transition is empty.");
//System.out.println("	i"+i);
							//we ignore 'scope' in version 1.0

			if(conditions[i] instanceof Otherwise && bMatchEvenOnlyOnce){
				break;
			}
			
			if(conditions[i].isMet(instance, "")){
				bMatchEvenOnlyOnce = true;
				setCurrentStep(instance, i);
				//queueActivity(child, instance);
				queuingActivities.add(child);
			}
			
			if(bMatchEvenOnlyOnce && isOperateAsIfThenElse()) break;
			
			i++;
		}
		
		setAllPathCount(instance, queuingActivities.size());
		for(int j=0; j<queuingActivities.size(); j++){
			queueActivity((Activity)queuingActivities.get(j), instance);
		}
		
		if(!bMatchEvenOnlyOnce){ 
			fireComplete(instance);
//			Activity defaultActivity = getDefaultActivity();
//			
//			if(defaultActivity==null){
//				fireComplete(instance);
//			}else{
//				setCurrentStep(instance, getChildActivities().size()-1);
//				queueActivity(getDefaultActivity(), instance);
//			}
		}
	}
	
	
	protected void onEvent(String command, ProcessInstance instance, Object payload) throws Exception{
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
			fireSkipped(instance);
		}else 
		if(command.equals(CHILD_DONE)){
			List<Activity> childs = getChildActivities();
			boolean stillRunning = false;
			
			int finishedPathCnt = getFinishedPathCount(instance) + 1;
			setFinishedPathCount(instance, finishedPathCnt);
			stillRunning = (getAllPathCount(instance) > finishedPathCnt);
			
			/*for(int i=0; i<childs.size(); i++){
				Activity theChild = (Activity)childs.get(i);
				
				String statusOfTheChild = theChild.getStatus(instance); 
				
				if(statusOfTheChild.equals(Activity.STATUS_RUNNING) || statusOfTheChild.equals(Activity.STATUS_SUSPENDED)){
					stillRunning = true;
					break;
				}
			}*/
			
			if(!stillRunning){
				fireComplete(instance);
			}
		}else
		if(command.equals(CHILD_RESUMED)){
			//mark the status as running since its child activity became running.
			ComplexActivity parentActivity = (ComplexActivity)this;
			do{
				parentActivity.setStatus(instance, Activity.STATUS_RUNNING);
				parentActivity = (ComplexActivity)parentActivity.getParentActivity();
			}while(parentActivity!=null);
			//
			
			Activity childActivity = (Activity)payload;
			int activityOrder = getChildActivities().indexOf(childActivity);
			if(activityOrder==-1) throw new UEngineException("Resuming activity is not a child of the parent activity. Some inconsistence status.");
			
			queueActivity(childActivity, instance);
			//super.onEvent(command, instance, payload);//replicate this msg to the super class
		}else		
			super.onEvent(command, instance, payload);
	}
	
/*	protected void executeActivity(ActivityInstance instance) throws Exception{
		Activity childActivity = (Activity)getChildActivities().elementAt(0);

		queueActivity(childActivity, instance);
	}
*/

	public ValidationContext validate(Map options){
		ValidationContext validationContext  = super.validate(options);
		
		if(getChildActivities()!= null)
		if(getConditions()==null || getChildActivities().size() != getConditions().length){
			validationContext.add(getActivityLabel()+" all of the conditions for each child activity must be specified." );
		}else{			
			Condition[] conditions = getConditions();
			for(int i=0; i<conditions.length; i++){
				Condition condition = conditions[i];
				HashMap map = new HashMap(1);
				map.put("activity", this);
				
				validationContext.addAll(condition.validate(map));
				
				if(conditions[i] instanceof Otherwise && i < conditions.length-1){
					validationContext.add(getActivityLabel()+" otherwise condition should be in the last condition.");
				}
				
				if(conditions[i] instanceof Evaluate){
					Evaluate evaluate = (Evaluate)conditions[i];
					if(evaluate.getKey() == null){
						validationContext.add(getActivityLabel()+" Evaluation key should be not-null.");
					}else{
						if(getProcessDefinition().getProcessVariable(evaluate.getKey())==null){
							validationContext.add(getActivityLabel()+" Evaluation key ["+evaluate.getKey()+"] is not declared.");
							
						}
					}
				}
			}
		}
		
		return validationContext;
	}
	
	protected void gatherPropagatedActivitiesOf(ProcessInstance instance, Activity child, List list) throws Exception{
		gatherPropagatedActivities(instance, list);
	}
	
	public void compensateOneStep(ProcessInstance instance) throws Exception{
		setFinishedPathCount(instance, 0);
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
			reset(instance); //The reason why it didn't call 'super.compensate()' directly is that the super class is ComplexActivity, which would compensate all of the childs.
			fireCompensate(instance);
		}else{
			setStatus(instance, Activity.STATUS_RUNNING);
		}
	}

	public void reset(ProcessInstance instance) throws Exception {
		super.reset(instance);
		setFinishedPathCount(instance, 0);
	}
	
	protected boolean compensateChild(ProcessInstance instance, Activity child) throws Exception{
		if(super.compensateChild(instance, child)){
			int finishedPathCnt = getFinishedPathCount(instance) - 1;
			setFinishedPathCount(instance, finishedPathCnt);
			
			return true;
		}else{
			return false;
		}
	}


}

