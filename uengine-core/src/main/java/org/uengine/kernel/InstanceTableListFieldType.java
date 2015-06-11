package org.uengine.kernel;

import java.util.Map;

import org.uengine.contexts.DatabaseSynchronizationOption;
import org.uengine.persistence.processinstance.ProcessInstanceDAO;
import org.uengine.kernel.GlobalContext;

public class InstanceTableListFieldType extends TableListFieldType{

	private static final long serialVersionUID = GlobalContext.SERIALIZATION_UID;
	
	public InstanceTableListFieldType(){
	}
	
	public InstanceTableListFieldType( String fieldName){
		setFieldName(fieldName);
	}

	public String getTableName() {
		return "BPM_PROCINST";
	}
}
