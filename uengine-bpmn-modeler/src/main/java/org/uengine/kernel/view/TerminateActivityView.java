package org.uengine.kernel.view;

import org.uengine.kernel.Terminate;
import org.uengine.kernel.bpmn.view.EventActivityView;
import org.uengine.modeling.IElement;
import org.uengine.modeling.Symbol;

public class TerminateActivityView extends EventActivityView {

	public final static String SHAPE_ID = "OG.shape.bpmn.E_Terminate";
	
	public TerminateActivityView(){
		
	}
	
	public TerminateActivityView(IElement element){
		super(element);
	}
	
	@Override
	public Symbol createSymbol() {
		Symbol symbol = new Symbol();
		symbol.setName("프로세스 종료");
		symbol.setShapeId(SHAPE_ID);
		symbol.setHeight(30);
		symbol.setWidth(30);
		symbol.setElementClassName(Terminate.class.getName());
		symbol.setShapeType("GEOM");
		
		return symbol;
	}
}
