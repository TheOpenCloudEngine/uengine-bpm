package org.uengine.util;

/**
 * XMLUtils - Use for XML
 * @author grayspec
 */
public class XMLUtil {

	/**
	 * XML attribute value Encode
	 * @param s xml attribute values
	 * @return convert "&"->"&amp;", "<"->"&lt;", ">"->"&gt;" 
	 */
	public static String encodeXMLReservedWords(String s){
		if(s.contains("&")){
			s = s.replaceAll("&", "&amp;");
		}
		
		if(s.contains("<")){
			s = s.replaceAll("<", "&lt;");
		}
		
		if(s.contains(">")){
			s = s.replaceAll(">", "&gt;");
		}
		return s;
	}
	
	/**
	 * XML attribute value Decode
	 * @param s xml attribute values
	 * @return convert "&amp;"->"&", "&lt;"->"<", "&gt;"->">" 
	 */
	public static String decodeXMLReservedWords(String s){
		if(s.contains("&amp;")){
			s = s.replaceAll("&amp;","&");
		}
		
		if(s.contains("&lt;")){
			s = s.replaceAll("&lt;","<");
		}
		
		if(s.contains("&gt;")){
			s = s.replaceAll("&gt;",">");
		}
		return s;
	}
}
