package org.uengine.kernel;

import java.util.*;

import org.uengine.util.UEngineUtil;

/**
 * @author Jinyoung Jang
 */
public class URLActivity extends HumanActivity {
	private static final long serialVersionUID = org.uengine.kernel.GlobalContext.SERIALIZATION_UID;

	String url;
		public String getUrl() {
			return url;
		}
		public void setUrl(String string) {
			url = string;
		}
		
	String urlWhenSuccess;
		public String getUrlWhenSuccess() {
			return urlWhenSuccess;
		}
		public void setUrlWhenSuccess(String urlWhenSuccess) {
			this.urlWhenSuccess = urlWhenSuccess;
		}
	
	ProcessVariable resultURLPV;
		public ProcessVariable getResultURLPV() {
			return resultURLPV;
		}
		public void setResultURLPV(ProcessVariable resultURLPV) {
			this.resultURLPV = resultURLPV;
		}
		
	ProcessVariable resultHTMLPV;
		public ProcessVariable getResultHTMLPV() {
			return resultHTMLPV;
		}
		public void setResultHTMLPV(ProcessVariable resultHTMLPV) {
			this.resultHTMLPV = resultHTMLPV;
		}
	
	ParameterContext[] mappingFromResultHTML;
		public ParameterContext[] getMappingFromResultHTML(ProcessInstance instance) throws Exception{
			return getMappingFromResultHTML();
		}
		public ParameterContext[] getMappingFromResultHTML() {
			return mappingFromResultHTML;
		}
		public void setMappingFromResultHTML(ParameterContext[] mappingFromResultHTML) {
			this.mappingFromResultHTML = mappingFromResultHTML;
		}
		
	
	public URLActivity(){
		super();
		setName("url");
	}
	
	protected String createURL(ProcessInstance inst){
		return evaluateContent(inst, getUrl()).toString();
	}

	public Map createParameter(ProcessInstance instance) throws Exception{
		Map parameters = super.createParameter(instance);
		
		parameters.put("url", createURL(instance));
		
		if(getUrlWhenSuccess()!=null)
			parameters.put("urlWhenSuccess", getUrlWhenSuccess());

		return parameters;
	}
		
	protected String getExtraMessage(ProcessInstance instance) {
		return "";
	}
	
//	public Map getActivityDetails(ProcessInstance inst) throws Exception{
//		Hashtable details = (Hashtable)super.getActivityDetails(inst);
//		
//		try{
//			String url = createURL(inst);
//			details.put("url", url);
//		}catch(Exception e){}
//		
//		return details;
//	}
	
	public boolean onComplete(ProcessInstance instance, Object payload) throws Exception {
		if(payload instanceof ResultPayload){
			ResultPayload resultPayload = ((ResultPayload)payload);
			Object value = resultPayload.getExtendedValue("url_of_application_result");
			
			System.out.println("URLActivity:: onComplete.url_of_application_result = " + value + " and type = ");			
			String url_of_application_result = (String)value;

			value = resultPayload.getExtendedValue("html_of_application_result");
			String html_of_application_result = (String)value;
			
			if(getResultURLPV()!=null)
				getResultURLPV().set(instance, "", url_of_application_result);
			
			if(getResultHTMLPV()!=null)
				getResultHTMLPV().set(instance, "", html_of_application_result);
			
			ParameterContext[] mappingFromResultHTML =  getMappingFromResultHTML(instance);
			
			if(html_of_application_result!=null && mappingFromResultHTML!=null && mappingFromResultHTML.length > 0){
				for(int i=0; i<mappingFromResultHTML.length; i++){
					String mappingFormat = mappingFromResultHTML[i].getArgument().getText();
					if(mappingFormat!=null){
						
						try{
							ProcessVariable mappedVariable = mappingFromResultHTML[i].getVariable();
							String valueKey = "<%=value%>";
							int valuePos = mappingFormat.indexOf("<%=value%>");
							String start = mappingFormat.substring(0, valuePos);
							String end = mappingFormat.substring(valuePos + valueKey.length());
							
							int startPos = html_of_application_result.indexOf(start) + start.length();
							int endPos = html_of_application_result.indexOf(end, startPos);
							
							String varValueFromHTML = html_of_application_result.substring(
								startPos, endPos									
							);
							
							mappedVariable.set(instance, "", varValueFromHTML);
						}catch(Exception e){
							e.printStackTrace();
						}
					}
				}
			}
		}
		
		return super.onComplete(instance, payload);
	}

}
