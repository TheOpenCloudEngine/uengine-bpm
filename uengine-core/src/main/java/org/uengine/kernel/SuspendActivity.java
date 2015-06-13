package org.uengine.kernel;

import org.uengine.kernel.GlobalContext;


/**
 * @author Jinyoung Jang
 */

public class SuspendActivity extends DefaultActivity{
	private static final long serialVersionUID = org.uengine.kernel.GlobalContext.SERIALIZATION_UID;

	public SuspendActivity(){
		super("suspend");
	}
	
	protected void executeActivity(ProcessInstance instance) throws Exception{
		getProcessDefinition().suspend(instance);
		//fireComplete(instance);
	}

	public void resume(ProcessInstance instance) throws Exception {
		//super.resume(instance);
		fireComplete(instance);
	}

}

