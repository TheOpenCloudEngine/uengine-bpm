package org.uengine.processpublisher.uengine3.importer;

import org.uengine.kernel.*;
import org.uengine.kernel.bpmn.Gateway;
import org.uengine.kernel.bpmn.SequenceFlow;
import org.uengine.modeling.ElementView;
import org.uengine.processpublisher.*;

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
        MigUtils.setParallelStructure(true);
        boolean makeEndGateway = this.makeEndGateway(complexActivity);

        Gateway startGateway = this.createStartGateway();
        Gateway endGateway = this.createEndGateway();

        ProcessDefinition processDefinition5 = (ProcessDefinition) keyedContext.get("root");

        //tracingtag duplication preventation
        startGateway.setTracingTag(complexActivity.getTracingTag());
        startGateway.getElementView().setId(startGateway.getTracingTag());
        if(makeEndGateway) {
            endGateway.setTracingTag(MigUtils.getNewTracingTag());
            endGateway.getElementView().setId(endGateway.getTracingTag());
        }
        processDefinition5.addChildActivity(startGateway);
        if(makeEndGateway) {
            processDefinition5.addChildActivity(endGateway);
        }
        keyedContext.put("root", processDefinition5);


        //전 액티비티가 트랜지션 없이 끝난경우 시퀀스플로우로 전 액티비티연결
        if( MigUtils.isDrawLinePreActivity()) {
            for(Activity preActivity : MigUtils.getPreAcitivities()) {
                SequenceFlow sequenceFlowActivity = new SequenceFlow();
                sequenceFlowActivity.setSourceRef(preActivity.getTracingTag());
                sequenceFlowActivity.setTargetRef(complexActivity.getTracingTag());
                processDefinition5.addSequenceFlow(sequenceFlowActivity);
            }
        }

        //스위치액티비티인 경우 전단계액티비티에 저장
        if( complexActivity.getClass().getName().equals(org.uengine.kernel.SwitchActivity.class.getName())) {
            MigUtils.removeAllPreActivities();
            MigUtils.addPreActivities(complexActivity);
        }

        int initialIndexX = Index.indexX.get();
        int i=0;

        //SwitchActivity의 경우, child 액티비티 기준으로 라이 드로우
        MigUtils.setDrawLinePreActivity(false);

        for(Activity activity : complexActivity.getChildActivities()){
            System.out.println("   complexActivity.getChildActivities.trcTAG : " + activity.getTracingTag());
            Index.indexX.set(initialIndexX);

            Adapter adapter = AdapterUtil.getAdapter(activity.getClass(), getClass());
            keyedContext.put("root", processDefinition5);
            processDefinition5 = (ProcessDefinition) adapter.convert(activity, keyedContext);

            //시퀀스 액티비티의 경우 처리 없음
            if(activity.getClass().getName().equals(org.uengine.kernel.SequenceActivity.class.getName())){
                 continue;
            }
            //link from startgateway to activity
            SequenceFlow sequenceFlowFromStartGatewayToActivity = new SequenceFlow();

            SwitchActivity switchActivity = (SwitchActivity) complexActivity;

            ExpressionEvaluateCondition expressionEvaluateCondition = new ExpressionEvaluateCondition();
            expressionEvaluateCondition.setConditionExpression(switchActivity.getConditions()[i].toString());
            sequenceFlowFromStartGatewayToActivity.setCondition(expressionEvaluateCondition);

            //set transtion
            sequenceFlowFromStartGatewayToActivity.setSourceRef(startGateway.getTracingTag());
            sequenceFlowFromStartGatewayToActivity.setTargetRef(activity.getTracingTag());
            sequenceFlowFromStartGatewayToActivity.setRelationView(sequenceFlowFromStartGatewayToActivity.createView());
            sequenceFlowFromStartGatewayToActivity.setName(switchActivity.getConditions()[i].getDescription().toString());

            processDefinition5.addSequenceFlow(sequenceFlowFromStartGatewayToActivity);

            if(makeEndGateway) {
                //link from activity to endgateway
                SequenceFlow sequenceFlowToEndGatewayToActivity = new SequenceFlow();

                //set transtion
                sequenceFlowToEndGatewayToActivity.setSourceRef(activity.getTracingTag());
                sequenceFlowToEndGatewayToActivity.setTargetRef(endGateway.getTracingTag());
                sequenceFlowToEndGatewayToActivity.setRelationView(sequenceFlowToEndGatewayToActivity.createView());

                processDefinition5.addSequenceFlow(sequenceFlowToEndGatewayToActivity);

            }else{
                //END GATEWAY가 만들어지지 않은 경우
                MigUtils.setDrawLinePreActivity(false);
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
        }

        MigUtils.setParallelStructure(false);

        return processDefinition5;
    }
}
