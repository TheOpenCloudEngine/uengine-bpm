package org.uengine.processpublisher.bpmn.importer;

import org.omg.spec.bpmn._20100524.model.TStartEvent;
import org.uengine.kernel.bpmn.*;
import org.uengine.kernel.*;

import java.util.Hashtable;

public class TStartEventAdapter extends TFlowNodeAdapter<TStartEvent, StartEvent> {

    @Override
    protected Activity createActivity(TStartEvent src, Hashtable keyedContext) {
        StartEvent startEvent = new StartEvent();

        initializeActivity(startEvent, src);

        return startEvent;
    }
}