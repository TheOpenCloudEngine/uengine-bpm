package org.uengine.kernel.bpmn.view;

import org.uengine.kernel.bpmn.Gateway;
import org.uengine.kernel.view.ActivityView;
import org.uengine.modeling.IElement;
import org.uengine.modeling.Symbol;
import org.uengine.modeling.modeler.symbol.GatewaySymbol;

public class GatewayView extends ActivityView {

	public final static String SHAPE_ID = "OG.shape.bpmn.G_Gateway";
	
	public GatewayView(){
		setShapeId(SHAPE_ID);
	}
	
	public GatewayView(IElement element){
		super(element);
	}
	

}
