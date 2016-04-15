package org.uengine.modeling.resource;

import org.metaworks.MetaworksContext;
import org.metaworks.annotation.Children;

import java.util.List;
import java.util.Set;

public interface IContainer extends IResource {

	public List<IResource> list() throws Exception;

	@Children
	public List<IResource> getChildren();
	public void setChildren(List<IResource> children);

	public void filtResources(Class<? extends IResource> clazz);
	public void filtResources(Class<? extends IResource> clazz, boolean filtOut);
	public void filtResources(Set<Class> clazz, boolean filtOut);

	public void filterResources(List<IResource> resources);
	public void initMetaworksContext(MetaworksContext metaworksContext);
}
