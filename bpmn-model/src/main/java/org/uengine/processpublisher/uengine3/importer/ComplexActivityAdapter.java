package org.uengine.processpublisher.uengine3.importer;

import org.uengine.kernel.Activity;
import org.uengine.kernel.ProcessDefinition;
import org.uengine.kernel.SwitchActivity;
import org.uengine.kernel.bpmn.FlowActivity;
import org.uengine.kernel.bpmn.SequenceFlow;
import org.uengine.processpublisher.Adapter;
import org.uengine.processpublisher.AdapterUtil;

import java.util.Hashtable;

/**
 * Created by uengine on 2017. 6. 16..
 */
public class ComplexActivityAdapter implements Adapter<SwitchActivity,FlowActivity> {

    @Override
    public FlowActivity convert(SwitchActivity src, Hashtable keyedContext) throws Exception {

        FlowActivity flowActivity = new FlowActivity();
        for(Activity activity : src.getChildActivities()){
            Adapter adapter = AdapterUtil.getAdapter(activity.getClass(), getClass());
            Activity activity5 = (Activity) adapter.convert(src, keyedContext);

            ProcessDefinition processDefinition5 = (ProcessDefinition) keyedContext.get("root");

            if(activity5 instanceof FlowActivity) {
                for(Activity childOfActivity5 : ((FlowActivity) activity5).getChildActivities()) {
                    processDefinition5.addChildActivity(activity5);
                }
                for(SequenceFlow sequenceFlowOfActivity5 : ((FlowActivity) activity5).getSequenceFlows()) {
                    processDefinition5.addSequenceFlow(sequenceFlowOfActivity5);
                }
            }else{
                processDefinition5.addChildActivity(activity5);
            }
        }

        return null;
    }
}
