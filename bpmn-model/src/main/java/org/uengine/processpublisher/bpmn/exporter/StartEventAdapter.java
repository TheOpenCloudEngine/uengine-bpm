package org.uengine.processpublisher.bpmn.exporter;

import org.omg.spec.bpmn._20100524.di.BPMNDiagram;
import org.omg.spec.bpmn._20100524.di.BPMNShape;
import org.omg.spec.bpmn._20100524.model.TStartEvent;
import org.uengine.kernel.bpmn.StartEvent;
import org.uengine.processpublisher.Adapter;

import javax.xml.namespace.QName;
import java.util.Hashtable;

/**
 * Created by uengine on 2016. 6. 25..
 */
public class StartEventAdapter implements Adapter<StartEvent, TStartEvent> {

    @Override
    public TStartEvent convert(StartEvent src, Hashtable keyedContext) throws Exception {
        // setting endEvent
        TStartEvent tStartEvent = ObjectFactoryUtil.createBPMNObject(TStartEvent.class);
        tStartEvent.setId(src.getTracingTag());
        tStartEvent.setName(src.getName());

        String outgoing = getOutgoing(src);
        String incoming = getIncoming(src);

        if(outgoing != null && outgoing.length() > 0) {
            tStartEvent.getOutgoing().add(new QName(outgoing));
        }

        if(incoming != null && incoming.length() > 0) {
            tStartEvent.getIncoming().add(new QName(incoming));
        }

        ElementViewAdapter elementViewAdapter = new ElementViewAdapter();
        BPMNShape bpmnShape = elementViewAdapter.convert(src.getElementView(), null);
        bpmnShape.setBpmnElement(new QName(src.getTracingTag()));

        BPMNDiagram bpmnDiagram = (BPMNDiagram) keyedContext.get("bpmnDiagram");
        // make diagramElement and PLane add bpmnShape
        bpmnDiagram.getBPMNPlane().getDiagramElement().add(ObjectFactoryUtil.createDefaultJAXBElement(BPMNShape.class, bpmnShape));

        return tStartEvent;
    }

    private String getOutgoing(StartEvent startEvent) {
        String outgoing = null;
        if(startEvent.getElementView().getFromEdge() != null && startEvent.getElementView().getFromEdge().length() > 0) {
            outgoing = startEvent.getElementView().getFromEdge();
        }
        return outgoing;
    }

    private String getIncoming(StartEvent startEvent) {
        String incoming = null;
        if(startEvent.getElementView().getToEdge() != null && startEvent.getElementView().getToEdge().length() > 0) {
            incoming = startEvent.getElementView().getToEdge();
        }
        return incoming;
    }
}
