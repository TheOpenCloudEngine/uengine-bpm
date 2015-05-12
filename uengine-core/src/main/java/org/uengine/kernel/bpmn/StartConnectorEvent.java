package org.uengine.kernel.bpmn;

public class StartConnectorEvent extends Event {
	

	String definitionId;
	    public String getDefinitionId() {
	      return definitionId;
	    }
	    public void setDefinitionId(String l) {
	      definitionId = l;
	    }
	String alias;
		public String getAlias() {
			return alias;
		}
		public void setAlias(String alias) {
			this.alias = alias;
		}

}
