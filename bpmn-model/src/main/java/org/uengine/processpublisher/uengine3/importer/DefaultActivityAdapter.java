package org.uengine.processpublisher.uengine3.importer;

import org.metaworks.annotation.Default;
import org.uengine.kernel.*;
import org.uengine.kernel.bpmn.SequenceFlow;
import org.uengine.modeling.ElementView;
import org.uengine.processpublisher.Adapter;
import org.uengine.processpublisher.Index;
import org.uengine.processpublisher.MigDrawPositoin;
import org.uengine.processpublisher.MigUtils;

import java.util.Hashtable;

/**
 * Created by uengine on 2017. 6. 16..
 */
public class DefaultActivityAdapter implements Adapter<DefaultActivity, ProcessDefinition> {

    private void createView(DefaultActivity humanActivity) throws Exception {
        int indexX = Index.indexX.get();
        int indexY = Index.indexY.get();
        ElementView elementView = humanActivity.createView();
        elementView.setX(MigDrawPositoin.getStartEventXPosition() + (MigDrawPositoin.getActivityIntervalX()*indexX));

        elementView.setWidth(MigDrawPositoin.getHumanActivityWidth());
        elementView.setHeight(MigDrawPositoin.getHumanActivityHeight());
        elementView.setId("HumanActivity_" + humanActivity.getTracingTag());
        humanActivity.setElementView(elementView);
        Index.indexX.set(indexX + 1);
    }

    @Override
    public ProcessDefinition convert(DefaultActivity humanActivity, Hashtable keyedContext) throws Exception {
        this.createView(humanActivity);
        ProcessDefinition processDefinition = (ProcessDefinition) keyedContext.get("root");
        processDefinition.addChildActivity(humanActivity);

        //롤 스윔레인에 위치 설정
        Role curRole = new Role();
        if( humanActivity instanceof  org.uengine.kernel.HumanActivity ) {
            curRole = processDefinition.getRole(((HumanActivity) humanActivity).getRole().getName());
            //humanActivity.getElementView().setY(humanActivity.getElementView().getY() + curRole.getElementView().getY());
            if( MigUtils.isParallelStructure() ){
                curRole.getElementView().setHeight(curRole.getElementView().getHeight() + MigDrawPositoin.getHumanActivityHeight());
                curRole.getElementView().setY(curRole.getElementView().getY() + (MigDrawPositoin.getHumanActivityHeight()/2));
            }
        }
        humanActivity.getElementView().setY(MigUtils.getYPosition(processDefinition, humanActivity));

        //전 액티비티가 트랜지션 없이 끝난경우 시퀀스플로우로 전 액티비티연결
        if( MigUtils.isDrawLinePreActivity()) {
            for(Activity preActivity : MigUtils.getPreAcitivities()) {
                SequenceFlow sequenceFlowActivity = new SequenceFlow();

                sequenceFlowActivity.setSourceRef(preActivity.getTracingTag());
                sequenceFlowActivity.setTargetRef(humanActivity.getTracingTag());

                processDefinition.addSequenceFlow(sequenceFlowActivity);
            }
            MigUtils.removeAllPreActivities();
            MigUtils.addPreActivities(humanActivity);
        }

        return processDefinition;
    }
}