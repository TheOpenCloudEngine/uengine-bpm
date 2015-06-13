package org.uengine.kernel;

import java.io.Serializable;

public interface SpecializedVariableValue extends Serializable{
	public Serializable get(ProcessInstance instance, String scope) throws Exception;		
	public boolean set(ProcessInstance instance, String scope, Serializable value) throws Exception;		

}
