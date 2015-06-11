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


/**
 * @author Jinyoung Jang
 */

public class CostDeterminationFilter implements ActivityFilter, Serializable{
	private static final long serialVersionUID = org.uengine.kernel.GlobalContext.SERIALIZATION_UID;
	public static final String PV_NAME_TOTALCOST = "_totalCost";
	
	public void afterExecute(Activity activity, ProcessInstance instance)
		throws Exception {
	}
				
	public void afterComplete(Activity activity, ProcessInstance instance)
		throws Exception {
		
	
	}

	public void beforeExecute(Activity activity, ProcessInstance instance)
		throws Exception {
	}

	public void onDeploy(ProcessDefinition definition) throws Exception {
	}
	
//	TODO: [performance] must change not to use processvariable as repository
	public static long getTotalCost(ProcessInstance instance) throws Exception{
		try{
			return ((Long)instance.get("", PV_NAME_TOTALCOST)).longValue();
		}catch(Exception e){
			return 0;
		}
	}

	public static void setTotalCost(ProcessInstance instance, long newTC) throws Exception{
		instance.set("", PV_NAME_TOTALCOST, new Long(newTC));
	}

	public void onPropertyChange(Activity activity, ProcessInstance instance, String propertyName, Object changedValue) throws Exception {
		// TODO Auto-generated method stub
		
	}
	
}
