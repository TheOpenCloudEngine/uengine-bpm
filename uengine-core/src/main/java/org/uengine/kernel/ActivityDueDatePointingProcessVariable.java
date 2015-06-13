package org.uengine.kernel;

import java.util.Calendar;
import java.util.Date;
import java.util.Map;

import org.metaworks.inputter.DateInput;
import org.metaworks.inputter.Inputter;
import org.metaworks.inputter.InputterAdapter;
import org.metaworks.inputter.TextInput;

public class ActivityDueDatePointingProcessVariable extends ProcessVariable{
	private static final long serialVersionUID = GlobalContext.SERIALIZATION_UID;
	
	public ActivityDueDatePointingProcessVariable(final HumanActivity finalActivity){

		setActivity(finalActivity);
		setTracingTag(finalActivity.getTracingTag());
	}
	
	HumanActivity activity;
		public HumanActivity getActivity() {
			return activity;
		}
		public void setActivity(HumanActivity activity) {
			this.activity = activity;
		}
		
	String tracingTag;
		public String getTracingTag() {
			return tracingTag;
		}
		public void setTracingTag(String tracingTag) {
			this.tracingTag = tracingTag;
		}
		
	public String getName(){
		return "[" + getActivity().getName() + ".DueDate]";
	}

	public Class getType(){
		return (Date.class);
	}
	
	public InputterAdapter getInputter(){
		return new DateInput(){
			public Object createValueFromHTTPRequest(Map parameterValues, String section, String fieldName, Object oldValue) {
				return createCommandVariableValue(super.createValueFromHTTPRequest(parameterValues, section, fieldName, oldValue));
			}
			public Object getValue() {
				return createCommandVariableValue(super.getValue());
			}
			public CommandVariableValue createCommandVariableValue(final Object value){
				return new CommandVariableValue(){
					public boolean doCommand(ProcessInstance instance, String variableKey) throws Exception {
						Calendar cal;
						if(value instanceof Calendar) cal = (Calendar)value;
						else{
							cal = Calendar.getInstance();
							cal.setTime((Date)value);
						}

						getActivity().setDueDate(instance, cal);
						return true;
					};	
				};
			}
		};
	}
}
