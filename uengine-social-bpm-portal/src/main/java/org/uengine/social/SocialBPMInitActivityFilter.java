package org.uengine.social;

import org.metaworks.dwr.MetaworksRemoteService;
import org.oce.garuda.multitenancy.TenantContext;
import org.uengine.codi.mw3.model.Session;
import org.uengine.kernel.*;

import java.io.Serializable;

/**
 * Created by jjy on 2016. 9. 17..
 */
public class SocialBPMInitActivityFilter implements SensitiveActivityFilter, Serializable {

    private static final long serialVersionUID = org.uengine.kernel.GlobalContext.SERIALIZATION_UID;

    public void afterExecute(Activity activity, final ProcessInstance instance)
            throws Exception {

    }

    public void afterComplete(Activity activity, ProcessInstance instance) throws Exception {

    }

    public void beforeExecute(Activity activity, ProcessInstance instance)
            throws Exception {

        if(TenantContext.getThreadLocalInstance()==null || TenantContext.getThreadLocalInstance().getTenantId()==null){
            new TenantContext(""+ ((EJBProcessInstance)instance).getProcessInstanceDAO().get("InitComCd"));
        }
    }

    public void onDeploy(ProcessDefinition definition) throws Exception {
    }

    public void onPropertyChange(Activity activity, ProcessInstance instance, String propertyName, Object changedValue) throws Exception {


    }

    @Override
    public void onEvent(Activity activity, ProcessInstance instance, String eventName, Object payload) throws Exception {

        // mark isSim = 1 if the process is simulation process:  this logic has been moved to ProcessMap.java
//        if(instance instanceof EJBProcessInstance){
//            EJBProcessInstance ejbProcessInstance = (EJBProcessInstance) instance;
//
//            if(ejbProcessInstance.getProcessInstanceDAO().getMainInstId()==null && "instance.beforeStart".equals(eventName)){
//
//                if(instance.getName().startsWith("[Test]")){
//                    ejbProcessInstance.getProcessInstanceDAO().set("isSim", 1);
//                }
//            }
//        }


    }
}
