/*
 * Created on 2004. 10. 14.
 */
package org.uengine.util;

import org.uengine.kernel.Activity;
import org.uengine.kernel.ComplexActivity;



import java.util.*;

/**
 * @author Jinyoung Jang
 */
public abstract class ActivityFor {
	boolean stopSignaled = false;
	Object returnValue;

	public abstract void logic(Activity activity);
	
	public void run(Activity activity){
		logic(activity);
		
		if(activity instanceof ComplexActivity){			
			final ActivityFor finalThis = this;
			
			List<Activity> childActivities = ((ComplexActivity)activity).getChildActivities();
			for(int i=0; i<childActivities.size() && !stopSignaled; i++){
				run((Activity)childActivities.get(i));
			}			
		}		
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
