package org.uengine.kernel.graph;

import java.util.ArrayList;
import java.util.List;

import org.metaworks.annotation.Hidden;
import org.metaworks.annotation.Id;
import org.uengine.kernel.*;
import org.uengine.modeling.Relation;
import org.uengine.modeling.RelationView;
import org.uengine.util.UEngineUtil;


public class Transition extends Relation implements java.io.Serializable {
	private static final long serialVersionUID = org.uengine.kernel.GlobalContext.SERIALIZATION_UID;

	private String source;
		public String getSource() {
			return source;
		}
		public void setSource(String source) {
			this.source = source;
		}
		
	private String target;
		public String getTarget() {
			return target;
		}
		public void setTarget(String target) {
			this.target = target;
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
		public Condition getCondition() {
			return condition;
		}
		public void setCondition(Condition condition) {
			this.condition = condition;
		}

	public Activity getSourceActivity(){
		return (Activity)this.getSourceElement();
	}
	public void setSourceActivity(Activity activity){
		this.setSourceElement(activity);
	}
	

	public Activity getTargetActivity(){
		return (Activity)this.getTargetElement();
	}
	public void setTargetActivity(Activity activity){
		this.setTargetElement(activity);
	}
	
	public Transition() {
	}

	public Transition(String source, String target) {
		setSource(source);
		setTarget(target);
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
	
	public boolean isMatch() throws Exception {
		
		if (getSourceElement() instanceof XorGatewayActivity) {
			// condition needed
		} else if (getSourceElement() instanceof OrGatewayActivity) {
			// condition needed
		} else if (getSourceElement() instanceof LoopGatewayActivity) {
			// assume to get the outgoing transition from sourceActivity
			if (((LoopGatewayActivity) getSourceElement()).getMap().get("outgoing").equals(this)) {
				return true;
			}
		} else {
			return true;
		}
		
		return false;
	}
	
	public List<Activity> fillActivityToTransition(List<Activity> activityList){
		for(Activity activity : activityList){
			activity.getOutgoingTransitions().clear();
			activity.getIncomingTransitions().clear();
			if( activity.getTracingTag().equals(this.getSource())) {
				List<Transition> list = activity.getOutgoingTransitions();
				boolean addFalg = true;
				for( Transition ts : list){
					if( ts.getSource().equals(this.getSource()) && ts.getTarget().equals(this.getTarget())){
						addFalg = false;
					}
				}
				if( addFalg ) list.add(this);
			}else if( activity.getTracingTag().equals(this.getTarget())) {
				List<Transition> list = activity.getIncomingTransitions();
				boolean addFalg = true;
				for( Transition ts : list){
					if( ts.getSource().equals(this.getSource()) && ts.getTarget().equals(this.getTarget())){
						addFalg = false;
					}
				}
				if( addFalg ) list.add(this);
			}
			
			if( activity instanceof ScopeActivity ){
				ArrayList<Transition> tsList = ((ScopeActivity) activity).getTransitions();
				if( tsList != null && ((ScopeActivity) activity).getChildActivities() != null ){
					for(Transition ts : tsList){
						ts.fillActivityToTransition(((ScopeActivity) activity).getChildActivities());
					}
				}
			}
		}
		return activityList;
	}
	

}
