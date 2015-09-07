package org.uengine.modeling.resource;

import org.apache.commons.lang.StringUtils;
import org.metaworks.*;
import org.metaworks.annotation.*;
import org.metaworks.annotation.Face;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.Id;
import java.io.File;

import static org.metaworks.dwr.MetaworksRemoteService.getComponent;
import static org.metaworks.dwr.MetaworksRemoteService.wrapReturn;

public class DefaultResource implements IResource {

	private String path;
		@Id
		@Hidden
		public String getPath() {
			return this.path;
		}
		public void setPath(String path) {
			this.path = path;
		}

	private IContainer parent;
	protected MetaworksContext metaworksContext;
//
//	boolean isSelected;
//		public boolean isSelected() {
//			return isSelected;
//		}
//		public void setIsSelected(boolean isSelected) {
//			this.isSelected = isSelected;
//		}



	@Autowired
	public ResourceManager resourceManager;




	public DefaultResource() {
	}

	public DefaultResource(String path) {
		this();
		setPath(path);
	}


	/**
	 *
	 * @return Returns only the file name **WITH** extension name
	 */
	public String getName() {

		if(path==null)
			return null;

		if (path.indexOf(File.separator) == StringUtils.INDEX_NOT_FOUND) {
			return path;
		}
		return StringUtils.substringAfterLast(path, File.separator);
	}

	@Name
	@Face(displayName = "name")
	/**
	 *
	 * @return Returns only the file name **WITHOUT** the extension name
	 */
	public String getDisplayName() {
		// File.separatorChar로 파일경로에서의 마지막 파일이나 폴더의 경로에서의 위치를 가져온다.

		if(path==null)
			return null;

		int index = this.path.lastIndexOf(File.separatorChar) + 1;
		// 경로에서 마지막 파일이나 폴더의 포인트 위치를 가져온다.
		int pos = this.path.substring(index).indexOf(".");
		if (pos == -1) {
			return this.path.substring(index);
		} else {
			return this.path.substring(index, index + pos);
		}
	}

	public void setDisplayName(String displayName){
	}


	/**
	 * type is filename extension if the file is folder String "folder" will be
	 * returned * for example,
	 * <p>
	 * folder : folder
	 * </p>
	 * <p>
	 * file : practice
	 * </p>
	 *
	 * @return filename extension
	 */
	public String getType() {

		if(path==null)
			return null;

		if (getName().indexOf(".") == StringUtils.INDEX_NOT_FOUND) {
			return TYPE_FOLDER;
		}
		return StringUtils.substringAfter(getName(), ".");
	}


	@Hidden
	public IContainer getParent() {
		if (this.parent == null && File.separator.equals(this.getPath())) {
			return null;
		}
		return parent;
	}

	@Override
	public void setParent(IContainer parent) {
		this.parent = parent;
	}




	/**
	 * 
	 * @param resourceControlDelegate
	 */
	@Order(6)
	@Face(displayName="open")
	@ServiceMethod(callByContent=true, except="children", eventBinding=EventContext.EVENT_DBLCLICK, inContextMenu=true)
	public void open(@AutowiredFromClient
							  ResourceControlDelegate resourceControlDelegate

	) throws Exception {

		if(resourceControlDelegate!=null)
			resourceControlDelegate.onDoubleClicked(this);
		else
			_newAndOpen(false);
	}


	@ServiceMethod(callByContent = true, except = "children", inContextMenu = true)
	public SelectedResource select() throws Exception {
		SelectedResource selectedResource = new SelectedResource();

		selectedResource.setPath(getPath());

		return selectedResource;
	}

	public void newOpen() throws Exception {
		_newAndOpen(true);
	}

	protected void _newAndOpen(boolean isNew) throws Exception {
		EditorPanel editorPanel = getComponent(EditorPanel.class);
		editorPanel.setResourcePath(getPath());
		editorPanel.setMetaworksContext(new MetaworksContext());
		editorPanel.getMetaworksContext().setWhen(MetaworksContext.WHEN_EDIT);

		String type = getType();

		String classNamePrefix = type.substring(0, 1).toUpperCase() + type.substring(1).toLowerCase();

		Class editorClass = Thread.currentThread().getContextClassLoader().loadClass("org.uengine.modeling.resource.editor." + classNamePrefix + "Editor");

		IEditor editor = (IEditor) editorClass.newInstance();

		if(isNew)
			editor.setEditingObject(editor.newObject(this));
		else
			editor.setEditingObject(resourceManager.getStorage().getObject(this));

		editorPanel.setEditor(editor);

		wrapReturn(new Refresh(editorPanel));
	}


	@Override
	public void accept(IResourceVisitor visitor) {
		visitor.visit(this);
	}

	@Override
	public void accept(IResourceVisitor visitor, boolean admin) {
		visitor.visit(this);
	}

	@Override
	public MetaworksContext getMetaworksContext() {
		return metaworksContext;
	}

	@Override
	public void setMetaworksContext(MetaworksContext metaworksContext) {
		this.metaworksContext = metaworksContext;
	}

	public Object delete() {

		resourceManager.getStorage().delete(this);

		return null;
	}


	@Override
	public boolean isContainer() {
		return false;
	}


	@Override
	public void rename(String newName) {

		resourceManager.getStorage().rename(this, newName);
	}


	public static IResource createResource(String path) throws Exception {
		String type = new DefaultResource(path).getType();

		try {

			String classNamePrefix = type.substring(0, 1).toUpperCase() + type.substring(1).toLowerCase();


			Class resourceClass = Thread.currentThread().getContextClassLoader().loadClass(DefaultResource.class.getPackage().getName() + ".resources." + classNamePrefix + "Resource");

			IResource resource = (IResource) resourceClass.newInstance();
			resource.setPath(path);

			return resource;


		}catch(ClassNotFoundException e){

			DefaultResource defaultResource = new DefaultResource();
			defaultResource.setPath(path);

			return defaultResource;

		}
	}


	@Override
	public void save(Object editingObject) throws Exception {
		resourceManager.getStorage().save(this, editingObject);
	}
}

