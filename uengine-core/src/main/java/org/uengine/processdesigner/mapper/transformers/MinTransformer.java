package org.uengine.processdesigner.mapper.transformers;

import java.util.Map;

import org.metaworks.FieldDescriptor;
import org.metaworks.Type;
import org.metaworks.inputter.SelectInput;
import org.uengine.kernel.ProcessInstance;
import org.uengine.kernel.UEngineException;
import org.uengine.processdesigner.mapper.Transformer;

public class MinTransformer extends Transformer {

	public MinTransformer(){
		setName("Min");
	}
	
	public static void metaworksCallback_changeMetadata(Type type){
		FieldDescriptor fd = type.getFieldDescriptor("InputType");
		fd.setInputter(new SelectInput(
			new Object[]{
				"Double",
				"Long",
				"String"
			},
			new Object[]{ 'D', 'L', 'S' }
		));
	}
	
	public String[] getInputArguments() {
		return (new String[]{"value1", "value2"});
	}

	public Object transform(ProcessInstance instance, Map parameterMap,  Map options) throws UEngineException {
		Object minValue = null;
		String[] inputArgs = getInputArguments();
		
		for(int i=0; i<inputArgs.length; i++){
			Object value = (Object) parameterMap.get(inputArgs[i]);
			try{
				switch (inputType) {
					case 'L':
						value = (long) Double.parseDouble(value.toString());
						break;
					case 'D':
						value = Double.parseDouble(value.toString());
						break;
					default:
						value = String.valueOf(value);
						break;
				}
			}catch (Exception e) {
				throw new UEngineException(e.getMessage(), e);
			}
			Comparable cmpVal = (Comparable) value;
			if(minValue == null || cmpVal.compareTo(minValue) < 0) minValue = cmpVal;			
		}

		return minValue;
	}
	
	char inputType;
		public char getInputType() {
			return inputType;
		}
	
		public void setInputType(char inputType) {
			this.inputType = inputType;
		}
}
