package org.uengine.kernel.bpmn.view;

import org.uengine.kernel.bpmn.Pool;
import org.uengine.modeling.ElementView;
import org.uengine.modeling.IElement;
import org.uengine.modeling.Symbol;


public class PoolView extends ElementView{

	public final static String SHAPE_ID = "OG.shape.HorizontalPoolShape";
	
	public PoolView(){
		setShapeId(SHAPE_ID);
	}
	
	public Symbol createSymbol() {
		Symbol symbol = new Symbol();
		symbol.setName("Pool");
		symbol.setShapeId(SHAPE_ID);
		symbol.setHeight(200);
		symbol.setWidth(400);
		symbol.setElementClassName(Pool.class.getName());
		symbol.setShapeType("GROUP");
		
		return symbol;
	}
}
