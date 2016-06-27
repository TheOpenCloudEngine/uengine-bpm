package org.uengine.processpublisher.bpmn.exporter;

import org.omg.spec.bpmn._20100524.di.BPMNDiagram;
import org.omg.spec.bpmn._20100524.di.BPMNShape;
import org.omg.spec.bpmn._20100524.model.TEndEvent;
import org.omg.spec.bpmn._20100524.model.TExpression;
import org.omg.spec.bpmn._20100524.model.TIntermediateCatchEvent;
import org.omg.spec.bpmn._20100524.model.TTimerEventDefinition;
import org.uengine.kernel.bpmn.StartEvent;
import org.uengine.kernel.bpmn.TimerEvent;
import org.uengine.processpublisher.Adapter;

import javax.xml.bind.JAXBElement;
import javax.xml.namespace.QName;
import java.util.Hashtable;

/**
 * Created by uengine on 2016. 6. 25..
 */
public class TimerEventAdapter implements Adapter<TimerEvent, TIntermediateCatchEvent> {
    @Override
    public TIntermediateCatchEvent convert(TimerEvent src, Hashtable keyedContext) throws Exception {
        TTimerEventDefinition tTimerEventDefinition = ObjectFactoryUtil.createBPMNObject(TTimerEventDefinition.class);

        ExpressionAdapter expressionAdapter = new ExpressionAdapter();
        tTimerEventDefinition.setTimeCycle(expressionAdapter.convert(src.getExpression(), null));
        JAXBElement<TTimerEventDefinition> tTimerEventDefinitionJAXBElement = ObjectFactoryUtil.createDefaultJAXBElement(TTimerEventDefinition.class, tTimerEventDefinition);

        TIntermediateCatchEvent tIntermediateCatchEvent = ObjectFactoryUtil.createBPMNObject(TIntermediateCatchEvent.class);
        tIntermediateCatchEvent.setId(src.getTracingTag());
        tIntermediateCatchEvent.getEventDefinition().add(tTimerEventDefinitionJAXBElement);

        String outgoing = getOutgoing(src);
        String incoming = getIncoming(src);

        if(outgoing != null && outgoing.length() > 0) {
            tIntermediateCatchEvent.getOutgoing().add(new QName(outgoing));
        }

        if(incoming != null && incoming.length() > 0) {
            tIntermediateCatchEvent.getIncoming().add(new QName(incoming));
        }

        ElementViewAdapter elementViewAdapter = new ElementViewAdapter();
        BPMNShape bpmnShape = elementViewAdapter.convert(src.getElementView(), null);
        bpmnShape.setBpmnElement(new QName(src.getTracingTag()));

        BPMNDiagram bpmnDiagram = (BPMNDiagram) keyedContext.get("bpmnDiagram");
        // make diagramElement and PLane add bpmnShape
        bpmnDiagram.getBPMNPlane().getDiagramElement().add(ObjectFactoryUtil.createDefaultJAXBElement(BPMNShape.class, bpmnShape));

        return tIntermediateCatchEvent;
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
