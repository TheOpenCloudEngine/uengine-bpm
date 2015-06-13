package org.uengine.kernel.bpmn.view;

import org.uengine.kernel.bpmn.Gateway;
import org.uengine.kernel.view.ActivityView;
import org.uengine.modeling.IElement;

public class GatewayView extends ActivityView {

	public final static String SHAPE_ID = "OG.shape.bpmn.G_Gateway";
	public final static String SHAPE_TYPE = "GEOM";
	public final static String ELEMENT_CLASSNAME = Gateway.class.getName();
	
	public GatewayView(){
		setShapeId(SHAPE_ID);
		setLabel(ELEMENT_CLASSNAME);
	}
}
