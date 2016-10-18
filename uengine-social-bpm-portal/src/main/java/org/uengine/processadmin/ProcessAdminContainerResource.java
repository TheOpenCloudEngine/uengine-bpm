package org.uengine.processadmin;

import org.metaworks.*;
import org.metaworks.annotation.*;
import org.metaworks.annotation.Face;
import org.metaworks.dwr.MetaworksRemoteService;
import org.metaworks.widget.ModalWindow;
import org.uengine.codi.mw3.model.Perspective;
import org.uengine.codi.mw3.model.Session;
import org.uengine.modeling.resource.*;

import java.io.File;
import java.io.IOException;

import static org.metaworks.dwr.MetaworksRemoteService.autowire;
import static org.metaworks.dwr.MetaworksRemoteService.getComponent;
import static org.metaworks.dwr.MetaworksRemoteService.wrapReturn;

/**
 * Created by jangjinyoung on 15. 7. 12..
 */
@Face(
        ejsPath="dwr/metaworks/genericfaces/TreeFace.ejs",
        ejsPathMappingByContext=
                {
                        "{when: 'tree', face: 'dwr/metaworks/genericfaces/TreeFace.ejs'}",
                        "{when: 'MoveTo', face: 'dwr/metaworks/genericfaces/TreeFace.ejs'}",
                        "{when: 'newFolder', face: 'dwr/metaworks/org/uengine/processadmin/ProcessAdminContainerResource_NewFolder.ejs'}"
                }
)
public class ProcessAdminContainerResource extends ContainerResource {

    public static final String WHEN_NEW_FOLDER = "newFolder";
    public static final String WHEN_NEW_TREE = "tree";

    @AutowiredFromClient public Session session;

    String newFolderName;
        public String getNewFolderName() {
            return newFolderName;
        }
        public void setNewFolderName(String newFolderName) {
            this.newFolderName = newFolderName;
        }

    @ServiceMethod(inContextMenu = true, target = ServiceMethodContext.TARGET_POPUP)
    public void newProcess() throws Exception {

        DefaultResource processResource = new DefaultResource();
        processResource.setPath(getPath() + "/" + "New Process.process");
        processResource.setParent(this);

        processResource.newOpen();
    }

    @Face(displayName = "New Folder")
    @ServiceMethod(inContextMenu = true, target = ServiceMethodContext.TARGET_POPUP)
    public ModalWindow openNewFolderPopup() throws Exception {
        setMetaworksContext(new MetaworksContext());
        this.getMetaworksContext().setWhen(WHEN_NEW_FOLDER);
        ModalWindow modalWindow = new ModalWindow(this, 200, 100, "New Folder");
        return modalWindow;
    }

    @Override
    public void move(IContainer container) throws IOException {
        super.move(container);
    }

    @Face(displayName = "Create")
    @ServiceMethod(callByContent = true, target = ServiceMethodContext.TARGET_AUTO)
    public void confirmNewFolder(@AutowiredFromClient ProcessAdminResourceNavigator processAdminResourceNavigator) throws Exception {
        ContainerResource containerResource = this.getClass().newInstance();
        containerResource.setPath(this.getPath() + "/" + newFolderName);
        autowire(containerResource);
        containerResource.createFolder();
        this.getChildren().add(containerResource);

        this.getMetaworksContext().setWhen(WHEN_NEW_TREE);

        processAdminResourceNavigator.load();
        wrapReturn(new Remover(new ModalWindow()),processAdminResourceNavigator);
    }

    @Face(displayName = "Cancel")
    @ServiceMethod()
    public Object cancelNewFolder(){
        return new Remover(new ModalWindow());
    }

    @ServiceMethod(inContextMenu = true, target = ServiceMethodContext.TARGET_POPUP)
    public void newClass() throws Exception {

        DefaultResource resource = new DefaultResource();

        autowire(resource);


        resource.setPath(getPath() + "/" + "New Form.class");
        resource.setParent(this);

        resource.newOpen();
    }

    @ServiceMethod(inContextMenu = true, target = ServiceMethodContext.TARGET_POPUP)
    public void newURLApplication() throws Exception {

        DefaultResource resource = new DefaultResource();

        autowire(resource);


        resource.setPath(getPath() + "/New URL Application.urlapp");
        resource.setParent(this);

        resource.newOpen();
    }

    @ServiceMethod(inContextMenu = true, target = ServiceMethodContext.TARGET_POPUP)
    public void newJavaClass() throws Exception {

        DefaultResource resource = new DefaultResource();

        autowire(resource);


        resource.setPath(getPath() + "/New Java Class.javaclass");
        resource.setParent(this);

        resource.newOpen();
    }

    @ServiceMethod(inContextMenu = true, target = ServiceMethodContext.TARGET_POPUP)
    @Face(displayName = "Import")
    public void importResource() throws Exception {

        MetaworksRemoteService.wrapReturn(new ModalWindow(new ImportPopup(this), 500, 200));

    }


    @ServiceMethod(target = ServiceMethod.TARGET_POPUP, payload = "rootPath", inContextMenu = true)
    public VersionManager versionManager() throws Exception {
        VersionManager versionManager = MetaworksRemoteService.getComponent(VersionManager.class);
        //MetaworksRemoteService.autowire(versionManager);
        versionManager.load(this);

        MetaworksRemoteService.wrapReturn(new ModalWindow(versionManager, 400, 1000, "Version Manager"));

        return versionManager;
    }
}