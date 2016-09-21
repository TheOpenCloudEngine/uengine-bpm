package org.uengine.kernel.bpmn.view;

import org.metaworks.EventContext;
import org.metaworks.MetaworksContext;
import org.metaworks.ServiceMethodContext;
import org.metaworks.annotation.ServiceMethod;
import org.uengine.kernel.ExpressionEvaluateCondition;
import org.uengine.kernel.bpmn.SequenceFlow;
import org.uengine.modeling.IRelation;
import org.uengine.modeling.RelationPropertiesView;
import org.uengine.modeling.RelationView;

public class SequenceFlowView extends RelationView {
	public final static String SHAPE_ID = "OG.shape.bpmn.C_Sequence";

    public SequenceFlowView(){
		super();
	}
	
	public SequenceFlowView(IRelation relation){
		super(relation);
	}

	@ServiceMethod(callByContent = true, eventBinding = EventContext.EVENT_DBLCLICK, target = ServiceMethodContext.TARGET_POPUP)
	public RelationPropertiesView showProperty() throws Exception {
        RelationPropertiesView relationPropertiesView = super.showProperty();
        relationPropertiesView.setWidth(800);
        relationPropertiesView.setHeight(1000);

		SequenceFlow sequenceFlow = (SequenceFlow) getRelation();

		if(sequenceFlow.getCondition()==null)
			sequenceFlow.setCondition(new ExpressionEvaluateCondition());

		sequenceFlow.getCondition().setMetaworksContext(new MetaworksContext());
		sequenceFlow.getCondition().getMetaworksContext().setWhen(MetaworksContext.WHEN_EDIT);

        return relationPropertiesView;

	}


	
}

