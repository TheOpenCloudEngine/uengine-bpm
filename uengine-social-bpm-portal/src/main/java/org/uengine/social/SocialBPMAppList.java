package org.uengine.social;


import org.metaworks.EventContext;
import org.metaworks.Refresh;
import org.metaworks.ServiceMethodContext;
import org.metaworks.ToEvent;
import org.metaworks.annotation.ServiceMethod;
import org.metaworks.dwr.MetaworksRemoteService;
import org.springframework.stereotype.Component;
import org.uengine.codi.mw3.model.AllAppList;

import static org.metaworks.dwr.MetaworksRemoteService.wrapReturn;

@Component
public class SocialBPMAppList extends AllAppList{

    @ServiceMethod(target = ServiceMethodContext.TARGET_APPEND)
    public void goProcessAdmin() throws Exception {
        ProcessAdminApplication application = new ProcessAdminApplication();

        //topPanel.setTopCenterTitle("$AppList.");

        wrapReturn(new Refresh(application), new Refresh(topPanel), new ToEvent(ServiceMethodContext.TARGET_SELF, EventContext.EVENT_CLOSE));
    }

}
