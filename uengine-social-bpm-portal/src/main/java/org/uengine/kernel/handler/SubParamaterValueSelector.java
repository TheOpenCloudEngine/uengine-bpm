package org.uengine.kernel.handler;

import org.metaworks.MetaworksContext;
import org.metaworks.ServiceMethodContext;
import org.metaworks.annotation.ServiceMethod;
import org.metaworks.model.SortableElement;
import org.metaworks.model.SortableList;
import org.metaworks.model.SortableListGroup;
import org.springframework.beans.factory.annotation.Autowired;
import org.uengine.kernel.*;
import org.uengine.processmanager.ProcessManagerRemote;
import org.uengine.social.SocialBPMWorkItemHandler;

import java.rmi.RemoteException;
import java.util.ArrayList;

/**
 * Created by jangjinyoung on 2016. 12. 29..
 */
public class SubParamaterValueSelector{

    SortableListGroup valueSelector;
        public SortableListGroup getValueSelector() {
            return valueSelector;
        }
        public void setValueSelector(SortableListGroup valueSelector) {
            this.valueSelector = valueSelector;
        }

    String instanceId;
        public String getInstanceId() {
            return instanceId;
        }
        public void setInstanceId(String instanceId) {
            this.instanceId = instanceId;
        }

    String variableName;
        public String getVariableName() {
            return variableName;
        }
        public void setVariableName(String variableName) {
            this.variableName = variableName;
        }



    public void load(ProcessInstance processInstance, String variableName) throws Exception {
        setInstanceId(processInstance.getInstanceId());
        setVariableName(variableName);

        final ProcessVariable processVariable = processInstance.getProcessDefinition().getProcessVariable(variableName);
        ProcessVariableValue valuesInTheSubProcess = processVariable.getMultiple(processInstance, "");

        ProcessVariableValue valuesInTheMainProcess =

                (ProcessVariableValue) new InParentExecutionScope(){

                    @Override
                    public Object logic(ProcessInstance instance) throws Exception {
                        return processVariable.getMultiple(instance, "");
                    }

                }.run(processInstance);

        SortableList mainValues = new SortableList();
        mainValues.setElements(new ArrayList<SortableElement>());
        int index = 0;
        do{
            SortableElement sortableElement = new SortableElement();
            sortableElement.setValue(valuesInTheMainProcess.getValue());
            sortableElement.setId(""+ index);
            mainValues.getElements().add(sortableElement);

            index ++;
        }while(valuesInTheMainProcess.next());

        mainValues.setTitle("All " + processVariable.getName());


        SortableList subValues = new SortableList();
        subValues.setTitle("Selected " + processVariable.getName());

        setValueSelector(new SortableListGroup());
        getValueSelector().setSortableLists(new ArrayList<SortableList>());

        getValueSelector().getSortableLists().add(mainValues);
        getValueSelector().getSortableLists().add(subValues);

    }

    boolean selectAll;
        public boolean isSelectAll() {
            return selectAll;
        }
        public void setSelectAll(boolean selectAll) {
            this.selectAll = selectAll;
        }

    public void narrowValue() throws RemoteException, ClassNotFoundException, Exception {
        SubParamaterValueSelectionActivity subParamaterValueSelectionActivity;
        ProcessInstance processInstance = processManager.getProcessInstance(getInstanceId());
        final ProcessVariable processVariable = processInstance.getProcessDefinition().getProcessVariable(variableName);


        SortableList subValueSortableList = getValueSelector().getSortableLists().get(0);

        if(subValueSortableList!=null && subValueSortableList.getElements()!=null && subValueSortableList.getElements().size() > 0) {

            ProcessVariableValue selectedVariablePointers = new ProcessVariableValue();

            for (SortableElement element : subValueSortableList.getElements()) {
                int variableIndex = Integer.parseInt(element.getId());

                VariablePointer variablePointer = new VariablePointer();
                variablePointer.setIndex(variableIndex);
                variablePointer.setKey(getVariableName());

                if(processInstance.getExecutionScopeContext()!=null)
                    variablePointer.setExecutionScope(processInstance.getParentExecutionScopeOf(processInstance.getExecutionScopeContext().getExecutionScope()));

                selectedVariablePointers.setValue(variablePointer);
                selectedVariablePointers.moveToAdd();
            }

            processVariable.set(processInstance, "", selectedVariablePointers);
        }
    }

    @Autowired
    public ProcessManagerRemote processManager;


}
