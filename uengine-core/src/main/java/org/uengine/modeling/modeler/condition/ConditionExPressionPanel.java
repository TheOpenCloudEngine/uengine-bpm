package org.uengine.modeling.modeler.condition;

public class ConditionExPressionPanel {

	ConditionTreeNodeView conditionTreeNode;
		public ConditionTreeNodeView getConditionTreeNode() {
			return conditionTreeNode;
		}
		public void setConditionTreeNode(ConditionTreeNodeView conditionTreeNode) {
			this.conditionTreeNode = conditionTreeNode;
		}
		
	public ConditionExPressionPanel(){
		conditionTreeNode = new ConditionTreeNodeView();
	}
}
