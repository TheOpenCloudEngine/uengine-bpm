package org.uengine.processadmin.debugger;

import org.metaworks.ContextAware;
import org.metaworks.MetaworksContext;
import org.metaworks.annotation.Face;
import org.metaworks.annotation.ServiceMethod;
import org.springframework.beans.factory.annotation.Autowired;
import org.uengine.kernel.ProcessInstance;
import org.uengine.modeling.resource.Serializer;
import org.uengine.processmanager.ProcessManagerRemote;

import java.util.Map;

/**
 * Created by uengine on 2017. 3. 10..
 */
public class Variables implements ContextAware{

    public Variables(){
        setMetaworksContext(new MetaworksContext());
        getMetaworksContext().setWhen(MetaworksContext.WHEN_EDIT);
    }


    @Override
    public MetaworksContext getMetaworksContext() {
        return metaworksContext;
    }

    @Override
    public void setMetaworksContext(MetaworksContext metaworksContext) {
        this.metaworksContext = metaworksContext;
    }

    MetaworksContext metaworksContext;

    public String instanceId;
        public String getInstanceId() {
            return instanceId;
        }
        public void setInstanceId(String instanceId) {
            this.instanceId = instanceId;
        }


    public String variables;
        @Face(ejsPath = "dwr/metaworks/genericfaces/richText.ejs")
        public String getVariables() {
            return variables;
        }
        public void setVariables(String variables) {
            this.variables = variables;
        }

    @ServiceMethod(payload = "instanceId")
    public void showVariables() throws Exception {
        ProcessInstance instance = processManagerRemote.getProcessInstance(getInstanceId());
        Map variables = instance.getAll();
        setVariables(Serializer.serialize(instance.getAll()));

        setMetaworksContext(new MetaworksContext());
        getMetaworksContext().setWhen(MetaworksContext.WHEN_EDIT);
    }

    @Autowired
    public ProcessManagerRemote processManagerRemote;


}
