package org.uengine.kernel;


/**
 * @author Jinyoung Jang
 */

public class BreakActivity extends DefaultActivity{
	private static final long serialVersionUID = org.uengine.kernel.GlobalContext.SERIALIZATION_UID;
	Class what;
	
	public BreakActivity(Class what){
		super("Break the box");
		
		this.what = what;
	}
	public BreakActivity(){
		this(ComplexActivity.class);
	}
	
	protected void executeActivity(ProcessInstance instance) throws Exception{
		Activity tracing = this;
		
		do{
			if(tracing.getClass().isAssignableFrom(what)){
				tracing.fireComplete(instance);
				
				break;
			}
			
			tracing = tracing.getParentActivity();
		}while(tracing != null);
		
		fireComplete(instance);
	}
}

