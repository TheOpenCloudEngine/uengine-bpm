package org.uengine.processadmin;

import org.metaworks.annotation.AutowiredToClient;
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
