package org.uengine.processdesigner.mapper.transformers;

import java.util.Map;

import org.metaworks.FieldDescriptor;
import org.metaworks.Type;
import org.uengine.kernel.ProcessInstance;
import org.uengine.processdesigner.mapper.Transformer;

public class ReplaceTransformer extends Transformer{
	
	private String oldString;
	
	private String newString;
	
	private boolean isRegularExp; 
	
	public String getOldString() {
		return oldString;
	}

	public void setOldString(String oldString) {
		this.oldString = oldString;
	}

	public String getNewString() {
		return newString;
	}

	public void setNewString(String newString) {
		this.newString = newString;
	}
	
	public boolean isRegularExp() {
		return isRegularExp;
	}

	public void setRegularExp(boolean isRegularExp) {
		this.isRegularExp = isRegularExp;
	}

	public ReplaceTransformer() {
		setName("Replace");
	}

	public static void metaworksCallback_changeMetadata(Type type){
		FieldDescriptor fd = type.getFieldDescriptor("OldString");
		fd.setDisplayName("Old String");
		
		fd = type.getFieldDescriptor("NewString");
		fd.setDisplayName("New String");
	}	

	public String[] getInputArguments() {
		return new String[]{"input"};
	}

	@Override
	public Object transform(ProcessInstance instance, Map parameterMap, Map options) {
		Object val = parameterMap.get("input");
		if (isRegularExp()) {
			return String.valueOf(val).replaceAll(getOldString(), getNewString() == null ? "" : getNewString());
		} 
		return String.valueOf(val).replace(getOldString(), getNewString() == null ? "" : getNewString());
	}
}
