package org.uengine.processdesigner.mapper.transformers;

import java.text.NumberFormat;
import java.util.Locale;
import java.util.Map;

import org.metaworks.FieldDescriptor;
import org.metaworks.Type;
import org.metaworks.inputter.SelectInput;
import org.uengine.kernel.ProcessInstance;
import org.uengine.processdesigner.mapper.Transformer;

public class NumberFormatTransformer extends Transformer{

	private char inputType;
	
	private char toType;
	
	public char getInputType() {
		return inputType;
	}

	public void setInputType(char inputType) {
		this.inputType = inputType;
	}

	public char getToType() {
		return toType;
	}

	public void setToType(char toType) {
		this.toType = toType;
	}

	public NumberFormatTransformer() {
		setName("NumberFormat");
	}
	
	public static void metaworksCallback_changeMetadata(Type type){
		FieldDescriptor fd = type.getFieldDescriptor("InputType");
		fd.setDisplayName("Input Number Type");
		fd.setInputter(new SelectInput(
				new Object[]{
					"Long",
					"Double"
				},
				new Object[]{ 'L', 'D' }
			));

		fd = type.getFieldDescriptor("ToType");
		fd.setDisplayName("Output Format");
		fd.setInputter(new SelectInput(
				new Object[]{
						"Integer",
						"Currency",
						"Number",
						"Percent"
				},
				new Object[]{ 'I', 'C', 'N', 'P' }
		));
	}	

	public String[] getInputArguments() {
		return new String[]{"input", "locale"};
	}

	@Override
	public Object transform(ProcessInstance instance, Map parameterMap, Map options) {
		Object val = parameterMap.get("input");
		NumberFormat nf = null;
		
		Locale inLocale = Locale.getDefault();
		if (parameterMap.get("locale") != null) {
			try {
				String language = (String) parameterMap.get("locale");
				inLocale = new Locale(language);
			} catch (Exception e) {}
		}
		
		switch(getToType()) {
		case 'C' :
			nf = NumberFormat.getCurrencyInstance(inLocale);
			break;
		case 'N' :
			nf = NumberFormat.getNumberInstance(inLocale);
			break;
		case 'P' :
			nf = NumberFormat.getPercentInstance(inLocale);
			break;
		default: 
			nf = NumberFormat.getIntegerInstance(inLocale);
		}
		
		if (getInputType() == 'D') {
			return nf.format(Double.parseDouble(val+""));
		} else {
			return nf.format(Long.parseLong(val+""));
		}
	}
}
