package org.uengine.kernel.handler;

import org.uengine.kernel.*;
import org.uengine.kernel.bpmn.SubProcess;
import org.uengine.social.SocialBPMWorkItemHandler;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jangjinyoung on 2016. 12. 29..
 */
public class SubParameterSelectionActivityHandler extends SocialBPMWorkItemHandler {

    //List<ParameterSelectionPanel> parameterSelectionPanels;

    List valuesInMain;
        public List getValuesInMain() {
            return valuesInMain;
        }
        public void setValuesInMain(List valuesInMain) {
            this.valuesInMain = valuesInMain;
        }



    @Override
    public void load() throws Exception {
        super.load();

        SubParamaterValueSelectionActivity subParamaterValueSelectionActivity;
        ProcessInstance processInstance = processManager.getProcessInstance(getInstanceId());
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

        List mainValues = new ArrayList();
        do{
            mainValues.add(valuesInTheMainProcess.getValue());
        }while(valuesInTheMainProcess.next());

        setValuesInMain(mainValues);

    }

    boolean selectAll;
        public boolean isSelectAll() {
            return selectAll;
        }
        public void setSelectAll(boolean selectAll) {
            this.selectAll = selectAll;
        }

}
