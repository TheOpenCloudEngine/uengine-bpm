package org.uengine.kernel.bpmn.view;

import org.metaworks.EventContext;
import org.metaworks.annotation.ServiceMethod;
import org.uengine.kernel.MessageSequenceFlow;
import org.uengine.kernel.bpmn.SequenceFlow;
import org.uengine.modeling.IRelation;
import org.uengine.modeling.RelationPropertiesView;
import org.uengine.modeling.Symbol;
import org.uengine.modeling.modeler.symbol.ConnectorSymbol;

public class MessageFlowView extends SequenceFlowView {
	
	public final static String SHAPE_ID = "OG.shape.bpmn.C_Message";
	
	public MessageFlowView(){
		
	}
	
	public MessageFlowView(IRelation relation){
		super(relation);
	}
	
//	@ServiceMethod(callByContent=true, eventBinding=EventContext.EVENT_DBLCLICK)
//	public Object properties() throws Exception {
//		SequenceFlow sequenceFlow;
//		if(this.getRelation() == null)
//			sequenceFlow = new SequenceFlow();
//		else
//			sequenceFlow = (SequenceFlow)this.getRelation();
//
//		return new RelationPropertiesView(sequenceFlow);
//	}

}
