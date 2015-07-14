package org.uengine.modeling.resource;

import org.apache.commons.lang.StringUtils;
import org.metaworks.*;
import org.metaworks.annotation.*;
import org.metaworks.annotation.Face;
import org.metaworks.dwr.MetaworksRemoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.uengine.modeling.Modeler;

import javax.persistence.Id;
import java.io.File;

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


	@Autowired ResourceManager resourceManager;



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


		if (path.indexOf(File.separator) == StringUtils.INDEX_NOT_FOUND) {
			return path;
		}
		return StringUtils.substringAfterLast(path, File.separator);
	}

	public void setName(String name) {
		StringBuilder newPath = new StringBuilder();
		if (this.path == null || this.path.contains(File.separator)) {
			newPath.append(StringUtils.substringBeforeLast(this.path, File.separator)).append(File.separator).append(name);
		} else {
			newPath.append(File.separator).append(name);
		}
		this.path = newPath.toString();
	}

	@Name
	@Face(displayName = "name")
	/**
	 *
	 * @return Returns only the file name **WITHOUT** the extension name
	 */
	public String getDisplayName() {
		// File.separatorChar로 파일경로에서의 마지막 파일이나 폴더의 경로에서의 위치를 가져온다.
		int index = this.path.lastIndexOf(File.separatorChar) + 1;
		// 경로에서 마지막 파일이나 폴더의 포인트 위치를 가져온다.
		int pos = this.path.substring(index).indexOf(".");
		if (pos == -1) {
			return this.path.substring(index);
		} else {
			return this.path.substring(index, index + pos);
		}
	}

	public void setDisplayName(String displayName) {
		if(this.path != null){
			StringBuffer sb = new StringBuffer();
			// File.separatorChar로 파일경로에서의 마지막 파일이나 폴더의 경로에서의 위치를 가져온다.
			int index = this.path.lastIndexOf(File.separatorChar) + 1;
			// 경로에서 마지막 파일이나 폴더의 포인트 위치를 가져온다.
			int pos = this.path.substring(index).indexOf(".");
			sb.append(this.path.substring(0, index) + displayName);
			if (pos != -1) {
				sb.append(this.path.substring(index + pos));
			}
			setPath(sb.toString());
		}
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
		if (getName().indexOf(".") == StringUtils.INDEX_NOT_FOUND) {
			return TYPE_FOLDER;
		}
		return StringUtils.substringAfter(getName(), ".");
	}

	public void setType(String type) {
		StringBuilder newPath = new StringBuilder();
		if (!TYPE_FOLDER.equals(type)) {
			if(type.startsWith(".")){
				newPath.append(StringUtils.substringBefore(this.path, ".")).append(type);
			} else { 
				newPath.append(StringUtils.substringBefore(this.path, ".")).append(".").append(type);
			}
			this.path = newPath.toString();
		}
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




	@Order(6)
	@Face(displayName = "open")
	@Available(condition = "metaworksContext.how == 'tree' && metaworksContext.where == 'navigator'")
	@ServiceMethod(callByContent = true, except = "children", eventBinding=EventContext.EVENT_DBLCLICK, inContextMenu = true, target=ServiceMethodContext.TARGET_APPEND)
	public void open() throws Exception {
		EditorPanel editorPanel = new EditorPanel();

		String type = getType();

		String classNamePrefix = type.substring(0, 1).toUpperCase() + type.substring(1).toLowerCase();

		Class modelerClass = Thread.currentThread().getContextClassLoader().loadClass("org.uengine.modeling.modeler." + classNamePrefix + "Modeler");

		Modeler modeler = (Modeler) modelerClass.newInstance();
		modeler.setModel(resourceManager.getStorage().getObject(this));

		editorPanel.setModeler(modeler);

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


}

