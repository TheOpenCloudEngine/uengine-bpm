package org.uengine.processadmin;

import org.metaworks.*;
import org.metaworks.annotation.AutowiredFromClient;
import org.metaworks.annotation.Available;
import org.metaworks.annotation.Face;
import org.metaworks.annotation.ServiceMethod;
import org.metaworks.widget.ModalWindow;
import org.uengine.codi.mw3.model.Perspective;
import org.uengine.codi.mw3.model.Session;
import org.uengine.modeling.resource.*;

import java.io.File;

import static org.metaworks.dwr.MetaworksRemoteService.autowire;
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
        processResource.setPath(getPath() + File.separator + "New Process.process");
        processResource.setParent(this);

        processResource.newOpen();
    }

    @Face(displayName = "New Folder")
    @ServiceMethod(inContextMenu = true, target = ServiceMethodContext.TARGET_POPUP)
    public ModalWindow openNewFolderPopup() throws Exception {
        this.getMetaworksContext().setWhen(WHEN_NEW_FOLDER);
        ModalWindow modalWindow = new ModalWindow(this, 200, 100, "New Folder");
        return modalWindow;
    }

    @Face(displayName = "Create")
    @ServiceMethod(callByContent = true, target = ServiceMethodContext.TARGET_AUTO)
    public void confirmNewFolder() throws Exception {
        ContainerResource containerResource = this.getClass().newInstance();
        containerResource.setPath(this.getPath() + File.separator + newFolderName);
        autowire(containerResource);
        containerResource.createFolder();
        this.getChildren().add(containerResource);

        this.getMetaworksContext().setWhen(WHEN_NEW_TREE);
        wrapReturn(new Refresh(this), new Remover(new ModalWindow()));
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

//    @ServiceMethod(callByContent=true, eventBinding=EventContext.EVENT_DBLCLICK, inContextMenu=true)
//    public void open(@AutowiredFromClient EditorPanel editorPanel,
//            @AutowiredFromClient ResourceNavigator resourceNavigator) throws Exception {
//        IResource defaultResource = DefaultResource.createResource(editorPanel.getResourcePath());
//        autowire(defaultResource);
//        defaultResource.move(this);
//
//        editorPanel.setEditor(null);
//
//        resourceNavigator.load();
//
//        wrapReturn(editorPanel, resourceNavigator,new Remover(new ModalWindow()));
//    }
}