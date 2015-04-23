package org.uengine.kernel.view;

import org.uengine.kernel.LoopActivity;
import org.uengine.modeling.IElement;
import org.uengine.modeling.Symbol;

public class LoopActivityView extends DefaultActivityView{

public final static String SHAPE_ID = "OG.shape.bpmn.A_LoopTask";
	
	public LoopActivityView(){
		
	}
	
	public LoopActivityView(IElement element){
		super(element);
	}

	public Symbol createSymbol() {
		Symbol symbol = new Symbol();
		symbol.setName("반복");
		symbol.setShapeId(SHAPE_ID);
		symbol.setHeight(100);
		symbol.setWidth(100);
		symbol.setElementClassName(LoopActivity.class.getName());
		symbol.setShapeType("GEOM");
		
		return symbol;
	}
	
}
