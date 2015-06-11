package org.uengine.kernel.bpmn.view;

import org.uengine.kernel.bpmn.ParallelGateway;
import org.uengine.kernel.view.ActivityView;
import org.uengine.modeling.IElement;
import org.uengine.modeling.Symbol;

public class ParallelGatewayView extends GatewayView {

public final static String SHAPE_ID = "OG.shape.bpmn.G_Parallel";
	
	public ParallelGatewayView(){
		setShapeId(SHAPE_ID);
	}
	
	public ParallelGatewayView(IElement element){
		super(element);
	}
	

}
