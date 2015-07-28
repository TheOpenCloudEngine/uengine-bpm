package org.uengine.modeling;

import org.metaworks.Refresh;
import org.metaworks.annotation.ServiceMethod;
import org.metaworks.widget.ModalWindow;
import org.uengine.kernel.Condition;
import org.uengine.kernel.bpmn.SequenceFlow;
import org.uengine.kernel.bpmn.view.SequenceFlowView;
import org.uengine.modeling.modeler.condition.ConditionTreeNode;

public class RelationPropertiesView extends ModalWindow {

    RelationView relationView;

    public RelationView getRelationView() {
        return relationView;
    }

    public void setRelationView(RelationView relationView) {
        this.relationView = relationView;
    }

    public RelationPropertiesView() {
    }

    public RelationPropertiesView(RelationView relationView) {
        this.setRelationView(relationView);
        setPanel(this.getRelationView().getRelation());
    }

    @ServiceMethod(callByContent = true)
    public Refresh apply() {
        Condition condition = null;
        SequenceFlowView sequenceFlowView = ((SequenceFlowView) this.getRelationView());
        SequenceFlow sequenceFlow = (SequenceFlow) sequenceFlowView.getRelation();
        ConditionTreeNode rootNode = sequenceFlow.getConditionPanel().getConditionTree().getNode();

        try {
            condition = sequenceFlowView.makeCondition(rootNode);
        } catch (Exception e) {
            e.printStackTrace();
        }

        sequenceFlow.setCondition(condition);

        return new Refresh(sequenceFlowView);
    }
}
