package org.uengine.kernel.bpmn.view;

import org.uengine.kernel.EndEvent;

public class EndEventView extends EventView {

	public final static String SHAPE_ID = "OG.shape.bpmn.E_End";
	public final static String SHAPE_TYPE = "GEOM";
	public final static String ELEMENT_CLASSNAME = EndEvent.class.getName();
	
	public EndEventView(){
		setShapeId(SHAPE_ID);
		setLabel(ELEMENT_CLASSNAME);
	}


}
