package org.uengine.kernel.view;

import org.uengine.kernel.bpmn.StartActivity;
import org.uengine.kernel.bpmn.view.EventActivityView;
import org.uengine.modeling.IElement;
import org.uengine.modeling.Symbol;

public class StartActivityView extends EventActivityView {

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


	
}
