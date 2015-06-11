package org.uengine.kernel.view;

import org.uengine.kernel.Terminate;
import org.uengine.kernel.bpmn.view.EventView;
import org.uengine.modeling.IElement;
import org.uengine.modeling.Symbol;

public class TerminateActivityView extends EventView {

	public final static String SHAPE_ID = "OG.shape.bpmn.E_Terminate";
	
	public TerminateActivityView(){
		setShapeId(SHAPE_ID);
	}
	
	public TerminateActivityView(IElement element){
		super(element);
	}
	

}
