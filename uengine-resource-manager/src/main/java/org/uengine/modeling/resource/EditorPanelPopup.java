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
        autowire(defaultResource);

        String newResourcePath = defaultResource.getPath().substring(0, defaultResource.getPath().lastIndexOf("/")) +
                "/" + saveAsFileName + "." + defaultResource.getType();
        String newResourceName = saveAsFileName + defaultResource.getType();

        // 기존 프로세스를 변경후에 saveAs를 한다면 기존에 있는 데피니션은 놔두고
        // 이후에 변경된 프로세스를 저장하고
        // 화면에 saveAs된 프로세스을 보여줘야 한다.

        // 화면에서 넘어온 정보를 통해 새로 saveAs할 경로를 갖는 DefaultResource를 생성한다.
        DefaultResource newDefaultResource = (DefaultResource) DefaultResource.createResource(newResourcePath);
        autowire(newDefaultResource);
        autowire(editorPanel.getEditor());

        Object editedObject = editorPanel.getEditor().createEditedObject();

        if(editedObject instanceof HasThumbnail && editorPanel.getEditor() instanceof Modeler){
            Modeler modeler = (Modeler) editorPanel.getEditor();
            if(modeler.getCanvas().getThumbnailURL()!=null ){
                ((HasThumbnail)editedObject).setThumbnailURL(modeler.getCanvas().getThumbnailURL());
            }
        }

        // save
        newDefaultResource.save(editedObject);

        // AutowiredFromClient로 넘오온 editorPanel에 saveAs된 DefaultResource를 담는다.
        IEditor editor = editorPanel.getEditor().getClass().newInstance();
        editor.setEditingObject(newDefaultResource.load());
        editorPanel.setEditor(editor);
        editorPanel.setResourceName(saveAsFileName);
        editorPanel.setResourcePath(newResourcePath);

        //if(resourceNavigator!=null)
        resourceNavigator.load();
//        else{
//
//        }

        // 기존에서 editorPanel도 함께 넘겨서 화면에 적용한다.
        wrapReturn(resourceNavigator, editorPanel, new Remover(new ModalWindow()));
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
