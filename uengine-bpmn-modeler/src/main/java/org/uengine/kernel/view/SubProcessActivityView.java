package org.uengine.kernel.view;

import org.metaworks.Refresh;
import org.metaworks.ServiceMethodContext;
import org.metaworks.annotation.AutowiredFromClient;
import org.metaworks.annotation.ServiceMethod;
import org.metaworks.widget.Clipboard;
import org.uengine.kernel.SubProcessActivity;
import org.uengine.modeling.IElement;
import org.uengine.modeling.Symbol;


public class SubProcessActivityView extends DefaultActivityView{
	
	public final static String SHAPE_ID_BPMN 		 = "OG.shape.bpmn.A_Subprocess";
	public final static String SHAPE_ID_VACD 		 = "OG.shape.bpmn.Value_Chain_Module";
	public final static String SHAPE_ID_VACD_MEGA 	 = "OG.shape.bpmn.Value_Chain";
	public final static String SHAPE_TYPE 		 	 = "GROUP";
	public final static String ELEMENT_CLASSNAME = SubProcessActivity.class.getName();
	
	@AutowiredFromClient
	public Clipboard clipboard;
	
	
	public SubProcessActivityView(){
		
	}
	
	public SubProcessActivityView(IElement element){
		super(element);
	}

	public Symbol createSymbol() {
		Symbol symbol = new Symbol();
		symbol.setName("하위 프로세스");
		symbol.setShapeId(SHAPE_ID);
		symbol.setHeight(100);
		symbol.setWidth(100);
		symbol.setElementClassName(SubProcessActivity.class.getName());
		symbol.setShapeType(SHAPE_TYPE);
		
		return symbol;
	}
	public Symbol createSymbol(String modelerType) {
		Symbol symbol = createSymbol();
		
		if(modelerType.equals("BPMN")){
			symbol.setShapeId(SHAPE_ID_BPMN);
		}else{
				symbol.setShapeId(SHAPE_ID_VACD );
		}
		return symbol;
	}
	
	@ServiceMethod(callByContent=true, mouseBinding="drop", target=ServiceMethodContext.TARGET_APPEND)
	public Object drop() {
		Object content = clipboard.getContent();

		return new Object[]{new Refresh(this, true, true)};
	}
	
	@ServiceMethod(callByContent=true, eventBinding="inclusionclick", target=ServiceMethodContext.TARGET_POPUP)
	public Object open(){
		
		/*SubProcessActivity subProcessActivity = (SubProcessActivity)getRealActivity();
		String id = subProcessActivity.getDefinitionId();
		ModalWindow modal = null;
		if("".equals(id)){
			
		}else{
			IncludedSubProcessPanel frontAndRearProcessPanel = new IncludedSubProcessPanel(editorInfo.getResource(), id, this);;
			modal = new ModalWindow(frontAndRearProcessPanel, 500, 500, "");
			
		}
		return modal;*/
		return null;
	}
	
}
