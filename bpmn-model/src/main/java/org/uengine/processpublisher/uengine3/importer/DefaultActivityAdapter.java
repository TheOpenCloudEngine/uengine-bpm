package org.uengine.processpublisher.uengine3.importer;

import org.uengine.kernel.*;
import org.uengine.modeling.ElementView;
import org.uengine.processpublisher.Adapter;

import java.util.Hashtable;

/**
 * Created by uengine on 2017. 6. 16..
 */
public class DefaultActivityAdapter implements Adapter<DefaultActivity, ConvertedContext> {

    @Override
    public ConvertedContext convert(DefaultActivity activity, Hashtable keyedContext) throws Exception {
        //System.out.println("===============DefaultActivityAdapter=========  : " + activity.getTracingTag());

        ElementView elementView = activity.createView();
        elementView.setId(activity.getTracingTag());
        activity.setElementView(elementView);

        ProcessDefinition processDefinition = (ProcessDefinition) keyedContext.get("root");
        processDefinition.addChildActivity(activity);

        ConvertedContext convertedContext = new ConvertedContext();
        return convertedContext;
    }
}