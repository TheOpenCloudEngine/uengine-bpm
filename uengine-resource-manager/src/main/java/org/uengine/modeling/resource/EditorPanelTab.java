package org.uengine.modeling.resource;

import org.metaworks.dwr.MetaworksRemoteService;

/**
 * Created by jjy on 2016. 10. 24..
 */
public class EditorPanelTab {

    EditorPanel editorPanel;
        public EditorPanel getEditorPanel() {
            return editorPanel;
        }
        public void setEditorPanel(EditorPanel editorPanel) {
            this.editorPanel = editorPanel;
        }

    public EditorPanelTab(){
        setEditorPanel(MetaworksRemoteService.getComponent(EditorPanel.class));
    }

}
