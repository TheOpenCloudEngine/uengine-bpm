/*
 * Created on 2004-04-02
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package org.uengine.kernel;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.bsf.*;
import org.codehaus.janino.ExpressionEvaluator;
import org.codehaus.janino.ScriptEvaluator;
import org.codehaus.janino.samples.DemoBase;
import org.codehaus.janino.samples.ScriptDemo;
import org.metaworks.FieldDescriptor;
import org.metaworks.Type;
import org.metaworks.inputter.RadioInput;
import org.metaworks.inputter.SelectInput;

/**
 * @author Jinyoung Jang
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class ScriptActivity extends DefaultActivity {

	private static final long serialVersionUID = org.uengine.kernel.GlobalContext.SERIALIZATION_UID;
	public static final int LANGUAGE_JAVASCRIPT				= 0; 
	public static final int LANGUAGE_JAVA        			= 1; 
	
	public static void metaworksCallback_changeMetadata(Type type){
		FieldDescriptor fd;
		
		fd = type.getFieldDescriptor("Language");	
		fd.setInputter(new RadioInput(
				new String[]{
						      "Javascript",
                              "Java"
						      },
				new Object[]{
						     new Integer(LANGUAGE_JAVASCRIPT),
         	                 new Integer(LANGUAGE_JAVA)
						    }
		));	
	}
	
	
	String script;
		public String getScript() {
			return script;
		}
		public void setScript(String string) {
			script = string;
		}

	ProcessVariable out;
		public ProcessVariable getOut() {
			return out;
		}
		public void setOut(ProcessVariable variable) {
			out = variable;
		}
		
	int language ;
		public int getLanguage() {
			return language;
		}
		public void setLanguage(int language) {
			this.language = language;
		}	
		
	public ScriptActivity(){
		super();
		setName("script");		
	}
	
	public Object scriptEngine(String scriptSmt) throws BSFException{
		BSFManager manager = new BSFManager();
		manager.setClassLoader(this.getClass().getClassLoader());
				
		BSFEngine engine = manager.loadScriptingEngine("javascript");
				
		return engine.eval("my_class.my_generated_method",0,0,"function getVal(){\nimportPackage(java.lang);\n"+ scriptSmt + "}\ngetVal();");
	}
	
	protected void executeActivity(ProcessInstance instance) throws Exception{
		if(getScript()==null) return;
	
		
		Object result=null;
		if(getLanguage()==LANGUAGE_JAVASCRIPT){
			BSFManager manager = new BSFManager();
			manager.setClassLoader(this.getClass().getClassLoader());
		
			if(instance !=null)
				manager.declareBean("instance", instance, ProcessInstance.class);

			try{
				manager.declareBean("request", instance.getProcessTransactionContext().getServletRequest(), HttpServletRequest.class);
			}catch(Exception e){
			}

			manager.declareBean("activity", this, Activity.class);
			manager.declareBean("definition", getProcessDefinition(), ProcessDefinition.class);
//			manager.declareBean("globalContext", GlobalContext.getInstance(), GlobalContext.class);
			manager.declareBean("util", new ScriptUtil(), ScriptUtil.class);
			
			try{
				manager.declareBean("loggedRoleMapping", instance.getProcessTransactionContext().getProcessManager().getGenericContext().get(HumanActivity.GENERICCONTEXT_CURR_LOGGED_ROLEMAPPING), RoleMapping.class);
			}catch(Exception e){
			}
			
			BSFEngine engine = manager.loadScriptingEngine("javascript");
				
			result = engine.eval("my_class.my_generated_method",0,0,"function getVal(){\nimportPackage(java.lang);\n"+ script + "}\ngetVal();");

		}else{
			
			RoleMapping loggedRoleMapping = null;
			try{
				loggedRoleMapping = (RoleMapping) instance.getProcessTransactionContext().getProcessManager().getGenericContext().get(HumanActivity.GENERICCONTEXT_CURR_LOGGED_ROLEMAPPING);
			}catch(Exception e){
			}
			
			HttpServletRequest request = null;
			try{
				request = (HttpServletRequest) instance.getProcessTransactionContext().getServletRequest();
			}catch(Exception e){
			}
			
			
			String[] parameterNames= {"instance", "activity", "definition", "util", "loggedRoleMapping", "request"};
			Object[] parameterValues = {
					instance,
					this,
					getProcessDefinition(),
					new ScriptUtil(),
					loggedRoleMapping,
					request,
			};
			
			Class[] parameterTypes = {
					ProcessInstance.class, 
					Activity.class, 
					ProcessDefinition.class,
					ScriptUtil.class,
					RoleMapping.class,
					HttpServletRequest.class,
			};
			
	        ScriptEvaluator se = new ScriptEvaluator(
	                script,
	                Object.class,
	                parameterNames,
	                parameterTypes
	            );

	            // Evaluate script with actual parameter values.
	        result = se.evaluate(parameterValues);
	        
	        if(result instanceof UEngineException){	        	
	        	throw (UEngineException)result;	        	
	        }
		}
				
		if(getOut()!=null)
			getOut().set(instance, "", (java.io.Serializable)result);
		fireComplete(instance);
	}
	
	public static void main(String [] args) throws Exception{
		ProcessDefinition scriptProcTest = new ProcessDefinition();
		
		ScriptActivity scriptAct = new ScriptActivity();
		scriptAct.setScript("instance.set(\"\", \"a\", util.formatSerializable(\"test\"))");
		// scriptAct.setScript("instance.get(\"\", \"a\");");

		scriptProcTest.setChildActivities(new Activity[]{scriptAct});
				
		ProcessInstance.USE_CLASS = DefaultProcessInstance.class;
		ComplexActivity.USE_JMS = false;
		ComplexActivity.USE_THREAD = false;
		
		ProcessInstance inst = scriptProcTest.createInstance();
		inst.execute();
		
		System.out.println(inst.get("", "a"));
	}


	public ValidationContext validate(Map options) {
		ValidationContext vc = super.validate(options);
		
		String script = getScript();
		 
		return vc;
	}


}
