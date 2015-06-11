package org.uengine.processdesigner.mapper.transformers;

import java.util.Map;

import org.metaworks.Type;
import org.uengine.kernel.GlobalContext;
import org.uengine.kernel.ProcessInstance;
import org.uengine.processdesigner.mapper.Transformer;

public class ConcatTransformer extends Transformer{
	
	private static final long serialVersionUID = GlobalContext.SERIALIZATION_UID;

	public ConcatTransformer() {
		// TODO Auto-generated constructor stub
		setName("Concat");
	}


	public String[] getInputArguments() {
		return new String[]{"str1", "str2", "str3", "str4", "str5"};
	}
	

	@Override
	public Object transform(ProcessInstance instance, Map parameterMap, Map options) {
		// TODO Auto-generated method stub
		String strTmp = "";
		for (int i = 1; i <= parameterMap.size(); i++) {
			if (parameterMap.get("str" + i) != null)
				strTmp += parameterMap.get("str" + i).toString();
		}
		return strTmp;
	}
}
