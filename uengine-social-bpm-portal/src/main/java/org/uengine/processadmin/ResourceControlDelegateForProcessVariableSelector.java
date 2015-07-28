package org.uengine.processadmin;

import org.metaworks.dwr.MetaworksRemoteService;
import org.uengine.modeling.resource.DefaultResource;
import org.uengine.modeling.resource.IResource;
import org.uengine.modeling.resource.ResourceControlDelegate;

/**
 * Created by jangjinyoung on 15. 7. 23..
 */
public class ResourceControlDelegateForProcessVariableSelector implements ResourceControlDelegate{
    @Override
    public void onDoubleClicked(IResource resource) {
        if(resource instanceof DefaultResource){
            try {
                MetaworksRemoteService.wrapReturn(((DefaultResource) resource).select());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onClicked(IResource resource) {
        return;
    }
}
