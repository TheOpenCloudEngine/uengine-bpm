package org.uengine.modeling.resource;

import org.metaworks.annotation.Children;

import java.util.List;

public interface IContainer extends IResource {

	public List<IResource> list() throws Exception;

	@Children
	public List<IResource> getChildren();
	public void setChildren(List<IResource> children);

	public <T extends IResource> void filterResource(Class<T> clazz);
}
