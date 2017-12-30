package org.uengine.processpublisher.uengine3.importer;

import org.uengine.kernel.*;
import org.uengine.kernel.bpmn.Gateway;
import org.uengine.kernel.bpmn.SequenceFlow;
import org.uengine.modeling.ElementView;
import org.uengine.modeling.cnv.layout.CnvFlowLayout;
import org.uengine.modeling.cnv.layout.CnvGridLayout;
import org.uengine.modeling.cnv.layout.CnvLayoutGroup;
import org.uengine.modeling.layout.LayoutGroup;
import org.uengine.processpublisher.*;

import java.util.Hashtable;

/**
 * Created by uengine on 2017. 6. 16..
 */
public class SwitchActivityAdapter extends ComplexActivityAdapter {

    /**
     *
     * a --> switch (seq(b),seq(c),seq(d)) --> e
     *
     * a --> gw -> b -+-> gw --> e
     *        +--> c -+
     *        +--> d -+
     *
     *
     * a --> switch (b, switch (c, e, f) ,d) --> e
     *
     * a --> gw -> b --------+--> gw --> e
     *       |               |    |
     *       +-->gw -> c --+-gw --+
     *       |   +---> e --+       |
     *       |   +---> f --+       |
     *       +-----> d ------------+
     *
     *
     */
    private Gateway createGateway(String tracingTag){
        Gateway gateway = new Gateway();
        gateway.setTracingTag(tracingTag);
        gateway.setName("");

        ElementView elementView = gateway.createView();
        elementView.setId(tracingTag);
        gateway.setElementView(elementView);

        return gateway;
    }

    @Override
    public ConvertedContext convert(ComplexActivity complexActivity, Hashtable keyedContext) throws Exception {
        ProcessDefinition processDefinition5 = (ProcessDefinition) keyedContext.get("root");

        //make startGateway
        Gateway startGateway = createGateway(complexActivity.getTracingTag());

        //make endGateway
        Gateway endGateway = createGateway(MigUtils.getNewTracingTag());
        //System.out.println("-======================== SwitchActivityAdapter : " + complexActivity.getTracingTag() + " - " + endGateway.getTracingTag());

        //init ConvertedContext
        ConvertedContext convertedContext = new ConvertedContext();
        convertedContext.setLayout(new CnvFlowLayout());

        convertedContext.setInActivity(startGateway);
        convertedContext.setOutActivity(endGateway);

        convertedContext.getLayout().add(startGateway.getElementView());

        //ADD start anc end Gateway
        processDefinition5.addChildActivity(startGateway);
        processDefinition5.addChildActivity(endGateway);

        keyedContext.put("root", processDefinition5);

        int i=0;
        //Switch activity 의 경우 flow-grid-flow형식 layout 3단계설정(2단계 설정)
        ConvertedContext gridConvertedContext = new ConvertedContext();
        gridConvertedContext.setLayout(new CnvGridLayout());

        SequenceFlow sequenceFlow = null;
        ExpressionEvaluateCondition expressionEvaluateCondition = null;

        for(Activity activity : complexActivity.getChildActivities()){

            Adapter adapter = AdapterUtil.getAdapter(activity.getClass(), getClass());
            keyedContext.put("root", processDefinition5);

            ConvertedContext childConvertedContext = (ConvertedContext) adapter.convert(activity, keyedContext);

            //SwitchActivity는 child별로 layout grouping
            if(childConvertedContext.getLayout()==null) {
                childConvertedContext.setLayout(new CnvFlowLayout());
                childConvertedContext.getLayout().add(activity.getElementView());
            }

            gridConvertedContext.getLayout().add(childConvertedContext.getLayout());

            //set SequenceFlow
            sequenceFlow = new SequenceFlow();
            expressionEvaluateCondition = new ExpressionEvaluateCondition();
            expressionEvaluateCondition.setConditionExpression(((SwitchActivity)complexActivity).getConditions()[i].toString());
            sequenceFlow.setCondition(expressionEvaluateCondition);
            sequenceFlow.setSourceRef(startGateway.getTracingTag());
            sequenceFlow.setTargetRef(childConvertedContext.getLayout().getElemnetId(1));
            sequenceFlow.setName(((SwitchActivity)complexActivity).getConditions()[i].getDescription().toString());
            processDefinition5.addSequenceFlow(sequenceFlow);
            //System.out.println(">>>>>>>>>>>>>SwitchActivityAdapter(1) : source(" + sequenceFlow.getSourceRef() + ")/target(" + sequenceFlow.getTargetRef() + ")");

            sequenceFlow = new SequenceFlow();
            sequenceFlow.setSourceRef(childConvertedContext.getLayout().getLastElementIdInFirstGroup());
            sequenceFlow.setTargetRef(endGateway.getTracingTag());
            processDefinition5.addSequenceFlow(sequenceFlow);
            //System.out.println(">>>>>>>>>>>>>SwitchActivityAdapter(2) : source(" + sequenceFlow.getSourceRef() + ")/target(" + sequenceFlow.getTargetRef() + ")");


            i++;
        }
        //2단계 layout group 설정
        convertedContext.getLayout().add(gridConvertedContext.getLayout());
        //endGateway elementView 설정
        convertedContext.getLayout().add(endGateway.getElementView());

        //otherwise생성 (=Switch인데 조건이 하나밖에 없는경우 )
        if( i == 1 ){
            sequenceFlow = new SequenceFlow();
            sequenceFlow.setSourceRef(startGateway.getTracingTag());
            sequenceFlow.setTargetRef(endGateway.getTracingTag());
            sequenceFlow.setName("Otherwise");
            sequenceFlow.setOtherwise(true);

            processDefinition5.addSequenceFlow(sequenceFlow);
        }

        return convertedContext;
    }



}
