package org.uengine.modeling.modeler.condition;

public class ConditionEditorExtension {

	ConditionTreeNodeView conditionTreeNode;
		public ConditionTreeNodeView getConditionTreeNode() {
			return conditionTreeNode;
		}
		public void setConditionTreeNode(ConditionTreeNodeView conditionTreeNode) {
			this.conditionTreeNode = conditionTreeNode;
		}
		
	public ConditionEditorExtension(){
		conditionTreeNode = new ConditionTreeNodeView();
	}
}
