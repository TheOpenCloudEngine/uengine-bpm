package org.uengine.kernel;

import org.metaworks.FieldDescriptor;
import org.metaworks.Type;
import org.metaworks.inputter.SelectInput;

/**
 * @author Jinyoung Jang
 */

public class StatusReportActivity extends DefaultActivity{
	private static final long serialVersionUID = org.uengine.kernel.GlobalContext.SERIALIZATION_UID;

	public static void metaworksCallback_changeMetadata(Type type){
		FieldDescriptor fd;
		
		fd = type.getFieldDescriptor("FieldName");
		fd.setInputter(new SelectInput(new String[]{"Info","Ext1","Ext2","Ext3","Ext4","Ext5","Ext6","Ext7","Ext8","Ext9","Ext10"}));
	}
	
	
	String content;
		public String getContent() {
			return content;
		}
		public void setContent(String string) {
			content = string;
		}
		
	String fieldName;
		public String getFieldName() {
			return fieldName;
		}
		public void setFieldName(String fieldName) {
			this.fieldName = fieldName;
		}
		
	public StatusReportActivity(){
		super("Mark Status");
		setHidden(true);
	}
	
	protected void executeActivity(ProcessInstance instance) throws Exception{
		String contentEvaluated = evaluateContent(instance, getContent()).toString();
		
		instance.setInfo(contentEvaluated);
		fireComplete(instance);
	}

	
}

