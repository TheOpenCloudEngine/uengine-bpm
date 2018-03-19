package org.uengine.processpublisher.bpmn.importer;

import org.omg.spec.bpmn._20100524.model.TEscalationEventDefinition;
import org.uengine.kernel.bpmn.Event;
import org.uengine.processpublisher.Adapter;

import java.util.Hashtable;

public class TEscalationEventDefinitionAdapter implements Adapter<TEscalationEventDefinition, Event> {
    @Override
    public Event convert(TEscalationEventDefinition src, Hashtable keyedContext) throws Exception {
        Event event = createEvent(keyedContext);


        if(src.getEscalationRef()!=null) {
            String escalationCode = src.getEscalationRef().getLocalPart();
            event.setName(escalationCode);
        }

        return event;
    }

    private static Event createEvent(Hashtable keyedContext) throws InstantiationException, IllegalAccessException, ClassNotFoundException {
        Event event;

        String eventClassName = "Escalation";

        String eventType = (String) keyedContext.get("eventType");

        if(eventType == null){
            eventType = "Intermediate";
        }

        eventClassName += eventType;

        if(new Boolean(true).equals(keyedContext.get("catching"))){
            eventClassName += "Catch";
        }else{
            eventClassName += "Throw";
        }

        eventClassName += "Event";

        event = (Event) Thread.currentThread().getContextClassLoader().loadClass("org.uengine.kernel.bpmn." + eventClassName).newInstance();
        return event;
    }
}