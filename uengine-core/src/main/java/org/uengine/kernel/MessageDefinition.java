package org.uengine.kernel;

import java.io.Serializable;

/**
 * @author Jinyoung Jang
 */
public class MessageDefinition implements Cloneable, Serializable{
	private static final long serialVersionUID = org.uengine.kernel.GlobalContext.SERIALIZATION_UID;
	String name;
		public String getName() {
			return name;
		}
		public void setName(String string) {
			name = string;
		}

	ParameterContext[] parameters;
		public ParameterContext[] getParameters() {
			return parameters;
		}
		public void setParameters(ParameterContext[] context) {
			parameters = context;
		}

		
	public String toString(){
		return ""+getName();
	}
	
	public boolean equals(Object dest){
		try{
			return getName().equals(((MessageDefinition)dest).getName());
		}catch(Exception e){
		}
		
		return false;
	}
	
	protected Object clone() throws CloneNotSupportedException {
		MessageDefinition copy = new MessageDefinition();
		copy.setName(getName());
		ParameterContext[] paramCopy = new ParameterContext[parameters.length]; 
		for(int i=0; i<parameters.length; i++){
			paramCopy[i] = new ParameterContext();
			paramCopy[i].setArgument(parameters[i].getArgument());		
			paramCopy[i].setVariable(parameters[i].getVariable());		
			paramCopy[i].setType(parameters[i].getType());
		}
		copy.setParameters(paramCopy);

		return copy;
	}
	
	public MessageDefinition copy(){
		try{
			return (MessageDefinition)clone();
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}
	}

}
