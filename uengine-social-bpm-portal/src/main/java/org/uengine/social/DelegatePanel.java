package org.uengine.social;

import org.metaworks.ContextAware;
import org.metaworks.MetaworksContext;
import org.metaworks.Remover;
import org.metaworks.annotation.Available;
import org.metaworks.annotation.Face;
import org.metaworks.annotation.Hidden;
import org.metaworks.annotation.ServiceMethod;
import org.metaworks.dwr.MetaworksRemoteService;
import org.metaworks.widget.Label;
import org.metaworks.widget.ModalWindow;
import org.springframework.beans.factory.annotation.Autowired;
import org.uengine.codi.mw3.model.RoleMappedUser;
import org.uengine.processmanager.ProcessManagerRemote;

import java.rmi.RemoteException;

/**
 * Created by jjy on 2016. 1. 8..
 */
@Face(displayName = "Delegate To ...")
public class DelegatePanel implements ContextAware{

    public DelegatePanel(){}

    public DelegatePanel(String instanceId, String tracingTag) {
        this.instanceId = instanceId;
        this.tracingTag = tracingTag;

        setDelegator(new RoleMappedUser());

        setMetaworksContext(new MetaworksContext());
        getMetaworksContext().setWhen(MetaworksContext.WHEN_EDIT);

    }

    MetaworksContext metaworksContext;
        public MetaworksContext getMetaworksContext() {
            return metaworksContext;
        }
        public void setMetaworksContext(MetaworksContext metaworksContext) {
            this.metaworksContext = metaworksContext;
        }


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

    RoleMappedUser delegator;
    @Available(when= MetaworksContext.WHEN_EDIT)
        public RoleMappedUser getDelegator() {
            return delegator;
        }
        public void setDelegator(RoleMappedUser delegator) {
            this.delegator = delegator;
        }


    @Autowired
    public ProcessManagerRemote processManagerRemote;

    @ServiceMethod(callByContent = true)
    @Available(when= MetaworksContext.WHEN_EDIT)
    public void delegate() throws RemoteException {
        processManagerRemote.delegateWorkitem(getInstanceId(), getTracingTag(), getDelegator());

        setMessage(new Label("<center><h4>Delegated. Please refresh the work-items.</h4></center>"));
        getMetaworksContext().setWhen("done");
    }

    Label message;
    @Available(when="done")
    @Face(displayName = "")
        public Label getMessage() {
            return message;
        }
        public void setMessage(Label message) {
            this.message = message;
        }



    @ServiceMethod
    @Available(when="done")
    public void dismiss(){
        MetaworksRemoteService.wrapReturn(new Remover(new ModalWindow()));
    }
}
