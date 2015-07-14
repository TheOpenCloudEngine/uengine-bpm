package org.uengine.processadmin;

import org.metaworks.ServiceMethodContext;
import org.metaworks.annotation.ServiceMethod;
import org.uengine.modeling.resource.ContainerResource;
import org.uengine.modeling.resource.DefaultResource;
import org.uengine.modeling.resource.resources.ProcessResource;

/**
 * Created by jangjinyoung on 15. 7. 12..
 */
public class ProcessAdminContainerResource extends ContainerResource {

    @ServiceMethod(inContextMenu = true, target = ServiceMethodContext.TARGET_POPUP)
    public void newProcess() throws Exception {

        ProcessResource processResource = new ProcessResource();
        processResource.setPath("newProcess.process");
        processResource.setParent(this);

        processResource.open();
    }


    @ServiceMethod(inContextMenu = true, target = ServiceMethodContext.TARGET_POPUP)
    public void newForm() throws Exception {

        DefaultResource resource = new DefaultResource();
        resource.setPath("newForm.form");
        resource.setParent(this);

        resource.open();
    }


}