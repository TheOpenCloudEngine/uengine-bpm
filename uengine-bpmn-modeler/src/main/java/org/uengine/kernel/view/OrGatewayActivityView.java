package org.uengine.kernel.view;

import org.uengine.kernel.OrGatewayActivity;
import org.uengine.modeling.IElement;
import org.uengine.modeling.Symbol;

public class OrGatewayActivityView extends ActivityView{

	public final static String SHAPE_ID = "OG.shape.bpmn.G_Inclusive";
	
	public OrGatewayActivityView(){
		
	}
	
	public OrGatewayActivityView(IElement element){
		super(element);
	}
	
	@Override
	public Symbol createSymbol() {
		Symbol symbol = new Symbol();
		symbol.setName("포괄적");
		symbol.setShapeId(SHAPE_ID);
		symbol.setHeight(40);
		symbol.setWidth(40);
		symbol.setElementClassName(OrGatewayActivity.class.getName());
		symbol.setShapeType("GEOM");
		
		return symbol;
	}
}
