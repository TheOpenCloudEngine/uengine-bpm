package org.uengine.kernel;

/**
 * Created by jjy on 2016. 10. 11..
 */
public class FaultMarker extends ProcessExecutionThread{

    @Override
    protected void logic(ProcessInstance instance, Activity act, String[] parameters) throws Exception {
        // act.setStatus(instance, Activity.STATUS_FAULT);

        if(act!=null && parameters.length > 3){
            Exception e = new Exception(parameters[3]);

            act.fireFault(instance, e);
        }
    }
}
