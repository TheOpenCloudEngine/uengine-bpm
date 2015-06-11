package org.uengine.kernel;

import java.io.Serializable;
import java.util.*;

/**
 * @author Jinyoung Jang
 */

public class ProcessVariableValue implements java.io.Serializable, Cloneable{
	private static final long serialVersionUID = org.uengine.kernel.GlobalContext.SERIALIZATION_UID;
	
    int cursor;
    boolean isSingle = true;
    
    String name;
    
    //for multiple role binding
    private ArrayList values = new ArrayList();

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Serializable getValue() {
		if(values.size() == 0) return null;
		
		return (Serializable)values.get(getCursor()); 
	}

	public void setValue(Serializable value) {
		if(getCursor() >= values.size())
			values.add(value);
		else
			values.set(getCursor(), value);
	}

	/**
	 * @deprecated
	 * @param value
	 */
	public void setValue(Object value) {
		setValue((Serializable)value);
	}

    public synchronized boolean next(){
    	if(values==null)
    		return false;
    	else if(cursor >= (values.size()-1))
    		return false;	
    	    	
    	cursor++;
    	
    	return true;
    }
    
    public synchronized void moveToAdd(){
		setCursor(values.size());
	}
    
    public synchronized int size(){
    	return values.size();
    }
    
	public synchronized int getCursor(){
		return cursor;
	}
	
	public synchronized void setCursor(int c){
		if(c>=0 && c<=size())
			cursor = c;
	}

	public synchronized void remove() {
		if(values==null) return;
		
		values.remove(getCursor());
		
		if(getCursor() >= size())
			setCursor(size() - 1);
	}
	
	public void beforeFirst(){
		setCursor(0);
	}


	public static void main(String [] args) throws Exception{
		ProcessVariableValue pvv = new ProcessVariableValue();
		
		
		pvv.setName("testval");
		pvv.setValue("value1");
		
		System.out.println(pvv.getValue());
		
		pvv.moveToAdd();
		pvv.setValue("value2");
		pvv.moveToAdd();
		pvv.setValue("value3");
		pvv.moveToAdd();
		pvv.setValue("value4");
		
		GlobalContext.serialize(pvv, System.out, ProcessVariableValue.class);
		
		pvv.beforeFirst();
		
		ProcessInstance instance = new DefaultProcessInstance();
		ProcessDefinition definition = new ProcessDefinition();
		instance.setProcessDefinition(definition);
		instance.set("", pvv);
		
		pvv = instance.getMultiple("", "testval");
		
		do{
			System.out.println(" value = " + pvv.getValue());			
		}while(pvv.next());		
		
		pvv.beforeFirst();
		pvv.remove();
		do{
			System.out.println(" value = " + pvv.getValue());			
		}while(pvv.next());		
		
	}
	
	public String toString() {
		if (size() > 1) {
			beforeFirst();

			StringBuilder names = new StringBuilder();
			do {
				if (names.length() > 0) names.append(", ");
				names.append(String.valueOf(getValue()));
			} while (next());

			beforeFirst();

			return names.toString();
		} else {
			return String.valueOf(getValue());
		}
	}


}
