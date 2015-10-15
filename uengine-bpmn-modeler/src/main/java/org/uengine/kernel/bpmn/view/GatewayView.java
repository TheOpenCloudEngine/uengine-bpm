package org.uengine.kernel.bpmn.view;

import org.uengine.kernel.bpmn.Gateway;
import org.uengine.kernel.view.ActivityView;
import org.uengine.modeling.IElement;
import org.uengine.modeling.Symbol;

public class GatewayView extends ActivityView {

	public final static String SHAPE_ID = "OG.shape.bpmn.G_Gateway";
	public final static String SHAPE_TYPE = "GEOM";
	public final static String ELEMENT_CLASSNAME = Gateway.class.getName();
	
	public GatewayView(){
		setShapeId(SHAPE_ID);
	}

	@Override
	public Symbol createSymbol() {
		Symbol symbol = super.createSymbol();
		symbol.setName("Exclusive");
		symbol.setHeight(30);
		symbol.setWidth(30);

		return symbol;
	}

}
