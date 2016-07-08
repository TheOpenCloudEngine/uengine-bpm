package org.uengine.processpublisher.bpmn.exporter;

import org.omg.spec.bpmn._20100524.di.BPMNDiagram;
import org.omg.spec.bpmn._20100524.di.BPMNEdge;
import org.omg.spec.bpmn._20100524.model.TSequenceFlow;
import org.uengine.kernel.Activity;
import org.uengine.kernel.bpmn.SequenceFlow;
import org.uengine.modeling.ElementView;
import org.uengine.modeling.RelationView;
import org.uengine.processpublisher.Adapter;
import org.uengine.processpublisher.BPMNUtil;

import javax.xml.namespace.QName;
import java.util.Hashtable;
import java.util.List;

/**
 * Created by MisakaMikoto on 2015. 8. 14..
 */
public class SequenceFlowAdapter implements Adapter <SequenceFlow, TSequenceFlow> {
    @Override
    public TSequenceFlow convert(SequenceFlow src, Hashtable keyedContext) throws Exception {
        // make TSequenceFlow
        TSequenceFlow tSequenceFlow = ObjectFactoryUtil.createBPMNObject(TSequenceFlow.class);
        tSequenceFlow.setId(src.getRelationView().getId());

        keyedContext.put("targetRefElementView", (findTargetRefElementView(src.getTargetRef(), keyedContext)));

        BPMNEdge bpmnEdge = (BPMNEdge) BPMNUtil.exportAdapt(src.getRelationView(), keyedContext);
        bpmnEdge.setBpmnElement(new QName(src.getRelationView().getId()));
        BPMNDiagram bpmnDiagram = (BPMNDiagram) keyedContext.get("bpmnDiagram");
        // make diagramElement and PLane add bpmnShape
        bpmnDiagram.getBPMNPlane().getDiagramElement().add(ObjectFactoryUtil.createDefaultJAXBElement(BPMNEdge.class, bpmnEdge));

        return tSequenceFlow;
    }

    private ElementView findTargetRefElementView(String targetRefTracingTag, Hashtable keyedContext) {
        List<Activity> childActivities = (List<Activity>) keyedContext.get("childActivities");
        for(Activity activity : childActivities) {
            if(activity.getTracingTag().equals(targetRefTracingTag)) {
                return activity.getElementView();
            }

        }
        return null;
    }
}
