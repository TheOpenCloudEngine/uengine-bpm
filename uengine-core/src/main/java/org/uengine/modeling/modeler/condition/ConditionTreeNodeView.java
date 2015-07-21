package org.uengine.modeling.modeler.condition;

public class ConditionTreeNodeView extends ConditionTreeNode {

	ConditionNode conditionNode;
		public ConditionNode getConditionNode() {
			return conditionNode;
		}
		public void setConditionNode(ConditionNode conditionNode) {
			this.conditionNode = conditionNode;
		}
	public ConditionTreeNodeView(){
		conditionNode = new ConditionNode();
	}
}
