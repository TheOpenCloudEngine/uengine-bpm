package org.uengine.social;

import org.metaworks.MetaworksContext;
import org.metaworks.WebFieldDescriptor;
import org.metaworks.annotation.AutowiredToClient;
import org.metaworks.annotation.Face;
import org.metaworks.annotation.Hidden;
import org.metaworks.dwr.MetaworksRemoteService;
import org.uengine.kernel.ProcessDefinition;
import org.uengine.kernel.ProcessInstance;
import org.uengine.kernel.ProcessVariable;
import org.uengine.modeling.ElementViewActionDelegate;
import org.uengine.modeling.modeler.ProcessCanvas;
import org.uengine.modeling.modeler.ProcessModeler;
import org.uengine.processmanager.ProcessManagerRemote;
import org.uengine.uml.model.Attribute;
import org.uengine.uml.model.ClassDefinition;
import org.uengine.uml.model.ObjectInstance;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by soo on 2015. 6. 12..
 */
//@Face(ejsPath = "dwr/metaworks/genericfaces/CleanObjectFace.ejs")
public class InstanceMonitorPanel {

    ElementViewActionDelegate elementViewActionDelegate;
    @Hidden
    @AutowiredToClient
        public ElementViewActionDelegate getElementViewActionDelegate() {
            return elementViewActionDelegate;
        }
        public void setElementViewActionDelegate(ElementViewActionDelegate elementViewActionDelegate) {
            this.elementViewActionDelegate = elementViewActionDelegate;
        }


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

        ElementViewActionDelegateForInstanceMonitoring elementViewActionDelegateForInstanceMonitoring = MetaworksRemoteService.getComponent(ElementViewActionDelegateForInstanceMonitoring.class);

        setElementViewActionDelegate(elementViewActionDelegateForInstanceMonitoring);

        ProcessInstance processInstance = processManager.getProcessInstance(String.valueOf(instanceId));
        ProcessDefinition processDefinition = processInstance.getProcessDefinition();

        getProcessModeler().setModel(processDefinition, processInstance);

        ProcessModeler pm = getProcessModeler();

        List<Attribute> fieldDescriptors = new ArrayList<Attribute>();

        setProcessVariables(new ObjectInstance());
        for(ProcessVariable processVariable : processInstance.getProcessDefinition().getProcessVariables()){

            Serializable value = processVariable.get(processInstance, "");
            getProcessVariables().setBeanProperty(processVariable.getName(), value);

            Attribute attribute = new Attribute();
            attribute.setName(processVariable.getName());
            attribute.setClassName(value != null ? value.getClass().getName() :  Object.class.getName());
//
//            if(attribute.getClassName()==null){
//                attribute.setClassName(Object.class.getName());  //must be not null
//            }

            fieldDescriptors.add(attribute);
        }

        getProcessVariables().setClassDefinition(new ClassDefinition());

        Attribute[] dummy = new Attribute[fieldDescriptors.size()];
        fieldDescriptors.toArray(dummy);
        getProcessVariables().getClassDefinition().setFieldDescriptors(dummy);
        getProcessVariables().getClassDefinition().setName("Process Variables");

        return pm;
    }

}
