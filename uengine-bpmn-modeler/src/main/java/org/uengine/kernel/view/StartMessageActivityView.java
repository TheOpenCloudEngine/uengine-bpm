package org.uengine.kernel.view;

import org.uengine.kernel.StartMessageActivity;
import org.uengine.modeling.IElement;
import org.uengine.modeling.Symbol;

public class StartMessageActivityView extends EventActivityView{

	public final static String SHAPE_ID = "OG.shape.bpmn.E_Start_Message";
	
	public StartMessageActivityView(){
		
	}
	
	public StartMessageActivityView(IElement element){
		super(element);
	}
	
	@Override
	public Symbol createSymbol() {
		Symbol symbol = new Symbol();
		symbol.setName("메시지 시작");
		symbol.setShapeId(SHAPE_ID);
		symbol.setHeight(30);
		symbol.setWidth(30);
		symbol.setElementClassName(StartMessageActivity.class.getName());
		symbol.setShapeType("GEOM");
		
		return symbol;
	}
	
}
