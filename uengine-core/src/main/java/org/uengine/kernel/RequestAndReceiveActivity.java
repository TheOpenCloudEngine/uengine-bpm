package org.uengine.kernel;

import java.util.ArrayList;


/**
 * @author Jinyoung Jang
 */

public class RequestAndReceiveActivity extends ComplexActivity{
	private static final long serialVersionUID = org.uengine.kernel.GlobalContext.SERIALIZATION_UID;

	public RequestAndReceiveActivity(){
		super();
		setName("req.~rcv.");
		ArrayList<Activity> chidActivity = new ArrayList<Activity>();
		chidActivity.add(new WebServiceActivity());
		chidActivity.add(new ReceiveActivity());
		setChildActivities(chidActivity);
	}
	
/*	protected void queueActivity(final Activity act, final ActivityInstance instance) throws Exception{
		act.executeActivity(instance);		
	}	

	ProcessVariable output;
		public ProcessVariable getOutput() {
			return output;
		}
		public void setOutput(ProcessVariable variable) {
			output = variable;
		}
*/
}

