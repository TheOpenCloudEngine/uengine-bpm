package org.uengine.processpublisher.uengine3.importer;

import org.metaworks.annotation.Default;
import org.uengine.kernel.*;
import org.uengine.kernel.bpmn.SequenceFlow;
import org.uengine.modeling.ElementView;
import org.uengine.processpublisher.Adapter;
import org.uengine.processpublisher.Index;
import org.uengine.processpublisher.MigDrawPositoin;
import org.uengine.processpublisher.MigUtils;

import java.util.Enumeration;
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
        //elementView.setId("HumanActivity_" + humanActivity.getTracingTag());
        //50은 액티비티 삭제시 elementview id = tracing tag 조건이 있음
        elementView.setId(humanActivity.getTracingTag());
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
            if( MigUtils.isDrawingSwitchActicity() ){
                curRole.getElementView().setHeight(curRole.getElementView().getHeight() + MigDrawPositoin.getHumanActivityHeight());
                curRole.getElementView().setY(curRole.getElementView().getY() + (MigDrawPositoin.getHumanActivityHeight()/2));
            }
        }
        humanActivity.getElementView().setY(MigUtils.getYPosition(processDefinition, humanActivity));
        
        if( MigUtils.isNotTargetTransition(processDefinition, humanActivity.getTracingTag())){
            Enumeration<Activity> enumeration = processDefinition.getWholeChildActivities().elements();
            while(enumeration.hasMoreElements()){
                Activity activity = (Activity)enumeration.nextElement();
                if( MigUtils.isNotSourceTransition(processDefinition, activity.getTracingTag())
                        && MigUtils.isNotTargetTransition(processDefinition, humanActivity.getTracingTag())
                        && ((MigUtils.isDrawingSwitchActicity() && !(activity.getClass().getName().equals(org.uengine.kernel.bpmn.Gateway.class.getName())))
                                || !MigUtils.isDrawingSwitchActicity())
                        
                        )
                {
                    if(activity.getTracingTag().equals(humanActivity.getTracingTag())==false){
                        SequenceFlow sequenceFlow = new SequenceFlow();
                        
                        sequenceFlow.setSourceRef(activity.getTracingTag());
                        sequenceFlow.setTargetRef(humanActivity.getTracingTag());
                        sequenceFlow.setRelationView(sequenceFlow.createView());
                        
                        processDefinition.addSequenceFlow(sequenceFlow);
                    }
                }
            }
        }
        return processDefinition;
    }
}