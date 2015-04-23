package org.uengine.kernel.view;

import org.uengine.kernel.ManualActivity;
import org.uengine.modeling.IElement;
import org.uengine.modeling.Symbol;

public class ManualActivityView extends ActivityView{

	public final static String SHAPE_ID_BPMN	 = "OG.shape.bpmn.A_ManualTask";
	public final static String SHAPE_ID_VACD	 = "OG.shape.bpmn.Value_Chain_Module";
	public final static String SHAPE_TYPE 		 = "GEOM";
	public final static String ELEMENT_CLASSNAME = ManualActivity.class.getName();

	
	public ManualActivityView(){
		
	}
	
	public ManualActivityView(IElement element){
		super(element);
	}

	public Symbol createSymbol() {
		Symbol symbol = new Symbol();
		symbol.setName("수동");
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
