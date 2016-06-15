package org.uengine.social;

import org.metaworks.dwr.MetaworksRemoteService;
import org.oce.garuda.multitenancy.Operation;
import org.oce.garuda.multitenancy.TenantContext;
import org.springframework.stereotype.Component;
import org.uengine.codi.mw3.model.TenantLifecycle;
import org.uengine.marketplace.ProcessApp;
import org.uengine.modeling.resource.DefaultResource;
import org.uengine.modeling.resource.ResourceManager;
import org.uengine.util.UEngineUtil;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.OutputStream;

/**
 * Created by jjy on 2016. 4. 21..
 */

@Component
public class SocialBPMTenantLifecycle implements TenantLifecycle {
    @Override
    public void onNewTenantSubscribe(final String tenantId) {
        ProcessApp processApp = null;

        try {
            processApp = new ProcessApp();
            MetaworksRemoteService.autowire(processApp);
            processApp.setAppId(-1); // -1 means default app, omg!
            processApp.setUrl("codi/StarterPack");

            processApp.addApp();

        } catch (Exception e){

            if(e.getCause() instanceof FileNotFoundException) {


                //if there's no default process installed, try to install from the program resource.
                TenantContext.nonTenantSpecificOperation(
                        new Operation() {
                            @Override
                            public Object run() {

                                DefaultResource processAppResource = new DefaultResource();
                                processAppResource.setPath("-1.processapp");

                                MetaworksRemoteService.autowire(processAppResource);

                                ResourceManager resourceManager = MetaworksRemoteService.getComponent(ResourceManager.class);
                                try {
                                    OutputStream os = resourceManager.getStorage().getOutputStream(processAppResource);

                                    UEngineUtil.copyStream(Thread.currentThread().getContextClassLoader().getResourceAsStream("processapps/-1.processapp"), os);

                                    onNewTenantSubscribe(tenantId);

                                } catch (Exception e1) {
                                    e1.printStackTrace();
                                }

                                return null;
                            }
                        }
                );
            }else{
                e.printStackTrace();
            }



        }
    }
}
