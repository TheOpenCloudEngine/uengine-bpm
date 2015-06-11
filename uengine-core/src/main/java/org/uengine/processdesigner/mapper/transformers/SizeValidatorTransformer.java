package org.uengine.processdesigner.mapper.transformers;

import java.util.Map;

import org.metaworks.FieldDescriptor;
import org.metaworks.Type;
import org.metaworks.inputter.TextInput;
import org.uengine.kernel.ProcessInstance;
import org.uengine.kernel.UEngineException;
import org.uengine.processdesigner.mapper.Transformer;
import org.uengine.util.UEngineUtil;

public class SizeValidatorTransformer extends AbstractValidatorTransformer {
	
	public SizeValidatorTransformer() {
		setName("Size Validator");
	}
	
	int size;
		public int getSize() {
			return size;
		}
	
		public void setSize(int size) {
			this.size = size;
		}

	public boolean validate(Object value, ProcessInstance instance, Map parameterMap, Map options){
		return 
			value==null || 
			((value instanceof String) && ((String)value).length()<=getSize()) ||
			((value instanceof StringBuffer) && ((StringBuffer)value).length() <=getSize()) ||
			((value instanceof Number) && ((Number)value).longValue() <=getSize())
		;
	}
	
	@Override
	protected Exception createException(String fieldNames, String validationMessage, ProcessInstance instance, Map parameterMap, Map options) {
		if(validationMessage==null) 
			validationMessage = org.uengine.kernel.GlobalContext.getLocalizedMessageForWeb("org.uengine.processdesigner.mapper.transformers.sizevalidatortransformer.message", null, "는 길이 " + getSize() + " 미만으로 입력되어야 합니다");

		return super.createException(fieldNames, validationMessage, instance,
				parameterMap, options);
	}


}
