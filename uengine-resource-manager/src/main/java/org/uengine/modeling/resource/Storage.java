package org.uengine.modeling.resource;

import org.uengine.modeling.IModel;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

/**
 * Created by jangjinyoung on 15. 7. 12..
 */
public interface Storage {
    public void delete(IResource resource);
    public void rename(IResource resource, String newName);
    public void copy(IResource src, String desPath) throws Exception;
    public void move(IResource src, IContainer container) throws IOException;
    public List<IResource> listFiles(IContainer containerResource) throws Exception;

    public void createFolder(IContainer containerResource) throws Exception;


    public boolean exists(IResource resource) throws Exception;

    Object getObject(IResource resource) throws Exception;

    public void save(IResource resource, Object object) throws Exception;

    public InputStream getInputStream(IResource resource) throws Exception;
    public OutputStream getOutputStream(IResource resource) throws Exception;


}
