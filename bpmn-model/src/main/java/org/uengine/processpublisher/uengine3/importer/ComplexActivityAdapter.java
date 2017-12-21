package org.uengine.processpublisher.uengine3.importer;

import org.uengine.kernel.*;
import org.uengine.kernel.bpmn.FlowActivity;
import org.uengine.kernel.bpmn.SequenceFlow;
import org.uengine.modeling.layout.FlowLayout;
import org.uengine.processpublisher.Adapter;
import org.uengine.processpublisher.AdapterUtil;
import org.uengine.processpublisher.Index;

import java.util.Hashtable;

/**
 * Created by uengine on 2017. 6. 16..
 */
public class ComplexActivityAdapter implements Adapter<ComplexActivity, ConvertedContext> {

    @Override
    public ConvertedContext convert(ComplexActivity complexActivity, Hashtable keyedContext) throws Exception {
        //
        ProcessDefinition processDefinition5 = (ProcessDefinition) keyedContext.get("root");
        ConvertedContext convertedContext = new ConvertedContext();
        convertedContext.setLayout(new FlowLayout());

        Activity previous = null;
        ConvertedContext childConvertedContext = null;
        for(Activity activity : complexActivity.getChildActivities()){

            Adapter adapter = AdapterUtil.getAdapter(activity.getClass(), getClass());
            keyedContext.put("root", processDefinition5);
            childConvertedContext = (ConvertedContext) adapter.convert(activity, keyedContext);

            Activity incomingActivity = childConvertedContext.getInActivity();
            Activity outgoingActivity = childConvertedContext.getOutActivity();

            if(previous==null){
                convertedContext.setInActivity(incomingActivity);
            }

            SequenceFlow sequenceFlow = new SequenceFlow();
            sequenceFlow.setSourceActivity(previous);
            sequenceFlow.setTargetActivity(incomingActivity);

            previous = outgoingActivity;

            processDefinition5.addSequenceFlow(sequenceFlow);

            if(childConvertedContext.getLayout()==null)
                convertedContext.getLayout().add(activity.getElementView());
            else
                convertedContext.getLayout().add(childConvertedContext.getLayout());


        }

        convertedContext.setOutActivity(childConvertedContext.getOutActivity());

        return convertedContext;
    }
}
