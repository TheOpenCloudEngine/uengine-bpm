/*
 * Created on 2004. 9. 26.
 */
package org.uengine.kernel;

import java.io.*;

/**
 * @author Jinyoung Jang
 */
public class KeyedParameter implements Serializable{
	private static final long serialVersionUID = org.uengine.kernel.GlobalContext.SERIALIZATION_UID;

	public final static String TOOL 										= "tool";
	public final static String URL 										= "url";
	public final static String MESSAGE 								= "message";
	public final static String TRACINGTAG 							= "tracingTag";
	public final static String PROCESSDEFINITION 				= "processDefinition";
	public final static String INSTANCEID 							= "instanceId";
	public final static String ROOTINSTANCEID 					= "rootInstanceId";
	public final static String TITLE 										= "activityName";
	public final static String INSTRUCTION 							= "instruction";
	public final static String DURATION 								= "duration";
	public final static String PRIORITY 								= "priority";
	public final static String KEYWORD 								= "keywords";
	public final static String DEFAULT_STATUS 						= "default status";
	public final static String DUEDATE 								= "dueDate";
	public final static String PROCESSDEFINITIONNAME 		= "definitionName";
	public final static String DISPATCHINGOPTION 				= "dispatchingOption";
	public final static String PARTYPE 									= "parType";
	public final static String CO2EMISSION  						= "co2Emission";
	
	String key;
	Object value;
	
	public KeyedParameter(String key, Object val){
		this.key = key;
		this.value = val;
	}
	
	public KeyedParameter(){
	}

	public String getKey() {
		return key;
	}

	public Object getValue() {
		return value;
	}

	public void setKey(String string) {
		key = string;
	}

	public void setValue(Object val) {
		value = val;
	}


	public static KeyedParameter[] fromMap(java.util.Map kpv){		
		KeyedParameter[] parameters = new KeyedParameter[kpv.size()];
		
		int i=0;
		for(java.util.Iterator iter = kpv.keySet().iterator(); iter.hasNext(); ){
			String key = (String)iter.next();
			parameters[i] = new KeyedParameter();
			parameters[i].setKey(key);
			parameters[i].setValue(kpv.get(key));
			i++;
		}
		
		return parameters;		
	}
}
