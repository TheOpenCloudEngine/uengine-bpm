package org.uengine.kernel.bpmn.view;

import org.uengine.kernel.bpmn.Intermediate;
import org.uengine.modeling.IElement;
import org.uengine.modeling.Symbol;

public class IntermediateActivityView extends EventActivityView {

	public final static String SHAPE_ID = "OG.shape.bpmn.E_Intermediate";
	
	public IntermediateActivityView(){
		
	}
	
	public IntermediateActivityView(IElement element){
		super(element);
	}
	
	
	@Override
	public Symbol createSymbol() {
		Symbol symbol = new Symbol();
		symbol.setName("중간");
		symbol.setShapeId(SHAPE_ID);
		symbol.setHeight(30);
		symbol.setWidth(30);
		symbol.setElementClassName(Intermediate.class.getName());
		symbol.setShapeType("GEOM");
		
		return symbol;
	}

}
