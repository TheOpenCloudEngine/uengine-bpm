package org.uengine.kernel.handler;

import org.metaworks.MetaworksContext;
import org.metaworks.ServiceMethodContext;
import org.metaworks.annotation.ServiceMethod;
import org.metaworks.dwr.MetaworksRemoteService;
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


    SubParamaterValueSelector subParamaterValueSelector;
        public SubParamaterValueSelector getSubParameterValueSelector() {
            return subParamaterValueSelector;
        }
        public void setSubParameterValueSelector(SubParamaterValueSelector subParamaterValueSelector) {
            this.subParamaterValueSelector = subParamaterValueSelector;
        }


    @Override
    public void load() throws Exception {
        super.load();


        SubParamaterValueSelectionActivity subParamaterValueSelectionActivity;
        ProcessInstance processInstance = getProcessInstance();
        subParamaterValueSelectionActivity = (SubParamaterValueSelectionActivity) processInstance.getProcessDefinition().getActivity(getTracingTag());

        SubParamaterValueSelector subParamaterValueSelector = new SubParamaterValueSelector();
        MetaworksRemoteService.autowire(subParamaterValueSelector);
        subParamaterValueSelector.load(getProcessInstance(), subParamaterValueSelectionActivity.getVariableToBeSelected().getName());

        setSubParameterValueSelector(subParamaterValueSelector);
    }

    @Override
    @ServiceMethod(payload = {"instanceId", "tracingTag", "subParamaterValueSelector.valueSelector.sortableLists[__index==1]", "taskId", "rootInstId", "executionScope"}, /*callByContent=true,*/ when= MetaworksContext.WHEN_EDIT, validate=true, target= ServiceMethodContext.TARGET_APPEND)
    public Object[] complete() throws RemoteException, ClassNotFoundException, Exception {

        SubParamaterValueSelectionActivity subParamaterValueSelectionActivity;
        ProcessInstance processInstance = getProcessInstance();
        subParamaterValueSelectionActivity = (SubParamaterValueSelectionActivity) processInstance.getProcessDefinition().getActivity(getTracingTag());

        getSubParameterValueSelector().setInstanceId(getInstanceId());
        getSubParameterValueSelector().setVariableName(subParamaterValueSelectionActivity.getVariableToBeSelected().getName());

        getSubParameterValueSelector().narrowValue();

        return super.complete();
    }
}
