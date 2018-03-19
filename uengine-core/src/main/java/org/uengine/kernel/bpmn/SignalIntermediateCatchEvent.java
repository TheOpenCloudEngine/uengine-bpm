package org.uengine.kernel.bpmn;

import org.uengine.kernel.*;
import org.uengine.processdesigner.mapper.TransformerMapping;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SignalIntermediateCatchEvent extends CatchingMessageEvent implements IntermediateEvent{

	public static final String SIGNAL_EVENTS = "signalEvents";

	public SignalIntermediateCatchEvent(){
		super();
	}

	String signalType;
		public String getSignalType() {
			return signalType;
		}
		public void setSignalType(String signalType) {
			this.signalType = signalType;
		}


	@Override
	protected void executeActivity(ProcessInstance instance) throws Exception {
		super.executeActivity(instance);

		//TODO: need to introduce global activity reference space (tag@tag@tag)

		Map<String, SignalEventInstance> signals = getSignalEvents(instance);

		if(!signals.containsKey(getName())) {

			SignalEventInstance signalEventInstance = new SignalEventInstance();
			signalEventInstance.setActivityRef(getTracingTag());
			signalEventInstance.setSignalName(getName());

			signals.put(getName(), signalEventInstance);

			setSignalEvents(instance, signals);
		}

	}

	public static Map<String, SignalEventInstance> getSignalEvents(ProcessInstance instance) throws Exception {
		Map<String, SignalEventInstance> signals = (Map<String, SignalEventInstance>) instance.getProperty("", SIGNAL_EVENTS);

		if(signals==null) signals = new HashMap<>();

		return signals;
	}

	public static void setSignalEvents(ProcessInstance instance, Map<String, SignalEventInstance> signals) throws Exception {
		instance/*.getRootProcessInstance()*/.setProperty("", SIGNAL_EVENTS, (Serializable) signals);
	}

	@Override
	protected void afterComplete(ProcessInstance instance) throws Exception {

		// remove signal
		Map<String, SignalEventInstance> signals = getSignalEvents(instance);
		signals.remove(getName());
		setSignalEvents(instance, signals);


		super.afterComplete(instance);
	}
}
