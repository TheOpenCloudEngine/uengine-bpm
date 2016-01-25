package org.uengine.processpublisher.bpmn.exporter;

import org.omg.spec.bpmn._20100524.di.BPMNDiagram;
import org.omg.spec.bpmn._20100524.di.BPMNEdge;
import org.omg.spec.bpmn._20100524.di.BPMNShape;
import org.omg.spec.bpmn._20100524.model.TSequenceFlow;
import org.uengine.kernel.bpmn.SequenceFlow;
import org.uengine.processpublisher.Adapter;
import org.uengine.processpublisher.BPMNUtil;

import javax.xml.namespace.QName;
import java.util.Hashtable;

/**
 * Created by MisakaMikoto on 2015. 8. 14..
 */
public class SequenceFlowAdapter implements Adapter <SequenceFlow, TSequenceFlow> {
    @Override
    public TSequenceFlow convert(SequenceFlow src, Hashtable keyedContext) throws Exception {
        // make TSequenceFlow
        TSequenceFlow tSequenceFlow = ObjectFactoryUtil.createBPMNObject(TSequenceFlow.class);
        tSequenceFlow.setId(src.getRelationView().getId());

        BPMNEdge bpmnEdge = (BPMNEdge) BPMNUtil.export(src.getRelationView());
        bpmnEdge.setBpmnElement(new QName(src.getRelationView().getId()));

        BPMNDiagram bpmnDiagram = (BPMNDiagram) keyedContext.get("bpmnDiagram");
        // make diagramElement and PLane add bpmnShape
        bpmnDiagram.getBPMNPlane().getDiagramElement().add(ObjectFactoryUtil.createDefaultJAXBElement(BPMNEdge.class, bpmnEdge));

        return tSequenceFlow;
    }
}
