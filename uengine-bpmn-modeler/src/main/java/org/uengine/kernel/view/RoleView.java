package org.uengine.kernel.view;

import org.uengine.kernel.Role;
import org.uengine.modeling.ElementView;
import org.uengine.modeling.IElement;
import org.uengine.modeling.Symbol;

public class RoleView extends ElementView{
	
	public final static String SHAPE_ID = "OG.shape.HorizontalLaneShape";
	
	public RoleView(){
		setShapeId(SHAPE_ID);
	}
	
	public RoleView(IElement element){
		super(element);
	}
	
	public Symbol createSymbol() {
		Symbol symbol = new Symbol();
		symbol.setName("SwimLane");
		symbol.setShapeId(SHAPE_ID);
		symbol.setHeight(200);
		symbol.setWidth(300);
		symbol.setElementClassName(Role.class.getName());
		symbol.setShapeType("GROUP");
		
		return symbol;
	}
}
