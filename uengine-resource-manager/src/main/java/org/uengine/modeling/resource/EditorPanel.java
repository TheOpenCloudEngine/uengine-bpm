package org.uengine.modeling.resource;

import org.metaworks.annotation.ServiceMethod;

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


	@ServiceMethod(keyBinding="Ctrl+S", callByContent = true)
	public void save() throws Exception {
		new DefaultResource(getResourcePath()).save(getEditor().getEditingObject());
	}

}
