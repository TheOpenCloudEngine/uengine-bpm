package org.uengine.processadmin;

import org.metaworks.MetaworksContext;
import org.metaworks.annotation.AutowiredToClient;
import org.metaworks.dwr.MetaworksRemoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.uengine.modeling.resource.ContainerResource;
import org.uengine.modeling.resource.ResourceManager;
import org.uengine.modeling.resource.ResourceControlDelegate;
import org.uengine.modeling.resource.ResourceNavigator;

import java.util.Collections;

/**
 * Created by jangjinyoung on 15. 7. 18..
 */
@Scope("prototype")
@Component
public class ProcessAdminResourceNavigator extends ResourceNavigator {

    public ProcessAdminResourceNavigator(){
        super();

        this.load();
    }

    @Override
    public void load(){

        ProcessAdminContainerResource processAdminContainerResource = MetaworksRemoteService.getComponent(ProcessAdminContainerResource.class);

        setRoot(processAdminContainerResource);
        getRoot().setPath("codi");
        getRoot().setMetaworksContext(new MetaworksContext());

        ResourceManager resourceManager;

        try {
            resourceManager = MetaworksRemoteService.getComponent(ResourceManager.class);

            if(!resourceManager.getStorage().exists(getRoot())){
                resourceManager.getStorage().createFolder(getRoot());
            }
        } catch (Exception e) {
            throw new RuntimeException("Error when to create root folder for tenant", e);
        }


        try {

            MetaworksRemoteService.autowire(getRoot());

            ((ContainerResource) getRoot()).refresh();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

//    ResourceControlDelegate processAdminResourceControlDelegate;
//    @AutowiredToClient
//        public ResourceControlDelegate getProcessAdminResourceControlDelegate() {
//            return processAdminResourceControlDelegate;
//        }
//
//        public void setProcessAdminResourceControlDelegate(ResourceControlDelegate processAdminResourceControlDelegate) {
//            this.processAdminResourceControlDelegate = processAdminResourceControlDelegate;
//        }
//


}
