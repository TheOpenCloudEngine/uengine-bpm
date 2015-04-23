package org.uengine.kernel.graph;

import org.uengine.kernel.LoopGatewayActivity;
import org.uengine.kernel.OrGatewayActivity;
import org.uengine.kernel.ProcessInstance;
import org.uengine.kernel.XorGatewayActivity;
import org.uengine.modeling.RelationView;

public class DataAssociationTransition extends Transition{
	
	@Override
	public RelationView createView() {
		RelationView relationView = null;
		try {
			relationView = (RelationView)Thread.currentThread().getContextClassLoader().loadClass("org.uengine.kernel.designer.ui.DataAssociationTransitionView").newInstance();
			relationView.setRelation(this);
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
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
}
