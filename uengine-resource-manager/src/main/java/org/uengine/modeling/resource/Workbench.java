package org.uengine.modeling.resource;

import org.metaworks.dwr.MetaworksRemoteService;

import static org.metaworks.dwr.MetaworksRemoteService.*;

public class Workbench {

	public Workbench(IContainer root) {
		this.resourceNavigator = new ResourceNavigator();
		this.resourceNavigator.setRoot(root);
		this.editorPanel = getComponent(EditorPanel.class);
	}

	public Workbench(ResourceNavigator resourceNavigator) {
		this.resourceNavigator = resourceNavigator;
		this.editorPanel = getComponent(EditorPanel.class);
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

}
