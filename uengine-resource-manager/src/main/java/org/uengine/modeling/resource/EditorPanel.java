package org.uengine.modeling.resource;

import org.metaworks.annotation.ServiceMethod;
import org.metaworks.dwr.MetaworksRemoteService;
import org.metaworks.widget.ModalWindow;

import static org.metaworks.dwr.MetaworksRemoteService.*;

public class EditorPanel {

	IEditor editor;
		public IEditor getEditor() {
			return editor;
		}
		public void setEditor(IEditor editor) {
			this.editor = editor;
		}

	String resourcePath;
		public String getResourcePath() {
			return resourcePath;
		}
		public void setResourcePath(String resourcePath) {
			this.resourcePath = resourcePath;
		}

	boolean isNew;

		public boolean isNew() {
			return isNew;
		}

		public void setIsNew(boolean isNew) {
			this.isNew = isNew;
		}

	@ServiceMethod(keyBinding="Ctrl+S", callByContent = true)
	public void save() throws Exception {
//		if(isNew){
//			wrapReturn(new ModalWindow());
//		}


		DefaultResource defaultResource = new DefaultResource(getResourcePath());

		autowire(defaultResource);

		defaultResource.save(getEditor().getEditingObject());
	}

}
