package org.uengine.processpublisher.bpmn.exporter;

import org.omg.spec.bpmn._20100524.di.BPMNDiagram;
import org.omg.spec.bpmn._20100524.di.BPMNShape;
import org.omg.spec.bpmn._20100524.model.TEndEvent;
import org.uengine.kernel.bpmn.EndEvent;
import org.uengine.kernel.bpmn.StartEvent;
import org.uengine.processpublisher.Adapter;
import org.uengine.processpublisher.BPMNUtil;

import javax.xml.namespace.QName;
import java.util.Hashtable;

/**
 * Created by MisakaMikoto on 2016. 6. 25..
 */
public class EndEventAdapter implements Adapter<EndEvent, TEndEvent> {

    @Override
    public TEndEvent convert(EndEvent src, Hashtable keyedContext) throws Exception {
        // setting endEvent
        TEndEvent tEndEvent = ObjectFactoryUtil.createBPMNObject(TEndEvent.class);
        tEndEvent.setId(src.getTracingTag());
        tEndEvent.setName(src.getName());

        String outgoing = getOutgoing(src);
        String incoming = getIncoming(src);

        if(outgoing != null && outgoing.length() > 0) {
            tEndEvent.getOutgoing().add(new QName(outgoing));
        }

        if(incoming != null && incoming.length() > 0) {
            tEndEvent.getIncoming().add(new QName(incoming));
        }

        BPMNShape bpmnShape = (BPMNShape) BPMNUtil.exportAdapt(src.getElementView());
        bpmnShape.setBpmnElement(new QName(src.getTracingTag()));

        BPMNDiagram bpmnDiagram = (BPMNDiagram) keyedContext.get("bpmnDiagram");
        // make diagramElement and PLane add bpmnShape
        bpmnDiagram.getBPMNPlane().getDiagramElement().add(ObjectFactoryUtil.createDefaultJAXBElement(BPMNShape.class, bpmnShape));

        return tEndEvent;
    }

    private String getOutgoing(EndEvent endEvent) {
        String outgoing = null;
        if(endEvent.getElementView().getFromEdge() != null && endEvent.getElementView().getFromEdge().length() > 0) {
            outgoing = endEvent.getElementView().getFromEdge();
        }
        return outgoing;
    }

    private String getIncoming(EndEvent endEvent) {
        String incoming = null;
        if(endEvent.getElementView().getToEdge() != null && endEvent.getElementView().getToEdge().length() > 0) {
            incoming = endEvent.getElementView().getToEdge();
        }
        return incoming;
    }
}
