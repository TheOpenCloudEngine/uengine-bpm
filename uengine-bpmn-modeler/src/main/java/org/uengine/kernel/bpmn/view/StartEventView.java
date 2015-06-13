package org.uengine.kernel.bpmn.view;

import org.uengine.kernel.bpmn.StartEvent;
import org.uengine.kernel.bpmn.view.EventView;
import org.uengine.modeling.IElement;

public class StartEventView extends EventView {

	public final static String SHAPE_ID = "OG.shape.bpmn.E_Start";
	public final static String SHAPE_TYPE = "GEOM";
	public final static String ELEMENT_CLASSNAME = StartEvent.class.getName();

	public StartEventView(){
		setShapeId(SHAPE_ID);
		setLabel(ELEMENT_CLASSNAME);
	}

}
