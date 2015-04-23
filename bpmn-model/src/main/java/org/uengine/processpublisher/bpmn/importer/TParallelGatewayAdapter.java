package org.uengine.processpublisher.bpmn.importer;

import org.omg.spec.bpmn._20100524.model.TGateway;
import org.uengine.kernel.AndGatewayActivity;
import org.uengine.kernel.GatewayActivity;

import java.util.Hashtable;

public class TParallelGatewayAdapter extends TFlowNodeAdapter<TGateway, GatewayActivity> {

    @Override
    public GatewayActivity create(TGateway src, Hashtable keyedContext){
        return new AndGatewayActivity();
    }

}
