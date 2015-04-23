package org.uengine.kernel.view;

import org.uengine.kernel.EndActivity;
import org.uengine.modeling.IElement;
import org.uengine.modeling.Symbol;

public class EndActivityView extends StartActivityView {

	public final static String SHAPE_ID = "OG.shape.bpmn.E_End";
	
	public EndActivityView(){
		
	}
	
	public EndActivityView(IElement element){
		super(element);
	}
	
	@Override
	public Symbol createSymbol() {
		Symbol symbol = new Symbol();
		symbol.setName("종료");
		symbol.setShapeId(SHAPE_ID);
		symbol.setHeight(30);
		symbol.setWidth(30);
		symbol.setElementClassName(EndActivity.class.getName());
		symbol.setShapeType("GEOM");
		
		return symbol;
	}
}
