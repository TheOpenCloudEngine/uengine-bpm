package org.uengine.kernel;

public class EventMessagePayload {
	String eventName;
	String triggerTracingTag;
	
	public String getEventName() {
		return eventName;
	}
	public void setEventName(String eventName) {
		this.eventName = eventName;
	}
	public String getTriggerTracingTag() {
		return triggerTracingTag;
	}
	public void setTriggerTracingTag(String triggerTracingTag) {
		this.triggerTracingTag = triggerTracingTag;
	}
}
