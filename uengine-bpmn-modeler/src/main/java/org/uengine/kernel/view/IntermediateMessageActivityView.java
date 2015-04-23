package org.uengine.kernel.view;

import org.uengine.kernel.IntermediateMessageActivity;
import org.uengine.modeling.IElement;
import org.uengine.modeling.Symbol;

public class IntermediateMessageActivityView extends EventActivityView {

	public final static String SHAPE_ID = "OG.shape.bpmn.E_Intermediate_Message";
	
	public IntermediateMessageActivityView(){
		
	}
	
	public IntermediateMessageActivityView(IElement element){
		super(element);
	}
	@Override
	public Symbol createSymbol() {
		Symbol symbol = new Symbol();
		symbol.setName("메시지 중간");
		symbol.setShapeId(SHAPE_ID);
		symbol.setHeight(30);
		symbol.setWidth(30);
		symbol.setElementClassName(IntermediateMessageActivity.class.getName());
		symbol.setShapeType("GEOM");
		
		return symbol;
	}
	
}
