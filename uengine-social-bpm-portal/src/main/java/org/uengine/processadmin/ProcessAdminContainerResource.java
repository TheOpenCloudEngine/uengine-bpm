package org.uengine.processadmin;

import org.metaworks.ServiceMethodContext;
import org.metaworks.annotation.ServiceMethod;
import org.uengine.modeling.resource.ContainerResource;
import org.uengine.modeling.resource.DefaultResource;
import org.uengine.modeling.resource.resources.ProcessResource;

import static org.metaworks.dwr.MetaworksRemoteService.autowire;

/**
 * Created by jangjinyoung on 15. 7. 12..
 */
public class ProcessAdminContainerResource extends ContainerResource {

    @ServiceMethod(inContextMenu = true, target = ServiceMethodContext.TARGET_POPUP)
    public void newProcess() throws Exception {

        ProcessResource processResource = new ProcessResource();
        processResource.setPath("newProcess.process");
        processResource.setParent(this);

        processResource.newOpen();
    }


    @ServiceMethod(inContextMenu = true, target = ServiceMethodContext.TARGET_POPUP)
    public void newClass() throws Exception {

        DefaultResource resource = new DefaultResource();

        autowire(resource);


        resource.setPath("newForm.class");
        resource.setParent(this);

        resource.newOpen();
    }


}