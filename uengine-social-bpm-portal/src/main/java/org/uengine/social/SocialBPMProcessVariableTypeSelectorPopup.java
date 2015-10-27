package org.uengine.social;

import com.sun.star.util.ModeChangeEvent;
import org.metaworks.MetaworksContext;
import org.metaworks.Remover;
import org.metaworks.ServiceMethodContext;
import org.metaworks.ToOpener;
import org.metaworks.annotation.*;
import org.metaworks.dwr.MetaworksRemoteService;
import org.metaworks.widget.ModalWindow;
import org.uengine.codi.mw3.model.User;
import org.uengine.contexts.ComplexType;
import org.uengine.modeling.resource.*;
import org.uengine.modeling.resource.resources.JavaClassResource;
import org.uengine.modeling.resource.resources.ProcessResource;
import org.uengine.processadmin.ProcessAdminResourceNavigator;
import org.uengine.processadmin.ResourceControlDelegateForProcessVariableSelector;

import java.util.Iterator;
import java.util.List;

/**
 * Created by jangjinyoung on 15. 9. 17..
 */
public class SocialBPMProcessVariableTypeSelectorPopup {

    String type;
    @Range(options={"Text","Number", "Date","Complex"}, values={"java.lang.String","java.lang.Long", "java.util.Date","org.uengine.contexts.ComplexType"})
    public String getType() {
        return type;
    }
    public void setType(String primitypeTypeName) {
        this.type = primitypeTypeName;
    }


    ResourceNavigator classResourceNavigator;
    public ResourceNavigator getClassResourceNavigator() {
        return classResourceNavigator;
    }
    public void setClassResourceNavigator(ResourceNavigator classResourceNavigator) {
        this.classResourceNavigator = classResourceNavigator;
    }


    @ServiceMethod(callByContent = true)
    public void select(@AutowiredFromClient SelectedResource selectedComplexClassResource){
        SocialBPMProcessVariableTypeSelector socialBPMProcessVariableTypeSelector = new SocialBPMProcessVariableTypeSelector();
        socialBPMProcessVariableTypeSelector.setSelectedClassName(selectedComplexClassResource.getPath());


       // MetaworksRemoteService.wrapReturn(new ToOpener(socialBPMProcessVariableTypeSelector), new Remover(new ModalWindow()));

//        return socialBPMProcessVariableTypeSelector;
        MetaworksRemoteService.wrapReturn(new Remover(new ModalWindow()),socialBPMProcessVariableTypeSelector);
    }


    public SocialBPMProcessVariableTypeSelectorPopup(){
        ProcessAdminResourceNavigator classResourceNavigator = new ProcessAdminResourceNavigator();
        classResourceNavigator.getRoot().filterResources(ProcessResource.class);

        MetaworksRemoteService.autowire(classResourceNavigator);


        {
            DefaultResource primitive = new JavaClassResource();
            primitive.setPath("java.lang.String");
            classResourceNavigator.getRoot().getChildren().add(0, primitive);
        }
        {
            DefaultResource primitive = new JavaClassResource();
            primitive.setPath(RoleUser.class.getName());
            classResourceNavigator.getRoot().getChildren().add(0, primitive);
        }



        classResourceNavigator.getRoot().setMetaworksContext(new MetaworksContext());
        classResourceNavigator.getRoot().getMetaworksContext().setWhen(MetaworksContext.WHEN_VIEW);

        classResourceNavigator.setResourceControlDelegate(new ResourceControlDelegateForProcessVariableSelector());

        setClassResourceNavigator(classResourceNavigator);


    }
}
