package org.uengine.kernel.view;

import org.metaworks.annotation.ServiceMethod;
import org.uengine.kernel.*;
import org.uengine.modeling.IDocument;
import org.uengine.modeling.IElement;
import org.uengine.modeling.Symbol;

public class DefaultActivityView extends ActivityView{

	public final static String SHAPE_ID_BPMN	 = "OG.shape.bpmn.A_Task";
	public final static String SHAPE_ID_VACD	 = "OG.shape.bpmn.Value_Chain_Module";
	public final static String SHAPE_TYPE 		 = "GEOM";
	public final static String ELEMENT_CLASSNAME = DefaultActivity.class.getName();

	
	public DefaultActivityView(){
		super();
	}

	public DefaultActivityView(IElement element){
		super(element);
	}

	public Symbol createSymbol() {
		Symbol symbol = new Symbol();
		
		symbol = fillSymbol(symbol);

		return symbol;
	}

	public Symbol createSymbol(String type) {
		Symbol symbol = createSymbol();

		symbol = changeShapeId(symbol, type);

		return symbol;
	}
	public Symbol createSymbol(String type, Class<? extends Symbol> symbolType) {
		Symbol symbol = null;
		
		try {
			symbol = (Symbol)Thread.currentThread().getContextClassLoader().loadClass(symbolType.getName()).newInstance();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		
		symbol = fillSymbol(symbol);
		symbol = changeShapeId(symbol, type);
		
		return symbol;
	}
	private Symbol fillSymbol(Symbol symbol){
		symbol.setName("추상");
		symbol.setShapeId(SHAPE_ID);
		symbol.setHeight(100);
		symbol.setWidth(100);
		symbol.setElementClassName(ELEMENT_CLASSNAME);
		symbol.setShapeType(SHAPE_TYPE);
		return symbol;
	}
	private Symbol changeShapeId(Symbol symbol, String type){
		if("BPMN".equals(type)){
			symbol.setShapeId(SHAPE_ID_BPMN);
		}else{
			symbol.setShapeId(SHAPE_ID_VACD );
		}
		return symbol;
	}
	

	@ServiceMethod(callByContent=true, eventBinding="changeToAbstract")
	public void changeToAbstract(){
		this.convertActivity(new DefaultActivity());
	}
	
	@ServiceMethod(callByContent=true, eventBinding="changeToHuman")
	public void changeToHuman(){
		this.convertActivity(new HumanActivity());
	}
	
	@ServiceMethod(callByContent=true, eventBinding="changeToManual")
	public void changeToManual(){
		this.convertActivity(new ManualActivity());	
	}
	
	@ServiceMethod(callByContent=true, eventBinding="changeToService")
	public void changeToService(){
		this.convertActivity(new WebServiceActivity());
	}
	
	private void convertActivity(DefaultActivity defaultActivity){
		DefaultActivity activity;
		
//		if(getElement() instanceof ReferenceActivity){
//			activity = ((ReferenceActivity)getElement()).getReferencedActivity();
//		}else{
			activity = (DefaultActivity)getElement();
//		}
		
		IDocument document = activity.getDocument();
		defaultActivity.setDocument(document);
		defaultActivity.setName(getLabel());
//		if(getElement() instanceof ReferenceActivity){
//			((ReferenceActivity)getElement()).setReferencedActivity(defaultActivity);
//			((ReferenceActivity)getElement()).setChanged(true);
//		}else{
//			setElement(defaultActivity);
//		}
	}
}
