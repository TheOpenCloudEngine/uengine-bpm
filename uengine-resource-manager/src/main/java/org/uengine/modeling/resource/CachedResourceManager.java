package org.uengine.modeling.resource;

import org.codehaus.jackson.map.util.LRUMap;
import org.metaworks.dao.TransactionContext;

import java.util.Map;

/**
 * Created by uengine on 2016. 12. 15..
 */
public class CachedResourceManager extends ResourceManager {

    public static final String RESOURCE_MANAGER_CACHE = "resourceManagerCache_";
    static LRUMap<String, Object> lruCache = new LRUMap<>(0, 10);

    boolean perTransaction;
        public boolean isPerTransaction() {
            return perTransaction;
        }
        public void setPerTransaction(boolean perTransaction) {
            this.perTransaction = perTransaction;
        }


    @Override
    public Object getObject(IResource resource) throws Exception {

        Object cache;
        if(isPerTransaction()){
            cache = TransactionContext.getThreadLocalInstance().getSharedContext(RESOURCE_MANAGER_CACHE + resource.getPath());
        }else{
            cache = lruCache.get(resource.getPath());
        }

        if(cache != null){
            return cache;
        }

        Object object = super.getObject(resource);

        if(isPerTransaction()){
            TransactionContext.getThreadLocalInstance().setSharedContext(RESOURCE_MANAGER_CACHE + resource.getPath(), object);
        }else {
            lruCache.put(resource.getPath(), object);
        }

        return object;
    }

    @Override
    public void save(IResource resource, Object object) throws Exception {
        super.save(resource, object);


        if(isPerTransaction()) {
            TransactionContext.getThreadLocalInstance().setSharedContext(RESOURCE_MANAGER_CACHE + resource.getPath(), object);
        }else{
            lruCache.put(resource.getPath(), object);
        }
        //

    }
}
