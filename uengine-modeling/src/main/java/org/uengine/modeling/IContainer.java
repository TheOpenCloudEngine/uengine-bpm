package org.uengine.modeling;

import java.util.List;

public interface IContainer {

	public String getPath();
	public List<IResource> list();
	public void setChildren(List<IResource> children);
}
