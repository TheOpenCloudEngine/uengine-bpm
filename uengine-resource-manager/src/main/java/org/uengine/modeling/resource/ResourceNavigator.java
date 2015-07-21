package org.uengine.modeling.resource;


/**
 * Created by jangjinyoung on 15. 7. 12..
 */
public class ResourceNavigator {

    public ResourceNavigator(){
        setSelectedResource(new SelectedResource());
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

}
