package org.uengine.processpublisher.uengine3.importer;

import org.uengine.kernel.Activity;
import org.uengine.kernel.ProcessDefinition;
import org.uengine.processpublisher.Adapter;
import org.uengine.processpublisher.AdapterUtil;

import java.util.Hashtable;

/**
 * Created by uengine on 2017. 6. 16..
 */
public class ProcessDefinitionAdapter implements Adapter<ProcessDefinition, ProcessDefinition>{

    public ProcessDefinitionAdapter(){}

    @Override
    public ProcessDefinition convert(ProcessDefinition src, Hashtable keyedContext) throws Exception {


        ProcessDefinition processDefinition = new ProcessDefinition();
        // index 초기화
        int index = 0;
        for(Activity activity : src.getChildActivities()){
            keyedContext.put("root", processDefinition);
            keyedContext.put("index", index);
            Adapter adapter = AdapterUtil.getAdapter(activity.getClass(), getClass());
            processDefinition = (ProcessDefinition) adapter.convert(activity, keyedContext);
            index++;
        }
        return processDefinition;
    }
}
