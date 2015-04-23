package org.uengine.kernel.view;

import org.uengine.kernel.ScopeActivity;
import org.uengine.modeling.IElement;
import org.uengine.modeling.Symbol;

public class ScopeActivityView extends ActivityView{

public final static String SHAPE_ID = "OG.shape.bpmn.ScopeActivity";
	
	public ScopeActivityView(){
		
	}
	
	public ScopeActivityView(IElement element){
		super(element);
	}
	
	@Override
	public Symbol createSymbol() {
		Symbol symbol = new Symbol();
		symbol.setName("그룹");
		symbol.setShapeId(SHAPE_ID);
		symbol.setHeight(80);
		symbol.setWidth(100);
		symbol.setElementClassName(ScopeActivity.class.getName());
		symbol.setShapeType("GEOM");
		
		return symbol;
	}
}
