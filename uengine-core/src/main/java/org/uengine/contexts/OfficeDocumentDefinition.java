package org.uengine.contexts;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.uengine.kernel.GlobalContext;

/**
 * 
 * @author Lee Hee-byung
 */

public class OfficeDocumentDefinition implements Serializable {
	private static final long serialVersionUID = GlobalContext.SERIALIZATION_UID;

	List fieldList = new ArrayList();
		public List getFieldList() {
			return fieldList;
		}
		public void setFieldList(List fieldList) {
			this.fieldList = fieldList;
		}

	String filePath;
		public String getFilePath() {
			return filePath;
		}
		public void setFilePath(String filePath) {
			this.filePath = filePath;
		}
}
