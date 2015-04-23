package org.uengine.kernel;

import org.metaworks.annotation.Face;
import org.uengine.modeling.ElementView;


public class EventActivity extends DefaultActivity{
	
	public static final String STOP_ACTIVITY = "STOP_ACTIVITY";
	public static final String PASS_ACTIVITY = "PASS_ACTIVITY";
	
	String activityStop;	
	@Face(displayName="호출한 상위 엑티비티 종료", ejsPath="dwr/metaworks/genericfaces/RadioButton.ejs", 
			options={"stop Activity where called this event", "no"}, 
			values={STOP_ACTIVITY, PASS_ACTIVITY})
		public String getActivityStop() {
			return activityStop;
		}
		public void setActivityStop(String activityStop) {
			this.activityStop = activityStop;
		}
		
	@Override
	protected void executeActivity(ProcessInstance instance) throws Exception {
		// TODO Auto-generated method stub
	}
	
}
