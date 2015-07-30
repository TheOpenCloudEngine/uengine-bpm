package org.uengine.kernel.bpmn;

import org.metaworks.annotation.Face;
import org.metaworks.annotation.Hidden;
import org.metaworks.annotation.Id;
import org.uengine.kernel.Activity;
import org.uengine.kernel.Condition;
import org.uengine.kernel.ProcessInstance;
import org.uengine.kernel.ScopeActivity;
import org.uengine.kernel.bpmn.face.ConditionFace;
import org.uengine.modeling.Relation;
import org.uengine.modeling.RelationView;
import org.uengine.util.UEngineUtil;

import java.util.ArrayList;
import java.util.List;


public class SequenceFlow extends Relation implements java.io.Serializable {
	private static final long serialVersionUID = org.uengine.kernel.GlobalContext.SERIALIZATION_UID;

	private String sourceRef;
    @Hidden
		public String getSourceRef() {
			return sourceRef;
		}
		public void setSourceRef(String source) {
			this.sourceRef = source;
		}
		
	private String targetRef;
    @Hidden
		public String getTargetRef() {
			return targetRef;
		}
		public void setTargetRef(String target) {
			this.targetRef = target;
		}
		
	String tracingTag;	
		@Id
		@Hidden
		public String getTracingTag() {
			return tracingTag;
		}
		public void setTracingTag(String tag) {
			tracingTag = tag;
		}
		
	Condition condition;
    @Face(faceClass=ConditionFace.class)
		public Condition getCondition() {
			return condition;
		}
		public void setCondition(Condition condition) {
			this.condition = condition;
		}

    @Hidden
	public Activity getSourceActivity(){
		return (Activity)this.getSourceElement();
	}
	public void setSourceActivity(Activity activity){
		this.setSourceElement(activity);
	}
	
    @Hidden
	public Activity getTargetActivity(){
		return (Activity)this.getTargetElement();
	}
	public void setTargetActivity(Activity activity){
		this.setTargetElement(activity);
	}
	
	public SequenceFlow() {
	}

	public SequenceFlow(String source, String target) {
		setSourceRef(source);
		setTargetRef(target);
	}
	

	@Override
	public RelationView createView() {

		RelationView relationView = (RelationView) UEngineUtil.getComponentByEscalation(getClass(), "view");

		if(relationView==null) throw new RuntimeException("can't find View component for " + getClass());

		relationView.setRelation(this);
		relationView.setLabel(getName());


		//relationView.setLabel(get);

		return relationView;
	}

	public boolean isMet(ProcessInstance instance, String scope) throws Exception {
		if (condition == null) {
			return true;
		}
		return condition.isMet(instance, scope);
	}
	
//	public boolean isMatch() throws Exception {
//
//		if (getSourceElement() instanceof ExclusiveGateway) {
//			// condition needed
//		} else if (getSourceElement() instanceof InclusiveGateway) {
//			// condition needed
//		} else if (getSourceElement() instanceof LoopGateway) {
//			// assume to get the outgoing transition from sourceActivity
//			if (((LoopGateway) getSourceElement()).getMap().get("outgoing").equals(this)) {
//				return true;
//			}
//		} else {
//			return true;
//		}
//
//		return false;
//	}
	
	public List<Activity> fillActivityToTransition(List<Activity> activityList){
		for(Activity activity : activityList){
			activity.getOutgoingSequenceFlows().clear();
			activity.getIncomingSequenceFlows().clear();
			if( activity.getTracingTag().equals(this.getSourceRef())) {
				List<SequenceFlow> list = activity.getOutgoingSequenceFlows();
				boolean addFalg = true;
				for( SequenceFlow ts : list){
					if( ts.getSourceRef().equals(this.getSourceRef()) && ts.getTargetRef().equals(this.getTargetRef())){
						addFalg = false;
					}
				}
				if( addFalg ) list.add(this);
			}else if( activity.getTracingTag().equals(this.getTargetRef())) {
				List<SequenceFlow> list = activity.getIncomingSequenceFlows();
				boolean addFalg = true;
				for( SequenceFlow ts : list){
					if( ts.getSourceRef().equals(this.getSourceRef()) && ts.getTargetRef().equals(this.getTargetRef())){
						addFalg = false;
					}
				}
				if( addFalg ) list.add(this);
			}
			
			if( activity instanceof ScopeActivity){
				ArrayList<SequenceFlow> tsList = ((ScopeActivity) activity).getSequenceFlows();
				if( tsList != null && ((ScopeActivity) activity).getChildActivities() != null ){
					for(SequenceFlow ts : tsList){
						ts.fillActivityToTransition(((ScopeActivity) activity).getChildActivities());
					}
				}
			}
		}
		return activityList;
	}
	

}
