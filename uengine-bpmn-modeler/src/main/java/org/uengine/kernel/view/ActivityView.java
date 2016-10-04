package org.uengine.kernel.view;

import org.metaworks.EventContext;
import org.metaworks.ServiceMethodContext;
import org.metaworks.annotation.AutowiredFromClient;
import org.metaworks.annotation.Available;
import org.metaworks.annotation.ServiceMethod;
import org.metaworks.dwr.MetaworksRemoteService;
import org.metaworks.widget.Label;
import org.metaworks.widget.Popup;
import org.uengine.kernel.Activity;
import org.uengine.kernel.HumanActivity;
//import org.uengine.kernel.ReferenceActivity;
import org.uengine.kernel.ProcessInstance;
import org.uengine.kernel.bpmn.face.ProcessVariablePanel;
import org.uengine.modeling.ElementView;
import org.uengine.modeling.IElement;
import org.uengine.modeling.PropertySettingDialog;
import org.uengine.modeling.Symbol;
import org.uengine.modeling.modeler.palette.BPMNPalette;
import org.uengine.modeling.modeler.palette.TaskPalette;
import org.uengine.processmanager.ProcessManagerRemote;
import org.uengine.util.UEngineUtil;

import java.util.HashMap;
import java.util.Map;

public class ActivityView extends ElementView {

	public final static String SHAPE_ID = "OG.shape.bpmn.A_Task";
	
	public ActivityView() {
		setShapeId(SHAPE_ID);
	}
	
	public ActivityView(IElement element){
		super(element);
	}

	@Override
	public Symbol createSymbol() {
		Symbol symbol = new Symbol();

		symbol.setElementClassName(UEngineUtil.getDomainClassName(getClass(), "view"));

		try {
			Activity activityInstance = (Activity) Class.forName(symbol.getElementClassName()).newInstance();

			symbol.setName(activityInstance.getName());
			symbol.setShapeId(getShapeId());
			symbol.setHeight(100);
			symbol.setWidth(100);
			symbol.setShapeType("GEOM");

			return symbol;


		} catch (Exception e) {
			throw new RuntimeException("Failed to create symbol", e);
		}

	}
	
	public Activity getRealActivity(){
		Activity activity = null;
//		if(getElement() instanceof ReferenceActivity){
//			activity = ((ReferenceActivity)getElement()).getReferencedActivity();
//		} else {
			activity = (Activity)getElement();
//		}
		return activity;
	}

	@Override
	public void setElement(IElement element) {
		if(element!=null && element instanceof Activity){
			Activity activity = (Activity) element;
//			setLabel(activity.getName());
			//setId(activity.getTracingTag());
		}

		super.setElement(element);
	}

//	@ServiceMethod(target=ServiceMethod.TARGET_POPUP, mouseBinding = "click", inContextMenu = true)
//	public void popup() {
//		MetaworksRemoteService.wrapReturn(new Popup(new TaskPalette()));
//	}


	@ServiceMethod(callByContent = true, eventBinding = EventContext.EVENT_DBLCLICK, target = ServiceMethodContext.TARGET_POPUP)
	public Object showProperty(@AutowiredFromClient ProcessVariablePanel processVariablePanel) throws Exception {

		return showProperty();
	}


//	@ServiceMethod(callByContent = true, inContextMenu = true)
//	@Available(condition = "mw3.getAutowiredObject('org.uengine.social.InstanceMonitorPanel')")
//	public void setBreakpoint(@AutowiredFromClient(payload = "instanceId") IInstanceMonitor instanceMonitorPanel) throws Exception {
//		String instanceId = instanceMonitorPanel.getInstanceId();
//
//		ProcessManagerRemote processManager = MetaworksRemoteService.getComponent(ProcessManagerRemote.class);
//
//		ProcessInstance instance = processManager.getProcessInstance(String.valueOf(instanceId));
//
//		HashMap<String, String> breakpoints = (HashMap<String, String>) instance.getProperty("", "breakpoints");
//
//		if(breakpoints==null)
//			breakpoints = new HashMap<String, String>();
//
//		Activity activity = ((Activity)getElement());
//
//		boolean isBreakpoint = instance.getBreakpoint(activity.getTracingTag());
//
//		//toggle
//		instance.setBreakpoint(activity.getTracingTag(), !isBreakpoint);
//		setBreakpoint(!isBreakpoint);
//
//	}
//
//	boolean isBreakpoint;
//		public boolean isBreakpoint() {
//			return isBreakpoint;
//		}
//		public void setBreakpoint(boolean isBreakpoint) {
//			this.isBreakpoint = isBreakpoint;
//		}


	@ServiceMethod(inContextMenu = true)
	public void pause() throws Exception {
	}

	@ServiceMethod(inContextMenu = true)
	public void resume() throws Exception {
	}

	@ServiceMethod(inContextMenu = true)
	public void stop() throws Exception {
	}

}
