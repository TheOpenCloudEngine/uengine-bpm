package org.uengine.processpublisher.bpmn.importer;

import org.omg.spec.bpmn._20100524.di.BPMNEdge;
import org.omg.spec.bpmn._20100524.di.BPMNShape;
import org.omg.spec.bpmn._20100524.model.TEndEvent;
import org.omg.spec.bpmn._20100524.model.TFlowNode;
import org.omg.spec.bpmn._20100524.model.TSequenceFlow;
import org.omg.spec.dd._20100524.dc.Point;
import org.uengine.kernel.EndEventActivity;
import org.uengine.kernel.ExpressionEvaluteCondition;
import org.uengine.kernel.FlowActivity;
import org.uengine.kernel.SequenceActivity;
import org.uengine.kernel.graph.Transition;
import org.uengine.modeling.RelationView;
import org.uengine.processpublisher.Adapter;

import javax.xml.namespace.QName;
import java.util.Hashtable;
import java.util.Map;

public class TSequenceFlowAdapter implements Adapter<TSequenceFlow, Transition> {



    @Override
    public Transition convert(TSequenceFlow src, Hashtable keyedContext) throws Exception {

        Transition transition = new Transition();

        transition.setTarget(((TFlowNode)src.getTargetRef()).getId());
        transition.setSource(((TFlowNode)src.getSourceRef()).getId());
        transition.setTracingTag(src.getId());

        if(src.getConditionExpression()!=null && src.getConditionExpression().getContent().size()>0)
            transition.setCondition(new ExpressionEvaluteCondition(src.getConditionExpression().getContent().get(0).toString()));

        Map bpmnDiagramElementMap = (Map) keyedContext.get("BPMNDiagramElementMap");

        BPMNEdge bpmnEdge = (BPMNEdge) bpmnDiagramElementMap.get(src.getId());

        if(bpmnEdge==null){
            bpmnEdge = new BPMNEdge();
            bpmnEdge.getWaypoint().add(new Point());
            bpmnEdge.getWaypoint().add(new Point());
        }

        //TODO: basically relationView should extend ElementView
        RelationView relationView = transition.createView();


        relationView.setX("" + Math.round(bpmnEdge.getWaypoint().get(0).getX()));
        relationView.setY("" + Math.round(bpmnEdge.getWaypoint().get(0).getY()));
        relationView.setWidth("" + Math.round(bpmnEdge.getWaypoint().get(1).getX()));
        relationView.setHeight("" + Math.round(bpmnEdge.getWaypoint().get(1).getY()));
        relationView.setId(transition.getTracingTag());
        relationView.setFrom(transition.getSource() + "_TERMINAL_C_INOUT_0");
        relationView.setTo(transition.getTarget() + "_TERMINAL_C_INOUT_0");
        relationView.setLabel(src.getName());

        transition.setRelationView(relationView);

//        FlowActivity parent = (FlowActivity) keyedContext.get("parent");
//
//        parent.addTransition(transition);

        return transition;
    }
}
