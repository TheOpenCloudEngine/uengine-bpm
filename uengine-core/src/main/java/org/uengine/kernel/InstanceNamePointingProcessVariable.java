package org.uengine.kernel;

import java.io.Serializable;
import java.util.Date;
import java.util.Map;

import org.metaworks.inputter.InputterAdapter;
import org.metaworks.inputter.TextInput;
import org.uengine.kernel.CommandVariableValue;
import org.uengine.kernel.GlobalContext;

public class InstanceNamePointingProcessVariable extends ProcessVariable{
	private static final long serialVersionUID = GlobalContext.SERIALIZATION_UID;
	
	public String getName(){
		return "[Instance.Name]";
	}

	public Class getType(){
		return (String.class);
	}
	
	public Serializable get(ProcessInstance instance, String scope) throws Exception {
		return instance!=null ? instance.getName() : null;
	}

	public InputterAdapter getInputter(){
		return 	new TextInput(){
			public Object createValueFromHTTPRequest(Map parameterValues, String section, String fieldName, Object oldValue) {
				return createCommandVariableValue(super.createValueFromHTTPRequest(parameterValues, section, fieldName, oldValue));
			}
			public Object getValue() {
				return createCommandVariableValue(super.getValue());
			}
			public CommandVariableValue createCommandVariableValue(final Object value){
				return new CommandVariableValue(){
					public boolean doCommand(ProcessInstance instance, String variableKey) throws Exception {
						instance.setName((String)value);
						return true;
					};	
				};
			}
		};		
	}

}
