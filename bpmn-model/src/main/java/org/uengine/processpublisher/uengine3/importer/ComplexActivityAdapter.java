package org.uengine.processpublisher.uengine3.importer;

import org.uengine.kernel.*;
import org.uengine.kernel.bpmn.SequenceFlow;
import org.uengine.modeling.cnv.layout.CnvFlowLayout;
import org.uengine.processpublisher.Adapter;
import org.uengine.processpublisher.AdapterUtil;

import java.util.Hashtable;

/**
 * Created by uengine on 2017. 6. 16..
 */
public class ComplexActivityAdapter implements Adapter<ComplexActivity, ConvertedContext> {

    @Override
    public ConvertedContext convert(ComplexActivity complexActivity, Hashtable keyedContext) throws Exception {
        //System.out.println("===============ComplexActivityAdapter========= : " + complexActivity.getTracingTag());
        ProcessDefinition processDefinition5 = (ProcessDefinition) keyedContext.get("root");

        ConvertedContext convertedContext = new ConvertedContext();
        convertedContext.setLayout(new CnvFlowLayout());
        ConvertedContext childConvertedContext = null;

        //set convertedContext
        String sourceRef = null, targetRef = null;
        for(Activity activity : complexActivity.getChildActivities()){
            //System.out.println("===============CA : " + activity.getTracingTag());
            Adapter adapter = AdapterUtil.getAdapter(activity.getClass(), getClass());
            keyedContext.put("root", processDefinition5);
            childConvertedContext = (ConvertedContext) adapter.convert(activity, keyedContext);

            if(childConvertedContext.getLayout()==null) {
                convertedContext.getLayout().add(activity.getElementView());
                if( sourceRef == null ) {
                    sourceRef = activity.getTracingTag();
                }
                else {
                    targetRef = activity.getTracingTag();
                }
            }else {
                convertedContext.getLayout().add(childConvertedContext.getLayout());
                if( sourceRef == null ) {
                    sourceRef = childConvertedContext.getOutActivity().getTracingTag();
                }
                else {
                    targetRef = childConvertedContext.getInActivity().getTracingTag();
                }
            }

            //set transition
            if( sourceRef != null && targetRef != null) {
                SequenceFlow sequenceFlow = new SequenceFlow();
                sequenceFlow.setSourceRef(sourceRef);
                sequenceFlow.setTargetRef(targetRef);
                processDefinition5.addSequenceFlow(sequenceFlow);
                //System.out.println(">>>>>>>>>>>>>ComplexActivityAdapter : source(" + sourceRef + ")/target(" + targetRef + ")");
                //앞단계가 layoutgroup 이면 앞단계의 outActivity의 tracingtag 설정
                sourceRef = (childConvertedContext.getLayout() == null)? targetRef:childConvertedContext.getOutActivity().getTracingTag();
                targetRef = null;

            }

        }

        return convertedContext;
    }

}
