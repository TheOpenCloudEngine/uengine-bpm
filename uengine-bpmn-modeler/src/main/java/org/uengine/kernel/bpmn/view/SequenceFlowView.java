package org.uengine.kernel.bpmn.view;

import org.metaworks.EventContext;
import org.metaworks.ServiceMethodContext;
import org.metaworks.annotation.ServiceMethod;
import org.uengine.modeling.IRelation;
import org.uengine.modeling.RelationPropertiesView;
import org.uengine.modeling.RelationView;
import org.uengine.modeling.modeler.condition.ConditionPanel;

public class SequenceFlowView extends RelationView {
	public final static String SHAPE_ID = "OG.shape.bpmn.C_Sequence";

    ConditionPanel conditionPanel;
        public ConditionPanel getConditionPanel() {
            return conditionPanel;
        }

        public void setConditionPanel(ConditionPanel conditionPanel) {
            this.conditionPanel = conditionPanel;
        }

    public SequenceFlowView(){
		super();
	}
	
	public SequenceFlowView(IRelation relation){
		super(relation);
	}
//
//	@ServiceMethod(callByContent=true, eventBinding=EventContext.EVENT_DBLCLICK)
//	public Object properties() throws Exception {
//		SequenceFlow sequenceFlow;
//		if(this.getRelation() == null)
//			sequenceFlow = new SequenceFlow();
//		else
//			sequenceFlow = (SequenceFlow)this.getRelation();
//
//		//transition.getMetaworksContext().setWhen(MetaworksContext.WHEN_EDIT);
//
//		return new RelationPropertiesView(sequenceFlow);
//	}
//

	@ServiceMethod(callByContent = true, eventBinding = EventContext.EVENT_DBLCLICK, target = ServiceMethodContext.TARGET_POPUP)
	public Object showProperty() throws Exception {


        return super.showProperty();
	}
	
}

