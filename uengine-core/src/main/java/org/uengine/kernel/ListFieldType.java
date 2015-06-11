package org.uengine.kernel;

import java.io.Serializable;
import java.util.Map;
import org.uengine.util.dao.IDAO;

public interface ListFieldType extends Serializable{
	
	public Object getFieldValue(IDAO dao, Map genericContext) throws Exception;
	public String getFieldName();
	
}
