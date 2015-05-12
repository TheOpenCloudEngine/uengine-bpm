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
	
	@ServiceMethod(callByContent=true, eventBinding=EventContext.EVENT_DBLCLICK)
	public Object properties() throws Exception {
		SequenceFlow sequenceFlow;
		if(this.getRelation() == null)
			sequenceFlow = new SequenceFlow();
		else
			sequenceFlow = (SequenceFlow)this.getRelation();
		
		//transition.getMetaworksContext().setWhen(MetaworksContext.WHEN_EDIT);
		
		return new RelationPropertiesView(sequenceFlow);
	}
	
	public static Symbol createSymbol() {
		Symbol symbol = new Symbol();
		symbol.setName("Sequence Flow");
		
		return fillSymbol(symbol);
	}
	
	public static Symbol createSymbol(Class<? extends Symbol> symbolType) {
		Symbol symbol = new Symbol();
		try {
			symbol = (Symbol)Thread.currentThread().getContextClassLoader().loadClass(symbolType.getName()).newInstance();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		symbol.setName("Connector");
		return fillSymbol(symbol);
	}
	
	private static Symbol fillSymbol (Symbol symbol){
		
		symbol.setShapeId(SHAPE_ID);
		symbol.setHeight(150);
		symbol.setWidth(200);
		symbol.setElementClassName(SequenceFlow.class.getName());
		symbol.setShapeType("EDGE");
		
		return symbol;
	}
	
	String viewType;
		public String getViewType() {
			return viewType;
		}
	
		public void setViewType(String viewType) {
			this.viewType = viewType;
		}
		
	
}

