package org.uengine.kernel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.uengine.kernel.Activity;
import org.uengine.kernel.GatewayActivity;
import org.uengine.kernel.ProcessInstance;
import org.uengine.kernel.graph.Transition;

public class LoopGatewayActivity extends GatewayActivity {

	public LoopGatewayActivity() {
		super("Loop");
		// TODO Auto-generated constructor stub
	}
	
	// store the mapping information about outgoing transition
	HashMap<String, Transition> map = new HashMap<String, Transition>();

	public HashMap<String, Transition> getMap() {
		return map;
	}

	public void setMap(HashMap<String, Transition> map) {
		this.map = map;
	}

	public void addCondition(String key, Transition transition) {
		map.put(key, transition);
	}

	@Override
	protected void executeActivity(ProcessInstance instance) throws Exception {
		if (isCompletedAllOfPreviousActivities(instance)) {
			fireComplete(instance);
			// 
		}

	}

	@Override
	public List<Activity> getPossibleNextActivities(ProcessInstance instance,
			String scope) throws Exception {

		List<Activity> activities = new ArrayList<Activity>();

		Transition transition = null;
		for (Iterator<Transition> it = getOutgoingTransitions().iterator(); it
				.hasNext();) {
			transition = it.next();
			// if (transition.isMet(instance, scope)) {
			if (transition.isMatch()) {
				activities.add(transition.getTargetActivity());
			}
		}
		return activities;
	}

	@Override
	protected boolean isCompletedAllOfPreviousActivities(
			ProcessInstance instance) throws Exception {
		return true;
	}

	public static void main(String[] args) {

//		GlobalContext.useEJB = false;

		/*
		FlowActivity flowActivity = new FlowActivity();
		flowActivity.setTracingTag("flowActivity");

		EmptyActivity a = new EmptyActivity();
		a.setTracingTag("A");
		EmptyActivity b = new EmptyActivity();
		b.setTracingTag("B");
		EmptyActivity c = new EmptyActivity();
		c.setTracingTag("C");

		LoopGatewayActivity m = new LoopGatewayActivity();
		m.setTracingTag("M(Loop)");

		Transition transitionAB = new Transition(a.getTracingTag(),
				b.getTracingTag());
		Transition transitionBM = new Transition(b.getTracingTag(),
				m.getTracingTag());
		Transition transitionMC = new Transition(m.getTracingTag(),
				c.getTracingTag());
		Transition transitionMB = new Transition(m.getTracingTag(),
				b.getTracingTag());

		m.addCondition("outgoing", transitionMC);

		ProcessDefinition definition = new ProcessDefinition();
		definition.setName("Test");

		definition.addChildActivity(flowActivity);
		flowActivity.addChildActivity(a);
		flowActivity.addChildActivity(b);
		flowActivity.addChildActivity(m);
		flowActivity.addChildActivity(c);

		flowActivity.addTransition(transitionAB);
		flowActivity.addTransition(transitionBM);
		flowActivity.addTransition(transitionMC);
		flowActivity.addTransition(transitionMB);
		flowActivity.afterDeserialization();
*/

	}

}
