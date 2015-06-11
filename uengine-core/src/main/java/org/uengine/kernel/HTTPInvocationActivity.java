package org.uengine.kernel;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.MultiThreadedHttpConnectionManager;
import org.apache.commons.httpclient.cookie.CookiePolicy;
import org.apache.commons.httpclient.methods.GetMethod;
import org.metaworks.FieldDescriptor;
import org.metaworks.Type;
import org.metaworks.inputter.RadioInput;

public class HTTPInvocationActivity extends DefaultActivity{
	
	private static final long serialVersionUID = GlobalContext.SERIALIZATION_UID;
	
	final static int INVOC_TYPE_GET = 0;
	final static int INVOC_TYPE_POST = 1;

	public HTTPInvocationActivity(){
		setName("HTTP");
	}

	public static void metaworksCallback_changeMetadata(Type type){
		FieldDescriptor fd;
		
		fd = type.getFieldDescriptor("InvocationType");	
		fd.setInputter(
			new RadioInput(
				new String[]{"GET", "POST"}, 
				new Integer[]{
					new Integer(INVOC_TYPE_GET), 
					new Integer(INVOC_TYPE_POST)
				}
			)
		);
	}

	String host;

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public String getProtocol() {
		return protocol;
	}

	public void setProtocol(String protocol) {
		this.protocol = protocol;
	}

	
	String targetURL;
	
	
	public String getTargetURL() {
		return targetURL;
	}

	public void setTargetURL(String targetURL) {
		this.targetURL = targetURL;
	}

	int port;
	
	String protocol;

	String uri;
	
	public String getUri() {
		return uri;
	}

	public void setUri(String uri) {
		this.uri = uri;
	}

	int invocationType;	
		public int getInvocationType() {
			return invocationType;
		}
		public void setInvocationType(int invocationType) {
			this.invocationType = invocationType;
		}
	
	ParameterContext[] parameters;
		public ParameterContext[] getParameters() {
			return parameters;
		}
		public void setParameters(ParameterContext[] parameters) {
			this.parameters = parameters;
		}

	String payload;
		public String getPayload() {
			return payload;
		}
		public void setPayload(String payload) {
			this.payload = payload;
		}

	ProcessVariable resultPV;
			
		public ProcessVariable getResultPV() {
			return resultPV;
		}
		public void setResultPV(ProcessVariable resultPV) {
			this.resultPV = resultPV;
		}

	public void executeActivity(ProcessInstance instance) throws Exception {

		
		HttpClient httpClient = 
			httpClient = new HttpClient(new MultiThreadedHttpConnectionManager());
			httpClient.getHostConfiguration().setHost(getHost(), getPort(), getProtocol());
			httpClient.getParams().setCookiePolicy(CookiePolicy.BROWSER_COMPATIBILITY);

		HttpMethod method = new GetMethod(getUri());

		httpClient.executeMethod(method);
		String result = method.getResponseBodyAsString();
		
/*		String realTargetURL = evaluateContent(instance, getTargetURL()).toString();
		ParameterContext[] parameters = getParameters();
		String[] keyAndValues = new String[getParameters().length * 2];
		for(int i=0; i<parameters.length; i++){
			ParameterContext theParameter = parameters[i];
			keyAndValues[i*2] = theParameter.getArgument().getText();
			keyAndValues[i*2+1] = ""+ theParameter.getVariable().get(instance, "");
		}

		String result = UEngineUtil.sendViaHttpPost(realTargetURL, keyAndValues);*/
		getResultPV().set(instance, "", result);

		fireComplete(instance);
	}

}
