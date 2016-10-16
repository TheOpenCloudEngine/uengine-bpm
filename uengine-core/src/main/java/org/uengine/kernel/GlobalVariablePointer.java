package org.uengine.kernel;

import java.io.Serializable;

/**
 * Created by jjy on 2016. 10. 15..
 */
public class GlobalVariablePointer extends VariablePointer {

    @Override
    public Serializable getValue(ProcessInstance instance) throws Exception {
        if(instance.getMainProcessInstanceId() == null)
            return instance.get(getKey());
        return instance.getRootProcessInstance().get(getKey());
    }

    @Override
    public void setValue(ProcessInstance instance, Serializable value) throws Exception {
        if(instance.getMainProcessInstanceId() == null){
            instance.set(getKey(), value);
            return;
        }
        instance.getRootProcessInstance().set(getKey(), value);
    }
}
