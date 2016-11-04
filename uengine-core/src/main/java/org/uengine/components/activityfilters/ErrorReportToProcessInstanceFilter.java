package org.uengine.components.activityfilters;

import org.uengine.kernel.*;

/**
 * Created by jjy on 2016. 11. 4..
 */
public class ErrorReportToProcessInstanceFilter implements SensitiveActivityFilter{

    @Override
    public void onEvent(Activity activity, ProcessInstance instance, String eventName, Object payload) throws Exception {
        if(Activity.ACTIVITY_FAULT.equals(eventName)){
            if(instance instanceof EJBProcessInstance){
                FaultContext faultContext = (FaultContext) payload;

                ((EJBProcessInstance) instance).getProcessInstanceDAO().setInfo(faultContext.getFault().getMessage());
            }
        }
    }

    @Override
    public void beforeExecute(Activity activity, ProcessInstance instance) throws Exception {

    }

    @Override
    public void afterExecute(Activity activity, ProcessInstance instance) throws Exception {

    }

    @Override
    public void afterComplete(Activity activity, ProcessInstance instance) throws Exception {

    }

    @Override
    public void onPropertyChange(Activity activity, ProcessInstance instance, String propertyName, Object changedValue) throws Exception {

    }

    @Override
    public void onDeploy(ProcessDefinition definition) throws Exception {

    }
}
