package org.uengine.processpublisher.bpmn.importer;

import org.omg.spec.bpmn._20100524.model.TEscalationEventDefinition;
import org.uengine.kernel.bpmn.Event;
import org.uengine.processpublisher.Adapter;

import java.util.Hashtable;

public class TEscalationEventDefinitionAdapter implements Adapter<TEscalationEventDefinition, Event> {

    @Override
    public Event convert(TEscalationEventDefinition src, Hashtable keyedContext) throws Exception {
        String escalationCode = src.getEscalationRef().getLocalPart();
        Event event = new Event();
        event.setName(escalationCode);

        return event;
    }
}