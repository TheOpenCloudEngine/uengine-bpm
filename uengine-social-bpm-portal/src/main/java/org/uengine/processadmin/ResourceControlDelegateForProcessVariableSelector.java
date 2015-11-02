package org.uengine.processadmin;

import org.metaworks.Remover;
import org.metaworks.ToOpener;
import org.metaworks.dwr.MetaworksRemoteService;
import org.metaworks.widget.ModalWindow;
import org.uengine.modeling.resource.DefaultResource;
import org.uengine.modeling.resource.IResource;
import org.uengine.modeling.resource.ResourceControlDelegate;
import org.uengine.social.SocialBPMProcessVariableTypeSelector;

/**
 * Created by jangjinyoung on 15. 7. 23..
 */
public class ResourceControlDelegateForProcessVariableSelector implements ResourceControlDelegate{
    @Override
    public void onDoubleClicked(IResource resource) {
        if(resource instanceof DefaultResource){
            try {

                SocialBPMProcessVariableTypeSelector socialBPMProcessVariableTypeSelector = new SocialBPMProcessVariableTypeSelector();
                socialBPMProcessVariableTypeSelector.setSelectedClassName(resource.getPath());

                MetaworksRemoteService.wrapReturn(new ToOpener(socialBPMProcessVariableTypeSelector), new Remover(new ModalWindow()));


//                MetaworksRemoteService.wrapReturn(((DefaultResource) resource).select());
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
