package org.uengine.kernel.view;

import org.uengine.kernel.WebServiceActivity;
import org.uengine.modeling.IElement;
import org.uengine.modeling.Symbol;

public class WebServiceActivityView extends ActivityView {

	public final static String SHAPE_ID_BPMN = "OG.shape.bpmn.A_WebServiceTask";
	public final static String SHAPE_ID_VACD = "OG.shape.bpmn.Value_Chain_Module";
	public final static String SHAPE_TYPE 		 = "GEOM";
	public final static String ELEMENT_CLASSNAME = WebServiceActivity.class.getName();
	
	public WebServiceActivityView(){
		
	}
	
	public WebServiceActivityView(IElement element){
		super(element);
	}
	
	public Symbol createSymbol() {
		Symbol symbol = new Symbol();
		symbol.setName("서비스");
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
