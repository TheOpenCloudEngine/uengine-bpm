package org.uengine.social;

import org.metaworks.annotation.Hidden;
import org.metaworks.annotation.ServiceMethod;
import org.springframework.beans.factory.annotation.Autowired;
import org.uengine.codi.mw3.model.*;
import org.uengine.processmanager.ProcessManagerRemote;

import java.rmi.RemoteException;

/**
 * Created by jjy on 2016. 1. 8..
 */
public class DelegatePanel {

    String instanceId;
    @Hidden
        public String getInstanceId() {
            return instanceId;
        }
        public void setInstanceId(String instanceId) {
            this.instanceId = instanceId;
        }

    String tracingTag;
    @Hidden
        public String getTracingTag() {
            return tracingTag;
        }
        public void setTracingTag(String tracingTag) {
            this.tracingTag = tracingTag;
        }

    String roleName;
    @Hidden
        public String getRoleName() {
            return roleName;
        }
        public void setRoleName(String roleName) {
            this.roleName = roleName;
        }

    RoleUser delegator;
        public RoleUser getDelegator() {
            return delegator;
        }
        public void setDelegator(RoleUser delegator) {
            this.delegator = delegator;
        }


    @Autowired
    public ProcessManagerRemote processManagerRemote;

    @ServiceMethod(callByContent = true)
    public void delegate() throws RemoteException {
        processManagerRemote.delegateWorkitem(getInstanceId(), getTracingTag(), getDelegator());
    }
}
