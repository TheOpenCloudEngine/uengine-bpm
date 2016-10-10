package org.uengine.kernel;

/**
 * Created by jjy on 2016. 10. 7..
 */
public class ActivityCompleter extends ProcessExecutionThread{

    @Override
    protected void logic(ProcessInstance instance, Activity act) throws Exception {
        act.fireComplete(instance);
    }
}
