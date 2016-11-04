package org.uengine.social;

import org.metaworks.annotation.ServiceMethod;
import org.metaworks.dao.TransactionContext;
import org.metaworks.dwr.MetaworksRemoteService;
import org.oce.garuda.multitenancy.TenantContext;
import org.uengine.codi.mw3.model.*;
import org.uengine.kernel.GlobalContext;
import org.uengine.security.ISession;

/**
 * Created by jjy on 2016. 11. 4..
 */
public class StandaloneProcessInstanceMonitor extends ContentWindow{

    String instanceId;
        public String getInstanceId() {
            return instanceId;
        }
        public void setInstanceId(String instanceId) {
            this.instanceId = instanceId;
        }

    Session session;
        public Session getSession() {
            return session;
        }
        public void setSession(Session session) {
            this.session = session;
        }

    String accessToken;
        public String getAccessToken() {
            return accessToken;
        }
        public void setAccessToken(String accessToken) {
            this.accessToken = accessToken;
        }



    public StandaloneProcessInstanceMonitor(){

    }

    @ServiceMethod(onLoad = true, callByContent = true)
    public void load() throws Exception {

        if(getAccessToken()!=null){
            session = new Session();
            session.setUser(new User());
            session.getUser().setEmail(getAccessToken());

            Employee emp = new Employee();
            emp.setEmail(session.getUser().getEmail());
            IEmployee findEmp = emp.findByEmail();

            if (findEmp == null)
                throw new Exception("<font color=blue>Wrong User or Password! forgot?</font>");

            session.setEmployee(findEmp);
            session.fillSession();

            if(GlobalContext.multiTenant)
                new TenantContext(session.getEmployee().getGlobalCom());

            MetaworksRemoteService.getAutowiredFromClientClassMap().put(Session.class, session);
        }

        InstanceView instanceView = Instance.createInstanceView(String.valueOf(getInstanceId()));
        instanceView.setInstanceNameChanger(null); // in simulation, don't show the instance name changer.
        instanceView.setFollowers(null);

        this.setPanel(instanceView);

    }

}
