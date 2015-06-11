package org.uengine.kernel;

public interface BeanPropertyResolver {

	public void setBeanProperty(String key, Object value);
	public Object getBeanProperty(String key);
}
