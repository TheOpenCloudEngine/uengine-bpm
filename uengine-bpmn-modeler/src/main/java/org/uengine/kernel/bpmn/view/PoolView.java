package org.uengine.kernel.bpmn.view;

import org.metaworks.EventContext;
import org.metaworks.ServiceMethodContext;
import org.metaworks.annotation.ServiceMethod;
import org.uengine.kernel.Activity;
import org.uengine.kernel.bpmn.Pool;
import org.uengine.kernel.view.ActivityView;
import org.uengine.kernel.view.DynamicDrawGeom;
import org.uengine.modeling.ElementView;
import org.uengine.modeling.PropertySettingDialog;
import org.uengine.modeling.Symbol;

import java.util.ArrayList;


public class PoolView extends ElementView{

	public final static String SHAPE_ID = "OG.shape.HorizontalPoolShape";
	
	public PoolView(){
		setShapeId(SHAPE_ID);
	}
	
	public Symbol createSymbol() {
		Symbol symbol = new Symbol();
		symbol.setName("Pool");
		symbol.setShapeId(SHAPE_ID);
		symbol.setHeight(200);
		symbol.setWidth(400);
		symbol.setElementClassName(Pool.class.getName());
		symbol.setShapeType("GROUP");
		
		return symbol;
	}

	@ServiceMethod(callByContent=true, target= ServiceMethodContext.TARGET_APPEND)
	public Object[] drawActivitysOnDesigner() throws Exception{
		DynamicDrawGeom ddg = ((Pool)this.getElement()).getPoolResolutionContext().drawActivitysOnDesigner();
//		ddg.setEditorId(this.getEditorId());
		ddg.setParentGeomId(this.getId());
		ArrayList<Activity> activityList = ddg.getActivityList();
		for(Activity activity : activityList){
			ActivityView activityView = (ActivityView) activity.getElementView();
//			activityView.setEditorId(this.getEditorId());
			activityView.setViewType(this.getViewType());
		}
		return new Object[]{ddg};
	}

	@Override
	@ServiceMethod(callByContent = true, eventBinding = EventContext.EVENT_DBLCLICK, target = ServiceMethodContext.TARGET_POPUP)
	public Object showProperty() throws Exception {
		PropertySettingDialog propertySettingDialog = new PropertySettingDialog(this);
		((Pool) propertySettingDialog.getElementView().getElement()).setViewId(propertySettingDialog.getElementView().getId());

		return propertySettingDialog;
	}
}
