package org.uengine.modeling.resource;

import java.util.List;

public interface IContainer extends IResource {

	public List<IResource> list() throws Exception;
	public void setChildren(List<IResource> children);
}
