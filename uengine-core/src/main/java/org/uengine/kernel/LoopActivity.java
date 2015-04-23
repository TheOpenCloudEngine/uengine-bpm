package org.uengine.kernel;

import java.io.Serializable;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.metaworks.annotation.Hidden;
import org.uengine.modeling.ElementView;


/**
 * @author Jinyoung Jang
 */

public class LoopActivity extends ComplexActivity{
	private static final long serialVersionUID = org.uengine.kernel.GlobalContext.SERIALIZATION_UID;
	private static final String PROP_LOOPCOUNT = "_loopCnt";

	Condition loopingCondition;
		@Hidden
		public Condition getLoopingCondition() {
			return loopingCondition;
		}
		public void setLoopingCondition(Condition value) {
			loopingCondition = value;
		}
		
	boolean worklistHistoryManagement;		
		@Hidden
		public boolean isWorklistHistoryManagement() {
			return worklistHistoryManagement;
		}
		public void setWorklistHistoryManagement(boolean worklistHistoryManagement) {
			this.worklistHistoryManagement = worklistHistoryManagement;
		}
		
	public void setLoopingCount(ProcessInstance instance, int val) throws Exception{
		instance.setProperty(getTracingTag(), PROP_LOOPCOUNT, new Integer(val));
	}
	public int getLoopingCount(ProcessInstance instance) throws Exception{
		 Serializable value = instance.getProperty(getTracingTag(), PROP_LOOPCOUNT);
		 
		 if(value == null) return 0;
		 if(!(value instanceof Integer)) throw new UEngineException("Internal Error:Property value should be Integer");
		 return ((Integer)value).intValue();
	}
		
	public LoopActivity(){
		super();
		setName("");
		setWorklistHistoryManagement(true);
	}	
		
	protected void executeActivity(ProcessInstance instance) throws Exception{
		boolean returned = false;
		
		int currStep = getCurrentStep(instance);
		if( currStep >= getChildActivities().size()){
			if(loopingCondition==null)
				throw new UEngineException("Condition is not specified.");
				
			if(loopingCondition.isMet(instance, "")){
				
				//Lets each child activity reset instance
				List<Activity> childActivities = getChildActivities();
				for(Activity child : childActivities){

					if(child instanceof HumanActivity){
						if(isWorklistHistoryManagement()){
							child.reset(instance);
						}else{
							if(isSuspendable(child.getStatus(instance))){
								child.reset(instance);
							}
						}
						
						child.setStatus(instance, STATUS_READY);
						child.setStartedTime(instance, (Calendar)null);
					}else{
						child.reset(instance);
						child.setStartedTime(instance, (Calendar)null);
					}
					
				}
				
//				instance.setStatus(getTracingTag(), STATUS_READY);
				
				currStep = 0;
				setCurrentStep(instance, currStep);
				
				setLoopingCount(instance, getLoopingCount(instance) + 1);
				
				returned = true;
				
			}else{	
				fireComplete(instance);
				
				return;
			}
		}
		
		Activity childActivity = getChildActivities().get(currStep);
		queueActivity(childActivity, instance);
		
		if(returned){
			fireEventToActivityFilters(instance, "loop-returned", this);
		}
	}
	
	private static String getNewStringFromConsole(){
		try{
			java.io.DataInputStream bis = new java.io.DataInputStream(new java.io.BufferedInputStream(System.in));
			String temp = bis.readLine();
			return temp;
			
		}catch(Exception e){
			return null;
		}		
	}

	public ValidationContext validate(Map options) {
		ValidationContext vc = super.validate(options);
		
		try{
			Condition condition = getLoopingCondition();
			
			if(condition==null)
				vc.add("Condition should be specified.");
			
			HashMap map = new HashMap(1);
			map.put("activity", this);				
			vc.addAll(condition.validate(map));			
		}catch(Exception e){}
		 		
		return vc;
	}
	
	public ElementView createView(){
		ElementView elementView = null;
		try {
			elementView = (ElementView)Thread.currentThread().getContextClassLoader().loadClass("org.uengine.kernel.designer.ui.LoopActivityView").newInstance();
			elementView.setElement(this);
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return elementView;
	}

}

