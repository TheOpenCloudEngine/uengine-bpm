package org.uengine.kernel;

import org.uengine.queue.workqueue.WorkProcessorBean;

/**
 * @author Jinyoung Jang
 */

public class AtomicActivity extends ComplexActivity {
	private static final long serialVersionUID = org.uengine.kernel.GlobalContext.SERIALIZATION_UID;
	public AtomicActivity(){
		super();
		setName("Atomic");
	}

	protected void queueActivity(final Activity act, final ProcessInstance instance) throws Exception{
		//don't use JMS
		instance.execute(act.getTracingTag());
	}
}
