package org.uengine.modeling.resource;

import org.metaworks.ContextAware;
import org.metaworks.MetaworksContext;
import org.metaworks.annotation.Order;
import org.metaworks.annotation.ServiceMethod;
import org.metaworks.dwr.MetaworksRemoteService;
import org.metaworks.widget.ModalWindow;

import static org.metaworks.dwr.MetaworksRemoteService.*;

public class EditorPanel implements ContextAware {

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

	String resourceName;
		public String getResourceName() {
			if(resourceName==null && resourcePath!=null){
				try {
					resourceName = resourcePath.substring(resourcePath.lastIndexOf("/") + 1, resourcePath.lastIndexOf("."));
				}catch (Exception e){
				}
			}

			return resourceName;
		}
		public void setResourceName(String resourceName) {
			this.resourceName = resourceName;
		}

	boolean isNew;
		public boolean isNew() {
			return isNew;
		}
		public void setIsNew(boolean isNew) {
			this.isNew = isNew;
		}

	MetaworksContext metaworksContext;
		@Override
		public MetaworksContext getMetaworksContext() {
			return metaworksContext;
		}

		@Override
		public void setMetaworksContext(MetaworksContext metaworksContext) {
			this.metaworksContext = metaworksContext;
		}



	@ServiceMethod(keyBinding="Ctrl+S", callByContent = true)
	@Order(1)
	public void save() throws Exception {

		if(isNew() && getResourceName()==null){
			throw new Exception("Please enter a file name");
		}

		if(getResourceName()!=null){
			String currResourcePath = getResourcePath();
			currResourcePath = currResourcePath.substring(0, currResourcePath.lastIndexOf("/") + 1) + getResourceName() + currResourcePath.substring(currResourcePath.lastIndexOf("."));

			setResourcePath(currResourcePath);
		}

		IResource defaultResource = DefaultResource.createResource(getResourcePath());
		autowire(defaultResource);

		autowire(getEditor());

		defaultResource.save(getEditor().createEditedObject());

	}

	@ServiceMethod
	@Order(2)
	public void saveAs() throws Exception {
		throw new Exception("Not implemented");
	}

	@ServiceMethod
	@Order(3)
	public void rename() throws Exception {
		throw new Exception("Not implemented");
	}

	@ServiceMethod
	@Order(4)
	public void moveTo() throws Exception {
		throw new Exception("Not implemented");
	}

	@ServiceMethod
	@Order(5)
	public void download() throws Exception {
		throw new Exception("Not implemented");
	}

	@ServiceMethod
	@Order(6)
	public void upload() throws Exception {
		throw new Exception("Not implemented");
	}

	@ServiceMethod
	@Order(7)
	public void delete() throws Exception {
		throw new Exception("Not implemented");
	}


}
