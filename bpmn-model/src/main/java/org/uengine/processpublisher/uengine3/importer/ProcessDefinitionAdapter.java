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
    @Override
    public ProcessDefinition convert(ProcessDefinition src, Hashtable keyedContext) throws Exception {

        keyedContext.put("root", src);

        for(Activity activity : src.getChildActivities()){

            Adapter adapter = AdapterUtil.getAdapter(activity.getClass(), getClass());
            Activity activity5 = (Activity) adapter.convert(src, keyedContext);

        }

        return null;
    }
}
