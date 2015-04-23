package org.uengine.processdesigner.mapper;

import java.awt.Point;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.uengine.kernel.NeedArrangementToSerialize;
import org.uengine.kernel.ProcessInstance;
import org.uengine.kernel.ProcessVariableValue;
import org.uengine.kernel.RoleMapping;
import org.uengine.util.UEngineUtil;


public abstract class Transformer implements NeedArrangementToSerialize, Serializable{

	public final static String OPTION_KEY_OUTPUT_ARGUMENT = "outputArgumentName";
	public final static String OPTION_KEY_FORM_FIELD_NAME = "formFieldName";
	
	String name;
		public String getName() {
			if(name==null) return UEngineUtil.getClassNameOnly(getClass());
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		
	Point location;
		public Point getLocation() {
			return location;
		}
//		public void setLocation(Point location) {
//			this.location = location;
//		}
		
	/**
	 * value will be available only when in run-time
	 */
	HashMap argumentSourceMap = new HashMap();
		public HashMap getArgumentSourceMap() {
			return argumentSourceMap;
		}	
		

	public Object letTransform(ProcessInstance instance, String outputArgumentName) throws Exception{
		Map options = new HashMap();
		options.put(OPTION_KEY_OUTPUT_ARGUMENT, outputArgumentName);
		
		return letTransform(instance, options);
	}
	
	// in run-time, there is no TransformerArgument objects, so we need to use the argumentSourceMap instead
	public Object letTransform(ProcessInstance instance, Map options) throws Exception{
		HashMap parameters = new HashMap();
		
		int maxNumberOfParameterValues = 1;
		
		String[] inputArguments = getInputArguments();

		/**
		 * variable 'resultForTransformers' stands for caching the result values from transformers within a transaction demarcation. 
		 */
		if(instance.getProcessTransactionContext().getSharedContext("resultForTransformers")==null){
			instance.getProcessTransactionContext().setSharedContext("resultForTransformers", new HashMap());
		}
		HashMap resultsForTransformers = (HashMap) instance.getProcessTransactionContext().getSharedContext("resultForTransformers");
		
		/**
		 * call the prior transformers so that the transform action can be done recursively.
		 */
		if(inputArguments != null)
		for(int i=0; i<inputArguments.length; i++){
			Object argumentSource = argumentSourceMap.get(inputArguments[i]);
			
			ArrayList argumentSources;
			if(argumentSource instanceof ArrayList){
				argumentSources = (ArrayList)argumentSource;
			}else{
				argumentSources = new ArrayList();
				argumentSources.add(argumentSource);
			}
		
			for(int j=0; j<argumentSources.size(); j++){
				argumentSource = argumentSources.get(j);
				
				if(argumentSource instanceof TransformerMapping){
	
					TransformerMapping tm = (TransformerMapping)argumentSource;
					
					Transformer transformer = tm.getTransformer();
					Object result = null;
	
					/**
					 * if there's cached result of this transformer, use it rather than transform again.
					 */
					/*if(resultsForTransformers.containsKey(transformer)){
						result = resultsForTransformers.get(transformer);
					}else
						result = transformer.letTransform(instance, options);
					*/
					
					options.put(org.uengine.processdesigner.mapper.Transformer.OPTION_KEY_OUTPUT_ARGUMENT, tm.getLinkedArgumentName() );
					result = transformer.letTransform(instance, options);
					
					/**
					 * if the transformer has two or more argument out, 
					 * the transformer should implement how to resolve the output value through 'resolveOutputValue' method.
					 */
					parameters.put(inputArguments[i], /*resolveOutputValue(transformerArgument.getName(), */result/*)*/);
				}else
				if(argumentSource instanceof String){
					Object parameter = instance.getBeanProperty((String)argumentSource);
					
					if(parameter instanceof ProcessVariableValue){
						ProcessVariableValue pvv = ((ProcessVariableValue)parameter);
						if(maxNumberOfParameterValues < pvv.size()) maxNumberOfParameterValues = pvv.size();
					}else
	
					if(parameter instanceof RoleMapping){
						RoleMapping rm = ((RoleMapping)parameter);
						if(maxNumberOfParameterValues < rm.size()) maxNumberOfParameterValues = rm.size();
					}
					
					parameters.put(inputArguments[i], parameter);
				}
			}
		}

		/**
		 * if the in-parameter has value more than one, it should let transformers run with each values.
		 */
		if(maxNumberOfParameterValues>1){
			ProcessVariableValue result = new ProcessVariableValue();
			for(int i=0; i<maxNumberOfParameterValues; i++){
				HashMap unitParameters = new HashMap();

				for(int j=0; j<inputArguments.length; j++){
					Object parameter = parameters.get(inputArguments[j]);
					if(parameter instanceof ProcessVariableValue){
						int valuePos = i;
						
						ProcessVariableValue pvv = ((ProcessVariableValue)parameter);
						if(valuePos >= pvv.size()) valuePos = pvv.size()-1;
						pvv.setCursor(valuePos);
						
						parameter = pvv.getValue();
					}else

					if(parameter instanceof RoleMapping){
						int valuePos = i;
						
						RoleMapping rm = ((RoleMapping)parameter);
						if(valuePos >= rm.size()) valuePos = rm.size()-1;
						rm.setCursor(valuePos);
						
						parameter = rm.getCurrentRoleMapping();
					}
					
					unitParameters.put(inputArguments[j], parameter);
				}
				
				Object unitResult = transform(instance, unitParameters, options);
				
				result.setValue((Serializable)unitResult);
				result.moveToAdd();
			}
			
			return result;
		}
		
		/**
		 * in case that the input parameter is single value
		 */
		return transform(instance, parameters, options);
	}
	
	// in run-time
	abstract protected Object transform(ProcessInstance instance, Map parameterMap, Map options) throws Exception;
	
//	transient String[] inputArguments;
		abstract public String[] getInputArguments();/* {
			return inputArguments;
		}*/
//		public void setInputArguments(String[] inputArguments) {
//			this.inputArguments = inputArguments;
//		}
		
//	transient String[] outputArguments = new String[]{"output"};
		public String[] getOutputArguments() {
			return new String[]{"out"};//outputArguments;
		}
//		public void setOutputArguments(String[] outputArguments) {
//			this.outputArguments = outputArguments;
//		}

		

	public void afterDeserialization() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void beforeSerialization() {

	}

	public void afterAdded(){
		
	}
		
}
