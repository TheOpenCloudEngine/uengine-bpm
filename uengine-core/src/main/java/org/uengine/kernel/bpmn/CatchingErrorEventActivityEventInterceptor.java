package org.uengine.kernel.bpmn;

import org.uengine.kernel.Activity;
import org.uengine.kernel.ActivityEventInterceptor;
import org.uengine.kernel.ProcessInstance;

/**
 * Created by uengine on 2018. 1. 15..
 */
public class CatchingErrorEventActivityEventInterceptor implements ActivityEventInterceptor {


    String tracingTag;

        public String getTracingTag() {
            return tracingTag;
        }

        public void setTracingTag(String tracingTag) {
            this.tracingTag = tracingTag;
        }


    @Override
    public boolean interceptEvent(Activity activity, String command, ProcessInstance instance, Object payload) throws Exception {

        CatchingErrorEvent catchingErrorEvent = (CatchingErrorEvent) instance.getProcessDefinition().getActivity(getTracingTag());

        if(command.equals(Activity.ACTIVITY_FAULT) && activity.getTracingTag().equals(catchingErrorEvent.getAttachedToRef())){
            catchingErrorEvent.onMessage(instance, activity);
        }

        return false;
    }
}
