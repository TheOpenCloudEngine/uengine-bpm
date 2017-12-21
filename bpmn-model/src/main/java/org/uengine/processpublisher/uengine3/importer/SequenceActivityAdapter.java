package org.uengine.processpublisher.uengine3.importer;

import org.uengine.kernel.*;
import org.uengine.kernel.bpmn.Gateway;
import org.uengine.kernel.bpmn.SequenceFlow;
import org.uengine.modeling.ElementView;
import org.uengine.processpublisher.Adapter;
import org.uengine.processpublisher.AdapterUtil;
import org.uengine.processpublisher.Index;

import java.util.Hashtable;

public class SequenceActivityAdapter extends ComplexActivityAdapter {


//    @Override
//    public ProcessDefinition convert(ComplexActivity complexActivity, Hashtable keyedContext) throws Exception {
//        System.out.println("<<--SequenceActivityAdapter.trcTAG : " + complexActivity.getTracingTag());
//
//        String parentActivityTracingTag = complexActivity.getParentActivity().getTracingTag();
//        ProcessDefinition processDefinition5 = (ProcessDefinition) keyedContext.get("root");
//        //do not add
//        //processDefinition5.addChildActivity(complexActivity);
//
//        int initialIndexX = Index.indexX.get();
//        int i=0;
//
//        for(Activity activity : complexActivity.getChildActivities()){
//            System.out.println("   complexActivity.getChildActivities.trcTAG : " + activity.getTracingTag());
//            Index.indexX.set(initialIndexX);
//            Adapter adapter = AdapterUtil.getAdapter(activity.getClass(), getClass());
//            keyedContext.put("root", processDefinition5);
//            processDefinition5 = (ProcessDefinition) adapter.convert(activity, keyedContext);
//
//            //link
//            SequenceFlow sequenceFlow = new SequenceFlow();
//
//            //set transtion
//            sequenceFlow.setSourceRef(parentActivityTracingTag);
//            sequenceFlow.setTargetRef(activity.getTracingTag());
//            sequenceFlow.setRelationView(sequenceFlow.createView());
//
//            processDefinition5.addSequenceFlow(sequenceFlow);
//
//
//            System.out.println("------ || start:"+sequenceFlow.getSourceRef()
//                    +"  end : " +sequenceFlow.getTargetRef());
//            i++;
//        }
//
//        Index.indexY.set(Index.indexY.get() + 1);
//
//        return processDefinition5;
//    }
}
