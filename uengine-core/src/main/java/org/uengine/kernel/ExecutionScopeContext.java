package org.uengine.kernel;

import java.io.Serializable;
import java.util.ArrayList;

public class ExecutionScopeContext implements Serializable{
	private static final long serialVersionUID = org.uengine.kernel.GlobalContext.SERIALIZATION_UID;
	
	String executionScope;
		public String getExecutionScope() {
			return executionScope;
		}
		public void setExecutionScope(String executionScope) {
			this.executionScope = executionScope;
		}

	transient Activity rootActivityInTheScope;

	String rootActivityTracingTag;
		public String getRootActivityTracingTag() {
			return rootActivityTracingTag;
		}
		public void setRootActivityTracingTag(String rootActivityTracingTag) {
			this.rootActivityTracingTag = rootActivityTracingTag;
		}

		public Activity getRootActivityInTheScope() {
				return rootActivityInTheScope;
			}
		public void setRootActivityInTheScope(Activity rootActivityInTheScope) {
			this.rootActivityInTheScope = rootActivityInTheScope;
			setRootActivityTracingTag(rootActivityInTheScope.getTracingTag());
		}

	String triggerActivityTracingTag;
		public String getTriggerActivityTracingTag() {
			return triggerActivityTracingTag;
		}
		public void setTriggerActivityTracingTag(String triggerActivityTracingTag) {
			this.triggerActivityTracingTag = triggerActivityTracingTag;
		}


	String name;
		public String getName() {
			return name;
		}
		public void setName(String name) {
		this.name = name;
	}


	ExecutionScopeContext executionScopeContext;
		public ExecutionScopeContext getExecutionScopeContext() {
			return executionScopeContext;
		}
		public void setExecutionScopeContext(ExecutionScopeContext executionScopeContext) {
			this.executionScopeContext = executionScopeContext;
		}

	ArrayList<ExecutionScopeContext> childs;
		public ArrayList<ExecutionScopeContext> getChilds() {
			return childs;
		}
		public void setChilds(ArrayList<ExecutionScopeContext> childs) {
			this.childs = childs;
		}

	ExecutionScopeContext parent;
		public ExecutionScopeContext getParent() {
			return parent;
		}
		public void setParent(ExecutionScopeContext parent) {
			this.parent = parent;
		}


}
