package org.uengine.kernel.bpmn.view;

import org.uengine.kernel.EndMessage;
import org.uengine.modeling.IElement;
import org.uengine.modeling.Symbol;

public class EndMessageActivityView extends EventActivityView {

	public final static String SHAPE_ID = "OG.shape.bpmn.E_End_Message";
	
	public EndMessageActivityView(){
		
	}
	
	public EndMessageActivityView(IElement element){
		super(element);
	}

	@Override
	public Symbol createSymbol() {
		Symbol symbol = new Symbol();
		symbol.setName("메시지 종료");
		symbol.setShapeId(SHAPE_ID);
		symbol.setHeight(30);
		symbol.setWidth(30);
		symbol.setElementClassName(EndMessage.class.getName());
		symbol.setShapeType("GEOM");
		
		return symbol;
	}
	
}