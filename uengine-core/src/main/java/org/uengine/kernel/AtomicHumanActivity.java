package org.uengine.kernel;

import java.util.List;
import java.util.Map;

public class AtomicHumanActivity extends ComplexActivity{

	public void executePreActivities(ProcessInstance instance) throws Exception{
		
		instance.addActivityEventInterceptor(new ActivityEventInterceptor(){

			public boolean interceptEvent(Activity activity, String command, ProcessInstance instance, Object payload) throws Exception {
				if(getChildActivities().contains(activity) && Activity.ACTIVITY_DONE.equals(command)){
					StackTraceElement[] stack = Thread.currentThread().getStackTrace();
					
					if(stack.length>1)
					for(int i=stack.length-1; i>0; i--){
						StackTraceElement stackElement = stack[i];
						if("executePreActivities".equals(stackElement.getMethodName())){
							return true;
						}
					}
					
				}
				
				return false;
			}
			
		});
		List childActs = getChildActivities();
		for(int i=0; !(childActs.get(i) instanceof HumanActivity); i++){
			Activity childAct = (Activity)childActs.get(i);
			childAct.executeActivity(instance);
		}
	}
	
	@Override
	public ValidationContext validate(Map options) {
		
		ValidationContext vc = super.validate(options);
		
		boolean atLeastHasOneHumanActivity = false;
		List childActs = getChildActivities();
		for(int i=0; i<childActs.size(); i++){
			Activity child = (Activity) childActs.get(i);
			if(child instanceof HumanActivity){
				atLeastHasOneHumanActivity = true;
				continue;
			}
		}
		
		if(!atLeastHasOneHumanActivity){
			vc.add("Must have one human activity as child");
		}
		
		return vc;
	}
	
	
	

}
