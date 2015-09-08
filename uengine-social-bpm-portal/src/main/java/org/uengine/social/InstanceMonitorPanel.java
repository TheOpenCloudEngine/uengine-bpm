package org.uengine.social;

import org.metaworks.MetaworksContext;
import org.metaworks.annotation.Face;
import org.uengine.kernel.ProcessDefinition;
import org.uengine.kernel.ProcessInstance;
import org.uengine.kernel.ProcessVariable;
import org.uengine.modeling.modeler.ProcessCanvas;
import org.uengine.modeling.modeler.ProcessModeler;
import org.uengine.processmanager.ProcessManagerRemote;
import org.uengine.uml.model.AttributeInstance;
import org.uengine.uml.model.ObjectInstance;

/**
 * Created by soo on 2015. 6. 12..
 */
@Face(ejsPath = "dwr/metaworks/genericfaces/CleanObjectFace.ejs")
public class InstanceMonitorPanel {

    ProcessModeler processModeler;
        public ProcessModeler getProcessModeler() {
            return processModeler;
        }
        public void setProcessModeler(ProcessModeler processModeler) {
            this.processModeler = processModeler;
        }

    ObjectInstance processVariables;
        public ObjectInstance getProcessVariables() {
            return processVariables;
        }
        public void setProcessVariables(ObjectInstance processVariables) {
            this.processVariables = processVariables;
        }


    public InstanceMonitorPanel(){
        ProcessModeler processModeler = new ProcessModeler();
        processModeler.setPalette(null);

        ((ProcessCanvas) processModeler.getCanvas()).setMetaworksContext(new MetaworksContext());
        ((ProcessCanvas) processModeler.getCanvas()).getMetaworksContext().setWhen("monitor");
        setProcessModeler(processModeler);
    }

    public ProcessModeler load(Long instanceId, ProcessManagerRemote processManager) throws Exception {

        ProcessInstance processInstance = processManager.getProcessInstance(String.valueOf(instanceId));
        ProcessDefinition processDefinition = processInstance.getProcessDefinition();


        getProcessModeler().setModelForMonitor(processDefinition, processInstance);

        ProcessModeler pm = getProcessModeler();

        setProcessVariables(new ObjectInstance());
        for(ProcessVariable processVariable : processInstance.getProcessDefinition().getProcessVariables()){

            AttributeInstance attributeInstance = new AttributeInstance();
            attributeInstance.setName(processVariable.getName());
            attributeInstance.setValue(processVariable.get(processInstance, ""));

            getProcessVariables().getAttributeInstanceList().add(attributeInstance);
        }

        return pm;
    }

}
