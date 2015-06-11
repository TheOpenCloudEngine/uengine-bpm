package org.uengine.kernel;

import java.util.*;


/**
 * @author Jinyoung Jang
 */

public class And extends Condition{

	private static final long serialVersionUID = org.uengine.kernel.GlobalContext.SERIALIZATION_UID;

	Vector conditionsVt;

	
	public And(){
		this( null);
	}

	public And( Condition first, Condition second){
		//this.first = first;
		//this.second = second;
		this( new Condition[] { first, second});
	}
	
	public And( Condition cond1, Condition cond2, Condition cond3){
		this( new Condition[] { cond1, cond2, cond3});
	}
	public And( Condition cond1, Condition cond2, Condition cond3, Condition cond4){
		this( new Condition[] { cond1, cond2, cond3, cond4});
	}
	
	public And( Condition[] conditions){
		conditionsVt = new Vector();
		
		if( conditions != null){
			for( int i=0; i< conditions.length; i++)
				conditionsVt.add( conditions[i]);
		}
	}

	public boolean isMet(ProcessInstance instance, String scope) throws Exception{
		
		//boolean matched = true;
		
		for( int i=0; i< conditionsVt.size(); i++){
			Condition con = (Condition)conditionsVt.get(i);
			
			if( !con.isMet(instance, scope))
				return false;
		}
			
		return true;
	}
	
	public void addCondition( Condition con){
		conditionsVt.add( con);
	}
	

	/*
	public void setFirstCondition( Condition first){
		this.first = first;
	}
	
	public void setSecondCondition( Condition second){
		this.second = second;
	}
	*/
	
	public Condition[] getConditions(){
		return (Condition[])conditionsVt.toArray( new Condition[ conditionsVt.size()]);
	}
	

/**
 * 
 * @uml.property name="conditionsVt"
 */
// For serialize
public void setConditionsVt(Vector vt) {
	conditionsVt = vt;
}

	/**
	 * 
	 * @uml.property name="conditionsVt"
	 */
	public Vector getConditionsVt() {
		return conditionsVt;
	}

	

	/* (non-Javadoc)
	 * @see org.uengine.kernel.Validatable#validate()
	 */
	public ValidationContext validate(Map options) {
		ValidationContext vc = super.validate(options);
		for(Enumeration enumeration = conditionsVt.elements(); enumeration.hasMoreElements(); ){
			Condition c = (Condition)enumeration.nextElement();
            //todo NullPointerException
            if(c == null) continue;
            ValidationContext cvc = c.validate(options);
			vc.addAll(cvc);
		}
		
		return vc;
	}

}