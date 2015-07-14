package org.uengine.kernel.view;

import org.metaworks.EventContext;
import org.metaworks.MetaworksContext;
import org.metaworks.ServiceMethodContext;
import org.metaworks.annotation.AutowiredFromClient;
import org.metaworks.annotation.ServiceMethod;
import org.metaworks.widget.ModalWindow;
import org.uengine.contexts.MappingContext;
import org.uengine.kernel.Activity;
import org.uengine.kernel.RestWebServiceActivity;
import org.uengine.kernel.WebServiceActivity;
import org.uengine.kernel.bpmn.Pool;
import org.uengine.kernel.bpmn.view.PoolView;
import org.uengine.modeling.IElement;
import org.uengine.modeling.PropertySettingDialog;
import org.uengine.modeling.Symbol;
import org.uengine.processdesigner.ActivityWindow;
import org.uengine.webservice.MethodProperty;
import org.uengine.webservice.ResourceProperty;
import org.uengine.webservice.WebServiceDefinition;

import java.util.ArrayList;

public class WebServiceActivityView extends ActivityView {

	public final static String SHAPE_ID_BPMN = "OG.shape.bpmn.A_WebServiceTask";
	public final static String SHAPE_ID_VACD = "OG.shape.bpmn.Value_Chain_Module";
	public final static String SHAPE_TYPE 		 = "GEOM";
	public final static String ELEMENT_CLASSNAME = WebServiceActivity.class.getName();
	
	public WebServiceActivityView(){
		setShapeId(SHAPE_ID_BPMN);
	}
	
	public WebServiceActivityView(IElement element){
		super(element);
	}

	String connectedService;
	public String getConnectedService() {
		return connectedService;
	}
	public void setConnectedService(String connectedService) {
		this.connectedService = connectedService;
	}

	@ServiceMethod(callByContent = true, eventBinding = EventContext.EVENT_DBLCLICK, target = ServiceMethodContext.TARGET_POPUP)
	public Object showProperty(@AutowiredFromClient PoolView poolView) throws Exception {
		Pool pool = (Pool) poolView.getElement();
		PropertySettingDialog propertySettingDialog = new PropertySettingDialog();
		propertySettingDialog.setElementView(this);

		Activity activity = (Activity) this.getElement();
		if( activity.getMetaworksContext() == null ){
			activity.setMetaworksContext(new MetaworksContext());
			activity.getMetaworksContext().setWhen(MetaworksContext.WHEN_EDIT);
		}

		if( activity instanceof RestWebServiceActivity){
			WebServiceDefinition webServiceDefinition = pool.getPoolResolutionContext().getWebServiceConnector().getWebServiceDefinition();
			webServiceDefinition.setParentActivity(activity);
			if( connectedService != null ){
				ArrayList<ResourceProperty> rpList = webServiceDefinition.getResourceList();
				for( ResourceProperty resourceProperty : rpList){
					ArrayList<MethodProperty> mpList = resourceProperty.getMethods();
					for( MethodProperty methodProperty : mpList){
						if( connectedService.equals(methodProperty.getId())){
							webServiceDefinition.setTargetMethod(methodProperty);
							((RestWebServiceActivity) activity).setMethod(methodProperty);
							if( methodProperty.getResponseClass() != null && !"void".equalsIgnoreCase(methodProperty.getResponseClass())){
								//drawInit 전에 이루어 져야한다.
								if( ((RestWebServiceActivity) activity).getMappingContextOut() == null ){
									((RestWebServiceActivity) activity).setMappingContextOut(new MappingContext());
								}
							}
						}
					}
				}
			}
			((RestWebServiceActivity) activity).setWebServiceDefinition(webServiceDefinition);
			((RestWebServiceActivity) activity).drawInit();
		}

		propertySettingDialog.setPanel(activity);
		propertySettingDialog.setWidth(1000);
		propertySettingDialog.setHeight(700);

		return propertySettingDialog;
	}

}
