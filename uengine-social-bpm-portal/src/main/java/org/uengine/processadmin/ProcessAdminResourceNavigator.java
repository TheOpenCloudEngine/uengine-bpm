package org.uengine.processadmin;

import org.metaworks.annotation.AutowiredToClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.uengine.bean.factory.MetaworksSpringBeanFactory;
import org.uengine.modeling.resource.ResourceManager;
import org.uengine.modeling.resource.ResourceControlDelegate;
import org.uengine.modeling.resource.ResourceNavigator;

/**
 * Created by jangjinyoung on 15. 7. 18..
 */
public class ProcessAdminResourceNavigator extends ResourceNavigator {

    public ProcessAdminResourceNavigator(){
        super();
        setRoot(new ProcessAdminContainerResource());
        getRoot().setPath("codi");

        ResourceManager resourceManager;

        try {
            resourceManager = MetaworksSpringBeanFactory.getBean(ResourceManager.class);

            if(!resourceManager.getStorage().exists(getRoot())){
                resourceManager.getStorage().createFolder(getRoot());
            }
        } catch (Exception e) {
            throw new RuntimeException("Error when to create root folder for tenant", e);
        }
    }


    ResourceControlDelegate processAdminResourceControlDelegate;
    @AutowiredToClient
        public ResourceControlDelegate getProcessAdminResourceControlDelegate() {
            return processAdminResourceControlDelegate;
        }

        public void setProcessAdminResourceControlDelegate(ResourceControlDelegate processAdminResourceControlDelegate) {
            this.processAdminResourceControlDelegate = processAdminResourceControlDelegate;
        }



}
