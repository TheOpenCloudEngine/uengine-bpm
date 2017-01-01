package org.uengine.kernel.handler;

import org.metaworks.MetaworksContext;
import org.metaworks.ServiceMethodContext;
import org.metaworks.annotation.ServiceMethod;
import org.metaworks.model.SortableElement;
import org.metaworks.model.SortableList;
import org.metaworks.model.SortableListGroup;
import org.uengine.kernel.*;
import org.uengine.kernel.bpmn.SubProcess;
import org.uengine.social.SocialBPMWorkItemHandler;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by jangjinyoung on 2016. 12. 29..
 */
public class SubParamaterValueSelectionActivityHandler extends SocialBPMWorkItemHandler {

    //List<ParameterSelectionPanel> parameterSelectionPanels;

//    List valuesInMain;
//        public List getValuesInMain() {
//            return valuesInMain;
//        }
//        public void setValuesInMain(List valuesInMain) {
//            this.valuesInMain = valuesInMain;
//        }
//
//    SortableList valuesInMain;
//        public SortableList getValuesInMain() {
//            return valuesInMain;
//        }
//        public void setValuesInMain(SortableList valuesInMain) {
//            this.valuesInMain = valuesInMain;
//        }
//
//    SortableList valuesInSub;
//        public SortableList getValuesInSub() {
//            return valuesInSub;
//        }
//        public void setValuesInSub(SortableList valuesInSub) {
//            this.valuesInSub = valuesInSub;
//        }


    SortableListGroup valueSelector;
        public SortableListGroup getValueSelector() {
            return valueSelector;
        }
        public void setValueSelector(SortableListGroup valueSelector) {
            this.valueSelector = valueSelector;
        }


    @Override
    public void load() throws Exception {
        super.load();

        SubParamaterValueSelectionActivity subParamaterValueSelectionActivity;
        ProcessInstance processInstance = getProcessInstance();
        subParamaterValueSelectionActivity = (SubParamaterValueSelectionActivity) processInstance.getProcessDefinition().getActivity(getTracingTag());
//
//        SubProcess subProcess = (SubProcess) subParamaterValueSelectionActivity.getParentActivity();
//
//        for(SubProcessParameterContext subProcessParameterContext : subProcess.getVariableBindings()){
//
//        }

        final ProcessVariable processVariable = subParamaterValueSelectionActivity.getVariableToBeSelected();
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

        mainValues.setTitle("All " + subParamaterValueSelectionActivity.getVariableToBeSelected().getName());


        SortableList subValues = new SortableList();
        subValues.setTitle("Selected " + subParamaterValueSelectionActivity.getVariableToBeSelected().getName());
//        subValues.setElements(new ArrayList<SortableElement>());

//        SortableElement sortableElement = new SortableElement();
//        sortableElement.setValue("test");
//        subValues.getElements().add(sortableElement);

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

    @Override
    @ServiceMethod(payload = {"instanceId", "tracingTag", "valueSelector.sortableLists[__index==1]", "taskId", "rootInstId", "executionScope"}, /*callByContent=true,*/ when= MetaworksContext.WHEN_EDIT, validate=true, target= ServiceMethodContext.TARGET_APPEND)
    public Object[] complete() throws RemoteException, ClassNotFoundException, Exception {
        SubParamaterValueSelectionActivity subParamaterValueSelectionActivity;
        ProcessInstance processInstance = getProcessInstance();
        subParamaterValueSelectionActivity = (SubParamaterValueSelectionActivity) processInstance.getProcessDefinition().getActivity(getTracingTag());

        SortableList subValueSortableList = getValueSelector().getSortableLists().get(0);

        if(subValueSortableList!=null && subValueSortableList.getElements()!=null && subValueSortableList.getElements().size() > 0) {

            ProcessVariableValue selectedVariablePointers = new ProcessVariableValue();

            for (SortableElement element : subValueSortableList.getElements()) {
                int variableIndex = Integer.parseInt(element.getId());

                VariablePointer variablePointer = new VariablePointer();
                variablePointer.setIndex(variableIndex);
                variablePointer.setKey(subParamaterValueSelectionActivity.getVariableToBeSelected().getName());

                if(processInstance.getExecutionScopeContext()!=null)
                    variablePointer.setExecutionScope(processInstance.getParentExecutionScopeOf(processInstance.getExecutionScopeContext().getExecutionScope()));

                selectedVariablePointers.setValue(variablePointer);
                selectedVariablePointers.moveToAdd();
            }

            subParamaterValueSelectionActivity.getVariableToBeSelected().set(processInstance, "", selectedVariablePointers);
        }

        return super.complete();
    }
}
