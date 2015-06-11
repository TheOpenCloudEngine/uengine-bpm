package org.uengine.modeling.modeler;

import java.util.ArrayList;
import java.util.List;

import com.sun.tools.javac.comp.Flow;
import org.metaworks.MetaworksContext;
import org.uengine.contexts.TextContext;
import org.uengine.kernel.Activity;
import org.uengine.kernel.ProcessDefinition;
import org.uengine.kernel.Role;
import org.uengine.kernel.UEngineException;
import org.uengine.kernel.bpmn.Event;
import org.uengine.kernel.bpmn.FlowActivity;
import org.uengine.kernel.bpmn.SequenceFlow;
import org.uengine.kernel.bpmn.view.SequenceFlowView;
import org.uengine.kernel.view.ActivityView;
import org.uengine.modeling.*;
import org.uengine.modeling.modeler.palette.SimplePalette;
import org.uengine.util.ActivityFor;

public class ProcessModeler extends DefaultModeler {
	
	public final static String SUFFIX = ".process";
	
	public ProcessModeler() {
		setType(SUFFIX);
		this.setCanvas(new ProcessCanvas(getType()));
		Palette palette = new SimplePalette(getType());
		this.setPalette(palette);

		setMetaworksContext(new MetaworksContext());
		getMetaworksContext().setWhen(MetaworksContext.WHEN_NEW);
	}
	
	@Override
	public void setModel(IModel model) throws Exception {
		
		if(model==null)
			return;
		
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
					Activity toAttachActivity = findAttachedActivity(activity,canvas.getElementViewList());

					if(toAttachActivity!=null)
						((Event)activity).setAttachedToRef(toAttachActivity.getTracingTag());
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
					
				elementView.setElement(null);
					
			}
			
			relationView.setRelation(null);
			sequenceFlow.setRelationView((SequenceFlowView)relationView);

			FlowActivity parentActivity = findParentActivity(relationView, parentActivities);

			def.addSequenceFlow(sequenceFlow);
		}
		
		def.setPools(null);
		return def;
	}

	//TODO: 원석 작업
	private Activity findAttachedActivity(Activity activity, List<ElementView> elementViews) {
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
