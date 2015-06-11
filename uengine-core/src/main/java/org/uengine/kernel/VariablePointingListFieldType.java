package org.uengine.kernel;

import java.util.Map;

import org.uengine.contexts.DatabaseSynchronizationOption;
import org.uengine.util.dao.IDAO;

public class VariablePointingListFieldType implements ListFieldType{
	
	private static final long serialVersionUID = GlobalContext.SERIALIZATION_UID;

	ProcessVariable variable;
		public ProcessVariable getVariable() {
			return variable;
		}
	
		public void setVariable(ProcessVariable variable) {
			this.variable = variable;
		}
		
		
	public Object getFieldValue(IDAO dao, Map genericContext) throws Exception{
		return dao.get(getFieldName());
	}
	
	public String toString() {
		return "[Variable] "+getVariable();
	}
	
	public String getFieldName(){
		DatabaseSynchronizationOption dso = variable.getDatabaseSynchronizationOption();
		return dso.getFieldName();
	}
}
