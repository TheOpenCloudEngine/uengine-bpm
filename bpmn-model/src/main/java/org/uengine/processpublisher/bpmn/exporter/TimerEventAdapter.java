package org.uengine.processpublisher.bpmn.exporter;

import org.omg.spec.bpmn._20100524.di.BPMNDiagram;
import org.omg.spec.bpmn._20100524.di.BPMNShape;
import org.omg.spec.bpmn._20100524.model.*;
import org.uengine.kernel.bpmn.StartEvent;
import org.uengine.kernel.bpmn.TimerEvent;
import org.uengine.processpublisher.Adapter;
import org.uengine.processpublisher.BPMNUtil;

import javax.xml.bind.JAXBElement;
import javax.xml.namespace.QName;
import java.util.Hashtable;

/**
 * Created by MisakaMikoto on 2016. 6. 25..
 */
public class TimerEventAdapter implements Adapter<TimerEvent, TBoundaryEvent> {
    @Override
    public TBoundaryEvent convert(TimerEvent src, Hashtable keyedContext) throws Exception {
        TBoundaryEvent tBoundaryEvent = ObjectFactoryUtil.createBPMNObject(TBoundaryEvent.class);
        tBoundaryEvent.setId(src.getTracingTag());
        tBoundaryEvent.setName(src.getName());
        tBoundaryEvent.setCancelActivity(false);
        tBoundaryEvent.setAttachedToRef(new QName(src.getParentActivity().getTracingTag()));

        TTimerEventDefinition tTimerEventDefinition = ObjectFactoryUtil.createBPMNObject(TTimerEventDefinition.class);

        TExpression tExpression = new TExpression();
        tExpression.getContent().add(src.getExpression());
        tTimerEventDefinition.setTimeCycle(tExpression);
        JAXBElement<TTimerEventDefinition> tTimerEventDefinitionJAXBElement = ObjectFactoryUtil.createDefaultJAXBElement(TTimerEventDefinition.class, tTimerEventDefinition);
        tBoundaryEvent.getEventDefinition().add(tTimerEventDefinitionJAXBElement);

        String outgoing = getOutgoing(src);
        String incoming = getIncoming(src);

        if(outgoing != null && outgoing.length() > 0) {
            tBoundaryEvent.getOutgoing().add(new QName(outgoing));
        }

        if(incoming != null && incoming.length() > 0) {
            tBoundaryEvent.getIncoming().add(new QName(incoming));
        }

        BPMNShape bpmnShape = (BPMNShape) BPMNUtil.exportAdapt(src.getElementView());
        bpmnShape.setBpmnElement(new QName(src.getTracingTag()));

        BPMNDiagram bpmnDiagram = (BPMNDiagram) keyedContext.get("bpmnDiagram");
        // make diagramElement and PLane add bpmnShape
        bpmnDiagram.getBPMNPlane().getDiagramElement().add(ObjectFactoryUtil.createDefaultJAXBElement(BPMNShape.class, bpmnShape));

        return tBoundaryEvent;
    }

    private String getOutgoing(TimerEvent timerEvent) {
        String outgoing = null;
        if(timerEvent.getElementView().getFromEdge() != null && timerEvent.getElementView().getFromEdge().length() > 0) {
            outgoing = timerEvent.getElementView().getFromEdge();
        }
        return outgoing;
    }

    private String getIncoming(TimerEvent timerEvent) {
        String incoming = null;
        if(timerEvent.getElementView().getToEdge() != null && timerEvent.getElementView().getToEdge().length() > 0) {
            incoming = timerEvent.getElementView().getToEdge();
        }
        return incoming;
    }
}
