package org.uengine.kernel.view;

import org.uengine.kernel.ScopeActivity;
import org.uengine.modeling.IElement;
import org.uengine.modeling.Symbol;

public class ScopeActivityView extends ActivityView{

public final static String SHAPE_ID = "OG.shape.bpmn.ScopeActivity";
	
	public ScopeActivityView(){
		setShapeId(SHAPE_ID);
	}
	
	public ScopeActivityView(IElement element){
		super(element);
	}

}
