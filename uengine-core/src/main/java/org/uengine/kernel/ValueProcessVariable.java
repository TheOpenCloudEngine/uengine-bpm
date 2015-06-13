package org.uengine.kernel;

import java.io.Serializable;
import java.util.Date;
import java.util.Map;

import org.metaworks.inputter.InputterAdapter;
import org.metaworks.inputter.TextInput;
import org.uengine.kernel.CommandVariableValue;
import org.uengine.kernel.GlobalContext;

public class ValueProcessVariable extends ProcessVariable{
	private static final long serialVersionUID = GlobalContext.SERIALIZATION_UID;
	
	Serializable value;
		public Serializable getValue() {
			return value;
		}
		public void setValue(Serializable value) {
			this.value = value;
		}
		
	boolean requireEvaluateExpression;
		public boolean isRequireEvaluateExpression() {
			return requireEvaluateExpression;
		}
		public void setRequireEvaluateExpression(boolean evaluate) {
			this.requireEvaluateExpression = evaluate;
		}
	
	public ValueProcessVariable(Serializable value, boolean requireEvaluateExp){
		setValue(value);
		setRequireEvaluateExpression(requireEvaluateExp);
	}
	
	public Class getType(){
		return getValue().getClass();
	}
	
	public Serializable get(ProcessInstance instance, String scope) throws Exception {
		if(isRequireEvaluateExpression()){
			String expression = "" + getValue();
			String evaluatedResult = (new DefaultActivity()).evaluateContent(instance, expression).toString();
			return evaluatedResult;
		}
		
		return getValue();
	}
	
	public ProcessVariableValue getMultiple(ProcessInstance instance, String scope, String key) throws Exception {
		ProcessVariableValue pvv = new ProcessVariableValue();
		pvv.setValue(get(instance, scope));
		
		return pvv;
	}

	
}
