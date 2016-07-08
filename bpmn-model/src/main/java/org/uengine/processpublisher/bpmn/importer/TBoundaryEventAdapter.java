package org.uengine.processpublisher.bpmn.importer;

import org.omg.spec.bpmn._20100524.model.TBoundaryEvent;
import org.omg.spec.bpmn._20100524.model.TEvent;
import org.omg.spec.bpmn._20100524.model.TEventDefinition;
import org.uengine.kernel.bpmn.Event;
import org.uengine.processpublisher.BPMNUtil;

import java.util.Hashtable;

public class TBoundaryEventAdapter extends TFlowNodeAdapter<TBoundaryEvent, Event> {

    @Override
    public Event create(TBoundaryEvent src, Hashtable keyedContext){
        TEventDefinition eventDefinition = src.getEventDefinition().get(0).getValue();

//        String eventDefinitionClassName = eventDefinition.getClass().getName();
//        String eventTypeName = eventDefinitionClassName.substring(0, eventDefinitionClassName.length() - "Definition".length());
        try {
            Event event = (Event) BPMNUtil.getAdapter(eventDefinition.getClass(), true).convert(eventDefinition, keyedContext);

            if(src.getAttachedToRef() != null)
                event.setAttachedToRef(src.getAttachedToRef().getLocalPart());

            return event;

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

}
