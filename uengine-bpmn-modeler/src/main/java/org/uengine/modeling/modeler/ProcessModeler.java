package org.uengine.modeling.modeler;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.metaworks.MetaworksContext;
import org.uengine.contexts.TextContext;
import org.uengine.kernel.Activity;
import org.uengine.kernel.ProcessDefinition;
import org.uengine.kernel.Role;
import org.uengine.kernel.UEngineException;
import org.uengine.kernel.bpmn.Event;
import org.uengine.kernel.bpmn.FlowActivity;
import org.uengine.kernel.bpmn.SequenceFlow;
import org.uengine.kernel.bpmn.view.EventView;
import org.uengine.kernel.bpmn.view.SequenceFlowView;
import org.uengine.modeling.Canvas;
import org.uengine.modeling.DefaultModeler;
import org.uengine.modeling.ElementView;
import org.uengine.modeling.IElement;
import org.uengine.modeling.IModel;
import org.uengine.modeling.IRelation;
import org.uengine.modeling.Palette;
import org.uengine.modeling.RelationView;
import org.uengine.modeling.modeler.palette.AttributePalette;
import org.uengine.modeling.modeler.palette.ModelerPalette;
import org.uengine.modeling.modeler.palette.SimplePalette;
import org.uengine.util.ActivityFor;

public class ProcessModeler extends DefaultModeler {

	public final static String SUFFIX = ".process";

	public ProcessModeler() {
		setType(SUFFIX);
		this.setCanvas(new ProcessCanvas(getType()));
		Palette palette = new ModelerPalette(getType());
		this.setPalette(palette);

		setMetaworksContext(new MetaworksContext());
		getMetaworksContext().setWhen(MetaworksContext.WHEN_NEW);
	}

	@Override
	public void setModel(IModel model) throws Exception {
		ProcessDefinition pd = (ProcessDefinition)model;

		if(model==null){
			return;
		}

		((AttributePalette)((ModelerPalette)getPalette()).getChildPallet().get(1)).getRolePanel().setRoleList(Arrays.asList(pd.getRoles()));
		((AttributePalette)((ModelerPalette)getPalette()).getChildPallet().get(1)).getProcessVariablePanel().setProcessVariableList(Arrays.asList(pd.getProcessVariables()));

		List<ElementView> elementViewList = new ArrayList<ElementView>();
		List<RelationView> relationViewList = new ArrayList<RelationView>();

		ProcessDefinition def = (ProcessDefinition)model;


//		for(IElement element : def.getPools()){
//			ElementView elementView = element.getElementView();
//			element.setElementView(null); //prevent cyclic reference
//			elementView.setElement(element);
//
//			elementViewList.add(elementView);
//		}

		for(IElement element : def.getChildActivities()){
			ElementView elementView = element.getElementView();
			element.setElementView(null); //prevent cyclic reference
			elementView.setElement(element);

			elementViewList.add(elementView);
		}

		for(IRelation relation : def.getSequenceFlows()){
			SequenceFlow sequenceFlow = (SequenceFlow) relation;
			SequenceFlowView sequenceFlowView = (SequenceFlowView) sequenceFlow.getRelationView();
			sequenceFlow.setRelationView(null);
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
		ProcessDefinition def = new ProcessDefinition();

		List<FlowActivity> parentActivities = new ArrayList<FlowActivity>();

		for(ElementView elementView : canvas.getElementViewList()){
			if (elementView.getElement() instanceof FlowActivity){
				parentActivities.add((FlowActivity) elementView.getElement());
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
					if(prevLength == def.getRoles().length){
						throw new UEngineException("There are duplicated names among lanes.");
					}
				}
			}else if (elementView.getElement() instanceof Activity){
				Activity activity = (Activity)elementView.getElement();

				activity.setName(elementView.getLabel());
				activity.setElementView(elementView);

				FlowActivity parentActivity = findParentActivity(elementView, parentActivities);

				if(parentActivity==null)
					parentActivity = def;

				parentActivity.addChildActivity(activity);

				if(activity instanceof Event){
					Activity toAttachActivity = findAttachedActivity(elementView, canvas.getElementViewList());

					if(toAttachActivity!=null) {
						((Event) activity).setAttachedToRef(toAttachActivity.getTracingTag());
					}
				}
			}

		}

		for(RelationView relationView : this.getCanvas().getRelationViewList()){
			SequenceFlow sequenceFlow = (SequenceFlow) relationView.getRelation();


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

			FlowActivity parentActivity = findParentActivity(relationView, parentActivities);


			def.addSequenceFlow(sequenceFlow);
		}

		for(ElementView elementView : canvas.getElementViewList()){
			elementView.setElement(null);
		}

		def.setPools(null);
		return def;
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

	private FlowActivity findParentActivity(Object what, List<FlowActivity> parentActivities) {
		for(FlowActivity flowActivity : parentActivities){

			long x = 0;
			long y = 0;
			long width = 0;
			long height = 0;

			if(what instanceof ElementView) {
				ElementView activityView = (ElementView)what;

				x = Long.parseLong(activityView.getX());
				y = Long.parseLong(activityView.getY());
				width = Long.parseLong(activityView.getWidth());
				height = Long.parseLong(activityView.getHeight());
			}else if(what instanceof RelationView){
				RelationView relationView = (RelationView)what;

				x = Long.parseLong(relationView.getX());
				y = Long.parseLong(relationView.getY());
				width = Long.parseLong(relationView.getWidth());
				height = Long.parseLong(relationView.getHeight());

			}

			long p_x = Long.parseLong(flowActivity.getElementView().getX());
			long p_y = Long.parseLong(flowActivity.getElementView().getY());
			long p_width = Long.parseLong(flowActivity.getElementView().getWidth());
			long p_height = Long.parseLong(flowActivity.getElementView().getHeight());

			if(p_x < x && p_y < y){ //TODO
				return flowActivity; //I'm your father..
			}
		}

		return null;
	}


}
