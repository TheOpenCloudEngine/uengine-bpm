package org.uengine.modeling.resource;

import org.metaworks.MetaworksContext;
import org.metaworks.annotation.Children;

import java.util.List;

public interface IContainer extends IResource {

	public List<IResource> list() throws Exception;

	@Children
	public List<IResource> getChildren();
	public void setChildren(List<IResource> children);

	public <T extends IResource> void filterResources(Class<T> clazz);
	public void filterResources(List<IResource> resources);
	public void initMetaworksContext(MetaworksContext metaworksContext);
}
