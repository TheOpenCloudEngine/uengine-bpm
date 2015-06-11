package org.uengine.kernel.bpmn;

import org.metaworks.annotation.Face;
import org.metaworks.annotation.Range;
import org.uengine.kernel.DefaultActivity;
import org.uengine.kernel.MessageListener;
import org.uengine.kernel.ProcessInstance;
import org.uengine.modeling.ElementView;


public class Event extends DefaultActivity implements MessageListener{
	
	public static final String STOP_ACTIVITY = "STOP_ACTIVITY";
	public static final String PASS_ACTIVITY = "PASS_ACTIVITY";
	
//	String activityStop;
//	@Face(displayName="호출한 상위 엑티비티 종료", ejsPath="dwr/metaworks/genericfaces/RadioButton.ejs",
//			options={"stop Activity where called this event", "no"},
//			values={STOP_ACTIVITY, PASS_ACTIVITY})
//		public String getActivityStop() {
//			return activityStop;
//		}
//		public void setActivityStop(String activityStop) {
//			this.activityStop = activityStop;
//		}
//

	String attachedToRef;
		public String getAttachedToRef() {
			return attachedToRef;
		}
		public void setAttachedToRef(String attachedToRef) {
			this.attachedToRef = attachedToRef;
		}

	@Override
	protected void executeActivity(ProcessInstance instance) throws Exception {
		// TODO Auto-generated method stub
	}


	///// slightly different from the OMG's BPMN 2.0 xml schema model. but more simple.

	String eventType;
	@Range(options={"Catching", "Throwing", "Non-interrupting"})
		public String getEventType() {
			return eventType;
		}
		public void setEventType(String eventType) {
			this.eventType = eventType;
		}

	boolean intermediate;
		public boolean isIntermediate() {
			return intermediate;
		}
		public void setIntermediate(boolean intermediate) {
			this.intermediate = intermediate;
		}


	@Override
	public boolean onMessage(ProcessInstance instance, Object payload) throws Exception {
		if(payload instanceof String){
			String message = (String)payload;

			if(getName().equals(message)){

				fireComplete(instance);  //let event starts

				return true;

			}


		}

		return false;
	}

	@Override
	public String getMessage() {
		return "event";
	}
}
