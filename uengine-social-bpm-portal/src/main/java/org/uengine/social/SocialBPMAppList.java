package org.uengine.social;


import org.metaworks.EventContext;
import org.metaworks.Refresh;
import org.metaworks.ServiceMethodContext;
import org.metaworks.ToEvent;
import org.metaworks.annotation.Face;
import org.metaworks.annotation.ServiceMethod;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.uengine.codi.mw3.marketplace.Marketplace;
import org.uengine.codi.mw3.model.AllAppList;

import static org.metaworks.dwr.MetaworksRemoteService.wrapReturn;

@Component
@Order(100)
//@Face(ejsPath = "dwr/metaworks/org/uengine/social/SocialBPMAppList.ejs")
public class SocialBPMAppList extends AllAppList{

    @ServiceMethod(target = ServiceMethodContext.TARGET_APPEND)
    public void goProcessAdmin() throws Exception {
        ProcessAdminApplication application = new ProcessAdminApplication();
        topPanel.setTopCenterTitle("Practice Composer");

        wrapReturn(new Refresh(application), new Refresh(topPanel), new ToEvent(ServiceMethodContext.TARGET_SELF, EventContext.EVENT_CLOSE));
    }



    @ServiceMethod(target = ServiceMethodContext.TARGET_APPEND)
    public Object[] goMarketplace() throws Exception {
        Marketplace essenciaMarketplace = new Marketplace(session);

        topPanel.setTopCenterTitle("Marketplace");
        return new Object[]{new Refresh(essenciaMarketplace), new Refresh(topPanel), new ToEvent(ServiceMethodContext.TARGET_SELF, EventContext.EVENT_CLOSE)};
    }


}
