package org.uengine.kernel.bpmn;

import org.uengine.kernel.Activity;
import org.uengine.kernel.ActivityEventInterceptor;
import org.uengine.kernel.ProcessInstance;

/**
 * Created by uengine on 2018. 1. 15..
 */
public class CatchingErrorEvent extends Event{

    @Override
    protected void executeActivity(final ProcessInstance instance) throws Exception {
        if(getAttachedToRef()!=null){
            Activity listeningActivity = instance.getProcessDefinition().getActivity(getAttachedToRef());
            CatchingErrorEventActivityEventInterceptor catchingErrorEventActivityEventInterceptor = new CatchingErrorEventActivityEventInterceptor();
            catchingErrorEventActivityEventInterceptor.setTracingTag(getTracingTag());
            instance.addActivityEventInterceptor(catchingErrorEventActivityEventInterceptor);
        }
    }

    @Override
    public boolean onMessage(ProcessInstance instance, Object payload) throws Exception {
        fireComplete(instance); // run the connected activity

        //let error is not fired.
        instance.getProcessTransactionContext().setSharedContext("faultTolerant", new Boolean(true));

        return true;
    }
}
