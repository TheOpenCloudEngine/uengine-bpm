package org.uengine.kernel;


/**
 * @author Jinyoung Jang
 */

public class Otherwise extends Condition{
	private static final long serialVersionUID = org.uengine.kernel.GlobalContext.SERIALIZATION_UID;

	public Otherwise(){
	}
	
	public Otherwise(String description){
		getDescription().setText(description);
	}
	
	public boolean isMet(ProcessInstance instance, String scope){
		return true;		
	}
	
	public String toString(){
		if(getDescription().getText()!=null)
			return getDescription().getText();
			
		return "otherwise";
	}
}