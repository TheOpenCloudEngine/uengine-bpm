package org.uengine.kernel.view;

import org.uengine.kernel.StartTimerActivity;
import org.uengine.modeling.IElement;
import org.uengine.modeling.Symbol;

public class StartTimerActivityView extends EventActivityView{

	public final static String SHAPE_ID = "OG.shape.bpmn.E_Start_Timer";
	
	public StartTimerActivityView(){
		
	}
	
	public StartTimerActivityView(IElement element){
		super(element);
	}
	
	@Override
	public Symbol createSymbol() {
		Symbol symbol = new Symbol();
		symbol.setName("타이머 시작");
		symbol.setShapeId(SHAPE_ID);
		symbol.setHeight(30);
		symbol.setWidth(30);
		symbol.setElementClassName(StartTimerActivity.class.getName());
		symbol.setShapeType("GEOM");
		
		return symbol;
	}
	

}
