package org.uengine.kernel.view;

import org.metaworks.EventContext;
import org.metaworks.annotation.ServiceMethod;
import org.uengine.kernel.graph.MessageTransition;
import org.uengine.kernel.graph.Transition;
import org.uengine.kernel.graph.view.TransitionView;
import org.uengine.modeling.IRelation;
import org.uengine.modeling.RelationPropertiesView;
import org.uengine.modeling.Symbol;
import org.uengine.modeling.modeler.symbol.ConnectorSymbol;

public class MessageTransitionView extends TransitionView {
	
	public final static String SHAPE_ID = "OG.shape.bpmn.C_Message";
	
	public MessageTransitionView(){
		
	}
	
	public MessageTransitionView(IRelation relation){
		super(relation);
	}
	
	@ServiceMethod(callByContent=true, eventBinding=EventContext.EVENT_DBLCLICK)
	public Object properties() throws Exception {
		Transition transition;
		if(this.getRelation() == null)
			transition = new Transition();
		else
			transition = (Transition)this.getRelation();
		
		return new RelationPropertiesView(transition);
	}
	
	public static Symbol createSymbol() {
		Symbol symbol = new Symbol();
		symbol.setName("메시지 플로우");
		
		return fillSymbol(symbol);
	}
	
	public static Symbol createSymbol(Class<? extends Symbol> symbolType) {
		Symbol symbol = new ConnectorSymbol();
		try {
			symbol = (Symbol)Thread.currentThread().getContextClassLoader().loadClass(symbolType.getName()).newInstance();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		symbol.setName("커낵터");
		return fillSymbol(symbol);
	}
	
	private static Symbol fillSymbol (Symbol symbol){
		
		symbol.setShapeId(SHAPE_ID);
		symbol.setHeight(150);
		symbol.setWidth(200);
		symbol.setElementClassName(MessageTransition.class.getName());
		symbol.setShapeType("EDGE");
		
		return symbol;
	}
}
