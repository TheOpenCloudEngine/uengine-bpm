package org.uengine.kernel;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Map;

import org.apache.commons.lang.StringEscapeUtils;
import org.codehaus.janino.ScriptEvaluator;
import org.metaworks.annotation.Face;
import org.metaworks.annotation.Hidden;
import org.uengine.util.UEngineUtil;


/**
 * @author Jinyoung Jang
 */

public class AssignActivity extends DefaultActivity{
	private static final long serialVersionUID = org.uengine.kernel.GlobalContext.SERIALIZATION_UID;
	
	public final static int JAVA_STYLE		= 1;
	public final static int NATIVE_STYLE	= 2;
	public final static int SIMPLE_STYLE	= 3;

	int assignStyle;
		@Hidden
		public int getAssignStyle() {
			if (this.assignStyle == 0 ) {
				return JAVA_STYLE;
			}
			return assignStyle;
		}
		public void setAssignStyle(int assignStyle) {
			this.assignStyle = assignStyle;
		}
		
	/**
	 * @deprecated rather use assignStyle
	 */
	@Deprecated
	String assignValueInputType;
		@Hidden
		@Deprecated
		public String getAssignValueInputType() {
			return assignValueInputType;
		}
		@Deprecated
		public void setAssignValueInputType(String assignValueInputType) {
			this.assignValueInputType = assignValueInputType;
		}

	ProcessVariable variable;
		@Face(displayName="대상변수")
		public ProcessVariable getVariable() {
			return variable;
		}
		public void setVariable(ProcessVariable value) {
			variable = value;
		}
	
	Object val;
		@Hidden
		public Object getVal() {
			return val;
		}
		public void setVal(Object value) {
			val = value;
		}
		
	String assignValue;
		@Face(displayName="할당값")
		public String getAssignValue() {
			return assignValue;
		}
		public void setAssignValue(String assignValue) {
			this.assignValue = assignValue;
		}
		
	public AssignActivity(ProcessVariable variable, Object val){
		super("assign");
		this.variable = variable;
		this.val = val;
	}
	
	public AssignActivity(){
		this(null,null);
		this.setAssignStyle(SIMPLE_STYLE);
	}
	
	private String generateJavaCode(String scriptSmt){
		String codes ="Object result=null;" ;
		codes+="try{" ;
		if (this.getAssignStyle() == NATIVE_STYLE) {
			codes+="    result= String.valueOf(\""+scriptSmt+"\");";
		} else {
			codes+="    result= String.valueOf("+scriptSmt+");";
		}
		codes+="}catch(Exception e){";
		codes+="}";
		codes+="return result;";
		
		return codes;
	}
		
	public Serializable scriptCalculator(ProcessInstance instance , String scriptSmt) throws Exception {
		String codes="";
		while(true){
			int findStartTag = scriptSmt.indexOf("<%");
			if(findStartTag!=-1){
				int findEndTag = scriptSmt.indexOf("%>");
				
				String tagName = scriptSmt.substring(findStartTag+2, findEndTag);
				
				if (variable.getClass() == ActivityDueDatePointingProcessVariable.class) {
					return instance.get("", tagName);
				}
				
				String tagValue=""+instance.get("", tagName);
								
				if(!UEngineUtil.isNotEmpty(tagValue)){
					return null;
				}
				
				String tag = scriptSmt.substring(findStartTag, findEndTag+2);
				
				scriptSmt = scriptSmt.replace(tag, tagValue);
	
			}else{
				
				if (this.getAssignStyle() == NATIVE_STYLE) {
					codes =generateJavaCode(StringEscapeUtils.escapeJava(scriptSmt));
				} else {
					codes =generateJavaCode(scriptSmt);
				}
				
				break;
			}
		}
		
		return (Serializable)scriptEngine(codes);
	}
	
	public Object scriptEngine(String scriptSmt) throws Exception{
		String[] parameterNames = {};
		Object[] parameterValues = {};
		Class[] parameterTypes = {};
		
	    ScriptEvaluator se = new ScriptEvaluator(
	    		scriptSmt,
	            Object.class,
	            parameterNames,
	            parameterTypes
	        );

	        // Evaluate script with actual parameter values.
	    return se.evaluate(parameterValues);
	}
		
	protected void executeActivity(ProcessInstance instance) throws Exception{
		
		Serializable value = null;
		
		if(getAssignStyle()==SIMPLE_STYLE){
			value =  getAssignValue(); 
		}else
			value =  scriptCalculator(instance, getAssignValue());

		if(getVariable() !=null) {
			if (variable.getClass() == InstanceNamePointingProcessVariable.class) {
				instance.setName((String) value);
			} else if (variable.getClass() == ActivityDueDatePointingProcessVariable.class) {
				String tracingTag = ((ActivityDueDatePointingProcessVariable)variable).getTracingTag();
				if (value != null && !"".equals(value)) {
					if (tracingTag != null) {
						Activity act = instance.getProcessDefinition(false).getActivity(tracingTag);
						if (act instanceof HumanActivity) {
							((HumanActivity) act).setDueDate(instance, (Calendar) value);
						}
					}
				}
			} else if (variable.getClass() == RolePointingProcessVariable.class) {
				Role role = ((RolePointingProcessVariable) variable).getRole();
				if (value != null && !"".equals(value)) {
					for (String endpoint : ((String) value).split(",")) {
						role.getMapping(instance).setEndpoint(endpoint.trim());
						role.getMapping(instance).moveToAdd();
					}
				}
			} else {
				variable.set(instance, "", value);
			}
			
		}
		
		fireComplete(instance);
	}
	
	public ValidationContext validate(Map options) {
		ValidationContext vc = super.validate(options);
		
		if (getAssignValue()==null) {
			vc.add("assignValue is null!");
		}else if (this.getAssignStyle() == 0) {
			vc.add("select a assign style!");
		}else if(getVariable()==null){
			vc.add("select a process Variable!");
		}else {
			String scriptSmt = getAssignValue();
			while(true){
				int findStartTag = scriptSmt.indexOf("<%");
				if(findStartTag!=-1){
					int findEndTag = scriptSmt.indexOf("%>");
					
					String tagName = scriptSmt.substring(findStartTag+2, findEndTag);
					
					ProcessVariable pv = this.getProcessDefinition().getProcessVariable(tagName);
									
					if(pv==null){
						vc.add("ProcessVariable('"+tagName+"') doesn't exist!!");
					}
					
					String tag = scriptSmt.substring(findStartTag, findEndTag+2);
					
					scriptSmt = scriptSmt.replace(tag, "");
		
				}else{
					break;
				}
			}
		}
		

		
		return vc;
	}
}