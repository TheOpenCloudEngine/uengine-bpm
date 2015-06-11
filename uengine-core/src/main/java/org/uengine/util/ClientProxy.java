package org.uengine.util;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.commons.httpclient.Credentials;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.MultiThreadedHttpConnectionManager;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.UsernamePasswordCredentials;
import org.apache.commons.httpclient.auth.AuthScope;
import org.apache.commons.httpclient.cookie.CookiePolicy;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.uengine.kernel.GlobalContext;
import org.uengine.kernel.UEngineException;

//import org.uengine.processdesigner.ProcessDesigner;


/**
 * ���ø����̼ǰ� ���?�� ���; ����Ѵ�.
 *
 * @author  <a href="mailto:ghbpark@hanwha.co.kr">Sungsoo Park</a>
 * @version    $Id: ClientProxy.java,v 1.3 2012/10/23 07:11:10 pongsor Exp $
 */
public class ClientProxy {

	private HttpClient httpClient;
	String userId = System.getProperty("userId");
	
	public HttpClient getHttpClient(){
		return httpClient;
	}
	
	public ClientProxy() throws Exception {
		String host = System.getProperty("bpm_host", "localhost");
		String port = System.getProperty("bpm_port", "80");
		init(host, port);
	}
	
	public ClientProxy(String host, String port) throws Exception {
		init(host, port);
	}	
	
	private void init(String host, String port) throws Exception {
		System.out.println("init ClientProxy...");
		
//		String host = System.getProperty("bpm_host", "127.0.0.1");
//		String port = System.getProperty("bpm_port", "80");
		
		
		String userId = System.getProperty("bpm_userId", "209105");
		String passwd = System.getProperty("bpm_encryptedPasswd", "pwd");
				
		System.out.println("host : " + host + ", port : " + port + ", userId : " + userId);
		
//		httpClient = new HttpClient();
		httpClient = new HttpClient(new MultiThreadedHttpConnectionManager());
		httpClient.getHostConfiguration().setHost(host, Integer.parseInt(port), "http");
		httpClient.getParams().setCookiePolicy(CookiePolicy.BROWSER_COMPATIBILITY);

		//TODO: login mechanism for jws client
		/*		CookieSpec cookiespec = CookiePolicy.getDefaultSpec();
		
		PostMethod authpost = new PostMethod(GlobalContext.WEB_CONTEXT_ROOT+"/logon/login.jsp");
		
		NameValuePair userid   = new NameValuePair("userId", userId);
		NameValuePair password = new NameValuePair("passwd", passwd);
		NameValuePair forcedLogin = new NameValuePair("forcedLogin", "true");
		authpost.setRequestBody(new NameValuePair[] {userid, password, forcedLogin});
		
		httpClient.executeMethod(authpost);
        
        System.out.println("Login form post: " + authpost.getStatusLine().toString()); 
		authpost.releaseConnection();

		Cookie[] logoncookies = cookiespec.match(
				host, Integer.parseInt(port), "/", false, httpClient.getState().getCookies());
		System.out.println("Logon cookies:");    
		if (logoncookies.length == 0) {
			System.out.println("None");    
		} else {
			for (int i = 0; i < logoncookies.length; i++) {
				System.out.println("- " + logoncookies[i].toString());    
			}
		}*/    	
 	}
	
	private InputStream executeMethodAsStream(HttpMethod method) throws Exception {
		InputStream is = null;
		try {
System.out.println("target url : "+ method.getPath());
			httpClient.executeMethod(method);
			is = method.getResponseBodyAsStream();
		} catch (Exception e) {
			throw e;
		} finally {
//			method.releaseConnection();
		}
		return is;
	}
	
	
	private BufferedReader executeMethod(HttpMethod method) throws Exception {
		return new BufferedReader(
				new InputStreamReader(executeMethodAsStream(method), GlobalContext.ENCODING));
	}
	
	private String executeMethodAsString(HttpMethod method) throws Exception {
		
		System.out.println("target url   : "+ method.getPath());
		System.out.println("query string : "+ method.getQueryString());
		
		String result = null;
		try {
			httpClient.executeMethod(method);
			result = method.getResponseBodyAsString();
		} catch (Exception e) {
			throw e;
		} finally {
			method.releaseConnection();
		}
		return result;
	}

