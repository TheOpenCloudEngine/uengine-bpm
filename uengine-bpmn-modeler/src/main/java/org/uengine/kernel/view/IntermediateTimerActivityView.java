package org.uengine.kernel.view;

import org.uengine.kernel.IntermediateTimerActivity;
import org.uengine.modeling.IElement;
import org.uengine.modeling.Symbol;

public class IntermediateTimerActivityView extends EventActivityView{

	public final static String SHAPE_ID = "OG.shape.bpmn.E_Intermediate_Timer";
	
	public IntermediateTimerActivityView(){
		
	}
	
	public IntermediateTimerActivityView(IElement element){
		super(element);
	}

	@Override
	public Symbol createSymbol() {
		Symbol symbol = new Symbol();
		symbol.setName("타이머 중간");
		symbol.setShapeId(SHAPE_ID);
		symbol.setHeight(30);
		symbol.setWidth(30);
		symbol.setElementClassName(IntermediateTimerActivity.class.getName());
		symbol.setShapeType("GEOM");
		
		return symbol;
	}
	
}
