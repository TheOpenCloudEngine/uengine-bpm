package org.uengine.modeling.resource;

import org.metaworks.*;
import org.metaworks.annotation.AutowiredFromClient;
import org.metaworks.annotation.Available;
import org.metaworks.annotation.Face;
import org.metaworks.annotation.ServiceMethod;
import org.metaworks.widget.ModalWindow;
import org.uengine.modeling.HasThumbnail;
import org.uengine.modeling.Modeler;

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
        autowire(editorPanel);
        String newResourcePath = defaultResource.getPath().substring(0, defaultResource.getPath().lastIndexOf("/")) +
                "/" + saveAsFileName + "." + defaultResource.getType();

        editorPanel.setResourceName(saveAsFileName);
        editorPanel.setResourcePath(newResourcePath); // change the file name.. need to be correct.
        editorPanel.save();
        ResourceNavigator resourceNavigator1 = editorPanel.refreshNavigator();
        // 기존에서 editorPanel도 함께 넘겨서 화면에 적용한다.
        wrapReturn(resourceNavigator1, editorPanel, new Remover(new ModalWindow()));
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
