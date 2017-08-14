package org.uengine.processpublisher.bpmn.importer;

import org.omg.spec.bpmn._20100524.model.TTask;
import org.uengine.kernel.Activity;
import org.uengine.kernel.HumanActivity;

import java.util.Hashtable;

public class TTaskAdapter extends TFlowNodeAdapter<TTask, Activity> {

//    @Override
//    protected Activity createActivity(TTask src, Hashtable keyedContext) {
//        HumanActivity humanActivity = new HumanActivity();
//        initializeActivity(humanActivity, src);
//
//        return humanActivity;
//    }
}
