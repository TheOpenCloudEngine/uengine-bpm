package org.uengine.kernel.bpmn.view;

import org.uengine.kernel.HumanActivity;
import org.uengine.modeling.ElementView;
import org.uengine.modeling.IElement;
import org.uengine.modeling.Symbol;


public class EventActivityView extends ElementView{
		
	public final static String SHAPE_ID = "OG.shape.bpmn.E_Start";
	
	public EventActivityView(){
		setShapeId(SHAPE_ID);
	}
	
	public EventActivityView(IElement element){
		super(element);
		setShapeId(SHAPE_ID);
	}
	
	@Override
	public Symbol createSymbol() {
		// TODO Auto-generated method stub
		Symbol symbol = new Symbol();
		symbol.setName("Event Activity");
		symbol.setShapeId(SHAPE_ID);
		symbol.setHeight(100);
		symbol.setWidth(100);
		symbol.setElementClassName(HumanActivity.class.getName());
		symbol.setShapeType("GEOM");
		
		return symbol;
	}
}
