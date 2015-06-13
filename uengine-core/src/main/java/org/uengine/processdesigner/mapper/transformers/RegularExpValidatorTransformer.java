package org.uengine.processdesigner.mapper.transformers;

import java.util.Map;

import org.metaworks.FieldDescriptor;
import org.metaworks.Type;
import org.metaworks.inputter.TextInput;
import org.uengine.kernel.ProcessInstance;
import org.uengine.kernel.UEngineException;
import org.uengine.processdesigner.mapper.Transformer;

public class RegularExpValidatorTransformer extends AbstractValidatorTransformer {
	
	public RegularExpValidatorTransformer() {
		setName("RegularExpValidator");
	}
	
	public String regularExpression;
		public String getRegularExpression() {
			return regularExpression;
		}
	
		public void setRegularExpression(String regularExpression) {
			this.regularExpression = regularExpression;
		}

	
	public boolean validate(Object value, ProcessInstance instance, Map parameterMap, Map options){
		return value==null || value.toString().matches( getRegularExpression() );
	}


}
