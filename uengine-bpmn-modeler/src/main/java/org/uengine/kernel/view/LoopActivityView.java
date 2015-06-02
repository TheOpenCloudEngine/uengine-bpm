package org.uengine.kernel.view;

import org.uengine.kernel.LoopActivity;
import org.uengine.modeling.IElement;
import org.uengine.modeling.Symbol;

public class LoopActivityView extends DefaultActivityView{

public final static String SHAPE_ID = "OG.shape.bpmn.A_LoopTask";
	
	public LoopActivityView(){
		
	}
	
	public LoopActivityView(IElement element){
		super(element);
	}


}
