package org.uengine.kernel.bpmn;

import org.uengine.kernel.ProcessInstance;

/**
 * Created by uengine on 2018. 3. 1..
 */
public class TimerIntermediateEvent extends TimerEvent implements IntermediateEvent{

    @Override
    public boolean onMessage(ProcessInstance instance, Object payload) throws Exception {
        if(super.onMessage(instance, payload)){
            unschedule(instance); //IntermediateEvent should triggered only once

            return true;
        }

        return false;
    }
}
