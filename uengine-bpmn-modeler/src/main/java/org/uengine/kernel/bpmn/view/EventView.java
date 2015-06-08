package org.uengine.kernel.bpmn.view;

import org.uengine.kernel.view.ActivityView;
import org.uengine.modeling.IElement;
import org.uengine.modeling.Symbol;


public class EventView extends ActivityView {
		
	public final static String SHAPE_ID = "OG.shape.bpmn.E_Start";
	
	public EventView(){
		setShapeId(SHAPE_ID);
	}
	
	public EventView(IElement element){
		super(element);
		setShapeId(SHAPE_ID);
	}
	
	@Override
	public Symbol createSymbol() {
		Symbol symbol = super.createSymbol();
		symbol.setHeight(30);
		symbol.setWidth(30);

		return symbol;
	}
}
