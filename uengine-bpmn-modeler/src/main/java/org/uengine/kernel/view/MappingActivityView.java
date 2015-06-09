package org.uengine.kernel.view;

import org.uengine.kernel.MappingActivity;
import org.uengine.modeling.IElement;
import org.uengine.modeling.Symbol;

public class MappingActivityView extends ActivityView {
	
	public final static String SHAPE_ID = "OG.shape.bpmn.A_MapperTask";
	
	public MappingActivityView(){
		setShapeId(SHAPE_ID);
	}
	
	public MappingActivityView(IElement element){
		super(element);
	}

}
