package org.uengine.modeling.resource.example;

import org.metaworks.ServiceMethodContext;
import org.metaworks.annotation.ServiceMethod;
import org.uengine.modeling.resource.ContainerResource;
import org.uengine.modeling.resource.resources.ProcessResource;

/**
 * Created by jangjinyoung on 15. 7. 12..
 */
public class ExampleContainerResource extends ContainerResource {

    @ServiceMethod(inContextMenu = true, target = ServiceMethodContext.TARGET_POPUP)
    public void newProcess() throws Exception {

        ProcessResource processResource = new ProcessResource();
        processResource.setPath("newProcess.process");
        processResource.setParent(this);

        processResource.open();
    }


}