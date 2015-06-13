package org.uengine.processdesigner.mapper.transformers;

import java.util.Map;

import org.metaworks.FieldDescriptor;
import org.metaworks.Type;
import org.metaworks.inputter.TextInput;
import org.uengine.kernel.ProcessInstance;
import org.uengine.kernel.UEngineException;
import org.uengine.processdesigner.mapper.Transformer;

public class MergerTransformer extends Transformer {
	
	public MergerTransformer() {
		setName("Merger");
	}
	
	@Override
	public String[] getInputArguments() {
		return new String[] { "in1", "in2", "in3", "in4", "in5", "in6", "in7", "in8", "in9", "in10" };
	}
	

	@Override
	public Object transform(ProcessInstance instance, Map parameterMap, Map options) throws Exception {
		return null;
	}
	

}
