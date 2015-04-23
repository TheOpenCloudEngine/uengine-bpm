package org.uengine.kernel.view;

import org.uengine.kernel.DataObjectActivity;
import org.uengine.modeling.IElement;
import org.uengine.modeling.Symbol;

public class DataObjectActivityView extends ActivityView{

public final static String SHAPE_ID = "OG.shape.bpmn.D_Data";
	
	public DataObjectActivityView(){
		
	}
	
	public DataObjectActivityView(IElement element){
		super(element);
	}
	
	@Override
	public Symbol createSymbol() {
		Symbol symbol = new Symbol();
		symbol.setName("데이터 객체");
		symbol.setShapeId(SHAPE_ID);
		symbol.setHeight(30);
		symbol.setWidth(30);
		symbol.setElementClassName(DataObjectActivity.class.getName());
		symbol.setShapeType("GEOM");
		
		return symbol;
	}
}
