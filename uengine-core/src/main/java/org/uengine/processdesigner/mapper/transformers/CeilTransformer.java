package org.uengine.processdesigner.mapper.transformers;

import java.util.Map;

import org.metaworks.Type;
import org.uengine.kernel.ProcessInstance;
import org.uengine.processdesigner.mapper.Transformer;

public class CeilTransformer extends Transformer{
	
	public CeilTransformer() {
		setName("Ceil");
	}

	public static void metaworksCallback_changeMetadata(Type type){
	}	

	public String[] getInputArguments() {
		return new String[]{"input"};
	}

	@Override
	public Object transform(ProcessInstance instance, Map parameterMap, Map options) {
		Object val = parameterMap.get("input");
		return Math.ceil(Double.parseDouble("" + val));
	}
}
