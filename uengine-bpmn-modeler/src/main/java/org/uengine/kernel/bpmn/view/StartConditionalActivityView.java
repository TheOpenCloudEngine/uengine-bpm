package org.uengine.kernel.bpmn.view;

import org.uengine.kernel.bpmn.StartConditional;
import org.uengine.modeling.IElement;
import org.uengine.modeling.Symbol;

public class StartConditionalActivityView extends EventActivityView {

public final static String SHAPE_ID = "OG.shape.bpmn.E_Start_Rule";
	
	public StartConditionalActivityView(){
		
	}
	
	public StartConditionalActivityView(IElement element){
		super(element);
	}
	
	@Override
	public Symbol createSymbol() {
		Symbol symbol = new Symbol();
		symbol.setName("조건부 시작");
		symbol.setShapeId(SHAPE_ID);
		symbol.setHeight(30);
		symbol.setWidth(30);
		symbol.setElementClassName(StartConditional.class.getName());
		symbol.setShapeType("GEOM");
		
		return symbol;
	}
	
}
