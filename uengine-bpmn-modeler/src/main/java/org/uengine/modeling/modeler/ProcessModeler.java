package org.uengine.modeling.modeler;

import java.util.ArrayList;
import java.util.List;

import org.metaworks.MetaworksContext;
import org.uengine.contexts.TextContext;
import org.uengine.kernel.Activity;
import org.uengine.kernel.ProcessDefinition;
import org.uengine.kernel.Role;
import org.uengine.kernel.UEngineException;
import org.uengine.kernel.bpmn.SequenceFlow;
import org.uengine.kernel.bpmn.view.SequenceFlowView;
import org.uengine.modeling.*;
import org.uengine.modeling.modeler.palette.SimplePalette;

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
				elementView.setElement(null);
				activity.setName(elementView.getLabel());
				activity.setElementView(elementView);
				def.addChildActivity(activity);
			}
				
		}
		
		for(RelationView relationView : this.getCanvas().getRelationViewList()){
			SequenceFlow sequenceFlow = (SequenceFlow) relationView.getRelation();
			relationView.setRelation(null);
			sequenceFlow.setRelationView((SequenceFlowView)relationView);
			def.addSequenceFlow(sequenceFlow);
		}
		def.setPools(null);
		return def;
	}

	
}
