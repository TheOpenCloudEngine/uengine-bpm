package org.uengine.kernel.view;

import org.uengine.kernel.AndGatewayActivity;
import org.uengine.modeling.IElement;
import org.uengine.modeling.Symbol;

public class AndGatewayActivityView extends ActivityView{

public final static String SHAPE_ID = "OG.shape.bpmn.G_Parallel";
	
	public AndGatewayActivityView(){
		
	}
	
	public AndGatewayActivityView(IElement element){
		super(element);
	}
	
	@Override
	public Symbol createSymbol() {
		Symbol symbol = new Symbol();
		symbol.setName("병렬");
		symbol.setShapeId(SHAPE_ID);
		symbol.setHeight(40);
		symbol.setWidth(40);
		symbol.setElementClassName(AndGatewayActivity.class.getName());
		symbol.setShapeType("GEOM");
		
		return symbol;
	}
}
