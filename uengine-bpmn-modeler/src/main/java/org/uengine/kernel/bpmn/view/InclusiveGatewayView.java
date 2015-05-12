package org.uengine.kernel.bpmn.view;

import org.uengine.kernel.bpmn.InclusiveGateway;
import org.uengine.kernel.view.ActivityView;
import org.uengine.modeling.IElement;
import org.uengine.modeling.Symbol;

public class InclusiveGatewayView extends ActivityView {

	public final static String SHAPE_ID = "OG.shape.bpmn.G_Inclusive";
	
	public InclusiveGatewayView(){
		
	}
	
	public InclusiveGatewayView(IElement element){
		super(element);
	}
	
	@Override
	public Symbol createSymbol() {
		Symbol symbol = new Symbol();
		symbol.setName("포괄적");
		symbol.setShapeId(SHAPE_ID);
		symbol.setHeight(40);
		symbol.setWidth(40);
		symbol.setElementClassName(InclusiveGateway.class.getName());
		symbol.setShapeType("GEOM");
		
		return symbol;
	}
}
