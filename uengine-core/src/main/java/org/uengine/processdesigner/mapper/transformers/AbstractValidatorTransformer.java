package org.uengine.processdesigner.mapper.transformers;

import java.util.Map;

import org.uengine.kernel.ProcessInstance;
import org.uengine.kernel.UEngineException;
import org.uengine.processdesigner.mapper.Transformer;

public abstract class AbstractValidatorTransformer extends Transformer {
	
	public AbstractValidatorTransformer() {
		setName("Validator");
	}
	
	public String validationMessage;
		public String getValidationMessage() {
			return validationMessage;
		}
	
		public void setValidationMessage(String validationMessage) {
			this.validationMessage = validationMessage;
		}
	
	@Override
	public String[] getInputArguments() {
		return new String[] { "in", "in2", "in3", "in4", "in5", "in6", "in7", "in8", "in9", "in10" };
	}
	
	@Override
	public String[] getOutputArguments() {
		return new String[] { "out", "out2", "out3", "out4", "out5", "out6", "out7", "out8", "out9", "out10" };
	}	
	

	String[] aliasesForEachInputArgument;
		public String[] getAliasesForEachInputArgument() {
			if(aliasesForEachInputArgument==null || aliasesForEachInputArgument.length == 0){
				String[] aliases = new String[getInputArguments().length];

				beforeSerialization();
				
				for(int i=0; i<getInputArguments().length; i++){
					String alias;
					String argName = getInputArguments()[i];
					if(getArgumentSourceMap().containsKey(argName)){
						alias = "" + getArgumentSourceMap().get(argName);
					}else{
						alias = argName;
					}
					
					aliases[i] = alias;
				}	
				
				return aliases;
			}
			
			return aliasesForEachInputArgument;
		}
		public void setAliasesForEachInputArgument(String[] aliasesForEachInputArgument) {
			this.aliasesForEachInputArgument = aliasesForEachInputArgument;
		}
	
	
	@Override
	public Object transform(ProcessInstance instance, Map parameterMap, Map options) throws Exception {
		
		
		String[] inputArgs = getInputArguments();
		String fieldNames = "";
		String sep = "";
		Object value=null;
		
		for(int i=0; i<getInputArguments().length; i++){		

			String argName = inputArgs[i];
			if(argName==null) continue;
			
			if(!getArgumentSourceMap().containsKey(argName)) continue;
			
			value = parameterMap.get(argName);
			
			//hook
			if ( validate(value, instance, parameterMap, options) ) continue;

			String alias = null;
			
			if(getAliasesForEachInputArgument()!=null && getAliasesForEachInputArgument().length > i){
				alias = getAliasesForEachInputArgument()[i];
			}
			
			if(alias==null){
				Object sourceObj = getArgumentSourceMap().get(argName);
				if(sourceObj instanceof String){
					alias = (String)sourceObj;
				}
			}
			
			if(alias == null) alias = "value";
			
			fieldNames += (sep + alias);
			
			sep = ", ";
		}

		if(fieldNames.length() > 0){
			
			Exception validationException = createException(fieldNames, getValidationMessage(), instance, parameterMap, options);
			throw validationException;
		}
		
		String outputArgumentName = (String) options.get(OPTION_KEY_OUTPUT_ARGUMENT);
		outputArgumentName = outputArgumentName.replaceAll("out", "in");
		
		return parameterMap.get(outputArgumentName);
	}
	
	abstract boolean validate(Object value, ProcessInstance instance, Map parameterMap, Map options);


	protected Exception createException(String fieldNames, String validationMessage, ProcessInstance instance, Map parameterMap, Map options){
		UEngineException validationException = new UEngineException(fieldNames + " " + validationMessage);
		validationException.setErrorLevel(UEngineException.MESSAGE_TO_USER);
		
		return validationException;
	}
}
