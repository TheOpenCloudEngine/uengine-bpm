package org.uengine.processadmin;

import org.metaworks.ServiceMethodContext;
import org.metaworks.annotation.AutowiredFromClient;
import org.metaworks.annotation.ServiceMethod;
import org.uengine.codi.mw3.model.Session;
import org.uengine.modeling.resource.ContainerResource;
import org.uengine.modeling.resource.DefaultResource;

import java.io.File;

import static org.metaworks.dwr.MetaworksRemoteService.autowire;

/**
 * Created by jangjinyoung on 15. 7. 12..
 */
public class ProcessAdminContainerResource extends ContainerResource {

    @AutowiredFromClient public Session session;

    @ServiceMethod(inContextMenu = true, target = ServiceMethodContext.TARGET_POPUP)
    public void newProcess() throws Exception {

        DefaultResource processResource = new DefaultResource();
        processResource.setPath(getPath() + File.separator + "New Process.process");
        processResource.setParent(this);

        processResource.newOpen();
    }


    @ServiceMethod(inContextMenu = true, target = ServiceMethodContext.TARGET_POPUP)
    public void newClass() throws Exception {

        DefaultResource resource = new DefaultResource();

        autowire(resource);


        resource.setPath(getPath() + File.separator + "New Form.class");
        resource.setParent(this);

        resource.newOpen();
    }

    @ServiceMethod(inContextMenu = true, target = ServiceMethodContext.TARGET_POPUP)
    public void newURLApplication() throws Exception {

        DefaultResource resource = new DefaultResource();

        autowire(resource);


        resource.setPath(getPath() + File.separator + "New URL Application.urlapp");
        resource.setParent(this);

        resource.newOpen();
    }

}