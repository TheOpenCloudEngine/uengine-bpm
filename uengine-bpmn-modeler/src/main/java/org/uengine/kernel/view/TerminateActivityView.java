package org.uengine.kernel.view;

import org.uengine.kernel.bpmn.view.EventView;

public class TerminateActivityView extends EventView {

	public final static String SHAPE_ID = "OG.shape.bpmn.E_Terminate";

	public TerminateActivityView(){
		setShapeId(SHAPE_ID);
	}

}
