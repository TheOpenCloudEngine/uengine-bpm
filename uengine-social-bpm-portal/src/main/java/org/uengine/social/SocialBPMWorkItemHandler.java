package org.uengine.social;

import org.metaworks.annotation.ServiceMethod;
import org.metaworks.dwr.MetaworksRemoteService;
import org.uengine.codi.mw3.model.Popup;
import org.uengine.codi.mw3.model.User;
import org.uengine.codi.mw3.model.WorkItemHandler;

/**
 * Created by jjy on 2016. 1. 8..
 */
public class SocialBPMWorkItemHandler extends WorkItemHandler{

    @ServiceMethod(payload = {"instanceId", "tracingTag", "workItem"}, target = ServiceMethod.TARGET_STICK)
    public void delegate() {

        DelegatePanel delegatePanel = new DelegatePanel();
        delegatePanel.setInstanceId(getInstanceId());
        delegatePanel.setTracingTag(getTracingTag());

//        RoleUser roleUser = new RoleUser();
//        roleUser.setUser(new User());
//        roleUser.getUser().setUserId(getWorkItem().getEndpoint());
//        delegatePanel.setDelegator(roleUser);

        MetaworksRemoteService.autowire(delegatePanel);

        MetaworksRemoteService.wrapReturn(new Popup(delegatePanel));


    }
}
