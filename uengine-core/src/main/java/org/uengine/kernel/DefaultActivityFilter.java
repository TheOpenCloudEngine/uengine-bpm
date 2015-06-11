/*
 * Created on 2004. 10. 9.
 */
package org.uengine.kernel;

import org.apache.bsf.*;
import org.metaworks.FieldDescriptor;
import org.metaworks.Type;
import org.metaworks.inputter.DateInput;

/**
 * @author Jinyoung Jang
 */
public class DefaultActivityFilter implements ActivityFilter{
	private static final long serialVersionUID = org.uengine.kernel.GlobalContext.SERIALIZATION_UID;

	ActivityFilter activityFilter;
	
	public static void metaworksCallback_changeMetadata(Type type){
		FieldDescriptor fd = type.getFieldDescriptor("AfterExecuteScript");
		fd.setInputter(new DateInput());
	}
	
	protected void runScript(String script, Activity activity, ProcessInstance instance) throws BSFException{
		BSFManager manager = new BSFManager();
		manager.setClassLoader(this.getClass().getClassLoader());
	
		manager.declareBean("instance", instance, ProcessInstance.class);
		manager.declareBean("activity", activity, Activity.class);
//		manager.declareBean("globalContext", GlobalContext.getInstance(), GlobalContext.class);
		manager.declareBean("util", new ScriptUtil(), ScriptUtil.class);
		
		BSFEngine engine = manager.loadScriptingEngine("javascript");
			
		Object result = engine.eval("my_class.my_generated_method",0,0,"function getVal(){\n"+ script + "}\ngetVal();");
		
	}
	
	public void onDeploy(ProcessDefinition definition) throws Exception {
		// TODO Auto-generated method stub
	}
	
	public void afterComplete(Activity activity, ProcessInstance instance) throws Exception{
		if(isUseScript()){
			runScript(getAfterCompleteScript(), activity, instance);
		}else if(getFilterClass()!=null){
			if(activityFilter==null)
				try{
					activityFilter = (ActivityFilter)Thread.currentThread().getContextClassLoader().loadClass(getFilterClass()).newInstance();
				}catch(Exception e){
					e.printStackTrace();
				}
			
			activityFilter.afterComplete(activity, instance);
		}
		
	}

	public void afterExecute(Activity activity, ProcessInstance instance)
		throws Exception {
		if(isUseScript()){
			runScript(getAfterExecuteScript(), activity, instance);
		}else if(getFilterClass()!=null){
			if(activityFilter==null)
				try{
					activityFilter = (ActivityFilter)Thread.currentThread().getContextClassLoader().loadClass(getFilterClass()).newInstance();
				}catch(Exception e){
					e.printStackTrace();
				}
		
			activityFilter.afterExecute(activity, instance);
		}
	}

	public void beforeExecute(Activity activity, ProcessInstance instance) throws Exception{
		if(isUseScript()){
			runScript(getBeforeExecuteScript(), activity, instance);
		}else if(getFilterClass()!=null){
			if(activityFilter==null)
				try{
					activityFilter = (ActivityFilter)Thread.currentThread().getContextClassLoader().loadClass(getFilterClass()).newInstance();
				}catch(Exception e){
					e.printStackTrace();
				}
			
			activityFilter.beforeExecute(activity, instance);
		}
	}

	String filterClass;
	String afterExecuteScript;
	String afterCompleteScript;
	String beforeExecuteScript;
	boolean useScript=true;
	String name;

	public String getAfterExecuteScript() {
		return afterExecuteScript;
	}

	public String getBeforeExecuteScript() {
		return beforeExecuteScript;
	}

	public String getFilterClass() {
		return filterClass;
	}

	public void setAfterExecuteScript(String string) {
		afterExecuteScript = string;
	}

	public void setBeforeExecuteScript(String string) {
		beforeExecuteScript = string;
	}

	public void setFilterClass(String class1) {
		filterClass = class1;
	}

	public boolean isUseScript() {
		return useScript;
	}

	public void setUseScript(boolean b) {
		useScript = b;
	}

	public String getName() {
		return name;
	}

	public void setName(String string) {
		name = string;
	}

	public String getAfterCompleteScript() {
		return afterCompleteScript;
	}

	public void setAfterCompleteScript(String string) {
		afterCompleteScript = string;
	}

	public void onPropertyChange(Activity activity, ProcessInstance instance, String propertyName, Object changedValue) throws Exception {
		if(isUseScript()){
//			runScript(getBeforeExecuteScript(), activity, instance);
		}else if(getFilterClass()!=null){
			if(activityFilter==null)
				try{
					activityFilter = (ActivityFilter)Thread.currentThread().getContextClassLoader().loadClass(getFilterClass()).newInstance();
				}catch(Exception e){
					e.printStackTrace();
				}
			
			activityFilter.onPropertyChange(activity, instance, propertyName, changedValue);
		}
	}

}
