package org.uengine.processpublisher.bpmn.importer;

import org.omg.spec.bpmn._20100524.model.TEvent;
import org.uengine.kernel.EventActivity;

import java.util.Hashtable;

public class TBoundaryEventAdapter extends TFlowNodeAdapter<TEvent, EventActivity> {

    @Override
    public EventActivity create(TEvent src, Hashtable keyedContext){
        return new EventActivity();
    }

}
