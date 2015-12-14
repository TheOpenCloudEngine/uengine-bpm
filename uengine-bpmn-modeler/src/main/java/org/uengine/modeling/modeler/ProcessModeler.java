package org.uengine.modeling.modeler;

import com.sun.tools.javac.comp.Flow;
import org.metaworks.annotation.AutowiredToClient;
import org.metaworks.annotation.Hidden;
import org.uengine.contexts.TextContext;
import org.uengine.kernel.*;
import org.uengine.kernel.bpmn.*;
import org.uengine.kernel.bpmn.face.ProcessVariablePanel;
import org.uengine.kernel.bpmn.view.EventView;
import org.uengine.kernel.bpmn.view.PoolView;
import org.uengine.kernel.bpmn.view.SequenceFlowView;
import org.uengine.kernel.view.ActivityView;
import org.uengine.kernel.view.RoleView;
import org.uengine.modeling.*;
import org.uengine.modeling.modeler.palette.BPMNPalette;
import org.uengine.util.ActivityFor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class ProcessModeler extends DefaultModeler {

	public final static String SUFFIX = ".process";

	ElementViewActionDelegate elementViewActionDelegate;
		@Hidden
		@AutowiredToClient
		public ElementViewActionDelegate getElementViewActionDelegate() {
			return elementViewActionDelegate;
		}
		public void setElementViewActionDelegate(ElementViewActionDelegate elementViewActionDelegate) {
			this.elementViewActionDelegate = elementViewActionDelegate;
		}


//	public RolePanel getRolePanel() {
//		try {
//			return ((SimplePalette) getPalette()).getRolePalette().getRolePanel();
//		}catch (Exception e){
//			return null;
//		}
//	}

	public ProcessVariablePanel getProcessVariablePanel() {
		try {
			return ((BPMNPalette) getPalette()).getProcessVariablePalette().getProcessVariablePanel();
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
		this.setPalette(new BPMNPalette(getType()));
//		this.setRolePanel(new RolePanel());
//		this.setProcessVariablePanel(new ProcessVariablePanel());

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

	public void setModel(IModel model, final ProcessInstance instance) throws Exception {
		if (model == null)
			return;


		if(instance==null)
			setElementViewActionDelegate(new DefaultElementViewActionDelegate());


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

				if(instance != null) {
					try {
						elementView.setInstStatus(instance.getStatus(activity.getTracingTag()));
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

					if(sequenceFlowView!=null) {
						sequenceFlowView.setRelation(sequenceFlow);
						relationViewList.add(sequenceFlowView);
					}
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

				//Role role = Role.forName(pool.getName());

				if(def.getPools() == null){
					List<Pool> pools = new ArrayList<Pool>();
					def.setPools(pools);
				}

				def.addPool(pool);
				//def.addRole(role);

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

					//Pool pool = findParentPool(elementView, canvas.getElementViewList());
					Role role = findParentRole(elementView, canvas.getElementViewList());

					if(role != null)
						humanActivity.setRole(Role.forName(role.getName()));
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
		double event_x_min = (eventView.getX()) - (Math.abs((eventView.getWidth()) / 2));
		double event_x_max = (eventView.getX()) + (Math.abs((eventView.getWidth()) / 2));
		double event_y_min = (eventView.getY()) - (Math.abs((eventView.getHeight()) / 2));
		double event_y_max = (eventView.getY()) + (Math.abs((eventView.getHeight()) / 2));

		for(ElementView elementView : elementViews) {
			if (!(elementView instanceof EventView)) {
				//if(elementView.getX() != null) {
					// elementView size
					double element_x_min = (elementView.getX()) - (Math.abs((elementView.getWidth()) / 2));
					double element_x_max = (elementView.getX()) + (Math.abs((elementView.getWidth()) / 2));
					double element_y_min = (elementView.getY()) - (Math.abs((elementView.getHeight()) / 2));
					double element_y_max = (elementView.getY()) + (Math.abs((elementView.getHeight()) / 2));

					boolean checkMinX = (element_x_min <= event_x_min) && (event_x_min <= element_x_max);
					boolean checkMaxX = (element_x_min <= event_x_max) && (event_x_max <= element_x_max);

					boolean checkMinY = (element_y_min <= event_y_min) && (event_y_min <= element_y_max);
					boolean checkMaxY = (element_y_min <= event_y_max) && (event_y_max <= element_y_max);

					if ((checkMinX || checkMaxX) && (checkMinY || checkMaxY) && elementView.getElement() instanceof Activity) {
						return (Activity) elementView.getElement();
					}
				//}
			}
		}
		return null;
	}

	private FlowActivity findParentActivity(Object what, List<ElementView> parentElementView) {

		double maxParentSize = 999999999; //big enough value
		FlowActivity candidateParent = null;

		for(ElementView elementView : parentElementView){

			if(!(elementView instanceof ActivityView))
				continue;

			double x = 0;
			double y = 0;
			double width = 0;
			double height = 0;
			double leftLine = 0;
			double rightLine = 0;
			double topLine = 0;
			double bottomLine = 0;

			if(what instanceof ElementView) {
				ElementView activityView = (ElementView)what;

				x = (activityView.getX());
				y = (activityView.getY());
				width = (activityView.getWidth());
				height = (activityView.getHeight());

				leftLine = x - width/2;
				rightLine = x + width/2;
				topLine = y + height/2;
				bottomLine = y - height/2;

			}else if(what instanceof RelationView){
				RelationView relationView = (RelationView)what;

				x = (relationView.getX());
				y = (relationView.getY());
				width = (relationView.getWidth());
				height = (relationView.getHeight());

				leftLine = x - width/2;
				rightLine = x + width/2;
				topLine = y + height/2;
				bottomLine = y - height/2;
			}else{
				continue;
			}

			//if(elementView.getX() != null) {
				double p_x = (elementView.getX());
			double p_y = (elementView.getY());
			double p_width = (elementView.getWidth());
			double p_height = (elementView.getHeight());
			double p_leftLine = p_x - p_width / 2;
			double p_rightLine = p_x + p_width / 2;
			double p_topLine = p_y + p_height / 2;
			double p_bottomLine = p_y - p_height / 2;

				if (p_leftLine < leftLine &&
						p_rightLine > rightLine &&
						p_topLine > topLine &&
						p_bottomLine < bottomLine
						) { //if the activity is in the parent

					double parentSize = (p_rightLine - p_leftLine) * (p_topLine - p_bottomLine);

					if(parentSize < maxParentSize) { //the smallest one is just the parent not grand parent.
						candidateParent = (FlowActivity) elementView.getElement();
						maxParentSize = parentSize;
					}
				}
			//}
		}

		return candidateParent;
	}

	public Pool findParentPool(ElementView elementView, List<ElementView> elementViewList){
		for(ElementView ev : elementViewList){
			if(!(ev instanceof PoolView)){
				continue;
			}

			if(isIn(elementView, ev)){ //TODO
				return (Pool) ev.getElement();
			}
		}

		return null;
	}

	public Role findParentRole(ElementView elementView, List<ElementView> elementViewList){
		for(ElementView ev : elementViewList){
			if(!(ev instanceof RoleView)){
				continue;
			}

			if(isIn(elementView, ev)){ //TODO
				return (Role) ev.getElement();
			}
		}

		return null;
	}

	private boolean isIn(ElementView elem1, ElementView elem2){
		double x = (elem1.getX());
		double y = (elem1.getY());
		double width = (elem1.getWidth());
		double height = (elem1.getHeight());
		double left = x - (width/2);
		double right = x + (width/2);
		double top = y - (height/2);
		double bottom = y + (height/2);

		double p_x = (elem2.getX());
		double p_y = (elem2.getY());
		double p_width = (elem2.getWidth());
		double p_height = (elem2.getHeight());
		double p_left = p_x - (p_width/2);
		double p_right = p_x + (p_width/2);
		double p_top = p_y - (p_height/2);
		double p_bottom = p_y + (p_height/2);

		return (p_left < left &&
				p_right > right &&
				p_top < top &&
				p_bottom > bottom
				);
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
