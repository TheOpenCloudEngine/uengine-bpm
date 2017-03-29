package org.uengine.modeling.resource;

import org.metaworks.dao.TransactionContext;

import java.util.Map;

/**
 * Created by uengine on 2016. 12. 15..
 */
public class CachedResourceManager extends ResourceManager {

    public static final String RESOURCE_MANAGER_CACHE = "resourceManagerCache_";

    @Override
    public Object getObject(IResource resource) throws Exception {
        Object cache = TransactionContext.getThreadLocalInstance().getSharedContext(RESOURCE_MANAGER_CACHE + resource.getPath());

        if(cache != null){
            return cache;
        }

        Object object = super.getObject(resource);

        TransactionContext.getThreadLocalInstance().setSharedContext(RESOURCE_MANAGER_CACHE + resource.getPath(), object);

        return object;
    }

    @Override
    public void save(IResource resource, Object object) throws Exception {
        super.save(resource, object);

        TransactionContext.getThreadLocalInstance().setSharedContext(RESOURCE_MANAGER_CACHE + resource.getPath(), object);

    }
}
