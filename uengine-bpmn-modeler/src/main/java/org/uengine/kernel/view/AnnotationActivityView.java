package org.uengine.kernel.view;

import org.uengine.kernel.AnnotationActivity;
import org.uengine.modeling.IElement;
import org.uengine.modeling.Symbol;

public class AnnotationActivityView extends ActivityView{

public final static String SHAPE_ID = "OG.shape.bpmn.M_Annotation";
	
	public AnnotationActivityView(){
		
	}
	
	public AnnotationActivityView(IElement element){
		super(element);
	}
	
	@Override
	public Symbol createSymbol() {
		Symbol symbol = new Symbol();
		symbol.setName("주석");
		symbol.setShapeId(SHAPE_ID);
		symbol.setHeight(30);
		symbol.setWidth(70);
		symbol.setElementClassName(AnnotationActivity.class.getName());
		symbol.setShapeType("GEOM");
		
		return symbol;
	}
}
