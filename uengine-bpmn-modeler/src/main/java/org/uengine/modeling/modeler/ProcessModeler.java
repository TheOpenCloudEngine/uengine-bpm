package org.uengine.modeling.modeler;

import org.metaworks.MetaworksContext;
import org.uengine.contexts.TextContext;
import org.uengine.kernel.*;
import org.uengine.kernel.bpmn.*;
import org.uengine.kernel.bpmn.face.ProcessVariablePanel;
import org.uengine.kernel.bpmn.face.RolePanel;
import org.uengine.kernel.bpmn.view.EventView;
import org.uengine.kernel.bpmn.view.PoolView;
import org.uengine.kernel.bpmn.view.SequenceFlowView;
import org.uengine.kernel.view.ActivityView;
import org.uengine.modeling.*;
import org.uengine.modeling.modeler.palette.SimplePalette;
import org.uengine.util.ActivityFor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class ProcessModeler extends DefaultModeler {

	public final static String SUFFIX = ".process";

//	public RolePanel getRolePanel() {
//		try {
//			return ((SimplePalette) getPalette()).getRolePalette().getRolePanel();
//		}catch (Exception e){
//			return null;
//		}
//	}

	public ProcessVariablePanel getProcessVariablePanel() {
		try {
			return ((SimplePalette) getPalette()).getProcessVariablePalette().getProcessVariablePanel();
		}catch (Exception e){
			return null;
		}
	}
//	public void setProcessVariablePanel(ProcessVariablePanel processVariablePanel) {
//		this.processVariablePanel = processVariablePanel;
//	}

	public ProcessModeler() {
		setType(SUFFIX);
		this.setCanvas(new ProcessCanvas(getType()));
		this.setPalette(new SimplePalette(getType()));
//		this.setRolePanel(new RolePanel());
//		this.setProcessVariablePanel(new ProcessVariablePanel());

		setMetaworksContext(new MetaworksContext());
		getMetaworksContext().setWhen(MetaworksContext.WHEN_NEW);
	}


//	@ServiceMethod(inContextMenu = true, target = ServiceMethodContext.TARGET_POPUP)
//	public ModalWindow openRolePanel(){
//		return new ModalWindow(getRolePanel());
//	}

//	public void setModelForMonitor(IModel model, final ProcessInstance insatnce) throws Exception {
//		if (model == null)
//			return;
//
//		ProcessDefinition def = (ProcessDefinition) GlobalContext.deserialize(GlobalContext.serialize(model, String.class), String.class);
//
//		final List<ElementView> elementViewList = new ArrayList<ElementView>();
//		List<RelationView> relationViewList = new ArrayList<RelationView>();
//
//		/**
//		 * on Load ProcessDefinition
//		 * if Acitivity is SubProcesss, get ChildActvities and adding to elementViewList
//		 */
//		ActivityFor addingElemenViewLoop = new ActivityFor() {
//
//			@Override
//			public void logic(Activity activity) {
//				ElementView elementView = activity.getElementView();
//
//				activity.setElementView(null); //prevent cyclic reference
//				elementView.setElement(activity);
//				try {
//					elementView.setInstStatus(insatnce.getStatus(activity.getTracingTag()));
//				} catch (Exception e) {
//					e.printStackTrace();
//				}
//
//				elementViewList.add(elementView);
//			}
//
//		};
//
//		for (Activity activity : def.getChildActivities()) {
//
//			addingElemenViewLoop.run(activity);
//
//			if (activity instanceof SubProcess) {
//				ArrayList<SequenceFlow> sequenceFlowList = ((SubProcess) activity).getSequenceFlows();
//
//				for (IRelation relation : sequenceFlowList) {
//
//					SequenceFlow sequenceFlow = (SequenceFlow) relation;
//					SequenceFlowView sequenceFlowView = (SequenceFlowView) sequenceFlow.getRelationView();
//					sequenceFlow.setRelationView(null);
//					sequenceFlowView.setRelation(sequenceFlow);
//					relationViewList.add(sequenceFlowView);
//				}
//			}
//
//		}
//		for (IRelation relation : def.getSequenceFlows()) {
//			SequenceFlow sequenceFlow = (SequenceFlow) relation;
//			SequenceFlowView sequenceFlowView = (SequenceFlowView) sequenceFlow.getRelationView();
//			sequenceFlow.setRelationView(null);
//			sequenceFlowView.setRelation(sequenceFlow);
//			relationViewList.add(sequenceFlowView);
//		}
//
//		if (def.getRoles() != null) {
//
//			for (Role role : def.getRoles()) {
//				if (role.getElementView() != null) {
//					ElementView elementView = role.getElementView();
//					role.setElementView(null);
//					elementView.setElement(role);
//
//					TextContext text = role.getDisplayName();
//					elementView.setLabel(text.getText());
//					elementViewList.add(elementView);
//				}
//			}
//
//		}
//
//		this.getCanvas().setElementViewList(elementViewList);
//		this.getCanvas().setRelationViewList(relationViewList);
//
//		ActivityFor loop = new ActivityFor() {
//			public int maxTT = 0;
//
//			@Override
//			public void logic(Activity activity) {
//
//				try {
//					int tt = Integer.parseInt(activity.getTracingTag());
//
//					if (tt > maxTT) {
//						maxTT = tt;
//
//						setReturnValue(maxTT);
//					}
//				} catch (Exception e) {
//				}
//			}
//
//		};
//
//		loop.run(def);
//
//		if (loop.getReturnValue() != null)
//			setLastTracingTag((int) loop.getReturnValue());
//	}

	@Override
	public void setModel(IModel model) throws Exception {
		this.setModel(model, null);
	}

	public void setModel(IModel model, final ProcessInstance insatnce) throws Exception {
		if (model == null)
			return;

		ProcessDefinition def = (ProcessDefinition) GlobalContext.deserialize(GlobalContext.serialize(model, String.class), String.class);;
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

				if(elementView==null){
					System.err.println("ElementView is not found for activity [" + activity + "]");
					//TODO: should be generated if elementView is not valid
					return;
				}

				if(insatnce != null) {
					try {
						elementView.setInstStatus(insatnce.getStatus(activity.getTracingTag()));
					} catch (Exception e) {
						e.printStackTrace();
					}
				}

				elementView.setElement(activity);
				elementViewList.add(elementView);
			}
		};

