package org.uengine.processpublisher.uengine3.importer;

import org.uengine.kernel.*;
import org.uengine.kernel.bpmn.Gateway;
import org.uengine.kernel.bpmn.SequenceFlow;
import org.uengine.modeling.ElementView;
import org.uengine.modeling.layout.GridLayout;
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

    public final static int SwitchActivity_WIDTH = 30;
    public final static int SwitchActivity_HEIGHT = 30;


    protected Gateway createSourceGateway(){

        return new Gateway();
    }
    protected Gateway createStartGateway() throws Exception {
        Gateway gateWay = createSourceGateway();
        ElementView elementView = gateWay.createView();
        elementView.setWidth(SwitchActivity_WIDTH);
        elementView.setHeight(SwitchActivity_HEIGHT);
        gateWay.setElementView(elementView);

        return gateWay;
    }

    protected Gateway createEndGateway() throws Exception {
        return createStartGateway();
    }


    private boolean makeEndGateway(ComplexActivity complexActivity) {
        boolean rv = true;
        for(Activity activity : complexActivity.getChildActivities()){
            if(activity.getClass().getName().equals(org.uengine.kernel.SwitchActivity.class.getName())
                    || activity.getClass().getName().equals(org.uengine.kernel.SequenceActivity.class.getName())){
                rv = false;
                continue;
            }
            rv = true;
        }
        return rv;
    }

    @Override
    public ConvertedContext convert(ComplexActivity complexActivity, Hashtable keyedContext) throws Exception {

        ConvertedContext convertedContext = new ConvertedContext();

        Gateway startGateway = this.createStartGateway();
        Gateway endGateway = this.createEndGateway();

        convertedContext.setInActivity(startGateway);
        convertedContext.setOutActivity(endGateway);
        convertedContext.setLayout(new GridLayout());


        ProcessDefinition processDefinition5 = (ProcessDefinition) keyedContext.get("root");

        //tracingtag duplication preventation
        startGateway.setTracingTag(complexActivity.getTracingTag());
        startGateway.getElementView().setId(startGateway.getTracingTag());
        endGateway.setTracingTag(MigUtils.getNewTracingTag());
        endGateway.getElementView().setId(endGateway.getTracingTag());

        processDefinition5.addChildActivity(startGateway);
        processDefinition5.addChildActivity(endGateway);

        keyedContext.put("root", processDefinition5);


        int i=0;
        for(Activity activity : complexActivity.getChildActivities()){

            Adapter adapter = AdapterUtil.getAdapter(activity.getClass(), getClass());
            keyedContext.put("root", processDefinition5);

            ConvertedContext childConvertedContext = (ConvertedContext) adapter.convert(activity, keyedContext);

            //link from startgateway to activity
            SequenceFlow sequenceFlowFromStartGatewayToActivity = new SequenceFlow();

            SwitchActivity switchActivity = (SwitchActivity) complexActivity;

//            ExpressionEvaluateCondition expressionEvaluateCondition = new ExpressionEvaluateCondition();
//            expressionEvaluateCondition.setConditionExpression(switchActivity.getConditions()[i].toString());
//            sequenceFlowFromStartGatewayToActivity.setCondition(expressionEvaluateCondition);

            //set transtion
            sequenceFlowFromStartGatewayToActivity.setSourceRef(startGateway.getTracingTag());
            sequenceFlowFromStartGatewayToActivity.setTargetRef(activity.getTracingTag());
            sequenceFlowFromStartGatewayToActivity.setRelationView(sequenceFlowFromStartGatewayToActivity.createView());
//            sequenceFlowFromStartGatewayToActivity.setName(switchActivity.getConditions()[i].getDescription().toString());

            processDefinition5.addSequenceFlow(sequenceFlowFromStartGatewayToActivity);

            //link from activity to endgateway
            SequenceFlow sequenceFlowToEndGatewayToActivity = new SequenceFlow();

            //set transtion
            sequenceFlowToEndGatewayToActivity.setSourceRef(activity.getTracingTag());
            sequenceFlowToEndGatewayToActivity.setTargetRef(endGateway.getTracingTag());
            sequenceFlowToEndGatewayToActivity.setRelationView(sequenceFlowToEndGatewayToActivity.createView());

            processDefinition5.addSequenceFlow(sequenceFlowToEndGatewayToActivity);

            if(childConvertedContext.getLayout()==null)
                convertedContext.getLayout().add(activity.getElementView());
            else
                convertedContext.getLayout().add(childConvertedContext.getLayout());

            i++;
        }

        return convertedContext;
    }
}
