package org.uengine.modeling.modeler.condition;

import org.metaworks.annotation.Face;

@Face(ejsPath="dwr/metaworks/org/uengine/modeling/modeler/condition/ConditionTreeNodeView.ejs")
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
