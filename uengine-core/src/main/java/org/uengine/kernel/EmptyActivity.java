package org.uengine.kernel;




/**
 * @author Jinyoung Jang
 */

public class EmptyActivity extends DefaultActivity{
	private static final long serialVersionUID = org.uengine.kernel.GlobalContext.SERIALIZATION_UID;

	public EmptyActivity(){
		super("Skip");
	}
	
	protected void executeActivity(ProcessInstance instance) throws Exception{
		fireComplete(instance);
	}
}

