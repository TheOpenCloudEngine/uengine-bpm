package org.uengine.kernel;

import java.util.Map;

import org.uengine.kernel.GlobalContext;
import org.uengine.persistence.processinstance.ProcessInstanceDAO;
import org.uengine.util.dao.IDAO;

public class TableListFieldType implements ListFieldType{

	private static final long serialVersionUID = GlobalContext.SERIALIZATION_UID;

	String fieldName;
		public String getFieldName() {
			return fieldName;
		}
		public void setFieldName(String fieldName) {
			this.fieldName = fieldName;
		}
		
	String tableName;
		public String getTableName() {
			return tableName;
		}
		public void setTableName(String tableName) {
			this.tableName = tableName;
		}
		
	public Object getFieldValue(IDAO dao, Map genericContext) throws Exception{
		return dao.get(getFieldName());
	}
	
	public String toString() {
		return "["+getTableName()+"] "+getFieldName();
	}
	
}
