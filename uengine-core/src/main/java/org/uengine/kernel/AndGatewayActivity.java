package org.uengine.kernel;

import java.util.Iterator;

import org.uengine.kernel.graph.Transition;
import org.uengine.modeling.ElementView;

public class AndGatewayActivity extends GatewayActivity {

	public AndGatewayActivity() {
		super("And");
	}
	
	@Override
	protected boolean isCompletedAllOfPreviousActivities(ProcessInstance instance) throws Exception {
		
		// this is before execute
//		System.out.println("---- parent activity's tracing tag: " + getParentActivity().getTracingTag());
		// why this previousActivities() is always 1?
//		System.out.println("---- previous activities number: " + getPreviousActivities().size());
		
		
		
		Transition transition = null;
		for (Iterator<Transition> it = getIncomingTransitions().iterator(); it.hasNext(); ) {
			transition = it.next();
			String status = (transition.getSourceActivity()).getStatus(instance);
			if (!status.equals(Activity.STATUS_COMPLETED)) {
				return false;
			}
		}
		
		return true;
	}

}
