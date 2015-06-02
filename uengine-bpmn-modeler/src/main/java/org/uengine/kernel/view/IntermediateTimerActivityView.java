package org.uengine.kernel.view;

import org.uengine.kernel.bpmn.IntermediateTimer;
import org.uengine.kernel.bpmn.view.EventActivityView;
import org.uengine.modeling.IElement;
import org.uengine.modeling.Symbol;

public class IntermediateTimerActivityView extends EventActivityView {

	public final static String SHAPE_ID = "OG.shape.bpmn.E_Intermediate_Timer";
	
	public IntermediateTimerActivityView(){
		
	}
	
	public IntermediateTimerActivityView(IElement element){
		super(element);
	}


	
}
