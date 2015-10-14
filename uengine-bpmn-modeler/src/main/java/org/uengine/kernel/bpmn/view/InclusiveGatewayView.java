package org.uengine.kernel.bpmn.view;

import org.uengine.kernel.bpmn.InclusiveGateway;
import org.uengine.kernel.view.ActivityView;
import org.uengine.modeling.IElement;
import org.uengine.modeling.Symbol;

public class InclusiveGatewayView extends ActivityView {

	public final static String SHAPE_ID = "OG.shape.bpmn.G_Inclusive";
	public final static String SHAPE_TYPE = "GEOM";
	public final static String ELEMENT_CLASSNAME = InclusiveGateway.class.getName();
	
	public InclusiveGatewayView(){
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
