package org.uengine.kernel;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.Date;
import java.util.Map;

import org.metaworks.inputter.InputterAdapter;
import org.metaworks.inputter.TextInput;
import org.uengine.kernel.CommandVariableValue;
import org.uengine.kernel.GlobalContext;

public class InstancePropertyPointingProcessVariable extends ProcessVariable{
	private static final long serialVersionUID = GlobalContext.SERIALIZATION_UID;
	
	protected String propertyName;
	
	protected Method getGetter(){
		try {
			return ProcessInstance.class.getMethod("get" + propertyName, new Class[]{});
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
		
		return null;
	}
	
	protected Method getSetter(){
		try {
			return ProcessInstance.class.getMethod("set" + propertyName, new Class[]{getGetter().getReturnType()});
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
		
		return null;
	}
	
	public InstancePropertyPointingProcessVariable(String propertyName){
		this.propertyName = propertyName;

	}
	
	public String getName(){
		return "[Instance."+ propertyName +"]";
	}

	public Class getType(){
		return getGetter().getReturnType();
	}
	
	public Serializable get(ProcessInstance instance, String scope) throws Exception {
		return (Serializable)getGetter().invoke(instance, new Object[]{});
	}

	public ProcessVariableValue getMultiple(ProcessInstance instance, String scope) throws Exception {
		ProcessVariableValue pvv = new ProcessVariableValue();
		pvv.setValue(get(instance, scope));
		
		return pvv;
	}

	public void set(ProcessInstance instance, String scope, Serializable value) throws Exception {
		getSetter().invoke(instance, new Object[]{value});
	}

	public boolean shouldAccessValueInSpecializedWay() {
		return true;
	}


}
