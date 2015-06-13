package org.uengine.kernel.bpmn.view;

import org.uengine.kernel.bpmn.InclusiveGateway;
import org.uengine.kernel.view.ActivityView;
import org.uengine.modeling.IElement;

public class InclusiveGatewayView extends ActivityView {

	public final static String SHAPE_ID = "OG.shape.bpmn.G_Inclusive";
	public final static String SHAPE_TYPE = "GEOM";
	public final static String ELEMENT_CLASSNAME = InclusiveGateway.class.getName();
	
	public InclusiveGatewayView(){
		setShapeId(SHAPE_ID);
		setLabel(ELEMENT_CLASSNAME);
	}
}
