package org.uengine.kernel.bpmn;

import org.uengine.kernel.*;
import org.uengine.kernel.bpmn.Event;

/**
 * Not used. Use CatchingErrorEvent instead.
 */
public class ReceiveErrorEvent extends Event implements MessageListener {
	public ReceiveErrorEvent(){
		super();
		if( this.getName() == null ){
			setName(this.getClass().getSimpleName());
		}
	}
	@Override
	protected void executeActivity(ProcessInstance instance) throws Exception {
		//start listens...
		Activity targetActivity = null;

		if(getAttachedToRef()==null)
			targetActivity = getParentActivity();
		else
			try {
				targetActivity = getProcessDefinition().getActivity(getAttachedToRef());
			}catch(Exception e) {
				throw new Exception("There's no activity where tracingTag [" + getAttachedToRef() + "] which is attached target of event " + getName(), e);
			}

		if(targetActivity instanceof ScopeActivity){
			instance.addActivityEventInterceptor(new ActivityEventInterceptor() {
				@Override
				public boolean interceptEvent(Activity activity, String command, ProcessInstance instance, Object payload) throws Exception {
					if(ACTIVITY_FAULT.equals(command)){
						setFaultTorelant(instance);

						(new ActivityCompleter()).queue(instance.getInstanceId(), getTracingTag());
						return true;
					}

					return false;
				}
			});
		}

		//super.executeActivity(instance);
	}

	private void setFaultTorelant(ProcessInstance instance) {
		instance.getProcessTransactionContext().setSharedContext("faultTolerant", true);
	}

	public boolean onMessage(ProcessInstance instance, Object payload) throws Exception {
		fireComplete(instance);
		return true;
	}

	public String getMessage() {
		return this.getName();
	}
}
