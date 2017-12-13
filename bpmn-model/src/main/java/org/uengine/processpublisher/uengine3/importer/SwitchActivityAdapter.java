package org.uengine.processpublisher.uengine3.importer;

import org.uengine.kernel.*;
import org.uengine.kernel.bpmn.Gateway;
import org.uengine.kernel.bpmn.SequenceFlow;
import org.uengine.modeling.ElementView;
import org.uengine.processpublisher.*;

import java.util.Enumeration;
import java.util.Hashtable;

/**
 * Created by uengine on 2017. 6. 16..
 */
public class SwitchActivityAdapter extends ComplexActivityAdapter {

    /**
     *
     * a --> switch (b,c,d) --> e
     *
     * a --> gw -> b -+-> gw --> e
     *        +--> c -+
     *        +--> d -+
     */

    public final static int SwitchActivity_WIDTH = 30;
    public final static int SwitchActivity_HEIGHT = 30;

    private Gateway createStartGateway() throws Exception {
        int indexX = Index.indexX.get();
        int indexY = Index.indexY.get();
        Gateway gateWay = new Gateway();
        ElementView elementView = gateWay.createView();
        elementView.setX(MigDrawPositoin.getStartEventXPosition() + (MigDrawPositoin.getActivityIntervalX()*indexX));
//        elementView.setY(MigDrawPositoin.getStartEventYPosition());
        elementView.setWidth(SwitchActivity_WIDTH);
        elementView.setHeight(SwitchActivity_HEIGHT);
        gateWay.setElementView(elementView);
        Index.indexX.set(indexX + 1);
        //if(MigUtils.isParallelStructure()) {
          //  Index.indexY.set(indexY + 1);
        //}
        return gateWay;
    }