//		if(def.getRoles()!=null && getRolePanel()!=null) {
//			this.getRolePanel().setRoleList(Arrays.asList(def.getRoles()));
//		}

		if(def.getProcessVariables()!=null && getProcessVariablePanel()!=null) {
			this.getProcessVariablePanel().setProcessVariableList(Arrays.asList(def.getProcessVariables()));
		}

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

		if(def.getPools()!=null){
			for(Pool pool : def.getPools()){
				if(pool.getElementView() != null){
					ElementView elementView = pool.getElementView();
					pool.setElementView(null);
					elementView.setElement(pool);

					elementView.setLabel(pool.getDescription());
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

		if (loop.getReturnValue() != null) {
			setLastTracingTag((int) loop.getReturnValue());
		}
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

	public ProcessDefinition makeProcessDefinitionFromCanvas(Canvas canvas) throws Exception{
		ProcessDefinition def = createEmptyProcessDefinition();

		HashMap<String, String> tracingTags = new HashMap<String, String>();

//		if(getRolePanel()!=null && getRolePanel().getRoleList()!=null){
//			Role[] roles = new Role[getRolePanel().getRoleList().size()];
//			getRolePanel().getRoleList().toArray(roles);
//			def.setRoles(roles);
//		}

		if(getProcessVariablePanel()!=null && getProcessVariablePanel().getProcessVariableList()!=null){
			ProcessVariable processVariables[] = new ProcessVariable[getProcessVariablePanel().getProcessVariableList().size()];
			getProcessVariablePanel().getProcessVariableList().toArray(processVariables);
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

			if (elementView.getElement() instanceof Activity) {
				Activity activity = (Activity) elementView.getElement();
				activity.setName(elementView.getLabel());

				try {
					long tracingTagNumber = Long.parseLong(activity.getTracingTag());
					if(def.getActivitySequence() < tracingTagNumber){
						def.setActivitySequence(tracingTagNumber);
					}
				}catch (Exception e){

				}
			}

		}

		for(ElementView elementView : canvas.getElementViewList()) {

			if (elementView.getElement() instanceof Role) {

				Role[] roles = null;
				Role role = (Role) elementView.getElement();
				elementView.setElement(null);
				role.setElementView(elementView);
				role.setName(elementView.getLabel());
				role.setDisplayName(elementView.getLabel());
				if (def.getRoles() == null) {
					roles = new Role[1];
					roles[0] = role;
					def.setRoles(roles);

				} else {
					int prevLength = def.getRoles().length;
					def.addRole(role);

					//TODO: enable and introspect why in the future
//					if(prevLength == def.getRoles().length){
//						throw new UEngineException("There are duplicated names among lanes.");
//					}
				}
			}else if(elementView.getElement() instanceof Pool){

				Pool pool = (Pool) elementView.getElement();
				elementView.setElement(null);
				pool.setElementView(elementView);
				pool.setName(elementView.getLabel());
				pool.setDescription(elementView.getLabel());

				Role role = Role.forName(pool.getName());

				if(def.getPools() == null){
					List<Pool> pools = new ArrayList<Pool>();
					def.setPools(pools);
				}

				def.addPool(pool);
				def.addRole(role);

			}else if (elementView.getElement() instanceof Activity){
				Activity activity = (Activity)elementView.getElement();
				activity.setName(elementView.getLabel());
				activity.setElementView(elementView);

				FlowActivity parentActivity = findParentActivity(elementView, parentElementView);

				if(parentActivity==null)
					parentActivity = def;

				//TODO:  if tracingTag collision occurs, issue new id. This may cause undesired operation.
				if(tracingTags.containsKey(activity.getTracingTag()))
					activity.setTracingTag(""+def.getNextActivitySequence());

				parentActivity.addChildActivity(activity);
				tracingTags.put(activity.getTracingTag(), activity.getTracingTag());


				if(activity instanceof Event){
					Activity toAttachActivity = findAttachedActivity(elementView, canvas.getElementViewList());

					if(toAttachActivity!=null)
						((Event)activity).setAttachedToRef(toAttachActivity.getTracingTag());
				}

				if(activity instanceof HumanActivity){
					HumanActivity humanActivity = (HumanActivity) activity;

					Pool pool = findParentPool(elementView, canvas.getElementViewList());

					if(pool != null)
						humanActivity.setRole(Role.forName(pool.getName()));
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
					relationView.setFrom(elementView.getId() + relationView.TERMINAL_IN_OUT);
				}

				if(targetRef.equals(elementView.getId())) {

					Activity toActivity = (Activity)elementView.getElement();
					sequenceFlow.setTargetRef(toActivity.getTracingTag());
					relationView.setTo(elementView.getId() + relationView.TERMINAL_IN_OUT);
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
				if(elementView.getX() != null) {
					// elementView size
					Long element_x_min = Long.parseLong(elementView.getX()) - (Math.abs(Long.parseLong(elementView.getWidth()) / 2));
					Long element_x_max = Long.parseLong(elementView.getX()) + (Math.abs(Long.parseLong(elementView.getWidth()) / 2));
					Long element_y_min = Long.parseLong(elementView.getY()) - (Math.abs(Long.parseLong(elementView.getHeight()) / 2));
					Long element_y_max = Long.parseLong(elementView.getY()) + (Math.abs(Long.parseLong(elementView.getHeight()) / 2));

					boolean checkMinX = (element_x_min <= event_x_min) && (event_x_min <= element_x_max);
					boolean checkMaxX = (element_x_min <= event_x_max) && (event_x_max <= element_x_max);

					boolean checkMinY = (element_y_min <= event_y_min) && (event_y_min <= element_y_max);
					boolean checkMaxY = (element_y_min <= event_y_max) && (event_y_max <= element_y_max);

					if ((checkMinX || checkMaxX) && (checkMinY || checkMaxY) && elementView.getElement() instanceof Activity) {
						return (Activity) elementView.getElement();
					}
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

			if(elementView.getX() != null) {
				long p_x = Long.parseLong(elementView.getX());
				long p_y = Long.parseLong(elementView.getY());
				long p_width = Long.parseLong(elementView.getWidth());
				long p_height = Long.parseLong(elementView.getHeight());
				long p_leftLine = p_x - p_width / 2;
				long p_rightLine = p_x + p_width / 2;
				long p_topLine = p_y + p_height / 2;
				long p_bottomLine = p_y - p_height / 2;

				if (p_leftLine < leftLine &&
						p_rightLine > rightLine &&
						p_topLine > topLine &&
						p_bottomLine < bottomLine
						) { //TODO
					return (FlowActivity) elementView.getElement(); //I'm your father..
				}
			}
		}

		return null;
	}

	public Pool findParentPool(ElementView elementView, List<ElementView> elementViewList){
		for(ElementView ev : elementViewList){
			if(!(ev instanceof PoolView)){
				continue;
			}

			long x = Long.parseLong(elementView.getX());
			long y = Long.parseLong(elementView.getY());
			long width = Long.parseLong(elementView.getWidth());
			long height = Long.parseLong(elementView.getHeight());
			long left = x - (width/2);
			long right = x + (width/2);
			long top = y - (height/2);
			long bottom = y + (height/2);

			long p_x = Long.parseLong(ev.getX());
			long p_y = Long.parseLong(ev.getY());
			long p_width = Long.parseLong(ev.getWidth());
			long p_height = Long.parseLong(ev.getHeight());
			long p_left = p_x - (p_width/2);
			long p_right = p_x + (p_width/2);
			long p_top = p_y - (p_height/2);
			long p_bottom = p_y + (p_height/2);

			if(p_left < left &&
					p_right > right &&
					p_top < top &&
					p_bottom > bottom
					){ //TODO
				return (Pool) ev.getElement();
			}
		}

		return null;
	}

	public ElementView findConnectedEvent(ElementView elementView, List<RelationView> relationViewList, List<ElementView> elementViewList){

		String id = null;

		for(RelationView relationView : relationViewList){
			for(String toedge : elementView.getToEdge().split(",")){
				if(toedge.equals(relationView.getId())){
					id = relationView.getTo().split("_TERMINAL")[0];
					break;
				}
			}
		}

		for(ElementView ev : elementViewList){
			if((ev.getId() != null) && (ev.getId().equals(id))){
				return ev;
			}
		}

		return null;
	}

}
