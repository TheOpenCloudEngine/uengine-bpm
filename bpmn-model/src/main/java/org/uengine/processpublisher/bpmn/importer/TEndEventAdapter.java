package org.uengine.processpublisher.bpmn.importer;

import org.omg.spec.bpmn._20100524.model.TEndEvent;
import org.uengine.kernel.*;
import org.uengine.kernel.bpmn.EndEvent;

import java.util.Hashtable;

public class TEndEventAdapter extends TFlowNodeAdapter<TEndEvent, EndEvent> {
    @Override
    protected Activity createActivity(TEndEvent src, Hashtable keyedContext) {
        EndEvent endEvent = new EndEvent();

        initializeActivity(endEvent, src);

        return endEvent;
    }
}