package org.uengine.kernel.view;

import org.uengine.kernel.ManualActivity;
import org.uengine.modeling.IElement;
import org.uengine.modeling.Symbol;

public class ManualActivityView extends ActivityView{

	public final static String SHAPE_ID_BPMN	 = "OG.shape.bpmn.A_ManualTask";
	public final static String SHAPE_ID_VACD	 = "OG.shape.bpmn.Value_Chain_Module";
	public final static String SHAPE_TYPE 		 = "GEOM";
	public final static String ELEMENT_CLASSNAME = ManualActivity.class.getName();

	
	public ManualActivityView(){
		setShapeId(SHAPE_ID);
	}
	
	public ManualActivityView(IElement element){
		super(element);
	}


}
