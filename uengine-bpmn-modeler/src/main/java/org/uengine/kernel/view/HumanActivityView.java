package org.uengine.kernel.view;

import org.uengine.kernel.HumanActivity;
import org.uengine.modeling.IElement;
import org.uengine.modeling.Symbol;

public class HumanActivityView extends ActivityView{
	
	public final static String SHAPE_ID_BPMN = "OG.shape.bpmn.A_HumanTask";
	public final static String SHAPE_ID_VACD = "OG.shape.bpmn.Value_Chain_Module";
	public final static String SHAPE_TYPE 		 = "GEOM";
	public final static String ELEMENT_CLASSNAME = HumanActivity.class.getName();
	
	public HumanActivityView(){
		setShapeId(SHAPE_ID_BPMN);
	}
	
	public HumanActivityView(IElement element){
		super(element);
	}
	
	@Override
	public Symbol createSymbol() {
		Symbol symbol = new Symbol();
		symbol.setName("사용자");
		symbol.setShapeId(SHAPE_ID);
		symbol.setHeight(100);
		symbol.setWidth(100);
		symbol.setElementClassName(ELEMENT_CLASSNAME);
		symbol.setShapeType(SHAPE_TYPE);
		
		return symbol;
	}
	
	public Symbol createSymbol(String modelerType) {
		Symbol symbol = createSymbol();

		if("BPMN".equals(modelerType)){
			symbol.setShapeId(SHAPE_ID_BPMN);
		}else{
			symbol.setShapeId(SHAPE_ID_VACD );
		}

		return symbol;
	}
	
}
