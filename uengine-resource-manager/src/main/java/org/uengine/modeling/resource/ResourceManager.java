package org.uengine.modeling.resource;

import org.metaworks.MetaworksFile;
import org.metaworks.dao.TransactionContext;
import org.oce.garuda.multitenancy.TenantContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.stereotype.Component;

import java.io.*;
import java.util.List;

/**
 * Created by jangjinyoung on 15. 7. 12..
 */
public class ResourceManager implements Storage{

    Storage storage;

        public Storage getStorage() {
            return storage;
        }

        public void setStorage(Storage storage) {
            this.storage = storage;
        }


    @Override
    public void delete(IResource resource) throws IOException {
        getStorage().delete(resource);
    }

    @Override
    public void rename(IResource resource, String newName) {
        getStorage().rename(resource, newName);
    }

    @Override
    public void copy(IResource src, String desPath) throws Exception {
        getStorage().copy(src, desPath);
    }

    @Override
    public void move(IResource src, IContainer container) throws IOException {
        getStorage().move(src, container);
    }

    @Override
    public List<IResource> listFiles(IContainer containerResource) throws Exception {
        return getStorage().listFiles(containerResource);
    }

    @Override
    public void createFolder(IContainer containerResource) throws Exception {
        getStorage().createFolder(containerResource);
    }

    @Override
    public boolean exists(IResource resource) throws Exception {
        return getStorage().exists(resource);
    }

    @Override
    public Object getObject(IResource resource) throws Exception {
        TransactionContext.getThreadLocalInstance().setSharedContext("resourceManager.resourcePath", resource.getPath());

        Object object = getStorage().getObject(resource);

        TransactionContext.getThreadLocalInstance().setSharedContext("resourceManager.resourcePath", null);

        return object;

    }

    @Override
    public void save(IResource resource, Object object) throws Exception {
        TransactionContext.getThreadLocalInstance().setSharedContext("resourceManager.resourcePath", resource.getPath());

        getStorage().save(resource, object);

        TransactionContext.getThreadLocalInstance().setSharedContext("resourceManager.resourcePath", null);
    }

    @Override
    public InputStream getInputStream(IResource resource) throws Exception {
        return getStorage().getInputStream(resource);
    }

    @Override
    public OutputStream getOutputStream(IResource resource) throws Exception {
        return getStorage().getOutputStream(resource);
    }
}
