package org.uengine.social;

import org.metaworks.dao.TransactionContext;
import org.metaworks.dwr.MetaworksRemoteService;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.uengine.codi.mw3.Login;
import org.uengine.codi.mw3.model.Application;
import org.uengine.processadmin.ProcessAdminStarter;

/**
 * Created by uengine on 2017. 1. 17..
 */
@Component
@Scope("prototype")
public class SocialBPMLogin extends Login{

    @Override
    protected Application getDefaultLoadingApplication(Application app) {
        String defaultLoadingResourcePath = (String) TransactionContext.getThreadLocalInstance().getSharedContext(ProcessAdminStarter.DEFAULT_LOADING_RESOURCE_PATH);

        if(defaultLoadingResourcePath!=null){
            return MetaworksRemoteService.getComponent(ProcessAdminApplication.class);
        }

        return super.getDefaultLoadingApplication(app);
    }
}
