package org.uengine.kernel.bpmn.view;

import org.uengine.kernel.bpmn.ParallelGateway;

public class ParallelGatewayView extends GatewayView {

	public final static String SHAPE_ID = "OG.shape.bpmn.G_Parallel";
	public final static String SHAPE_TYPE = "GEOM";
	public final static String ELEMENT_CLASSNAME = ParallelGateway.class.getName();
	
	public ParallelGatewayView(){
		setShapeId(SHAPE_ID);
		setLabel(ELEMENT_CLASSNAME);
	}
}