    private Gateway createEndGateway() throws Exception {
        int indexX = Index.indexX.get();
        int indexY = Index.indexY.get();
        Gateway gateWay = new Gateway();
        ElementView elementView = gateWay.createView();
        //elementView.setX(MigDrawPositoin.getStartEventXPosition() + (MigDrawPositoin.getActivityIntervalX()*indexX));
        //elementView.setY(MigDrawPositoin.getStartEventYPosition() + (MigDrawPositoin.getActivityIntervalY()*indexY));
        elementView.setWidth(SwitchActivity_WIDTH);
        elementView.setHeight(SwitchActivity_HEIGHT);
        gateWay.setElementView(elementView);
        //Index.indexX.set(indexX + 1);
        return gateWay;
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
    public ProcessDefinition convert(ComplexActivity complexActivity, Hashtable keyedContext) throws Exception {
        System.out.println("<<--------------SwitchActivity.trcTAG : " + complexActivity.getTracingTag());
        MigUtils.setDrawingSwitchActicity(true);
        boolean makeEndGateway = this.makeEndGateway(complexActivity);
        boolean makeOtherwise = false;
        
        Gateway startGateway = this.createStartGateway();
        Gateway endGateway = this.createEndGateway();

        ProcessDefinition processDefinition5 = (ProcessDefinition) keyedContext.get("root");

        //tracingtag duplication preventation
        startGateway.setTracingTag(complexActivity.getTracingTag());
        startGateway.getElementView().setId(startGateway.getTracingTag());
        
        processDefinition5.addChildActivity(startGateway);
        keyedContext.put("root", processDefinition5);
        
        //액티비티간연결
        //StartGateWay가 Target 연결이 안된 경우
        if( MigUtils.isNotTargetTransition(processDefinition5, startGateway.getTracingTag())){
            Enumeration<Activity> enumeration = processDefinition5.getWholeChildActivities().elements();
            while(enumeration.hasMoreElements()){
                Activity activity = (Activity)enumeration.nextElement();
                if(activity.getTracingTag().equals(startGateway.getTracingTag())==false){
                    SequenceFlow sequenceFlow = new SequenceFlow();
                    
                    sequenceFlow.setSourceRef(activity.getTracingTag());
                    sequenceFlow.setTargetRef(startGateway.getTracingTag());
                    sequenceFlow.setRelationView(sequenceFlow.createView());
                    
                    processDefinition5.addSequenceFlow(sequenceFlow);
                }
            }
        }


        int initialIndexX = Index.indexX.get();
        int i=0;

        for(Activity activity : complexActivity.getChildActivities()){
            System.out.println("   complexActivity.getChildActivities.trcTAG : " + activity.getTracingTag());
            Index.indexX.set(initialIndexX);

            Adapter adapter = AdapterUtil.getAdapter(activity.getClass(), getClass());
            keyedContext.put("root", processDefinition5);
            processDefinition5 = (ProcessDefinition) adapter.convert(activity, keyedContext);

            //시퀀스 액티비티의 경우 처리 없음
            if(activity.getClass().getName().equals(org.uengine.kernel.SequenceActivity.class.getName())){
                 MigUtils.setParentSequenceActivity(true);
            }
            
            //link from startgateway to activity
            SequenceFlow sequenceFlowFromStartGatewayToActivity = new SequenceFlow();

            SwitchActivity switchActivity = (SwitchActivity) complexActivity;

            ExpressionEvaluateCondition expressionEvaluateCondition = new ExpressionEvaluateCondition();
            expressionEvaluateCondition.setConditionExpression(switchActivity.getConditions()[i].toString());
            sequenceFlowFromStartGatewayToActivity.setCondition(expressionEvaluateCondition);

            //condition size=1이면 otherwise강제생성
            if(switchActivity.getConditions().length == 1){
                makeOtherwise = true;
            }
            
            //set transtion
            sequenceFlowFromStartGatewayToActivity.setSourceRef(startGateway.getTracingTag());
            if(MigUtils.isParentSequenceActivity() && activity instanceof SequenceActivity){
                sequenceFlowFromStartGatewayToActivity.setTargetRef(
                        ((SequenceActivity)activity).getChildActivities().get(0).getTracingTag());
                
            }else{
                sequenceFlowFromStartGatewayToActivity.setTargetRef(activity.getTracingTag());
            }
            sequenceFlowFromStartGatewayToActivity.setTargetRef(activity.getTracingTag());
            sequenceFlowFromStartGatewayToActivity.setRelationView(sequenceFlowFromStartGatewayToActivity.createView());
            sequenceFlowFromStartGatewayToActivity.setName(switchActivity.getConditions()[i].getDescription().toString());

            processDefinition5.addSequenceFlow(sequenceFlowFromStartGatewayToActivity);

            if(makeEndGateway) {
                if( i == 0){
                    endGateway.setTracingTag(MigUtils.getNewTracingTag());
                    endGateway.getElementView().setId(endGateway.getTracingTag());
                    processDefinition5.addChildActivity(endGateway);
                 }
                
                //link from activity to endgateway
                SequenceFlow sequenceFlowToEndGatewayToActivity = new SequenceFlow();

                //set transtion
                if(MigUtils.isParentSequenceActivity() && activity instanceof SequenceActivity){
                    sequenceFlowFromStartGatewayToActivity.setSourceRef(
                            ((SequenceActivity)activity).getChildActivities().get(
                                    ((SequenceActivity)activity).getChildActivities().size()-1).getTracingTag()
                            );
                }else{
                    sequenceFlowToEndGatewayToActivity.setSourceRef(activity.getTracingTag());
                }
                sequenceFlowToEndGatewayToActivity.setTargetRef(endGateway.getTracingTag());
                sequenceFlowToEndGatewayToActivity.setRelationView(sequenceFlowToEndGatewayToActivity.createView());

                processDefinition5.addSequenceFlow(sequenceFlowToEndGatewayToActivity);

            }
            
            i++;
        }

        //START GATE 좌표
        startGateway.getElementView().setY(MigUtils.getYPosition(processDefinition5, startGateway));

        //ENDGATE 좌표 (Y좌표는 STARTGATE와 동일)
        if( makeEndGateway) {
            endGateway.getElementView().setX(MigDrawPositoin.getStartEventXPosition() + (MigDrawPositoin.getActivityIntervalX() * Index.indexX.get()));
            endGateway.getElementView().setY(startGateway.getElementView().getY());
            Index.indexX.set(Index.indexX.get() + 1);
            
            if( makeOtherwise ){
                SequenceFlow sequenceFlowOtherwise = new SequenceFlow();
                sequenceFlowOtherwise.setSourceRef(startGateway.getTracingTag());
                sequenceFlowOtherwise.setTargetRef(endGateway.getTracingTag());
                sequenceFlowOtherwise.setRelationView(sequenceFlowOtherwise.createView());
                
                sequenceFlowOtherwise.setName("Otherwise");
                sequenceFlowOtherwise.setOtherwise(true);
                sequenceFlowOtherwise.getRelationView().setValue(MigUtils.getOtherwiseSequenceElementValue(startGateway, endGateway));
                
                processDefinition5.addSequenceFlow(sequenceFlowOtherwise);
                
            }
        }

        MigUtils.setDrawingSwitchActicity(false);
        MigUtils.setParentSequenceActivity(false);

        return processDefinition5;
    }
}
