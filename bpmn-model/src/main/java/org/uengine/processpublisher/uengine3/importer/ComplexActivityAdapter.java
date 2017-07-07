package org.uengine.processpublisher.uengine3.importer;

import org.uengine.kernel.*;
import org.uengine.kernel.bpmn.FlowActivity;
import org.uengine.processpublisher.Adapter;
import org.uengine.processpublisher.AdapterUtil;
import org.uengine.processpublisher.Index;

import java.util.Hashtable;

/**
 * Created by uengine on 2017. 6. 16..
 */
public class ComplexActivityAdapter implements Adapter<ComplexActivity, FlowActivity> {

    @Override
    public ProcessDefinition convert(ComplexActivity complexActivity, Hashtable keyedContext) throws Exception {
        //
        ProcessDefinition processDefinition5 = (ProcessDefinition) keyedContext.get("root");
        int initialIndexX = Index.indexX.get();
        for(Activity activity : complexActivity.getChildActivities()){
            Index.indexX.set(initialIndexX);
            Adapter adapter = AdapterUtil.getAdapter(activity.getClass(), getClass());
            keyedContext.put("root", processDefinition5);
            processDefinition5 = (ProcessDefinition) adapter.convert(activity, keyedContext);
        }

        Index.indexY.set(Index.indexY.get() + 1);
        return processDefinition5;
    }
}
