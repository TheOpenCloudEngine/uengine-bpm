package org.uengine.kernel.bpmn.view;

import org.uengine.kernel.bpmn.InclusiveGateway;
import org.uengine.kernel.view.ActivityView;
import org.uengine.modeling.IElement;
import org.uengine.modeling.Symbol;

public class InclusiveGatewayView extends ActivityView {

	public final static String SHAPE_ID = "OG.shape.bpmn.G_Inclusive";
	
	public InclusiveGatewayView(){
		setShapeId(SHAPE_ID);
	}
	
	public InclusiveGatewayView(IElement element){
		super(element);
	}

}
