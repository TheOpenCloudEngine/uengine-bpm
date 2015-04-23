package org.uengine.processpublisher.bpmn.importer;

import org.omg.spec.bpmn._20100524.model.TEndEvent;
import org.omg.spec.bpmn._20100524.model.TTask;
import org.uengine.kernel.*;

import java.util.Hashtable;

public class TEndEventAdapter extends TFlowNodeAdapter<TEndEvent, EndActivity> {

    @Override
    protected Activity create(TEndEvent src, Hashtable keyedContext) {
        return new EndActivity();
    }
}