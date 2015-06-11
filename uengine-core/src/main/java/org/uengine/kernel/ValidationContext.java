package org.uengine.kernel;

import java.util.*;

/**
 * @author Jinyoung Jang
 */

public class ValidationContext extends java.util.Vector{
	private static final long serialVersionUID = org.uengine.kernel.GlobalContext.SERIALIZATION_UID;
	public static String OPTIONKEY_DISABLE_REPLICATION = "disable replication";
	HashMap<String,Object> codedMap = new HashMap<String,Object>();
	
	ArrayList<String> errorMessage;
		public ArrayList<String> getErrorMessage() {
			return errorMessage;
		}
		public void setErrorMessage(ArrayList<String> errorMessage) {
			this.errorMessage = errorMessage;
		}
	
	public ValidationContext(){
		errorMessage = new ArrayList<String>();
	}
	
	public void add(String cause, String resolution, int errorLevel, String code){
		UEngineException e = new UEngineException(cause, resolution);
		e.setErrorLevel(errorLevel);

		if(code!=null)
			codedMap.put(code, e);
		
		super.add(e);
	}
	
	public void add(String cause, String resolution){
		add(cause, resolution, UEngineException.ERROR, null);
	}

	public void add(String cause, String resolution, String code){
		add(cause, resolution, UEngineException.ERROR, code);
	}
	
	public void add(String cause){
		add(cause, null);
	}
	
	public void addWithCode(String cause, String code){
		add(cause, null, UEngineException.ERROR, code);
	}

	public void addWarning(String cause, String resolution){
		add(cause, resolution, UEngineException.WARNING, null);
	}

	public void addWarning(String cause, String resolution, String code){
		add(cause, resolution, UEngineException.WARNING, code);
	}
	
	public void addWarning(String cause){
		add(cause, null, UEngineException.WARNING, null);
	}

	public void addWarningWithCode(String cause, String code){
		add(cause, null, UEngineException.WARNING, code);
	}
	
	public boolean isWarning(){
		for(Enumeration enumeration = this.elements(); enumeration.hasMoreElements();){
			UEngineException item = (UEngineException)enumeration.nextElement();
			if(item.getErrorLevel() != UEngineException.WARNING)
				return false;
		}
		
		return true;
	}
	
	public boolean hasNoError(){
		return (size() == 0 || isWarning());
	}
	
	public void clearWithCode(String code){
		if(!codedMap.containsKey(code)) return;
		
		Object errorOrWarning = codedMap.get(code);
		remove(errorOrWarning);
		codedMap.remove(code);
	}
	
	public void setMessageToArray(){
		for(Enumeration enumeration = this.elements(); enumeration.hasMoreElements();){
			UEngineException item = (UEngineException)enumeration.nextElement();
			errorMessage.add(item.toString());
		}
	}
}