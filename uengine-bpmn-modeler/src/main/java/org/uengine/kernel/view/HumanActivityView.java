package org.uengine.kernel.view;

import org.metaworks.EventContext;
import org.metaworks.ServiceMethodContext;
import org.metaworks.annotation.AutowiredFromClient;
import org.metaworks.annotation.ServiceMethod;
import org.uengine.kernel.HumanActivity;
import org.uengine.kernel.bpmn.face.ProcessVariablePanel;
import org.uengine.kernel.bpmn.face.RolePanel;
import org.uengine.modeling.IElement;
import org.uengine.modeling.Symbol;

public class HumanActivityView extends ActivityView{
	
	public final static String SHAPE_ID_BPMN = "OG.shape.bpmn.A_HumanTask";
	public final static String SHAPE_ID_VACD = "OG.shape.bpmn.Value_Chain_Module";
	public final static String SHAPE_TYPE 		 = "GEOM";
	public final static String ELEMENT_CLASSNAME = HumanActivity.class.getName();
	public final static int DIALOG_HEIGHT = 700;
	public final static int DIALOG_WIDTH = 800;
	
	public HumanActivityView() {
		setShapeId(SHAPE_ID_BPMN);
		setLabel(ELEMENT_CLASSNAME);
		setPropertyDialogHeight(DIALOG_HEIGHT);
		setPropertyDialogWidth(DIALOG_WIDTH);
	}
	
	public HumanActivityView(IElement element){
		super(element);
	}

	@ServiceMethod(callByContent = true, eventBinding = EventContext.EVENT_DBLCLICK, target = ServiceMethodContext.TARGET_POPUP)
	public Object showProperty(
			@AutowiredFromClient RolePanel rolePanel,
			@AutowiredFromClient ProcessVariablePanel processVariablePanel
	) throws Exception {
		return super.showProperty();
	}
}
