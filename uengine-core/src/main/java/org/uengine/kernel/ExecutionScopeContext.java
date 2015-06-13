package org.uengine.kernel;

import java.io.Serializable;

public class ExecutionScopeContext implements Serializable{
	private static final long serialVersionUID = GlobalContext.SERIALIZATION_UID;
	
	String executionScope;
	transient Activity rootActivityInTheScope;
	String rootActivityTracingTag;
	String triggerActivityTracingTag;
	String name;
	
	public String getExecutionScope() {
		return executionScope;
	}
	public void setExecutionScope(String executionScope) {
		this.executionScope = executionScope;
	}
	public Activity getRootActivityInTheScope() {
		return rootActivityInTheScope;
	}
	public void setRootActivityInTheScope(Activity rootActivityInTheScope) {
		this.rootActivityInTheScope = rootActivityInTheScope;
		setRootActivityTracingTag(rootActivityInTheScope.getTracingTag());
	}
	public String getRootActivityTracingTag() {
		return rootActivityTracingTag;
	}
	public void setRootActivityTracingTag(String rootActivityTracingTag) {
		this.rootActivityTracingTag = rootActivityTracingTag;
	}
	public String getTriggerActivityTracingTag() {
		return triggerActivityTracingTag;
	}
	public void setTriggerActivityTracingTag(String triggerActivityTracingTag) {
		this.triggerActivityTracingTag = triggerActivityTracingTag;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
}
