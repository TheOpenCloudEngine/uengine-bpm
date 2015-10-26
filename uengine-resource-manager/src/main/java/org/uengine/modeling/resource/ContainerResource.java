package org.uengine.modeling.resource;

import org.metaworks.MetaworksContext;
import org.metaworks.ServiceMethodContext;
import org.metaworks.annotation.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class ContainerResource extends DefaultResource implements IContainer {

	private List<IResource> children;

	private boolean isContainer;

	public ContainerResource() {
		setContainer(true);
	}

	@Override
	public SelectedResource select() throws Exception {
		return super.select();
	}

	public ContainerResource(String path) {
		this();
		setPath(path);
	}

//	@Override
//	public void open(@AutowiredFromClient ResourceControlDelegate resourceControlDelegate) throws Exception {
//		super.open(resourceControlDelegate);
//	}

	@Children
//	@Available(when = MetaworksContext.WHEN_VIEW)
	public List<IResource> getChildren() {
		if (children == null) {
			children = new ArrayList<IResource>();
		}
		return children;
	}

	public void setChildren(List<IResource> children) {
		this.children = children;
		sort();
	}

	@Field(descriptor = "container")
	public boolean isContainer() {
		return isContainer;
	}

	public void setContainer(boolean container) {
		isContainer = container;
	}

	public void accept(IResourceVisitor visitor) {
		visitor.visit(this);

		try {
			for (IResource resource : this.list())
                resource.accept(visitor);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public void accept(IResourceVisitor visitor, boolean admin) {
		super.accept(visitor, admin);

		try {
			for (IResource resource : this.list())
                resource.accept(visitor);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@Order(6)
	@Face(displayName="refresh")
	@Available(condition="metaworksContext.how == 'tree' && metaworksContext.where == 'navigator'")
	@ServiceMethod(callByContent=true, eventBinding="refresh",  inContextMenu=true, bindingHidden=true, target=ServiceMethodContext.TARGET_SELF)
	public void refresh() throws Exception {
		this.setChildren(this.list());
	}


	@Override
	public List<IResource> list() throws Exception {

		List<IResource> list = resourceManager.getStorage().listFiles(this);

		return list;

	}


	public String getResourceType(){
		return null;
	}

	public void createFolder() throws Exception {
		resourceManager.getStorage().createFolder(this);
	}

	@Override
	public <T extends IResource> void filterResources(Class<T> clazz){
		List<IResource> resourceList = this.getChildren();
		Iterator<IResource> resourceIterator = resourceList.iterator();

		while(resourceIterator.hasNext()){
			IResource resource = resourceIterator.next();
			if(resource instanceof ContainerResource){
				((ContainerResource)resource).filterResources(clazz);
			}else if(clazz.isInstance(resource)){
				resourceIterator.remove();;
			}
		}
	}

	@Override
	public void filterResources(List<IResource> resources) {

		for(IResource resource : resources){
			filterResource(resource);
		}

	}

	@Override
	public void initMetaworksContext(MetaworksContext metaworksContext) {
		List<IResource> resourceList = this.getChildren();
		Iterator<IResource> resourceIterator = resourceList.iterator();

		this.setMetaworksContext(metaworksContext);

		while(resourceIterator.hasNext()){

			IResource resourceItem = resourceIterator.next();

			if(resourceItem instanceof ContainerResource){

				((ContainerResource)resourceItem).initMetaworksContext(metaworksContext);

			}else{

				resourceItem.setMetaworksContext(metaworksContext);

			}
		}
	}

	protected void filterResource(IResource resource){
		List<IResource> resourceList = this.getChildren();
		Iterator<IResource> resourceIterator = resourceList.iterator();

		while(resourceIterator.hasNext()){

			IResource resourceItem = resourceIterator.next();

			if(resourceItem instanceof ContainerResource){
				((ContainerResource)resourceItem).filterResource(resource);

			}else if(resource.getPath().equals(resourceItem.getPath())){
				resourceIterator.remove();;
				return;
			}

		}
	}

	protected void sort(){
		Collections.sort(this.getChildren());
	}
}
