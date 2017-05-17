package org.uengine.social;

import org.metaworks.dwr.MetaworksRemoteService;
import org.oce.garuda.multitenancy.Operation;
import org.oce.garuda.multitenancy.TenantContext;
import org.springframework.stereotype.Component;
import org.uengine.codi.CodiInstaller;
import org.uengine.codi.mw3.CodiDwrServlet;
import org.uengine.codi.mw3.model.TenantLifecycle;
import org.uengine.marketplace.ProcessApp;

import java.io.FileNotFoundException;

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
            processApp.setUrl("StarterPack");

            processApp.addApp();

        } catch (Exception e){

            if(e.getCause() instanceof FileNotFoundException) {


                //if there's no default process installed, try to install from the program resource.
                TenantContext.nonTenantSpecificOperation(
                        new Operation() {
                            @Override
                            public Object run() {

                                CodiInstaller codiInstaller = MetaworksRemoteService.getComponent(CodiInstaller.class);

                                codiInstaller.installDefaultProcessApp();

                                return null;
                            }
                        }
                );

//                onNewTenantSubscribe(tenantId); //try again.
            }else{
                e.printStackTrace();
            }



        }
    }
}
