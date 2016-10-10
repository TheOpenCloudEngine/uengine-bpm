package org.uengine.kernel.bpmn.view;

import org.uengine.kernel.bpmn.StartEvent;

public class ReceiveErrorEventView extends EventView {

	public final static String SHAPE_ID = "OG.shape.bpmn.ReceiveErrorEventView";
	public final static String SHAPE_TYPE = "GEOM";
	public final static String ELEMENT_CLASSNAME = StartEvent.class.getName();

	public ReceiveErrorEventView(){
		setShapeId(SHAPE_ID);
	}

}
