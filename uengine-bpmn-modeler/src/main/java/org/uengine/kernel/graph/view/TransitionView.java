package org.uengine.kernel.graph.view;

import org.metaworks.EventContext;
import org.metaworks.annotation.ServiceMethod;
import org.uengine.kernel.graph.Transition;
import org.uengine.modeling.IRelation;
import org.uengine.modeling.RelationPropertiesView;
import org.uengine.modeling.RelationView;
import org.uengine.modeling.Symbol;

//import org.uengine.essencia.modeling.ConnectorSymbol;


public class TransitionView extends RelationView {
	public final static String SHAPE_ID = "OG.shape.bpmn.C_Sequence";
	
	public TransitionView(){
		super();
	}
	
	public TransitionView(IRelation relation){
		super(relation);
	}
	
	@ServiceMethod(callByContent=true, eventBinding=EventContext.EVENT_DBLCLICK)
	public Object properties() throws Exception {
		Transition transition;
		if(this.getRelation() == null)
			transition = new Transition();
		else
			transition = (Transition)this.getRelation();
		
		//transition.getMetaworksContext().setWhen(MetaworksContext.WHEN_EDIT);
		
		return new RelationPropertiesView(transition);
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
		symbol.setElementClassName(Transition.class.getName());
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

