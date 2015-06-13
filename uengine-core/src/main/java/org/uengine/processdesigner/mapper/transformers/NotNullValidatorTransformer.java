package org.uengine.processdesigner.mapper.transformers;

import java.util.Map;

import org.metaworks.FieldDescriptor;
import org.metaworks.Type;
import org.metaworks.inputter.TextInput;
import org.uengine.kernel.ProcessInstance;
import org.uengine.kernel.UEngineException;
import org.uengine.processdesigner.mapper.Transformer;
import org.uengine.util.UEngineUtil;

public class NotNullValidatorTransformer extends AbstractValidatorTransformer {
	
	public NotNullValidatorTransformer() {
		setName("Not Null Validator");
	}


	@Override
	protected Exception createException(String fieldNames, String validationMessage, ProcessInstance instance, Map parameterMap, Map options) {
		if(validationMessage==null) 
			validationMessage = org.uengine.kernel.GlobalContext.getLocalizedMessageForWeb("org.uengine.processdesigner.mapper.transformers.notnullvalidatortransformer.message", null, "는 필수입력입니다");

		return super.createException(fieldNames, validationMessage, instance,
				parameterMap, options);
	}


	@Override
	boolean validate(Object value, ProcessInstance instance, Map parameterMap, Map options) {
		// TODO Auto-generated method stub
		return value!=null && !"null".equals(value) && !"".equals(value.toString().trim());
	}
	

}
