package org.uengine.modeling.resource;

import org.metaworks.*;
import org.metaworks.annotation.*;
import org.metaworks.dao.TransactionContext;
import org.metaworks.dwr.MetaworksRemoteService;
import org.metaworks.widget.Download;
import org.metaworks.widget.Label;
import org.metaworks.widget.ModalWindow;
import org.springframework.beans.factory.annotation.Autowired;
import org.uengine.kernel.LeveledException;
import org.uengine.kernel.Validatable;
import org.uengine.kernel.ValidationContext;
import org.uengine.modeling.HasThumbnail;
import org.uengine.modeling.LanguageSelector;
import org.uengine.modeling.Modeler;

import javax.activation.MimetypesFileTypeMap;
import javax.mail.Session;
import javax.xml.ws.Service;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static org.metaworks.dwr.MetaworksRemoteService.*;

public class EditorPanel implements ContextAware {

	public static final String WHEN_SAVEAS = "SaveAs";
	public static final String WHEN_RENAME = "Rename";
	public static final String WHEN_UPLOAD = "Upload";
	public static final String WHEN_MOVETO = "MoveTo";

	@Autowired
	public ResourceManager resourceManager;

	IEditor editor;
		public IEditor getEditor() {
			return editor;
		}
		public void setEditor(IEditor editor) {
			this.editor = editor;
		}

	String resourcePath;
	@Id
		public String getResourcePath() {
			return resourcePath;
		}
		public void setResourcePath(String resourcePath) {
			this.resourcePath = resourcePath;
		}


	LanguageSelector languageSelector = new LanguageSelector();
		public LanguageSelector getLanguageSelector() {
			return languageSelector;
		}

		public void setLanguageSelector(LanguageSelector languageSelector) {
			this.languageSelector = languageSelector;
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

	@ServiceMethod(keyBinding="Ctrl+S", callByContent = true, when = MetaworksContext.WHEN_EDIT)
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

		if(isNew() && resourceManager.getStorage().exists(defaultResource)){
			throw new Exception("$ExistingResourceName");
		}

		Object editedObject = getEditor().createEditedObject();


		if(editedObject instanceof HasThumbnail && getEditor() instanceof Modeler){
			Modeler modeler = (Modeler) getEditor();
			if(modeler.getCanvas().getThumbnailURL()!=null ){
				((HasThumbnail)editedObject).setThumbnailURL(modeler.getCanvas().getThumbnailURL());
			}
		}

		defaultResource.save(editedObject);

		saveRecentList(defaultResource);




		String appName = getResourcePath().substring(0, getResourcePath().indexOf("/"));

		ResourceNavigator resourceNavigator = new ResourceNavigator();
		resourceNavigator.setRoot(new ContainerResource());
		resourceNavigator.getRoot().setPath(appName);
		MetaworksRemoteService.wrapReturn(resourceNavigator);
		resourceNavigator.load();

		autowire(this);

		this.setNew(false);

		IEditor editor = getEditor().getClass().newInstance();

		editor.setEditingObject(defaultResource.load());

		this.setEditor(editor);




		wrapReturn(resourceNavigator,this);
	}

	private void saveRecentList(IResource defaultResource) throws Exception{
		String appRoot = defaultResource.getPath().split("/")[0];

		DefaultResource recentListRS = new DefaultResource(appRoot + "_recent.xml");

		try{
			List<String> recentList = null;
			try {
				recentList = ((List<String>) resourceManager.getObject(recentListRS));
			}catch (FileNotFoundException e){
				//ignore.
			}

			if (recentList==null) recentList = new ArrayList<String>();

			if(recentList.contains(defaultResource.getPath())) recentList.remove(defaultResource.getPath());

			recentList.add(0, defaultResource.getPath());

			if(recentList.size() > 10)
				recentList = recentList.subList(0, 10);

			resourceManager.save(recentListRS, recentList);

		}catch (Exception e){
			throw new Exception("failed to store recent list", e);
		}
	}

	@ServiceMethod(callByContent = true, when = MetaworksContext.WHEN_EDIT, target=ServiceMethod.TARGET_POPUP)
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
			String desResourcePath = srcResourcePath.substring(0, srcResourcePath.lastIndexOf("/") + 1) + getResourceName() + srcResourcePath.substring(srcResourcePath.lastIndexOf("."));;
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

//	@Hidden
	@ServiceMethod(callByContent=true, except="fileTransfer", target="append")
	@Order(5)
	public Download download() throws FileNotFoundException, IOException, Exception{
		this.save();

		String fileName = getResourceName() + getResourcePath().substring(getResourcePath().lastIndexOf("."));
		MimetypesFileTypeMap mimetypesFileTypeMap = new MimetypesFileTypeMap();

		DefaultResource defaultResource = (DefaultResource) DefaultResource.createResource(getResourcePath());
		Download download = defaultResource.download(fileName, mimetypesFileTypeMap.getContentType(fileName));

		MetaworksRemoteService.wrapReturn(download);

		return download;
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

	@ServiceMethod(callByContent = true, target= ServiceMethod.TARGET_POPUP)
	public ValidationContext validate() throws Exception{
		Object editedObject = getEditor().createEditedObject();
		if(editedObject instanceof Validatable){
			ValidationContext validationContext = ((Validatable) editedObject).validate(null);

			List<LeveledException> exceptions = new ArrayList<LeveledException>();

			if(validationContext.size() > 0){
				exceptions.addAll(validationContext);

				wrapReturn(new ModalWindow(exceptions, "Validation Result"));

				return validationContext;
			}
		}

		wrapReturn(new ModalWindow(new Label("<center><h2>Congratulations!<br> No error found in your model!</h2></center>"), 600, 200, "Validation Result"));
		return null;
	}

	@ServiceMethod(callByContent = true, when = MetaworksContext.WHEN_EDIT, needToConfirm = true)
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


	@ServiceMethod(callByContent = true, target = ServiceMethod.TARGET_POPUP)
//	@Available(where = "simulatable")
	public void simulate() throws Exception {
		TransactionContext.getThreadLocalInstance().setSharedContext("isDevelopmentTime", true);

		save();

		if(getEditor() instanceof Simulatable) {
			ModalWindow runner = new ModalWindow();

			runner.setWidth(3000);
			runner.setTitle("Simulation");

			IResource resource = new DefaultResource();
			resource.setPath(getResourcePath());

			runner.setPanel(((Simulatable) getEditor()).simulator(resource));

			MetaworksRemoteService.wrapReturn(runner);
		}else

			MetaworksRemoteService.wrapReturn(new ModalWindow(new Label("This resource is not supporting simulation")));
	}

}
