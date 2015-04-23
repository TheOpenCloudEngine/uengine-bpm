package org.uengine.kernel;


public class StartEventActivity extends DefaultActivity {
	@Override
	protected void executeActivity(ProcessInstance instance) throws Exception {
		// TODO flowActivity 의 getInitiatorHumanActivityReference 에서 휴먼을 찾아서 롤 셋팅을 해줘야함
		super.executeActivity(instance);
	}
}
