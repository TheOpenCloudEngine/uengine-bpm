package org.uengine.processpublisher.bpmn.importer;

import org.omg.spec.bpmn._20100524.model.TBoundaryEvent;
import org.uengine.kernel.bpmn.TimerEvent;

import java.util.Hashtable;

public class TBoundaryEventAdapter extends TFlowNodeAdapter<TBoundaryEvent, TimerEvent> {

    @Override
    public TimerEvent createActivity(TBoundaryEvent src, Hashtable keyedContext){
//        String eventDefinitionClassName = eventDefinition.getClass().getName();
//        String eventTypeName = eventDefinitionClassName.substring(0, eventDefinitionClassName.length() - "Definition".length());

        return new TimerEvent();

    }

}
