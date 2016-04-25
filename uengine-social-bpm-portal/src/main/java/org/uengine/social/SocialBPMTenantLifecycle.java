package org.uengine.social;

import org.metaworks.dwr.MetaworksRemoteService;
import org.springframework.stereotype.Component;
import org.uengine.codi.mw3.model.TenantLifecycle;
import org.uengine.marketplace.ProcessApp;

/**
 * Created by jjy on 2016. 4. 21..
 */

@Component
public class SocialBPMTenantLifecycle implements TenantLifecycle {
    @Override
    public void onNewTenantSubscribe(String tenantId) {
        ProcessApp processApp = null;
        try {
            processApp = new ProcessApp();
            MetaworksRemoteService.autowire(processApp);
            processApp.setAppId(-1); // -1 means default app, omg!

            processApp.addApp();

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
