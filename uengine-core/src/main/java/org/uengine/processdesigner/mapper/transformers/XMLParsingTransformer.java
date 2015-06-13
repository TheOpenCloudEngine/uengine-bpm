package org.uengine.processdesigner.mapper.transformers;

import java.io.ByteArrayInputStream;
import java.util.Map;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;

import org.uengine.kernel.ProcessInstance;
import org.uengine.processdesigner.mapper.Transformer;
import org.xml.sax.InputSource;

public class XMLParsingTransformer extends Transformer {

	@Override
	public String[] getInputArguments() {
		// TODO Auto-generated method stub
		return new String[]{"xml"};
	}

	public String[] getOutputArguments() {
		// TODO Auto-generated method stub
		return xpathExpressions;
	}

	@Override
	protected Object transform(ProcessInstance instance, Map parameterMap,
			Map options) throws Exception {

			String xml = (String) parameterMap.get("xml");
			String outputArgumentName = (String)options.get(OPTION_KEY_OUTPUT_ARGUMENT);

			InputSource inputSource;
			XPath xpath = null;
			
			String contextKey = this.getClass().getName() + this.hashCode() + ".result";
			String contextKey2 = this.getClass().getName() + this.hashCode() + ".xpath";
			
			//try to find existing parsed one.
			inputSource = (InputSource) instance.getProcessTransactionContext().getSharedContext(contextKey);

			if(inputSource ==null){
				xpath = XPathFactory.newInstance().newXPath();

				inputSource = new InputSource(new ByteArrayInputStream(xml.getBytes()));

				//put the parsed inputSource to prevent duplicated parsing
				instance.getProcessTransactionContext().setSharedContext(contextKey, inputSource);
				instance.getProcessTransactionContext().setSharedContext(contextKey2, xpath);
			}
			
		 
			String expression = outputArgumentName;
			
			Object nodes = xpath.evaluate(expression, inputSource, XPathConstants.STRING);


		return nodes;
	}


	String[] xpathExpressions;
		
		public String[] getXpathExpressions() {
			return xpathExpressions;
		}
	
		public void setXpathExpressions(String[] xpathExpressions) {
			this.xpathExpressions = xpathExpressions;
		}

	
}

