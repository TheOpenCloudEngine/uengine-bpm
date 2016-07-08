package org.uengine.processpublisher.bpmn.importer;

import org.omg.spec.bpmn._20100524.model.TGateway;
import org.uengine.kernel.bpmn.ParallelGateway;
import org.uengine.kernel.bpmn.Gateway;

import java.util.Hashtable;

public class TParallelGatewayAdapter extends TFlowNodeAdapter<TGateway, ParallelGateway> {
    @Override
    public ParallelGateway create(TGateway src, Hashtable keyedContext){
        return new ParallelGateway();
    }

}
