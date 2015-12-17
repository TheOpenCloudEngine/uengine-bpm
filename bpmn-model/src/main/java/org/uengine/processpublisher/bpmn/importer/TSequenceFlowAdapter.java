package org.uengine.processpublisher.bpmn.importer;

import org.omg.spec.bpmn._20100524.di.BPMNEdge;
import org.omg.spec.bpmn._20100524.di.BPMNShape;
import org.omg.spec.bpmn._20100524.model.TEndEvent;
import org.omg.spec.bpmn._20100524.model.TFlowNode;
import org.omg.spec.bpmn._20100524.model.TSequenceFlow;
import org.omg.spec.dd._20100524.dc.Point;
import org.uengine.kernel.ExpressionEvaluteCondition;
import org.uengine.kernel.bpmn.SequenceFlow;
import org.uengine.modeling.RelationView;
import org.uengine.processpublisher.Adapter;

import javax.xml.namespace.QName;
import java.util.Hashtable;
import java.util.Map;

public class TSequenceFlowAdapter implements Adapter<TSequenceFlow, SequenceFlow> {



    @Override
    public SequenceFlow convert(TSequenceFlow src, Hashtable keyedContext) throws Exception {

        SequenceFlow transition = new SequenceFlow();

        transition.setTargetRef(((TFlowNode)src.getTargetRef()).getId());
        transition.setSourceRef(((TFlowNode)src.getSourceRef()).getId());
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


        relationView.setX((bpmnEdge.getWaypoint().get(0).getX()));
        relationView.setY((bpmnEdge.getWaypoint().get(0).getY()));
        relationView.setWidth((bpmnEdge.getWaypoint().get(1).getX()));
        relationView.setHeight((bpmnEdge.getWaypoint().get(1).getY()));
        relationView.setId(transition.getTracingTag());
        relationView.setFrom(transition.getSourceRef() + "_TERMINAL_C_INOUT_0");
        relationView.setTo(transition.getTargetRef() + "_TERMINAL_C_INOUT_0");
        relationView.setLabel(src.getName());

        transition.setRelationView(relationView);

//        FlowActivity parent = (FlowActivity) keyedContext.get("parent");
//
//        parent.addTransition(transition);

        return transition;
    }
}
