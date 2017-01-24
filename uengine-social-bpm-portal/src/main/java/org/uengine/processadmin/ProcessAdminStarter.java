package org.uengine.processadmin;

import org.metaworks.ServiceMethodContext;
import org.metaworks.annotation.ServiceMethod;
import org.metaworks.dao.TransactionContext;
import org.uengine.codi.mw3.StartCodi;

/**
 * Created by jjy on 2016. 10. 24..
 */
public class ProcessAdminStarter extends StartCodi{

    public static final String DEFAULT_LOADING_RESOURCE_PATH = "defaultLoadingResourcePath";
    String defaultLoadingResourcePath;

    public String getDefaultLoadingResourcePath() {
        return defaultLoadingResourcePath;
    }

    public void setDefaultLoadingResourcePath(String defaultLoadingResourcePath) {
        this.defaultLoadingResourcePath = defaultLoadingResourcePath;
    }


    @Override
    @ServiceMethod(payload={"key","lastVisitPage", "lastVisitValue", "ssoService", "defaultLoadingResourcePath"}, target= ServiceMethodContext.TARGET_APPEND)
    public Object load() throws Exception {
        setDefaultLoadingResourcePathToTransactionContext();

        return super.load();
    }

    private void setDefaultLoadingResourcePathToTransactionContext() {
        if(getDefaultLoadingResourcePath()!=null){//&& !"NONE".equalsIgnoreCase(getDefaultLoadingResourcePath())){
            TransactionContext.getThreadLocalInstance().setSharedContext(DEFAULT_LOADING_RESOURCE_PATH, getDefaultLoadingResourcePath());
        }
    }

    @Override
    public Object[] login() throws Exception {
        setDefaultLoadingResourcePathToTransactionContext();

        return super.login();
    }
}
