package org.uengine.kernel;

import java.util.HashMap;

import org.uengine.contexts.ActivitySelectionContext;

public class FlagActivity extends DefaultActivity {
	static final String PREFIX_PROP_KEY_FLAGS = "FLAG_";
	static final String PROP_KEY_FLAGS = "FLAGS";
	
	public FlagActivity(){
		setName("Flag");
	}
	
	String flag;
		public String getFlag() {
			return flag;
		}
		public void setFlag(String flag) {
			this.flag = flag;
		}

	protected void executeActivity(ProcessInstance instance) throws Exception {
		ActivitySelectionContext asc = new ActivitySelectionContext();
		asc.setInstanceId(instance.getInstanceId());
		asc.setTracingTag(getTracingTag());
		
		ProcessInstance rootInstance = instance.getRootProcessInstance();
		
		rootInstance.setProperty("", PREFIX_PROP_KEY_FLAGS + getFlag(), asc);

		fireComplete(instance);
	}
	
	static public ActivitySelectionContext getFlagLocationForFlowChart(ProcessInstance instance, String flag) throws Exception{
		ProcessInstance rootInstance = null;
		
		if (instance instanceof DefaultProcessInstance) {
			rootInstance = instance;
		} else {
			rootInstance = instance.getRootProcessInstance();
		}
		
		ActivitySelectionContext asc = (ActivitySelectionContext) rootInstance.getProperty("", PREFIX_PROP_KEY_FLAGS + flag);

		// for old version 
		if(asc==null){
			HashMap flags = (HashMap) rootInstance.getProperty("", PROP_KEY_FLAGS);
			
			if(flags!=null)
				return (ActivitySelectionContext) flags.get(flag);
		}
		
		return asc;
	}
	
	static public ActivitySelectionContext getFlagLocation(ProcessInstance instance, String flag) throws Exception{
		ProcessInstance rootInstance = instance.getRootProcessInstance();
		ActivitySelectionContext asc = (ActivitySelectionContext) rootInstance.getProperty("", PREFIX_PROP_KEY_FLAGS + flag);

		// for old version 
		if(asc==null){
			HashMap flags = (HashMap) rootInstance.getProperty("", PROP_KEY_FLAGS);
			
			if(flags!=null)
				return (ActivitySelectionContext) flags.get(flag);
		}
		
		return asc;
	}
	
}
