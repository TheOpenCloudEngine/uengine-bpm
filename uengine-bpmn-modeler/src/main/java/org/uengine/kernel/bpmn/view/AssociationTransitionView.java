package org.uengine.kernel.bpmn.view;

import org.metaworks.EventContext;
import org.metaworks.annotation.ServiceMethod;
import org.uengine.kernel.bpmn.Association;
import org.uengine.kernel.bpmn.SequenceFlow;
import org.uengine.modeling.IRelation;
import org.uengine.modeling.RelationPropertiesView;
import org.uengine.modeling.Symbol;
import org.uengine.modeling.modeler.symbol.ConnectorSymbol;

public class AssociationTransitionView extends SequenceFlowView {
	
	public final static String SHAPE_ID = "OG.shape.bpmn.C_Association";
	
	public AssociationTransitionView(){
		
	}
	
	public AssociationTransitionView(IRelation relation){
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
//
//	public static Symbol createSymbol() {
//		Symbol symbol = new Symbol();
//		symbol.setName("연관");
//
//		return fillSymbol(symbol);
//	}
//
//	public static Symbol createSymbol(Class<? extends Symbol> symbolType) {
//		Symbol symbol = new ConnectorSymbol();
//		try {
//			symbol = (Symbol)Thread.currentThread().getContextClassLoader().loadClass(symbolType.getName()).newInstance();
//		} catch (ClassNotFoundException e) {
//			e.printStackTrace();
//		} catch (InstantiationException e) {
//			e.printStackTrace();
//		} catch (IllegalAccessException e) {
//			e.printStackTrace();
//		}
//		symbol.setName("커낵터");
//		return fillSymbol(symbol);
//	}
//
//	private static Symbol fillSymbol (Symbol symbol){
//
//		symbol.setShapeId(SHAPE_ID);
//		symbol.setHeight(150);
//		symbol.setWidth(200);
//		symbol.setElementClassName(Association.class.getName());
//		symbol.setShapeType("EDGE");
//
//		return symbol;
//	}
}
