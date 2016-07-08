package org.uengine.processpublisher.bpmn.importer;

import org.omg.spec.bpmn._20100524.model.TGateway;
import org.uengine.kernel.bpmn.Gateway;

import java.util.Hashtable;

public class TGatewayAdapter extends TFlowNodeAdapter<TGateway, Gateway> {
    @Override
    public Gateway create(TGateway src, Hashtable keyedContext){
        return new Gateway();
    }

}
