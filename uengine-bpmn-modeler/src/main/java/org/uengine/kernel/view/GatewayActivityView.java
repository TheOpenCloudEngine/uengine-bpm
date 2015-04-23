package org.uengine.kernel.view;

import org.uengine.kernel.GatewayActivity;
import org.uengine.modeling.IElement;
import org.uengine.modeling.Symbol;
import org.uengine.modeling.modeler.symbol.GatewaySymbol;
import sun.security.provider.SHA;

public class GatewayActivityView extends ActivityView{

	public final static String SHAPE_ID = "OG.shape.bpmn.G_Gateway";
	
	public GatewayActivityView(){
		setShapeId(SHAPE_ID);
	}
	
	public GatewayActivityView(IElement element){
		super(element);
	}
	
	@Override
	public Symbol createSymbol() {
		Symbol symbol = new Symbol();
		
		return fillSymbol(symbol);
	}
	
	@Override
	public Symbol createSymbol(Class<? extends Symbol> symbolType) {
		Symbol symbol = new GatewaySymbol();
		
		return fillSymbol(symbol);
	}
	
	private Symbol fillSymbol(Symbol symbol){
		symbol.setName("배타적");
		symbol.setShapeId(SHAPE_ID);
		symbol.setHeight(40);
		symbol.setWidth(40);
		symbol.setElementClassName(GatewayActivity.class.getName());
		symbol.setShapeType("GEOM");
		return symbol;
	}
}
