package org.uengine.processpublisher.bpmn.exporter;

import org.omg.spec.bpmn._20100524.model.TUserTask;
import org.uengine.kernel.HumanActivity;
import org.uengine.processpublisher.Adapter;
import org.uengine.processpublisher.ObjectFactoryUtil;
import java.util.Hashtable;

/**
 * Created by uengine on 2015. 8. 14..
 */
public class HumanActivityAdapter implements Adapter<HumanActivity, TUserTask> {
    @Override
    public TUserTask convert(HumanActivity src, Hashtable keyedContext) throws Exception {
        // setting HumanActivity
        TUserTask tUserTask = ObjectFactoryUtil.createBPMNObject(TUserTask.class);
        tUserTask.setId(src.getElementView().getId());
        tUserTask.setName(src.getName());

        return tUserTask;
    }
}
