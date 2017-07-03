package org.uengine.processpublisher.uengine3.importer;

import org.uengine.kernel.*;
import org.uengine.kernel.bpmn.FlowActivity;
import org.uengine.processpublisher.Adapter;
import org.uengine.processpublisher.AdapterUtil;

import java.util.Hashtable;

/**
 * Created by uengine on 2017. 6. 16..
 */
public class ComplexActivityAdapter implements Adapter<ComplexActivity, FlowActivity> {

    @Override
    public ProcessDefinition convert(ComplexActivity src, Hashtable keyedContext) throws Exception {
        //
        ProcessDefinition processDefinition5 = (ProcessDefinition) keyedContext.get("root");
        for(Activity activity : src.getChildActivities()){
            Adapter adapter = AdapterUtil.getAdapter(activity.getClass(), getClass());
            //newKeyedContext.put("root", processDefinition5);
            //newKeyedContext.put("index", Index.getIndex());
            keyedContext.put("root", processDefinition5);
            processDefinition5 = (ProcessDefinition) adapter.convert(activity, keyedContext);
        }

        return processDefinition5;
    }
}
