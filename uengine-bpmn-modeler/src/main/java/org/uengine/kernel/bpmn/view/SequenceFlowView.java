package org.uengine.kernel.bpmn.view;

import org.metaworks.EventContext;
import org.metaworks.annotation.ServiceMethod;
import org.uengine.kernel.bpmn.SequenceFlow;
import org.uengine.modeling.IRelation;
import org.uengine.modeling.RelationPropertiesView;
import org.uengine.modeling.RelationView;
import org.uengine.modeling.Symbol;

//import org.uengine.essencia.modeling.ConnectorSymbol;


public class SequenceFlowView extends RelationView {
	public final static String SHAPE_ID = "OG.shape.bpmn.C_Sequence";
	
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

	
}

