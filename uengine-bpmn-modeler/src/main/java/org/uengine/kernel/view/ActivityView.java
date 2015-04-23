package org.uengine.kernel.view;

import org.uengine.kernel.Activity;
import org.uengine.kernel.HumanActivity;
import org.uengine.kernel.ReferenceActivity;
import org.uengine.modeling.ElementView;
import org.uengine.modeling.IElement;
import org.uengine.modeling.Symbol;

public class ActivityView extends ElementView {

	public final static String SHAPE_ID = "OG.shape.bpmn.A_Task";
	
	public ActivityView() {
		setShapeId(SHAPE_ID);
	}
	
	public ActivityView(IElement element){
		super(element);
	}

	@Override
	public Symbol createSymbol() {
		Symbol symbol = new Symbol();
		symbol.setName("Default Activity");
		symbol.setShapeId(SHAPE_ID);
		symbol.setHeight(100);
		symbol.setWidth(100);
		symbol.setElementClassName(HumanActivity.class.getName());
		symbol.setShapeType("GEOM");
		
		return symbol;
	}
	
	public Activity getRealActivity(){
		Activity activity = null;
		if(getElement() instanceof ReferenceActivity){
			activity = ((ReferenceActivity)getElement()).getReferencedActivity();
		} else {
			activity = (Activity)getElement();
		}
		return activity;
	}

	@Override
	public void setElement(IElement element) {
		if(element!=null && element instanceof Activity){
			Activity activity = (Activity) element;
			setLabel(activity.getName());
			setId(activity.getTracingTag());
		}

		super.setElement(element);
	}
}
