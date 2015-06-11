package org.uengine.processdesigner.mapper.transformers;

import java.util.Map;

import org.metaworks.FieldDescriptor;
import org.metaworks.Type;
import org.metaworks.inputter.SelectInput;
import org.uengine.kernel.GlobalContext;
import org.uengine.kernel.ProcessInstance;
import org.uengine.kernel.UEngineException;
import org.uengine.processdesigner.mapper.Transformer;

public class NumberTransformer extends Transformer{
	
	public NumberTransformer() {
		setName("To Number");
	}

	public static void metaworksCallback_changeMetadata(Type type){
		
		FieldDescriptor fd = type.getFieldDescriptor("ToType");
		fd.setInputter(new SelectInput(
			new Object[]{
				"Integer",
				"Long",
				"Double"
			},
			new Object[]{ 'I', 'L', 'D' }
		));
	}

	public Object transform(ProcessInstance instance, Map parameterMap,  Map options) throws UEngineException {
		Object value = null;
		Object pram = parameterMap.get("input");
		try{
			
			switch (toType) {
				case 'L':
					value = (long) Double.parseDouble(pram.toString());
					break;
				case 'D':
					value = Double.parseDouble(pram.toString());
					break;
				default:
					value = (int) Double.parseDouble(pram.toString());
					break;
			}
		}catch (Exception e) {
			throw new UEngineException("This value("+pram+") is null or don't transform to Number ",e);
		}
		
		return value;
	}

	public String[] getInputArguments() {
		return new String[]{"input"};
	}
	

	char toType;
		public char getToType() {
			return toType;
		}
	
		public void setToType(char toType) {
			this.toType = toType;
		}

}
