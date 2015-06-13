package org.uengine.kernel;

import java.util.Calendar;
import java.util.Date;
import java.util.Map;

import org.metaworks.inputter.DateInput;
import org.metaworks.inputter.InputterAdapter;
import org.metaworks.inputter.TextInput;
import org.uengine.kernel.CommandVariableValue;

public class InstanceDueDatePointingProcessVariable extends ProcessVariable{
	private static final long serialVersionUID = GlobalContext.SERIALIZATION_UID;
	
	public String getName(){
		return "[Instance.DueDate]";
	}

	public Class getType(){
		return (Date.class);
	}
	
	public InputterAdapter getInputter(){
		return new DateInput(){
			private static final long serialVersionUID = GlobalContext.SERIALIZATION_UID;

			public Object createValueFromHTTPRequest(Map parameterValues, String section, String fieldName, Object oldValue) {
				return createCommandVariableValue(super.createValueFromHTTPRequest(parameterValues, section, fieldName, oldValue));
			}
			public Object getValue() {
				return createCommandVariableValue(super.getValue());
			}
			public CommandVariableValue createCommandVariableValue(final Object value){
				return new CommandVariableValue(){
					public boolean doCommand(ProcessInstance instance, String variableKey) throws Exception {
						Calendar cal = Calendar.getInstance();
						
						if(value==null) return true;
						
						cal.setTime((Date)value);
						instance.setDueDate(cal);
						return true;
					};	
				};
			}
		};		
	}
}
