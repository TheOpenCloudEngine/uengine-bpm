package org.uengine.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.Map.Entry;

/**
 * @author YongHong Lee
 * 
 */
public class URLConn {
	
	private String urlAddress;
		public String getUrlAddress() {
			return urlAddress;
		}
	    /**
	     * URL Address 값을 설정하는 메서드.<p>
	     * urlAddress 의 접두에 http:// 가 없다면 생성합니다.
	     * @param	urlAddress	a String
	     */
		public void setUrlAddress(String urlAddress) {
			if (urlAddress.indexOf("://") > -1) {
				this.urlAddress = urlAddress;
			} else {
				this.urlAddress = "http://" + urlAddress;
			}
		}

	private String argument;
		public String getArgument() {
			return argument;
		}
	    /**
	     * argument 값을 설정하는 메서드.<p>
	     * ?를 제외한 parameter1=value1<code>&</code>parameter2=value2<br>
	     * <code>&</code>parameter3=value3 의 형식을 권고합니다.
	     * @param	argument	a String
	     */
		public void setArgument(String argument) {
			this.argument = argument;
		}
		
	    /**
	     * argument 값을 설정하는 메서드.<p>
	     * HashMap의 key와 value는 String형의 사용을 권고합니다.<p>
	     * <code>Map argumentMap = new HashMap();
	     * argumentMap.put("key", "value");</code><p>
	     * key는 parameter name, value는 parameter value 로 사용됩니다.
	     * @param	argumentMap	a HashMap
	     */
		public void setArgument(Map argumentMap) {
			this.setArgument(argumentMap.entrySet());
		}
		
		public void setArgument(Properties properties) {
			this.setArgument(properties.entrySet());
		}
		
		private void setArgument(Set<Entry<Object, Object>> entrySet) {
			StringBuilder arguments = new StringBuilder();
			for (Entry<Object, Object> entry : entrySet) {
				try {
					
					String value = String.valueOf(entry.getValue());
					if (value != null && value.length() > 0) {
						if (arguments.length() > 0) arguments.append("&");
						value = URLEncoder.encode(value, "UTF-8");
						arguments.append(entry.getKey()).append("=").append(value);
					}
					
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}
			}
			
			this.argument = arguments.toString();
		}
		
	public String getFullAddress() {
		String tempArgument = getArgument() != null ? "?" + getArgument() : "";
		String fullAddress = getUrlAddress() + tempArgument;
		return fullAddress;
	}
	
	public boolean isLive(int connectTimeout) {
		
		boolean isLive = false;
		
		InputStream is = null;
		BufferedReader br = null;

		try {
			URL url = new URL(getUrlAddress());
			URLConnection urlC;
			urlC = url.openConnection();
			urlC.setUseCaches(false);
			urlC.setDoOutput(false);
			urlC.setConnectTimeout(connectTimeout);
			urlC.setRequestProperty("content-type", "application/x-www-form-urlencoded");
			urlC.setRequestProperty("Connection", "close");
			String headerType = urlC.getContentType();
			urlC.connect();

			is = urlC.getInputStream();

			if (headerType.toUpperCase().indexOf("UTF-8") != -1)
				br = new BufferedReader(new InputStreamReader(is, "UTF-8"));
			else
				br = new BufferedReader(new InputStreamReader(is, "EUC-KR"));

			isLive = true;
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (br != null)
				try {
					br.close();
				} catch (IOException e) {
				}
			if (is != null)
				try {
					is.close();
				} catch (IOException e) {
				}
		}

		
		return isLive;
	}
		
	public String execute() throws IOException {
		
		StringBuffer returnStr = new StringBuffer();
		
		InputStream is = null;
		BufferedReader br = null;
		
		try {
			
			System.out.println(this.getFullAddress());
			
			URL url = new URL(this.getFullAddress());
			URLConnection urlC;
			urlC = url.openConnection();
			urlC.setUseCaches(false);
			urlC.setDoOutput(false);
			urlC.setRequestProperty("content-type", "application/x-www-form-urlencoded");
			urlC.setRequestProperty("Connection", "close");
			String headerType = urlC.getContentType();
			urlC.connect();
			
			is = urlC.getInputStream();

			if (headerType.toUpperCase().indexOf("UTF-8") != -1)
				br = new BufferedReader(new InputStreamReader(is, "UTF-8"));
			else
				br = new BufferedReader(new InputStreamReader(is, "EUC-KR"));

			char[] buff = new char[1024];
	        int len = -1;
			
			while ((len = br.read(buff)) != -1) {
				returnStr.append(new String(buff, 0, len));
			}
			
		} finally {
			if (br != null)
				try {
					br.close();
				} catch (IOException e) {
				}
			if (is != null)
				try {
					is.close();
				} catch (IOException e) {
				}
		}
		
		return returnStr.toString();
	}
	
	public static void main(String args[]) {
		URLConn urlConn = new URLConn();
		
		urlConn.setUrlAddress("http://www.naver.com");
		
		java.util.LinkedHashMap<String, Object> argumentMap = new java.util.LinkedHashMap<String, Object>();
		argumentMap.put("StringValue", "str");
		argumentMap.put("BooleanValue", true);
		argumentMap.put("IntValue", 1);
		urlConn.setArgument(argumentMap);
		
		System.out.println(urlConn.getFullAddress());
		try {
			System.out.println(urlConn.execute());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}
