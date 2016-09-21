package org.uengine.kernel;

import org.metaworks.ContextAware;
import org.metaworks.annotation.Children;
import org.metaworks.annotation.Order;
import org.metaworks.annotation.ServiceMethod;

import java.util.*;


/**
 * @author Jinyoung Jang
 */

@org.metaworks.annotation.Face(ejsPath="dwr/metaworks/genericfaces/TreeFace.ejs")
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

	
	public Condition[] getConditions(){
		refresh();

		if(conditionsVt==null)
			return null;

		return (Condition[])conditionsVt.toArray( new Condition[ conditionsVt.size()]);
	}


	@Children
	public Vector getConditionsVt() {
		return conditionsVt;
	}
	public void setConditionsVt(Vector vt) {
	conditionsVt = vt;
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


	@ServiceMethod(inContextMenu=true, callByContent=true)
	@Order(6)
	public void newAnd(){
		if(getConditionsVt()==null)
			setConditionsVt(new Vector());

		getConditionsVt().add(new And());
	}

	@ServiceMethod(inContextMenu=true, callByContent=true)
	@Order(5)
	public void newOr(){
		if(getConditionsVt()==null)
			setConditionsVt(new Vector());

		getConditionsVt().add(new Or());
	}

	@ServiceMethod(inContextMenu=true, callByContent=true)
	@Order(1)
	public void newEvaluate(){
		if(getConditionsVt()==null)
			setConditionsVt(new Vector());

		getConditionsVt().add(new Evaluate());
	}

	@ServiceMethod(inContextMenu=true, callByContent=true)
	@Order(2)
	public void newScriptEvaluation(){
		if(getConditionsVt()==null)
			setConditionsVt(new Vector());

		getConditionsVt().add(new ExpressionEvaluateCondition());
	}

	@ServiceMethod(inContextMenu=true, callByContent=true)
	@Order(10)
	public void refresh(){
		if(getConditionsVt()!=null) {

			ArrayList copy = new ArrayList(getConditionsVt());

			for(Object condition : copy) {

				if (condition instanceof Object[]) {

					for (Object condition1 : (Object[]) condition) {
						ContextAware contextAware = (ContextAware) condition1;
						if (contextAware.getMetaworksContext() != null && "removed".equals(contextAware.getMetaworksContext().getWhere())) {
							getConditionsVt().remove(condition);
						}

						int where = getConditionsVt().indexOf(condition);
						getConditionsVt().set(where, condition1);

					}

				} else if (condition instanceof ContextAware) {
					ContextAware contextAware = (ContextAware) condition;
					if (contextAware.getMetaworksContext() != null && "removed".equals(contextAware.getMetaworksContext().getWhere())) {
						getConditionsVt().remove(condition);
					}
				}

			}
		}
	}


	@Override
	public String toString() {
		return "And";
	}
}