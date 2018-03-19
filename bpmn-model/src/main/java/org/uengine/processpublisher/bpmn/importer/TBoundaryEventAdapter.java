package org.uengine.processpublisher.bpmn.importer;

import org.omg.spec.bpmn._20100524.model.TBoundaryEvent;
import org.omg.spec.bpmn._20100524.model.TEventDefinition;
import org.uengine.kernel.bpmn.Event;
import org.uengine.kernel.bpmn.TimerEvent;
import org.uengine.processpublisher.Adapter;
import org.uengine.processpublisher.BPMNUtil;

import javax.xml.bind.JAXBElement;
import java.util.Hashtable;

public class TBoundaryEventAdapter extends TFlowNodeAdapter<TBoundaryEvent, Event> {

    @Override
    public Event createActivity(TBoundaryEvent src, Hashtable keyedContext){

        for(JAXBElement<? extends TEventDefinition> element : src.getEventDefinition()){

            TEventDefinition eventDefinition = element.getValue();

            Adapter adapter = BPMNUtil.getAdapter(eventDefinition.getClass(), true);
            keyedContext.put("eventType", "Intermediate");
            keyedContext.put("catching", true);
            try {
                Event event = (Event) adapter.convert(eventDefinition, keyedContext);
                event.setAttachedToRef(src.getAttachedToRef().getLocalPart());
                return event;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        throw new RuntimeException("There's no adaptor to convert Event type: " + src.getClass());


    }

}
