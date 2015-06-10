package org.uengine.kernel.bpmn.view;

import org.uengine.kernel.bpmn.DataStore;
import org.uengine.kernel.view.ActivityView;
import org.uengine.modeling.IElement;
import org.uengine.modeling.Symbol;

public class DataStoreActivityView extends ActivityView {

public final static String SHAPE_ID = "OG.shape.bpmn.D_Store";
	
	public DataStoreActivityView(){
		
	}
	
	public DataStoreActivityView(IElement element){
		super(element);
	}
	
//	@Override
//	public Symbol createSymbol() {
//		Symbol symbol = new Symbol();
//		symbol.setName("데이터 저장소");
//		symbol.setShapeId(SHAPE_ID);
//		symbol.setHeight(50);
//		symbol.setWidth(50);
//		symbol.setElementClassName(DataStore.class.getName());
//		symbol.setShapeType("GEOM");
//
//		return symbol;
//	}
}