	private NameValuePair[] createParameter(Map map) throws Exception {
//		System.out.println("map : " + map);
		NameValuePair[] nvPair = new NameValuePair[map.size()];
		Iterator iter = map.keySet().iterator();
		for(int i=0; iter.hasNext(); i++) {
			String key = (String)iter.next();
			String value = (String)map.get(key);
			if ( value != null ) value = fromEncode(value);
			nvPair[i] = new NameValuePair(key, value);
		}
		return nvPair;
	}
	
	private static String fromEncode(String str) {
        if (str == null)
            return null;
        try {
			return new String(str.getBytes("UTF-8"), "8859_1");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return null;
    }	
	
	public InputStream showProcessDefinitionWithDefinitionId(String id) throws Exception {
//		GetMethod method = 
//				new GetMethod(GlobalContext.WEB_CONTEXT_ROOT+"/processmanager/showDefinitionInLanguage.jsp?language=XPD&processDefinition="+URLEncoder.encode(id)+"&userId=" + userId);
		GetMethod method = 
				new GetMethod(GlobalContext.WEB_CONTEXT_ROOT+"/dwr/xstr-rpc?className=org.uengine.codi.mw3.model.ResourceFile&methodName=getProcessDefinition&object=" + URLEncoder.encode("<org.uengine.codi.mw3.model.ResourceFile><alias>"+id+"</alias></org.uengine.codi.mw3.model.ResourceFile>"));
		return executeMethodAsStream(method);
	}
	
	public InputStream showProcessDefinitionWithVersionId(String id) throws Exception {
		String loaderURL = System.getProperty("loader_url", GlobalContext.WEB_CONTEXT_ROOT+"/processmanager/showDefinitionInLanguage.jsp");
		System.out.println("loaderURL: " + loaderURL);
		GetMethod method = 
			new GetMethod(loaderURL + "?language=XPD&versionId="+id);
		return executeMethodAsStream(method);
	}
	
	public InputStream showProcessDefinitionWithInstanceId(String id) throws Exception {
		GetMethod method = 
			new GetMethod(GlobalContext.WEB_CONTEXT_ROOT+"/processmanager/showDefinitionInLanguage.jsp?language=XPD&instanceId="+id);
		return executeMethodAsStream(method);
	}
	
	public InputStream showRuleDefinitionWithDefinitionId(String id) throws Exception {
		GetMethod method = 
			new GetMethod(GlobalContext.WEB_CONTEXT_ROOT+"/processmanager/showRuleDefinitionInLanguage.jsp?language=XPD&ruleDefId="+id);
		return executeMethodAsStream(method);
	}	
	
	public InputStream showFormDefinitionWithDefinitionId(String id) throws Exception {
		GetMethod method = 
			new GetMethod(GlobalContext.WEB_CONTEXT_ROOT+"/processmanager/showFormDefinitionInLanguage.jsp?language=XPD&formDefId="+URLEncoder.encode(id, "UTF-8")+"&userId=" + userId);
		return executeMethodAsStream(method);
	}
	
	public InputStream showFormDefinitionWithVersionId(String id) throws Exception {
		GetMethod method = 
			new GetMethod(GlobalContext.WEB_CONTEXT_ROOT+"/processmanager/showFormDefinitionWithVersionIdInLanguage.jsp?language=XPD&versionId="+id);
		return executeMethodAsStream(method);
	}

	public InputStream showObjectDefinitionWithDefinitionId(String id) throws Exception {
		GetMethod method = 
			new GetMethod(GlobalContext.WEB_CONTEXT_ROOT+"/processmanager/showObjectDefinition.jsp?objDefId="+id);
		return executeMethodAsStream(method);
	}	

	public InputStream getProcessInstance(String id) throws Exception {
		GetMethod method = 
			new GetMethod(GlobalContext.WEB_CONTEXT_ROOT+"/processmanager/getProcessInstance.jsp?instanceId="+id);
		return executeMethodAsStream(method);
	}
	
	public InputStream openStream(String path) throws Exception {
		
		try{
			GetMethod method = 
				new GetMethod(GlobalContext.WEB_CONTEXT_ROOT+path);
			return executeMethodAsStream(method);
		}catch(Exception e){
			throw new UEngineException("Fail to open url : " + getHttpClient().getHostConfiguration().getHostURL() + GlobalContext.WEB_CONTEXT_ROOT+path);
		}
	}
	
	//visual mashup에서 사용하기 위해 임시로 추가함.
	public InputStream openStreamForVisualMashup(String uri) throws Exception {
		
		String vmHost = GlobalContext.getPropertyString("visualmashup.host", "localhost");
		String vmPort = GlobalContext.getPropertyString("visualmashup.port", "9090");
		
		try{
			GetMethod method = 
				new GetMethod(uri);
			
			InputStream is = null;
			try {
				HttpClient httpClient = new HttpClient(new MultiThreadedHttpConnectionManager());
				
				httpClient = new HttpClient(new MultiThreadedHttpConnectionManager());
				httpClient.getHostConfiguration().setHost(vmHost, Integer.parseInt(vmPort), "http");
				httpClient.getParams().setCookiePolicy(CookiePolicy.DEFAULT);
				
				httpClient.executeMethod(method);
				is = method.getResponseBodyAsStream();
			} catch (Exception e) {
				throw e;
			} finally {
//				method.releaseConnection();
			}
			return is;
			
		}catch(Exception e){
			throw new UEngineException("Fail to open url : " + uri);
		}
	}

	public InputStream postStream(String path, Map formValueMap) throws Exception {
		
		try{
			PostMethod method = 
				new PostMethod(GlobalContext.WEB_CONTEXT_ROOT+path);
			method.setRequestBody(createParameter(formValueMap));
			method.setRequestHeader("Content-Type", PostMethod.FORM_URL_ENCODED_CONTENT_TYPE 
					+ "; charset=" + "UTF-8");
			return executeMethodAsStream(method);
		}catch(Exception e){
			throw new UEngineException("Fail to open url : " + getHttpClient().getHostConfiguration().getHostURL() + GlobalContext.WEB_CONTEXT_ROOT+path);
		}
	}

	public String saveProcessDefinition(Map formValueMap) throws Exception {
		String saverUrl = System.getProperty("saver_url",GlobalContext.WEB_CONTEXT_ROOT+"/processmanager/saveProcessDefinition.jsp");
		
		formValueMap.put("userId", userId);
		
		PostMethod method = 
			new PostMethod(saverUrl);
		method.setRequestBody(createParameter(formValueMap));
		method.setRequestHeader("Content-Type", PostMethod.FORM_URL_ENCODED_CONTENT_TYPE 
				+ "; charset=" + "UTF-8");
		System.out.println("saveUrl:" + saverUrl);

//		method.setRequestHeader("Content-type", "text/plain; charset=UTF-8");
//		NameValuePair[] nvPair = method.getParameters();
//		for(int i=0; i<nvPair.length; i++) {
//			System.out.println(i + " : " + nvPair[i].getName() + ", " + nvPair[i].getValue());
//		}
		return executeMethodAsString(method);
	}
	
	public String saveFormDefinition(String defVerId, String definition) throws Exception {
		HashMap formValueMap = new HashMap();
		formValueMap.put("definition", definition);
		formValueMap.put("defVerId", defVerId);
		
		//String saverUrl = System.getProperty("saver_url",GlobalContext.WEB_CONTEXT_ROOT+"/processmanager/saveProcessDefinition.jsp");
		
		PostMethod method = 
			new PostMethod(GlobalContext.WEB_CONTEXT_ROOT + "/processmanager/saveFormDefinitionWithDefinitionAndDefVerId.jsp");
		method.setRequestBody(createParameter(formValueMap));
		method.setRequestHeader("Content-Type", PostMethod.FORM_URL_ENCODED_CONTENT_TYPE 
				+ "; charset=" + "UTF-8");

		return executeMethodAsString(method);
	}
	
	public String createFormDefinition(String name, String alias, String definition) throws Exception {
		HashMap formValueMap = new HashMap();
		formValueMap.put("name", name);
		formValueMap.put("alias", alias);
		formValueMap.put("definition", definition);
		
		//String saverUrl = System.getProperty("saver_url",GlobalContext.WEB_CONTEXT_ROOT+"/processmanager/saveProcessDefinition.jsp");
		
		PostMethod method = 
			new PostMethod(GlobalContext.WEB_CONTEXT_ROOT + "/processmanager/saveFormDefinitionWithDefinitionAndDefVerId.jsp");
		method.setRequestBody(createParameter(formValueMap));
		method.setRequestHeader("Content-Type", PostMethod.FORM_URL_ENCODED_CONTENT_TYPE 
				+ "; charset=" + "UTF-8");

		return executeMethodAsString(method);
	}
	
	public String changeProcessDefinition(Map map) throws Exception {
		PostMethod method = 
			new PostMethod(GlobalContext.WEB_CONTEXT_ROOT+"/processmanager/changeProcessDefinition.jsp");
		method.setRequestBody(createParameter(map));
		method.setRequestHeader("Content-Type", PostMethod.FORM_URL_ENCODED_CONTENT_TYPE 
				+ "; charset=" + "UTF-8");

		return executeMethodAsString(method);
	}

	public BufferedReader getProcessDefinition(){
		
		return null;
	}
	//mr.heo
	public BufferedReader getProcessDefinitionVersionList(NameValuePair[] defid) throws Exception {
		PostMethod method = 
			new PostMethod(GlobalContext.WEB_CONTEXT_ROOT+"/admin/processmanager/processDefinitionVersionListXML.jsp");
		method.setRequestBody(defid);
		BufferedReader reader = executeMethod(method);
		return reader;
	}
	public BufferedReader getProcessDefinitionList() throws Exception {
		PostMethod method = 
			new PostMethod(GlobalContext.WEB_CONTEXT_ROOT+"/admin/processmanager/processDefinitionListXML.jsp");
		BufferedReader reader = executeMethod(method);
		return reader;
	}
	
	public BufferedReader getDeptList(int id) throws Exception {
		GetMethod method = 
			new GetMethod(GlobalContext.WEB_CONTEXT_ROOT+"/common/org/getGroupXML.jsp?id="+id);
		System.out.println("[getDeptList] DEBUG1 : ");
		BufferedReader reader = executeMethod(method);
		System.out.println("[getDeptList] DEBUG2");
		return reader;
	}
	
	//heo
	public BufferedReader getChildDeptList(int deptId) throws Exception {
		GetMethod method = 
			new GetMethod(GlobalContext.WEB_CONTEXT_ROOT+"/common/org/getGroupXML.jsp?deptId="+deptId);
		System.out.println("[getDeptList] DEBUG1 : ");
		BufferedReader reader = executeMethod(method);
		System.out.println("[getDeptList] DEBUG2");
		return reader;
	}
	
	public BufferedReader getRootDept() throws Exception {
		GetMethod method = 
			new GetMethod(GlobalContext.WEB_CONTEXT_ROOT+"/common/org/getGroupXML.jsp?deptId=root");
		System.out.println("[getDeptList] DEBUG1 : ");
		BufferedReader reader = executeMethod(method);
		System.out.println("[getDeptList] DEBUG2");
		return reader;
	}
	
	

	public BufferedReader getUserList(long id) throws Exception {
		GetMethod method = 
			new GetMethod(GlobalContext.WEB_CONTEXT_ROOT+"/common/org/getUserXML.jsp?id="+id);
		System.out.println("[getUserList] DEBUG1 : ");
		BufferedReader reader = executeMethod(method);
		System.out.println("[getUserList] DEBUG2");
		return reader;
	}

	public BufferedReader getFormGroupList(int id) throws Exception {
		GetMethod method = 
			new GetMethod(GlobalContext.WEB_CONTEXT_ROOT+"/admin/formmanager/getFormGroupXML.jsp?ownCompany="+id);
		System.out.println("[getFormGroupList] DEBUG1 : ");
		BufferedReader reader = executeMethod(method);
		System.out.println("[getFormGroupList] DEBUG2");
		return reader;
	}


	public BufferedReader getFormList(int id) throws Exception {
		GetMethod method = 
			new GetMethod(GlobalContext.WEB_CONTEXT_ROOT+"/admin/formmanager/getFormXML.jsp?id="+id);
		System.out.println("[getFormList] DEBUG1 : ");
		BufferedReader reader = executeMethod(method);
		System.out.println("[getFormList] DEBUG2");
		return reader;
	}
	
	public BufferedReader getRoleBasedRoleResolutionContext() throws Exception {
		PostMethod method = 
			new PostMethod(GlobalContext.WEB_CONTEXT_ROOT+"/common/org/RoleBasedXML.jsp");
		BufferedReader reader = executeMethod(method);
		return reader;
	}
	
	public BufferedReader getGroupBasedRoleResolutionContext() throws Exception {
		PostMethod method = 
			new PostMethod(GlobalContext.WEB_CONTEXT_ROOT+"/common/org/GroupBasedXML.jsp");
		//method.setRequestBody(useYN);
		BufferedReader reader = executeMethod(method);
		return reader;
	}
	
	
	public BufferedReader findByRoleID(NameValuePair[] roleid) throws Exception {
		PostMethod method = 
			new PostMethod(GlobalContext.WEB_CONTEXT_ROOT+"/common/org/RoleBasedXML.jsp");
		method.setRequestBody(roleid);
		BufferedReader reader = executeMethod(method);
		return reader;
	}
	
	public String makeMime(Map map) throws Exception {
		PostMethod method = 
			new PostMethod(GlobalContext.WEB_CONTEXT_ROOT+"/modules/transfer/makeMime.jsp");
		method.setRequestBody(createParameter(map));
		method.setRequestHeader("Content-Type", PostMethod.FORM_URL_ENCODED_CONTENT_TYPE 
				+ "; charset=" + "UTF-8");

		return executeMethodAsString(method);
	}	
	
	

//	/**
//	 *  ���缱���ø� ����
//	 */
//	public BufferedReader saveWF(String wfXml) throws Exception {
//		
//		fullURL = new URL(urlBase, 
//			contextBase + "servlet/hanwha.gw.approval.web.servlet.SaveWFServlet"); // real
//		System.out.println("[saveWF] DEBUG1");
//		
//		System.out.println("[saveWF] DEBUG1 : " + fullURL);
//		in = ServletWriter.postXML(fullURL, wfXml);
//		System.out.println("[saveWF] DEBUG2");
//		return reader;
//	}
//
//	/**
//	 *  ���缱���ø� ��n�1�
//	 */
//	public BufferedReader getWF(int docId) throws Exception {
//		fullURL = new URL(urlBase,
//			contextBase + "servlet/hanwha.gw.approval.web.servlet.GetWFServlet?docId=" + docId);
//		System.out.println("[getWF] DEBUG1");
//		in = ServletWriter.postXML(fullURL, null);
//		System.out.println("[getWF] DEBUG2");
//		return reader;
//	}
//	
//	
//	/**
//	 *  �Ⱓ�迬�� d�� ����
//	 */
//	public BufferedReader saveAI(String aiXml) throws Exception {
//		
//		fullURL = new URL(urlBase, 
//			contextBase + "servlet/hanwha.gw.approval.web.servlet.SaveAIServlet"); // real
//		System.out.println("[saveAI] DEBUG1");
//		
//		System.out.println("[saveAI] DEBUG1 : " + fullURL);
//		in = ServletWriter.postXML(fullURL, aiXml);
//		System.out.println("[saveAI] DEBUG2");
//		return reader;
//	}
//
//	/**
//	 *  �Ⱓ�迬�� d�� ��n�1�
//	 */
//	public BufferedReader getAI(String fileName) throws Exception {
//		fullURL = new URL(urlBase,
//			contextBase + "servlet/hanwha.gw.approval.web.servlet.GetAIServlet?fileName=" + fileName);
//		System.out.println("[getAI] DEBUG1");
//		in = ServletWriter.postXML(fullURL, null);
//		System.out.println("[getAI] DEBUG2");
//		return reader;
//	}


	public static void main(String args[]) {
//		ClientProxy proxy = ClientProxy.getInstance();
//		try {
//			Reader reader = proxy.getWF(1);
//			String wfXml = StreamUtils.getString(reader);
//			System.out.println("wfXml : " + wfXml);
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
	}
}

/*
 * $Log: ClientProxy.java,v $
 * Revision 1.3  2012/10/23 07:11:10  pongsor
 * first version of uengine 4 mw3 (codi)
 *
 * Revision 1.2  2012/04/18 09:55:42  pongsor
 * *** empty log message ***
 *
 * Revision 1.1  2012/02/13 05:29:10  sleepphoenix4
 * initial commit uEngine package, since 2012
 *
 * Revision 1.15  2009/03/08 01:56:34  pongsor
 * Automatic Form Generation
 * Automatic Variable Generation
 *
 * Revision 1.14  2009/01/05 13:40:20  kmooje
 * *** empty log message ***
 *
 * Revision 1.13  2008/01/29 04:10:29  pongsor
 * *** empty log message ***
 *
 * Revision 1.12  2007/12/12 06:36:41  pongsor
 * *** empty log message ***
 *
 * Revision 1.11  2007/12/12 06:26:44  pongsor
 * *** empty log message ***
 *
 * Revision 1.10  2007/12/05 02:31:29  curonide
 * *** empty log message ***
 *
 * Revision 1.5  2007/12/04 07:34:42  bpm
 * *** empty log message ***
 *
 * Revision 1.3  2007/12/04 05:25:42  bpm
 * *** empty log message ***
 *
 * Revision 1.9  2007/10/03 16:39:37  pongsor
 * *** empty log message ***
 *
 * Revision 1.8  2007/08/27 04:21:45  pongsor
 * Transaction management
 *
 * Revision 1.7  2007/02/02 04:20:35  pongsor
 * *** empty log message ***
 *
 * Revision 1.5  2006/11/21 07:05:09  pongsor
 * *** empty log message ***
 *
 * Revision 1.4  2006/11/08 07:35:10  yongheekim
 * *** empty log message ***
 *
 * Revision 1.3  2006/09/02 06:28:42  pongsor
 * *** empty log message ***
 *
 * Revision 1.2  2006/07/07 01:53:23  pongsor
 * 1. Database Synchronized Process Variable
 * 2. Customizable Instance List Feature is added
 * 3. 'Key' field name has been renamed as 'KeyName' in the ProcessVariable table since the 'key' is a reserved word in MySQL.
 *
 * Revision 1.1  2006/06/17 07:51:43  pongsor
 * uEngine 2.0
 *
 * Revision 1.18  2006/01/20 03:56:06  mrheo
 * getchilddeptlist, getrootdept �߰�
 *
 * Revision 1.17  2006/01/06 16:23:46  phz
 * *** empty log message ***
 *
 * Revision 1.16  2006/01/06 16:02:47  phz
 * *** empty log message ***
 *
 * Revision 1.15  2006/01/04 12:30:46  ghbpark
 * *** empty log message ***
 *
 * Revision 1.14  2006/01/04 01:34:54  mrheo
 * �׷�8�� ��d
 *
 * Revision 1.13  2005/12/29 00:43:39  uengine
 * 1. Hot process definition deploy
 * 2. Event Driven Sub Process�� event Handler activity�� ���� instanceó��
 *
 * Revision 1.12  2005/12/23 11:34:44  southshine
 * v�� �����Ͱ� ū���� ����ֱ⶧���� int --> long, Integer --> BigDecimal �� ��ü�Ͽ�=
 *
 * Revision 1.11  2005/12/15 05:02:14  mrheo
 * *** empty log message ***
 *
 * Revision 1.10  2005/12/10 06:40:15  mrheo
 * *** empty log message ***
 *
 * Revision 1.9  2005/11/22 08:29:13  mrheo
 * *** empty log message ***
 *
 * Revision 1.8  2005/11/15 09:47:43  mrheo
 * *** empty log message ***
 *
 * Revision 1.7  2005/11/01 04:09:44  mahler
 * *** empty log message ***
 *
 * Revision 1.6  2005/10/14 08:23:26  ghbpark
 * *** empty log message ***
 *
 * Revision 1.5  2005/10/14 07:51:58  uengine
 * *** empty log message ***
 *
 * Revision 1.4  2005/10/10 01:17:13  ghbpark
 * FormLink �۾� 1�� �Ϸ�
 *
 * Revision 1.3  2005/09/13 10:31:44  ghbpark
 * ClientProxy -> Singleton 8�� ���� �� ���� ó�� �߰�
 *
 * Revision 1.2  2005/09/12 01:56:56  ghbpark
 * /hwbpm 8�� ����
 *
 * Revision 1.1  2005/09/06 07:08:17  ghbpark
 * EagleBPM 2.0 start
 *
 * Revision 1.1  2005/04/11 10:29:17  ghbpark
 * *** empty log message ***
 *
 * Revision 1.1  2005/04/03 02:34:12  ghbpark
 * *** empty log message ***
 *
 * Revision 1.2  2005/02/11 07:28:23  ghbpark
 * *** empty log message ***
 *
 * Revision 1.1  2005/01/11 09:10:28  ghbpark
 * *** empty log message ***
 *
 * Revision 1.3  2005/01/11 01:01:06  ghbpark
 * *** empty log message ***
 *
 * Revision 1.2  2005/01/10 03:04:27  ghbpark
 * *** empty log message ***
 *
 * Revision 1.1  2004/12/28 11:24:43  ghbpark
 * *** empty log message ***
 *
 * Revision 1.1  2004/12/28 09:05:19  ghbpark
 * *** empty log message ***
 *
 * Revision 1.1  2004/12/28 02:43:38  ghbpark
 * *** empty log message ***
 *
 * Revision 1.1  2004/12/28 02:09:56  ghbpark
 * *** empty log message ***
 *
 * Revision 1.3  2004/09/07 23:30:27  ghbpark
 * *** empty log message ***
 *
 * Revision 1.2  2004/08/19 04:21:57  ghbpark
 * ��, ��å ȣ�� �κ� ��� ��d
 *
 * Revision 1.1  2004/08/16 10:31:13  ghbpark
 * *** empty log message ***
 *
 */