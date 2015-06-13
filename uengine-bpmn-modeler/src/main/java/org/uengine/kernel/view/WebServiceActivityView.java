package org.uengine.kernel.view;

import org.uengine.kernel.WebServiceActivity;
import org.uengine.modeling.IElement;
import org.uengine.modeling.Symbol;

public class WebServiceActivityView extends ActivityView {

	public final static String SHAPE_ID_BPMN = "OG.shape.bpmn.A_WebServiceTask";
	public final static String SHAPE_ID_VACD = "OG.shape.bpmn.Value_Chain_Module";
	public final static String SHAPE_TYPE 		 = "GEOM";
	public final static String ELEMENT_CLASSNAME = WebServiceActivity.class.getName();
	
	public WebServiceActivityView(){
		setShapeId(SHAPE_ID);
	}
	
	public WebServiceActivityView(IElement element){
		super(element);
	}
	

}
