package org.uengine.modeling.resource;

/**
 * Created by uengine on 2015. 7. 14..
 */
public interface IEditor<T> {
    public void setEditingObject(T object);
    public T getEditingObject();

    public T newObject(IResource resource);
}
