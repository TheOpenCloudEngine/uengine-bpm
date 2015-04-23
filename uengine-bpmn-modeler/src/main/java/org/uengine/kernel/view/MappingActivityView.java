package org.uengine.kernel.view;

import org.uengine.kernel.MappingActivity;
import org.uengine.modeling.IElement;
import org.uengine.modeling.Symbol;

public class MappingActivityView extends ActivityView {
	
	public final static String SHAPE_ID = "OG.shape.bpmn.A_MapperTask";
	
	public MappingActivityView(){
		
	}
	
	public MappingActivityView(IElement element){
		super(element);
	}
	
	public Symbol createSymbol() {
		Symbol symbol = new Symbol();
		symbol.setName("Mapping Activity");
		symbol.setShapeId(SHAPE_ID);
		symbol.setHeight(100);
		symbol.setWidth(100);
		symbol.setElementClassName(MappingActivity.class.getName());
		symbol.setShapeType("GEOM");
		
		return symbol;
	}
}
