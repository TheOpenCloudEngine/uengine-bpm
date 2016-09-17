package org.uengine.social;

import com.itextpdf.text.Meta;
import org.metaworks.ContextAware;
import org.metaworks.Face;
import org.metaworks.MetaworksContext;
import org.metaworks.annotation.*;
import org.metaworks.dwr.MetaworksRemoteService;
import org.metaworks.widget.ModalWindow;
import org.springframework.context.annotation.Scope;
import org.springframework.core.annotation.*;
import org.springframework.stereotype.Component;
import org.uengine.contexts.ComplexType;
import org.uengine.kernel.face.ProcessVariableTypeSelector;
import org.uengine.modeling.resource.DefaultResource;
import org.uengine.modeling.resource.ResourceNavigator;
import org.uengine.modeling.resource.SelectedResource;
import org.uengine.processadmin.ProcessAdminResourceNavigator;
import org.uengine.processadmin.ResourceControlDelegateForProcessVariableSelector;

/**
 * Created by jangjinyoung on 15. 7. 18..
 */
@Component
@Scope("prototype")
@org.springframework.core.annotation.Order(10)
@org.metaworks.annotation.Face(ejsPath="genericfaces/CleanObjectFace.ejs")
public class SocialBPMProcessVariableTypeSelector extends ProcessVariableTypeSelector implements ContextAware{

    @Override
    @Hidden
    public String getType() {
        return super.getType();
    }

    String selectedClassName;
    @NonEditable
        public String getSelectedClassName() {
            return selectedClassName;
        }
        public void setSelectedClassName(String selectedClassName) {
            this.selectedClassName = selectedClassName;
        }


    MetaworksContext metaworksContext;
        @Override
        public MetaworksContext getMetaworksContext() {
            return metaworksContext;
        }
        @Override
        public void setMetaworksContext(MetaworksContext metaworksContext) {
            this.metaworksContext = metaworksContext;
        }



    @ServiceMethod(callByContent = true, target=ServiceMethod.TARGET_POPUP)
    public void select(){

        MetaworksRemoteService.wrapReturn(new ModalWindow(new SocialBPMProcessVariableTypeSelectorPopup(), "Select Process Data Type"));

    }


    public SocialBPMProcessVariableTypeSelector(){


    }


    @Override
    public void setValueToFace(String value) {

        setSelectedClassName(value);

        if(value!=null && value.indexOf(".") > 0){
            setType("org.uengine.contexts.ComplexType");
        }
    }



    @Override
    public String createValueFromFace() {

        return getSelectedClassName();

    }
}
