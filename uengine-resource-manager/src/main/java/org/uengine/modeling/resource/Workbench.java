package org.uengine.modeling.resource;

public class Workbench {

	public Workbench(IContainer root) {
		this.resourceNavigator = new ResourceNavigator();
		this.resourceNavigator.setRoot(root);
		this.editorPanel = new EditorPanel();
	}

	public Workbench(ResourceNavigator resourceNavigator) {
		this.resourceNavigator = resourceNavigator;
		this.editorPanel = new EditorPanel();
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
