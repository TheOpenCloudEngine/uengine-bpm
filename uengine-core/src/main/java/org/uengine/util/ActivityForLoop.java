/*
 * Created on 2004. 10. 14.
 */
package org.uengine.util;

import org.uengine.kernel.Activity;
import org.uengine.kernel.ComplexActivity;
import org.uengine.kernel.ProcessInstance;




import java.util.*;

/**
 * @author Jinyoung Jang
 */
public abstract class ActivityForLoop {
	boolean stopSignaled = false;
	Object returnValue;

	public abstract void logic(Activity activity);
	
	public void run(Activity activity, boolean backward){
		logic(activity);
		
		if(activity instanceof ComplexActivity){			
			final ActivityForLoop finalThis = this;
			
			List<Activity> childActivities = ((ComplexActivity)activity).getChildActivities();
			if(backward){
				for(int i=childActivities.size()-1; i>-1 && !stopSignaled; i--){
					run((Activity)childActivities.get(i), backward);
				}
			}else{
				for(int i=0; i<childActivities.size() && !stopSignaled; i++){
					run((Activity)childActivities.get(i), backward);
				}
			}
		}		
	}
	
	public void run(Activity activity, ProcessInstance instance, boolean backward){
		logic(activity);
		
		if(activity instanceof ComplexActivity){			
			final ActivityForLoop finalThis = this;
			
			List<Activity> childActivities = ((ComplexActivity)activity).getChildActivities();
			if(backward){
				for(int i=childActivities.size()-1; i>-1 && !stopSignaled; i--){
					run((Activity)childActivities.get(i), backward);
				}
			}else{
				for(int i=0; i<childActivities.size() && !stopSignaled; i++){
					run((Activity)childActivities.get(i), backward);
				}
			}
		}		
	}
	
	public void runBackward(Activity activity){
		run(activity, true);
	}

	public void run(Activity activity){
		run(activity, false);
	}
	
	public void stop(){
		stopSignaled = true;
	}

	public void stop(Object returnValue){
		stopSignaled = true;
		setReturnValue(returnValue);
	}
	
	public Object getReturnValue() {
		return returnValue;
	}
	public void setReturnValue(Object returnValue) {
		this.returnValue = returnValue;
	}
}
