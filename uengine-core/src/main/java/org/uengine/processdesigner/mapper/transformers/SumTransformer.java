package org.uengine.processdesigner.mapper.transformers;

import java.util.Map;

import org.metaworks.FieldDescriptor;
import org.metaworks.Type;
import org.metaworks.inputter.SelectInput;
import org.uengine.kernel.ProcessInstance;
import org.uengine.processdesigner.mapper.Transformer;

public class SumTransformer extends Transformer{
	
	public SumTransformer() {
		setName("Sum");
	}

	public static void metaworksCallback_changeMetadata(Type type){
		FieldDescriptor fd = type.getFieldDescriptor("InputType");
		fd.setDisplayName("Number Type");
		fd.setInputter(new SelectInput(
			new Object[]{
				"Double",
				"Integer",
				"Long"
			},
			new Object[]{ 'D', 'I', 'L' }
		));
	}	

	public String[] getInputArguments() {
		return new String[]{"val1", "val2", "val3", "val4", "val5"};
	}


	@Override
	public Object transform(ProcessInstance instance, Map parameterMap, Map options) {
		double sum = 0;
		for (int i = 1; i <= parameterMap.size(); i++) {
			
			Object val = parameterMap.get("val" + i);
			switch(getInputType()) {
			case 'I' :
				sum += (int) Double.parseDouble("" + val);
				break;
			case 'L' :
				sum += (long) Double.parseDouble("" + val);
				break;
			default  : 
				sum += Double.parseDouble("" + val);
			}
		}
		
		switch(getInputType()) {
		case 'I' :
			return (int) sum;
		case 'L' :
			return (long) sum;
		default : 
			return sum;
		}
	}

	char inputType;
		public char getInputType() {
			return inputType;
		}
	
		public void setInputType(char inputType) {
			this.inputType = inputType;
		}

}
