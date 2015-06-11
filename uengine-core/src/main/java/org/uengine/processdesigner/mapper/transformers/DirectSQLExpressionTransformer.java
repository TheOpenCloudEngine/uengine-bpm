package org.uengine.processdesigner.mapper.transformers;

import java.util.Map;

import org.metaworks.FieldDescriptor;
import org.metaworks.Type;
import org.metaworks.inputter.SelectInput;
import org.metaworks.inputter.TextInput;
import org.uengine.kernel.ProcessInstance;
import org.uengine.processdesigner.mapper.Transformer;

public class DirectSQLExpressionTransformer extends Transformer {
	
	private String value;
		public String getValue() {
			return value;
		}

		public void setValue(String strValue) {
			this.value = strValue;
		}
	
	private char type;
		public char getType() {
			return type;
		}

		public void setType(char type) {
			this.type = type;
		}

	public DirectSQLExpressionTransformer() {
		// TODO Auto-generated constructor stub
		setName("Direct SQL Expression");
	}


	public static void metaworksCallback_changeMetadata(Type type) {

		FieldDescriptor fd1 = type.getFieldDescriptor("Type");
		fd1.setInputter(new SelectInput(
			new Object[]{ "String", "Integer", "Double", "Long" },
			new Object[]{ 'S', 'I', 'L', 'D' }
		));
		
		FieldDescriptor fd2 = type.getFieldDescriptor("value");
		fd2.setInputter(new TextInput());
		
	}


	@Override
	public String[] getInputArguments() {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public Object transform(ProcessInstance instance, Map parameterMap, Map options) {
		Object objValue = null;
		switch (type) {
			case 'S':
				objValue = value;
				break;
			case 'I':
				objValue = new Integer(value);
				break;
			case 'L':
				objValue = new Long(value);
				break;
			case 'D':
				objValue = new Double(value);
				break;
			default:
				objValue = value;
				break;
		}
		
		return objValue;
	}
}
