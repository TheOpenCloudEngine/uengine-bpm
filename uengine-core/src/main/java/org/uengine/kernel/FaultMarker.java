package org.uengine.kernel;

/**
 * Created by jjy on 2016. 10. 11..
 */
public class FaultMarker extends ProcessExecutionThread{

    @Override
    protected void logic(ProcessInstance instance, Activity act) throws Exception {
        act.setStatus(instance, Activity.STATUS_FAULT);
    }
}
