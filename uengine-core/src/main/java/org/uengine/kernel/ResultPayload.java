/*
 * Created on 2004. 12. 14.
 */
package org.uengine.kernel;

/**
 * @author Jinyoung Jang
 */

import java.io.*;
import java.util.*;

import org.uengine.util.ForLoop;


public class ResultPayload implements Serializable{
	private static final long serialVersionUID = org.uengine.kernel.GlobalContext.SERIALIZATION_UID;

	KeyedParameter[] extendedValues;
	KeyedParameter[] processVariablesChanges;
	
	transient Map extendedValuesMap = new HashMap();
	transient Map processVariableChangesMap = new HashMap();
	
	public KeyedParameter[] getExtendedValues() {
		
		final ArrayList keyedParameters = new ArrayList();
		ForLoop forLoop = new ForLoop(){

			public void logic(Object target) {
				String key = (String)target;
				KeyedParameter kp = (KeyedParameter)extendedValuesMap.get(key);
				keyedParameters.add(kp);
			}
			
		};
		
		forLoop.run(extendedValuesMap.keySet());
		
		KeyedParameter[] keyedParametersArray = new KeyedParameter[keyedParameters.size()];
		keyedParameters.toArray(keyedParametersArray);
		
		return keyedParametersArray;
	}

	public KeyedParameter[] getProcessVariableChanges() {
		final ArrayList keyedParameters = new ArrayList();
		ForLoop forLoop = new ForLoop(){

			public void logic(Object target) {
				String key = (String)target;
				KeyedParameter kp = (KeyedParameter)processVariableChangesMap.get(key);
				keyedParameters.add(kp);
			}
			
		};
		
		forLoop.run(processVariableChangesMap.keySet());
		
		KeyedParameter[] keyedParametersArray = new KeyedParameter[keyedParameters.size()];
		keyedParameters.toArray(keyedParametersArray);
		
		return keyedParametersArray;	
	}

	public void setExtendedValues(KeyedParameter[] parameters) {
		for(int i=0; i<parameters.length; i++)
			setExtendedValue(parameters[i]);
	}

	public void setExtendedValue(KeyedParameter parameter) {
		extendedValuesMap.put(parameter.getKey(), parameter);
	}

	public void setProcessVariableChanges(KeyedParameter[] parameters) {
		for(int i=0; i<parameters.length; i++)
			setProcessVariableChange(parameters[i]);
	}
	
	public void setProcessVariableChange(KeyedParameter parameter) {
		processVariableChangesMap.put(parameter.getKey(), parameter);
	}

	public Serializable getProcessVariableChange(String k){
		if(processVariableChangesMap.containsKey(k))
			return (Serializable)((KeyedParameter)processVariableChangesMap.get(k)).getValue();
			
		return null;
	}

	public Serializable getExtendedValue(String k){		
		if(extendedValuesMap.containsKey(k))
			return (Serializable)((KeyedParameter)extendedValuesMap.get(k)).getValue();
			
		return null;
	}
}
