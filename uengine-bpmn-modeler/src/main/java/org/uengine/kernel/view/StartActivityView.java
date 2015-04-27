package org.uengine.kernel.view;

import org.uengine.kernel.StartActivity;
import org.uengine.modeling.IElement;
import org.uengine.modeling.Symbol;

public class StartActivityView extends EventActivityView{

	public final static String SHAPE_ID = "OG.shape.bpmn.E_Start";
	public final static String SHAPE_TYPE = "GEOM";
	public final static String ELEMENT_CLASSNAME = StartActivity.class.getName();

	public StartActivityView(){
		setShapeId(SHAPE_ID);
	}

	public StartActivityView(IElement element){
		super(element);
	}
	
	
//	@AutowiredFromClient
//	public Session session;
//
//	@AutowiredFromClient
//	public Clipboard clipboard;
//
//	@AutowiredFromClient
//	public EditorInfo editorInfo;

	@Override
	public Symbol createSymbol() {
		Symbol symbol = new Symbol();

		return fillSymbol(symbol);
	}
	
	@Override
	public Symbol createSymbol(Class<? extends Symbol> symbolType) {
		Symbol symbol = new Symbol();
		
		return fillSymbol(symbol);
	}
	
	private Symbol fillSymbol(Symbol symbol){
		symbol.setName("시작");
		symbol.setShapeId(SHAPE_ID);
		symbol.setHeight(30);
		symbol.setWidth(30);
		symbol.setElementClassName(ELEMENT_CLASSNAME);
		symbol.setShapeType(SHAPE_TYPE);
		return symbol;
	}
	
}
