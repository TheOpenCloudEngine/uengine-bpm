package org.uengine.social;

import org.metaworks.ContextAware;
import org.metaworks.MetaworksContext;
import org.metaworks.annotation.Hidden;
import org.metaworks.annotation.NonEditable;
import org.metaworks.annotation.ServiceMethod;
import org.metaworks.dwr.MetaworksRemoteService;
import org.metaworks.widget.ModalWindow;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.uengine.codi.mw3.model.Popup;
import org.uengine.kernel.face.ProcessVariableTypeSelector;

public class SocialBPMProcessDefinitionSelector extends SocialBPMProcessVariableTypeSelector{


    @ServiceMethod(callByContent = true, target=ServiceMethod.TARGET_POPUP)
    @Override
    public void select(){

        Popup popup = new Popup(new SocialBPMProcessDefinitionSelectorPopup());
        popup.setName("Select Process Definition");

        MetaworksRemoteService.wrapReturn(popup);


    }

}
