package org.uengine.modeling.resource;


import org.metaworks.annotation.AutowiredToClient;

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
}
