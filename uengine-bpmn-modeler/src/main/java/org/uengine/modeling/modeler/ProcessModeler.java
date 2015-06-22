package org.uengine.modeling.modeler;

import org.metaworks.MetaworksContext;
import org.metaworks.ServiceMethodContext;
import org.metaworks.annotation.AutowiredFromClient;
import org.metaworks.annotation.AutowiredToClient;
import org.metaworks.annotation.Hidden;
import org.metaworks.annotation.ServiceMethod;
import org.metaworks.widget.ModalWindow;
import org.uengine.contexts.TextContext;
import org.uengine.kernel.*;
import org.uengine.kernel.bpmn.Event;
import org.uengine.kernel.bpmn.FlowActivity;
import org.uengine.kernel.bpmn.SequenceFlow;
import org.uengine.kernel.bpmn.face.ProcessVariablePanel;
import org.uengine.kernel.bpmn.face.RolePanel;
import org.uengine.kernel.bpmn.SubProcess;
import org.uengine.kernel.bpmn.view.EventView;
import org.uengine.kernel.bpmn.view.SequenceFlowView;
import org.uengine.kernel.view.ActivityView;
import org.uengine.modeling.*;
import org.uengine.modeling.modeler.palette.SimplePalette;
import org.uengine.util.ActivityFor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ProcessModeler extends DefaultModeler {

	public final static String SUFFIX = ".process";

	public ProcessModeler() {
		setType(SUFFIX);
		this.setCanvas(new ProcessCanvas(getType()));
		Palette palette = new SimplePalette(getType());
		this.setPalette(palette);

//		setRolePanel(new RolePanel());

		setMetaworksContext(new MetaworksContext());
		getMetaworksContext().setWhen(MetaworksContext.WHEN_NEW);
	}

//	RolePanel rolePanel;
////	@Hidden
//	@AutowiredToClient
//		public RolePanel getRolePanel() {
//			return rolePanel;
//		}
//		public void setRolePanel(RolePanel rolePanel) {
//			this.rolePanel = rolePanel;
//		}

//	@ServiceMethod(inContextMenu = true, target = ServiceMethodContext.TARGET_POPUP)
//	public ModalWindow openRolePanel(){
//		return new ModalWindow(getRolePanel());
//	}

	public void setModelForMonitor(IModel model, final ProcessInstance insatnce) throws Exception {
		if (model == null)
			return;

		ProcessDefinition def = (ProcessDefinition) GlobalContext.deserialize(GlobalContext.serialize(model, String.class), String.class);

		final List<ElementView> elementViewList = new ArrayList<ElementView>();
		List<RelationView> relationViewList = new ArrayList<RelationView>();

		/**
		 * on Load ProcessDefinition
		 * if Acitivity is SubProcesss, get ChildActvities and adding to elementViewList
		 */
		ActivityFor addingElemenViewLoop = new ActivityFor() {

			@Override
			public void logic(Activity activity) {
				ElementView elementView = activity.getElementView();

				activity.setElementView(null); //prevent cyclic reference
				elementView.setElement(activity);
				try {
					elementView.setInstStatus(insatnce.getStatus(activity.getTracingTag()));
				} catch (Exception e) {
					e.printStackTrace();
				}

				elementViewList.add(elementView);
			}

		};

		for (Activity activity : def.getChildActivities()) {

			addingElemenViewLoop.run(activity);

			if (activity instanceof SubProcess) {
				ArrayList<SequenceFlow> sequenceFlowList = ((SubProcess) activity).getSequenceFlows();

				for (IRelation relation : sequenceFlowList) {

					SequenceFlow sequenceFlow = (SequenceFlow) relation;
					SequenceFlowView sequenceFlowView = (SequenceFlowView) sequenceFlow.getRelationView();
					sequenceFlow.setRelationView(null);
					sequenceFlowView.setRelation(sequenceFlow);
					relationViewList.add(sequenceFlowView);
				}
			}

		}
		for (IRelation relation : def.getSequenceFlows()) {
			SequenceFlow sequenceFlow = (SequenceFlow) relation;
			SequenceFlowView sequenceFlowView = (SequenceFlowView) sequenceFlow.getRelationView();
			sequenceFlow.setRelationView(null);
			sequenceFlowView.setRelation(sequenceFlow);
			relationViewList.add(sequenceFlowView);
		}

		if (def.getRoles() != null) {

			for (Role role : def.getRoles()) {
				if (role.getElementView() != null) {
					ElementView elementView = role.getElementView();
					role.setElementView(null);
					elementView.setElement(role);

					TextContext text = role.getDisplayName();
					elementView.setLabel(text.getText());
					elementViewList.add(elementView);
				}
			}

		}

		this.getCanvas().setElementViewList(elementViewList);
		this.getCanvas().setRelationViewList(relationViewList);

		ActivityFor loop = new ActivityFor() {
			public int maxTT = 0;

			@Override
			public void logic(Activity activity) {

				try {
					int tt = Integer.parseInt(activity.getTracingTag());

					if (tt > maxTT) {
						maxTT = tt;

						setReturnValue(maxTT);
					}
				} catch (Exception e) {
				}
			}

		};

		loop.run(def);

		if (loop.getReturnValue() != null)
			setLastTracingTag((int) loop.getReturnValue());
	}

	@Override
	public void setModel(IModel model) throws Exception {

		if(model==null)
			return;

		ProcessDefinition def = (ProcessDefinition)model;

		if(def.getRoles()!=null)
			((SimplePalette)getPalette()).getRolePanel().setRoleList(Arrays.asList(def.getRoles()));

		if(def.getProcessVariables()!=null)
			((SimplePalette)getPalette()).getProcessVariablePanel().setProcessVariableList(Arrays.asList(def.getProcessVariables()));

		final List<ElementView> elementViewList = new ArrayList<ElementView>();
		List<RelationView> relationViewList = new ArrayList<RelationView>();


//		for(IElement element : def.getPools()){
//			ElementView elementView = element.getElementView();
//			element.setElementView(null); //prevent cyclic reference
//			elementView.setElement(element);
//
//			elementViewList.add(elementView);
//		}

		/**
		 * on Load ProcessDefinition
		 * if Acitivity is SubProcesss, get ChildActvities and adding to elementViewList
		 */
		ActivityFor addingElemenViewLoop = new ActivityFor(){

			@Override
			public void logic(Activity activity) {
				ElementView elementView = activity.getElementView();

				activity.setElementView(null); //prevent cyclic reference

				if(elementView==null){

					System.err.println("ElementView is not found for activity [" + activity + "]");
					//TODO: should be generated if elementView is not valid
					return;
				}


				elementView.setElement(activity);

				elementViewList.add(elementView);

			}

		};

		for(Activity activity: def.getChildActivities()) {

			addingElemenViewLoop.run(activity);

			if(activity instanceof SubProcess) {
				ArrayList<SequenceFlow> sequenceFlowList = ((SubProcess) activity).getSequenceFlows();

				for(IRelation relation: sequenceFlowList) {

					SequenceFlow sequenceFlow = (SequenceFlow) relation;
					SequenceFlowView sequenceFlowView = (SequenceFlowView) sequenceFlow.getRelationView();
					sequenceFlow.setRelationView(null);
					sequenceFlowView.setRelation(sequenceFlow);
					relationViewList.add(sequenceFlowView);
				}
			}

		}

//		for(IElement element : def.getChildActivities()){
//			ElementView elementView = element.getElementView();
//
//			if (elementView.getElement() instanceof FlowActivity){
//				FlowActivity parentActivity = (FlowActivity) elementView.getElement();
//				List<Activity> list = parentActivity.getChildActivities();
//				for(Activity activity: list) {
//					elementViewList.add(activity.getElementView());
//				}
//			}
//
//			element.setElementView(null); //prevent cyclic reference
//			elementView.setElement(element);
//
//			elementViewList.add(elementView);
//		}
//
		for(IRelation relation : def.getSequenceFlows()){
			SequenceFlow sequenceFlow = (SequenceFlow) relation;
			SequenceFlowView sequenceFlowView = (SequenceFlowView) sequenceFlow.getRelationView();
			sequenceFlow.setRelationView(null);

			if(sequenceFlowView==null){

				//TODO: view should be generated if null
				continue;
			}

			sequenceFlowView.setRelation(sequenceFlow);
			relationViewList.add(sequenceFlowView);
		}

		if(def.getRoles()!=null){

			for(Role role : def.getRoles()){
				if(role.getElementView() != null){
					ElementView elementView = role.getElementView();
					role.setElementView(null);
					elementView.setElement(role);

					TextContext text = role.getDisplayName();
					elementView.setLabel(text.getText());
					elementViewList.add(elementView);
				}
			}

		}

		this.getCanvas().setElementViewList(elementViewList);
		this.getCanvas().setRelationViewList(relationViewList);

		ActivityFor loop = new ActivityFor(){
			public int maxTT = 0;
			@Override
			public void logic(Activity activity) {

				try{
					int tt = Integer.parseInt(activity.getTracingTag());

					if(tt > maxTT){
						maxTT = tt;

						setReturnValue(maxTT);
					}
				}catch(Exception e){}
			}

		};

		loop.run(def);

		if(loop.getReturnValue()!=null)
			setLastTracingTag((int)loop.getReturnValue());


	}



	public void setModelForMonitor(IModel model, final ProcessInstance insatnce) throws Exception {
		if (model == null)
			return;

		ProcessDefinition def = (ProcessDefinition) GlobalContext.deserialize(GlobalContext.serialize(model, String.class), String.class);

		final List<ElementView> elementViewList = new ArrayList<ElementView>();
		List<RelationView> relationViewList = new ArrayList<RelationView>();

		/**
		 * on Load ProcessDefinition
		 * if Acitivity is SubProcesss, get ChildActvities and adding to elementViewList
		 */
		ActivityFor addingElemenViewLoop = new ActivityFor() {

			@Override
			public void logic(Activity activity) {
				ElementView elementView = activity.getElementView();

				activity.setElementView(null); //prevent cyclic reference
				elementView.setElement(activity);
				try {
					elementView.setInstStatus(insatnce.getStatus(activity.getTracingTag()));
				} catch (Exception e) {
					e.printStackTrace();
				}

				elementViewList.add(elementView);
			}

		};

		for (Activity activity : def.getChildActivities()) {

			addingElemenViewLoop.run(activity);

			if (activity instanceof SubProcess) {
				ArrayList<SequenceFlow> sequenceFlowList = ((SubProcess) activity).getSequenceFlows();

				for (IRelation relation : sequenceFlowList) {

					SequenceFlow sequenceFlow = (SequenceFlow) relation;
					SequenceFlowView sequenceFlowView = (SequenceFlowView) sequenceFlow.getRelationView();
					sequenceFlow.setRelationView(null);
					sequenceFlowView.setRelation(sequenceFlow);
					relationViewList.add(sequenceFlowView);
				}
			}

		}
		for (IRelation relation : def.getSequenceFlows()) {
			SequenceFlow sequenceFlow = (SequenceFlow) relation;
			SequenceFlowView sequenceFlowView = (SequenceFlowView) sequenceFlow.getRelationView();
			sequenceFlow.setRelationView(null);
			sequenceFlowView.setRelation(sequenceFlow);
			relationViewList.add(sequenceFlowView);
		}

		if (def.getRoles() != null) {

			for (Role role : def.getRoles()) {
				if (role.getElementView() != null) {
					ElementView elementView = role.getElementView();
					role.setElementView(null);
					elementView.setElement(role);

					TextContext text = role.getDisplayName();
					elementView.setLabel(text.getText());
					elementViewList.add(elementView);
				}
			}

		}

		this.getCanvas().setElementViewList(elementViewList);
		this.getCanvas().setRelationViewList(relationViewList);

		ActivityFor loop = new ActivityFor() {
			public int maxTT = 0;

			@Override
			public void logic(Activity activity) {

				try {
					int tt = Integer.parseInt(activity.getTracingTag());

					if (tt > maxTT) {
						maxTT = tt;

						setReturnValue(maxTT);
					}
				} catch (Exception e) {
				}
			}

		};

		loop.run(def);

		if (loop.getReturnValue() != null)
			setLastTracingTag((int) loop.getReturnValue());


	}


	protected void updateLastTracingTag(String tracingTag){
		int compareTracingTag = Integer.parseInt(tracingTag);

		if(this.getLastTracingTag() < compareTracingTag)
			this.setLastTracingTag(compareTracingTag);
	}

	public IModel createModel() {
		try {
			return makeProcessDefinitionFromCanvas(getCanvas());
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}


	@AutowiredFromClient public RolePanel rolePanel;
	@AutowiredFromClient public ProcessVariablePanel processVariablePanel;

	public ProcessDefinition makeProcessDefinitionFromCanvas(Canvas canvas) throws Exception{
		ProcessDefinition def = createEmptyProcessDefinition();

		if(rolePanel!=null && rolePanel.getRoleList()!=null){
			Role[] roles = new Role[rolePanel.getRoleList().size()];
			rolePanel.getRoleList().toArray(roles);
			def.setRoles(roles);
		}

		if(processVariablePanel!=null && processVariablePanel.getProcessVariableList()!=null){
			ProcessVariable processVariables[] = new ProcessVariable[processVariablePanel.getProcessVariableList().size()];
			processVariablePanel.getProcessVariableList().toArray(processVariables);
			def.setProcessVariables(processVariables);
		}


		List<ElementView> parentElementView = new ArrayList<ElementView>();

		for(ElementView elementView : canvas.getElementViewList()){

			parentElementView.add(elementView);

			//child activities in the getElement() should be removed before rearranging them
			if(elementView.getElement() instanceof FlowActivity){
				FlowActivity flowActivity = ((FlowActivity) elementView.getElement());

				if(flowActivity.getChildActivities()!=null)
					flowActivity.getChildActivities().clear();
			}

		}

		for(ElementView elementView : canvas.getElementViewList()){

			if(elementView.getElement() instanceof Role){

				Role[] roles = null;
				Role role = (Role) elementView.getElement();
				elementView.setElement(null);
				role.setElementView(elementView);
				role.setName(elementView.getLabel());
				role.setDisplayName(elementView.getLabel());
				if(def.getRoles() == null){
					roles = new Role[1];
					roles[0] = role;
					def.setRoles(roles);

				}else{
					int prevLength = def.getRoles().length;
					def.addRole(role);

					//TODO: enable and introspect why in the future
//					if(prevLength == def.getRoles().length){
//						throw new UEngineException("There are duplicated names among lanes.");
//					}
				}
			}else if (elementView.getElement() instanceof Activity){
				Activity activity = (Activity)elementView.getElement();
				activity.setName(elementView.getLabel());
				activity.setElementView(elementView);

				FlowActivity parentActivity = findParentActivity(elementView, parentElementView);

				if(parentActivity==null)
					parentActivity = def;

				parentActivity.addChildActivity(activity);

				if(activity instanceof Event){
					Activity toAttachActivity = findAttachedActivity(elementView, canvas.getElementViewList());

					if(toAttachActivity!=null)
						((Event)activity).setAttachedToRef(toAttachActivity.getTracingTag());
				}
			}

		}

		for(RelationView relationView : this.getCanvas().getRelationViewList()){
			SequenceFlow sequenceFlow = (SequenceFlow) relationView.getRelation();

			//TODO: fix later
			if(sequenceFlow == null)
				continue;

			String sourceRef = relationView.getFrom().substring(0, relationView.getFrom().indexOf("_TERMINAL_"));
			String targetRef = relationView.getTo().substring(0, relationView.getTo().indexOf("_TERMINAL_"));

			for(ElementView elementView : this.getCanvas().getElementViewList()){
				if(sourceRef.equals(elementView.getId())) {

					Activity fromActivity = (Activity)elementView.getElement();
					sequenceFlow.setSourceRef(fromActivity.getTracingTag());
					relationView.setFrom(fromActivity.getTracingTag() + relationView.TERMINAL_IN_OUT);
				}

				if(targetRef.equals(elementView.getId())) {

					Activity toActivity = (Activity)elementView.getElement();
					sequenceFlow.setTargetRef(toActivity.getTracingTag());
					relationView.setTo(toActivity.getTracingTag() + relationView.TERMINAL_IN_OUT);
				}

			}

			relationView.setRelation(null);
			sequenceFlow.setRelationView((SequenceFlowView)relationView);

			FlowActivity parentActivity = findParentActivity(relationView, parentElementView);

			if(parentActivity==null)
				parentActivity = def;

			parentActivity.addSequenceFlow(sequenceFlow);

		}

		for(ElementView elementView : canvas.getElementViewList()){
			elementView.setElement(null);
		}

		def.setPools(null);
		return def;
	}

	protected ProcessDefinition createEmptyProcessDefinition() {
		return new ProcessDefinition();
	}

	private Activity findAttachedActivity(ElementView eventView, List<ElementView> elementViews) {
		// eventView size
		Long event_x_min = Long.parseLong(eventView.getX()) - (Math.abs(Long.parseLong(eventView.getWidth()) / 2));
		Long event_x_max = Long.parseLong(eventView.getX()) + (Math.abs(Long.parseLong(eventView.getWidth()) / 2));
		Long event_y_min = Long.parseLong(eventView.getY()) - (Math.abs(Long.parseLong(eventView.getHeight()) / 2));
		Long event_y_max = Long.parseLong(eventView.getY()) + (Math.abs(Long.parseLong(eventView.getHeight()) / 2));

		for(ElementView elementView : elementViews) {
			if (!(elementView instanceof EventView)) {
				// elementView size
				Long element_x_min = Long.parseLong(elementView.getX()) - (Math.abs(Long.parseLong(elementView.getWidth()) / 2));
				Long element_x_max = Long.parseLong(elementView.getX()) + (Math.abs(Long.parseLong(elementView.getWidth()) / 2));
				Long element_y_min = Long.parseLong(elementView.getY()) - (Math.abs(Long.parseLong(elementView.getHeight()) / 2));
				Long element_y_max = Long.parseLong(elementView.getY()) + (Math.abs(Long.parseLong(elementView.getHeight()) / 2));

				boolean checkMinX = (element_x_min <= event_x_min) && (event_x_min <= element_x_max);
				boolean checkMaxX = (element_x_min <= event_x_max) && (event_x_max <= element_x_max);

				boolean checkMinY = (element_y_min <= event_y_min) && (event_y_min <= element_y_max);
				boolean checkMaxY = (element_y_min <= event_y_max) && (event_y_max <= element_y_max);

				if ((checkMinX || checkMaxX) && (checkMinY || checkMaxY)) {
					return (Activity) elementView.getElement();
				}
			}
		}
		return null;
	}

	private FlowActivity findParentActivity(Object what, List<ElementView> parentElementView) {
		for(ElementView elementView : parentElementView){

			if(!(elementView instanceof ActivityView))
				continue;

			long x = 0;
			long y = 0;
			long width = 0;
			long height = 0;
			long leftLine = 0;
			long rightLine = 0;
			long topLine = 0;
			long bottomLine = 0;

			if(what instanceof ElementView) {
				ElementView activityView = (ElementView)what;

				x = Long.parseLong(activityView.getX());
				y = Long.parseLong(activityView.getY());
				width = Long.parseLong(activityView.getWidth());
				height = Long.parseLong(activityView.getHeight());

				leftLine = x - width/2;
				rightLine = x + width/2;
				topLine = y + height/2;
				bottomLine = y - height/2;

			}else if(what instanceof RelationView){
				RelationView relationView = (RelationView)what;

				x = Long.parseLong(relationView.getX());
				y = Long.parseLong(relationView.getY());
				width = Long.parseLong(relationView.getWidth());
				height = Long.parseLong(relationView.getHeight());

				leftLine = x - width/2;
				rightLine = x + width/2;
				topLine = y + height/2;
				bottomLine = y - height/2;
			}else{
				continue;
			}

			long p_x = Long.parseLong(elementView.getX());
			long p_y = Long.parseLong(elementView.getY());
			long p_width = Long.parseLong(elementView.getWidth());
			long p_height = Long.parseLong(elementView.getHeight());
			long p_leftLine = p_x - p_width/2;
			long p_rightLine = p_x + p_width/2;
			long p_topLine = p_y + p_height/2;
			long p_bottomLine = p_y - p_height/2;

			if(p_leftLine < leftLine &&
					p_rightLine > rightLine &&
					p_topLine > topLine &&
					p_bottomLine < bottomLine
					){ //TODO
				return (FlowActivity)elementView.getElement(); //I'm your father..
			}
		}

		return null;
	}


}
