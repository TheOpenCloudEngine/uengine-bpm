package org.uengine.kernel.handler;

import org.metaworks.MetaworksContext;
import org.metaworks.ServiceMethodContext;
import org.metaworks.annotation.ServiceMethod;
import org.metaworks.dwr.MetaworksRemoteService;
import org.uengine.kernel.*;
import org.uengine.social.SocialBPMWorkItemHandler;

import java.rmi.RemoteException;

/**
 * Created by jangjinyoung on 2016. 12. 29..
 */
public class SubParamaterValueSelectionActivityHandler extends SocialBPMWorkItemHandler {


    SubParameterValueSelector subParameterValueSelector;
        public SubParameterValueSelector getSubParameterValueSelector() {
            return subParameterValueSelector;
        }
        public void setSubParameterValueSelector(SubParameterValueSelector subParameterValueSelector) {
            this.subParameterValueSelector = subParameterValueSelector;
        }


    @Override
    public void load() throws Exception {
        super.load();


        SubParamaterValueSelectionActivity subParamaterValueSelectionActivity;
        ProcessInstance processInstance = getProcessInstance();
        subParamaterValueSelectionActivity = (SubParamaterValueSelectionActivity) processInstance.getProcessDefinition().getActivity(getTracingTag());

        SubParameterValueSelector subParameterValueSelector = new SubParameterValueSelector();
        MetaworksRemoteService.autowire(subParameterValueSelector);
        subParameterValueSelector.load(getProcessInstance(), subParamaterValueSelectionActivity.getVariableToBeSelected().getName());

        setSubParameterValueSelector(subParameterValueSelector);
    }

    @Override
    @ServiceMethod(payload = {"instanceId", "tracingTag", "subParameterValueSelector.valueSelector.sortableLists[__index==1]", "taskId", "rootInstId", "executionScope"}, /*callByContent=true,*/ when= MetaworksContext.WHEN_EDIT, validate=true, target= ServiceMethodContext.TARGET_APPEND)
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
