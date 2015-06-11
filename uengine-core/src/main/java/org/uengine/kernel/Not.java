package org.uengine.kernel;

import java.util.*;

/**
 * @author Jinyoung Jang
 */

public class Not extends Condition{
	private static final long serialVersionUID = org.uengine.kernel.GlobalContext.SERIALIZATION_UID;

	Condition condition;
		public Condition getCondition() {
			return condition;
		}
		public void setCondition(Condition condition) {
			this.condition = condition;
		}

	public Not(Condition condition){
		this.condition = condition;
	}
	
	public boolean isMet(ProcessInstance instance, String scope) throws Exception{
		return !condition.isMet(instance, scope);		
	}
	
	public String toString(){
		String exp = condition.toString();
		return "not (" + exp + ")";
	}

	public ValidationContext validate(Map options){
		return condition.validate(options);		
	}


}