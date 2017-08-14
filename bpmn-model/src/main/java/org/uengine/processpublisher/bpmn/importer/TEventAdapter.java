package org.uengine.processpublisher.bpmn.importer;

import org.omg.spec.bpmn._20100524.model.TEvent;
import org.uengine.kernel.bpmn.Event;

import java.util.Hashtable;

public class TEventAdapter extends TFlowNodeAdapter<TEvent, Event> {
    @Override
    public Event createActivity(TEvent src, Hashtable keyedContext){
        Event event = new Event();
        initializeActivity(event, src);

        return event;
    }

}
