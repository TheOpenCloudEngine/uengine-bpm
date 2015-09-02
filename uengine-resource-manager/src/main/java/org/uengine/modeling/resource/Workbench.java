package org.uengine.modeling.resource;

import org.metaworks.annotation.AutowiredToClient;
import org.metaworks.dwr.MetaworksRemoteService;
import org.metaworks.widget.Clipboard;

import static org.metaworks.dwr.MetaworksRemoteService.*;

public class Workbench {

	public Workbench(IContainer root) {
		this(new ResourceNavigator());
		this.resourceNavigator.setRoot(root);
	}

	public Workbench(ResourceNavigator resourceNavigator) {
		this.resourceNavigator = resourceNavigator;
		this.editorPanel = getComponent(EditorPanel.class);
		setClipboard(new Clipboard());
	}

	private ResourceNavigator resourceNavigator;

		public ResourceNavigator getResourceNavigator() {
			return resourceNavigator;
		}

		public void setResourceNavigator(ResourceNavigator resourceNavigator) {
			this.resourceNavigator = resourceNavigator;
		}


	private EditorPanel editorPanel;

		public EditorPanel getEditorPanel() {
			return editorPanel;
		}

		public void setEditorPanel(EditorPanel editorPanel) {
			this.editorPanel = editorPanel;
		}


	Clipboard clipboard;
	@AutowiredToClient
		public Clipboard getClipboard() {
			return clipboard;
		}

		public void setClipboard(Clipboard clipboard) {
			this.clipboard = clipboard;
		}

}
