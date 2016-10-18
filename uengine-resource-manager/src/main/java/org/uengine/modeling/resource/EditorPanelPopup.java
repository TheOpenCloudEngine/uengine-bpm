package org.uengine.modeling.resource;

import org.metaworks.ContextAware;
import org.metaworks.MetaworksContext;
import org.metaworks.MetaworksFile;
import org.metaworks.Remover;
import org.metaworks.annotation.AutowiredFromClient;
import org.metaworks.annotation.Available;
import org.metaworks.annotation.Face;
import org.metaworks.annotation.ServiceMethod;
import org.metaworks.widget.ModalWindow;

import java.io.File;

import static org.metaworks.dwr.MetaworksRemoteService.autowire;
import static org.metaworks.dwr.MetaworksRemoteService.wrapReturn;

/**
 * Created by hoo.lim on 9/14/2015.
 */
public class EditorPanelPopup implements ContextAware{
    MetaworksContext metaworksContext;

    @Override
    public MetaworksContext getMetaworksContext() {
        return metaworksContext;
    }

    @Override
    public void setMetaworksContext(MetaworksContext metaworksContext) {
        this.metaworksContext = metaworksContext;
    }

    String saveAsFileName;
        public String getSaveAsFileName() {
            return saveAsFileName;
        }
        public void setSaveAsFileName(String saveAsFileName) {
            this.saveAsFileName = saveAsFileName;
        }

    MetaworksFile metaworksFile;
        public MetaworksFile getMetaworksFile() {
            return metaworksFile;
        }
        public void setMetaworksFile(MetaworksFile metaworksFile) {
            this.metaworksFile = metaworksFile;
        }

    @Face(displayName = "OK")
    @ServiceMethod(callByContent = true)
    public void saveAs(@AutowiredFromClient EditorPanel editorPanel, @AutowiredFromClient(payload = "rootPath") ResourceNavigator resourceNavigator) throws Exception {
        DefaultResource defaultResource = (DefaultResource) DefaultResource.createResource(editorPanel.getResourcePath());
        autowire(defaultResource);

        String desPath = defaultResource.getPath().substring(0, defaultResource.getPath().lastIndexOf("/")) +
                "/" + saveAsFileName + "." + defaultResource.getType();
        defaultResource.copy(desPath);

        //if(resourceNavigator!=null)
        resourceNavigator.load();
//        else{
//
//        }

        wrapReturn(resourceNavigator, new Remover(new ModalWindow()));
    }

    @Face(displayName = "Cancel")
    @ServiceMethod()
    public Object cancelSaveAs(){
        return new Remover(new ModalWindow());
    }

    @Face(displayName = "Upload")
    @ServiceMethod(callByContent = true)
    public Object upload(@AutowiredFromClient EditorPanel editorPanel) throws Exception {
        DefaultResource defaultResource = (DefaultResource) DefaultResource.createResource(editorPanel.getResourcePath());
        autowire(defaultResource);

        defaultResource.upload(getMetaworksFile().getFileTransfer().getInputStream());

        defaultResource.reopen();

        return new Remover(new ModalWindow());
    }
}
