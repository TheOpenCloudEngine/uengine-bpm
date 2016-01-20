package org.uengine.modeling.resource;


import org.metaworks.annotation.AutowiredToClient;
import org.metaworks.annotation.Id;
import org.metaworks.annotation.ServiceMethod;
import org.metaworks.dwr.MetaworksRemoteService;
import org.metaworks.widget.ModalWindow;

/**
 * Created by jangjinyoung on 15. 7. 12..
 */
public class ResourceNavigator {

    public ResourceNavigator(){
        setSelectedResource(new SelectedResource());
        setResourceControlDelegate(new DefaultResourceControlDelegate());
    }

    SelectedResource selectedResource;
        public SelectedResource getSelectedResource() {
            return selectedResource;
        }
        public void setSelectedResource(SelectedResource selectedResource) {
            this.selectedResource = selectedResource;
        }


    IContainer root;
        public IContainer getRoot() {
            return root;
        }
        public void setRoot(IContainer root) {
            this.root = root;
            setRootPath(root.getPath());
        }

    String rootPath;
    @Id
        public String getRootPath() {
            return rootPath;
        }
        public void setRootPath(String rootPath) {
            this.rootPath = rootPath;
        }


    ResourceControlDelegate resourceControlDelegate;

        @AutowiredToClient
        public ResourceControlDelegate getResourceControlDelegate() {
            return resourceControlDelegate;
        }

        public void setResourceControlDelegate(ResourceControlDelegate resourceControlDelegate) {
            this.resourceControlDelegate = resourceControlDelegate;
        }

    public void load(){
    }

    @ServiceMethod(target = ServiceMethod.TARGET_POPUP, payload = "rootPath", inContextMenu = true)
    public VersionManager versionManager() throws Exception {
        VersionManager versionManager = new VersionManager();
        MetaworksRemoteService.autowire(versionManager);
        versionManager.load(this);

        MetaworksRemoteService.wrapReturn(new ModalWindow(versionManager, 400, 1000, "Version Manager"));

        return versionManager;
    }
}
