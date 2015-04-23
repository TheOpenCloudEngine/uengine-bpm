package org.uengine.kernel.view;

import org.uengine.kernel.IntermediateConditionalActivity;
import org.uengine.modeling.IElement;
import org.uengine.modeling.Symbol;


public class IntermediateConditionalActivityView extends EventActivityView{

public final static String SHAPE_ID = "OG.shape.bpmn.E_Intermediate_Rule";
	
	public IntermediateConditionalActivityView(){
		
	}
	
	public IntermediateConditionalActivityView(IElement element){
		super(element);
	}
	
	
	@Override
	public Symbol createSymbol() {
		Symbol symbol = new Symbol();
		symbol.setName("조건부 중간");
		symbol.setShapeId(SHAPE_ID);
		symbol.setHeight(30);
		symbol.setWidth(30);
		symbol.setElementClassName(IntermediateConditionalActivity.class.getName());
		symbol.setShapeType("GEOM");
		
		return symbol;
	}
	
}
