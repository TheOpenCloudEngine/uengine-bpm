package org.uengine.modeling.resource;

import org.metaworks.ContextAware;
import org.metaworks.MetaworksContext;
import org.metaworks.MetaworksFile;
import org.metaworks.Refresh;
import org.metaworks.annotation.AutowiredFromClient;
import org.metaworks.annotation.Hidden;
import org.metaworks.annotation.Order;
import org.metaworks.annotation.ServiceMethod;
import org.metaworks.widget.Download;
import org.metaworks.widget.ModalWindow;

import javax.activation.MimetypesFileTypeMap;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import static org.metaworks.dwr.MetaworksRemoteService.*;

public class EditorPanel implements ContextAware {

	public static final String WHEN_SAVEAS = "SaveAs";
	public static final String WHEN_RENAME = "Rename";
	public static final String WHEN_UPLOAD = "Upload";
	public static final String WHEN_MOVETO = "MoveTo";

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
					resourceName = resourcePath.substring(resourcePath.lastIndexOf(File.separator) + 1, resourcePath.lastIndexOf("."));
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
		public void setNew(boolean isNew) {
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
	public void save(@AutowiredFromClient ResourceNavigator resourceNavigator) throws Exception {
		if(isNew() && getResourceName()==null){
			throw new Exception("Please enter a file name");
		}

		if(getResourceName()!=null){
			String currResourcePath = getResourcePath();
			currResourcePath = currResourcePath.substring(0, currResourcePath.lastIndexOf(File.separator) + 1) + getResourceName() + currResourcePath.substring(currResourcePath.lastIndexOf("."));

			setResourcePath(currResourcePath);
		}

		IResource defaultResource = DefaultResource.createResource(getResourcePath());
		autowire(defaultResource);

		autowire(getEditor());

		defaultResource.save(getEditor().createEditedObject());

		setNew(false);

		resourceNavigator.load();

		wrapReturn(resourceNavigator);
	}

	@ServiceMethod(callByContent = true)
	@Order(2)
	public ModalWindow saveAs() throws Exception {
		EditorPanelPopup editorPanelPopup = getComponent(EditorPanelPopup.class);
		editorPanelPopup.setMetaworksContext(new MetaworksContext());
		editorPanelPopup.getMetaworksContext().setWhen(WHEN_SAVEAS);

		ModalWindow modalWindow = new ModalWindow(editorPanelPopup, 300, 200, "Save As");
		return modalWindow;
	}

	@ServiceMethod(callByContent = true)
	@Order(3)
	public void rename(@AutowiredFromClient ResourceNavigator resourceNavigator) throws Exception {
		if(WHEN_RENAME.equals(getMetaworksContext().getWhen())){
			IResource defaultResource = DefaultResource.createResource(getResourcePath());
			autowire(defaultResource);

			String srcResourcePath = getResourcePath();
			String desResourcePath = srcResourcePath.substring(0, srcResourcePath.lastIndexOf(File.separator) + 1) + getResourceName() + srcResourcePath.substring(srcResourcePath.lastIndexOf("."));;
			defaultResource.rename(desResourcePath);

			getMetaworksContext().setWhen(metaworksContext.WHEN_EDIT);

			resourceNavigator.load();

			wrapReturn(resourceNavigator,this);
		}else{
			getMetaworksContext().setWhen(WHEN_RENAME);
		}
	}

	@Hidden
	@ServiceMethod(callByContent = true)
	@Order(4)
	public ModalWindow moveTo() throws Exception {
		ResourceNavigator resourceNavigator = getComponent(ResourceNavigator.class);
		resourceNavigator.setResourceControlDelegate(new ResourceControlDelegateForMoveTo());
		filterResource(resourceNavigator.getRoot());

		ModalWindow modalWindow = new ModalWindow(resourceNavigator, 300, 400, "Move To");

		return modalWindow;
	}

	@Hidden
	@ServiceMethod(callByContent=true, except="fileTransfer", target="append")
	@Order(5)
	public Download download(@AutowiredFromClient ResourceNavigator resourceNavigator) throws FileNotFoundException, IOException, Exception{
		this.save(resourceNavigator);

		String fileName = getResourceName() + getResourcePath().substring(getResourcePath().lastIndexOf("."));
		MimetypesFileTypeMap mimetypesFileTypeMap = new MimetypesFileTypeMap();

		DefaultResource defaultResource = (DefaultResource) DefaultResource.createResource(getResourcePath());
		return defaultResource.download(fileName, mimetypesFileTypeMap.getContentType(fileName));
	}

	@Hidden
	@ServiceMethod(callByContent = true)
	@Order(6)
	public ModalWindow upload() throws Exception {
		EditorPanelPopup editorPanelPopup = getComponent(EditorPanelPopup.class);
		editorPanelPopup.setMetaworksContext(new MetaworksContext());
		editorPanelPopup.getMetaworksContext().setWhen(WHEN_UPLOAD);

		MetaworksFile metaworksFile = new MetaworksFile();
		editorPanelPopup.setMetaworksFile(metaworksFile);

		ModalWindow modalWindow = new ModalWindow(editorPanelPopup, 300, 200, "Upload");
		return modalWindow;
	}

	@ServiceMethod(callByContent = true, when = MetaworksContext.WHEN_EDIT)
	@Order(7)
	public void delete(@AutowiredFromClient ResourceNavigator resourceNavigator) throws Exception {
		DefaultResource defaultResource = (DefaultResource) DefaultResource.createResource(getResourcePath());
		autowire(defaultResource);
		defaultResource.delete();

		this.setEditor(null);

		resourceNavigator.load();

		wrapReturn(resourceNavigator,this);
	}

	protected void filterResource(IContainer container){
		List<IResource> resourceList = container.getChildren();
		Iterator<IResource> resourceIterator = resourceList.iterator();

		while(resourceIterator.hasNext()){
			IResource resource = resourceIterator.next();
			if(resource instanceof ContainerResource){
				filterResource((ContainerResource)resource);
				resource.setMetaworksContext(new MetaworksContext());
				resource.getMetaworksContext().setWhen(WHEN_MOVETO);
			}else{
				resourceIterator.remove();;
			}
		}
	}

}
