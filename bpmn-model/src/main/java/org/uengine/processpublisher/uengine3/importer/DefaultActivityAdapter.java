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
public class DefaultActivityAdapter implements Adapter<DefaultActivity, ConvertedContext> {

    private void createView(DefaultActivity humanActivity) throws Exception {
        ElementView elementView = humanActivity.createView();

        elementView.setWidth(MigDrawPositoin.getHumanActivityWidth());
        elementView.setHeight(MigDrawPositoin.getHumanActivityHeight());
        //elementView.setId("HumanActivity_" + humanActivity.getTracingTag());
        //50은 액티비티 삭제시 elementview id = tracing tag 조건이 있음
        elementView.setId(humanActivity.getTracingTag());
        humanActivity.setElementView(elementView);
    }

    @Override
    public ConvertedContext convert(DefaultActivity activity, Hashtable keyedContext) throws Exception {
        this.createView(activity);
        ProcessDefinition processDefinition = (ProcessDefinition) keyedContext.get("root");
        processDefinition.addChildActivity(activity);

        //롤 스윔레인에 위치 설정
        Role curRole = new Role();
        if( activity instanceof  org.uengine.kernel.HumanActivity ) {
            curRole = processDefinition.getRole(((HumanActivity) activity).getRole().getName());
            //humanActivity.getElementView().setY(humanActivity.getElementView().getY() + curRole.getElementView().getY());
            if( MigUtils.isParallelStructure() ){
                curRole.getElementView().setHeight(curRole.getElementView().getHeight() + MigDrawPositoin.getHumanActivityHeight());
                curRole.getElementView().setY(curRole.getElementView().getY() + (MigDrawPositoin.getHumanActivityHeight()/2));
            }
        }
        activity.getElementView().setY(MigUtils.getYPosition(processDefinition, activity));

        //전 액티비티가 트랜지션 없이 끝난경우 시퀀스플로우로 전 액티비티연결
        if( MigUtils.isDrawLinePreActivity()) {
            for(Activity preActivity : MigUtils.getPreAcitivities()) {
                SequenceFlow sequenceFlowActivity = new SequenceFlow();

                sequenceFlowActivity.setSourceRef(preActivity.getTracingTag());
                sequenceFlowActivity.setTargetRef(activity.getTracingTag());

                processDefinition.addSequenceFlow(sequenceFlowActivity);
            }
            MigUtils.removeAllPreActivities();
            MigUtils.addPreActivities(activity);
        }

        ConvertedContext convertedContext = new ConvertedContext();
        convertedContext.setInActivity(activity);
        convertedContext.setOutActivity(activity);

        return convertedContext;
    }
}