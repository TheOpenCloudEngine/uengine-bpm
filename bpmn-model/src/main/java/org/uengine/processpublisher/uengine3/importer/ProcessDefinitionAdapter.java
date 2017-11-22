package org.uengine.processpublisher.uengine3.importer;

import org.uengine.kernel.Activity;
import org.uengine.kernel.HumanActivity;
import org.uengine.kernel.ProcessDefinition;
import org.uengine.kernel.Role;
import org.uengine.kernel.bpmn.EndEvent;
import org.uengine.kernel.bpmn.SequenceFlow;
import org.uengine.kernel.bpmn.StartEvent;
import org.uengine.modeling.ElementView;
import org.uengine.processpublisher.*;

import java.util.Enumeration;
import java.util.Hashtable;

/**
 * Created by uengine on 2017. 6. 16..
 */
public class ProcessDefinitionAdapter implements Adapter<ProcessDefinition, ProcessDefinition>{

    public ProcessDefinitionAdapter(){}

    private StartEvent createStartEventView(StartEvent startEvent) throws Exception {
        int indexX = Index.indexX.get();
        int indexY = Index.indexY.get();
        ElementView elementView = startEvent.createView();
        //elementView.setViewType("org.uengine.kernel.view.DefaultActivityView");
        elementView.setShapeId("OG.shape.bpmn.A_Task");
        elementView.setX(MigDrawPositoin.getStartEventXPosition());
        elementView.setY(MigDrawPositoin.getStartEventYPosition());
        elementView.setWidth(MigDrawPositoin.getStartEventWidth());
        elementView.setHeight(MigDrawPositoin.getStartEventHeight());
        startEvent.setElementView(elementView);
        Index.indexX.set(indexX + 1);
        return startEvent;
    }


    private EndEvent createEndEventView(EndEvent endEvent) throws Exception {
        int indexX = Index.indexX.get();
        int indexY = Index.indexY.get();
        ElementView elementView = endEvent.createView();
        //elementView.setViewType("org.uengine.kernel.view.DefaultActivityView");
        elementView.setShapeId("OG.shape.bpmn.A_Task");
        elementView.setX(MigDrawPositoin.getStartEventXPosition() + (MigDrawPositoin.getActivityIntervalX()*indexX));
        elementView.setY(MigDrawPositoin.getStartEventYPosition() + (MigDrawPositoin.getActivityIntervalY()*indexY));
        elementView.setWidth(MigDrawPositoin.getEndEventWidth());
        elementView.setHeight(MigDrawPositoin.getEndEventHeight());
        endEvent.setElementView(elementView);
        Index.indexX.set(indexX + 1);
        return endEvent;
    }

    @Override
    public ProcessDefinition convert(ProcessDefinition src, Hashtable keyedContext) throws Exception {

//processdefinition
//        SequenceActivity --> adapter: org.uengine.processpublisher.uengine3.importer.SequenceActivityAdapter
//                            --> org.uengine.processpublisher.uengine3.importer.ComplexActivityAdapter
//            a;
//            b;
//        allActivity
//                switchActivity
//                    c;
//                    d;

// root.child.add(a,b,c,d);


        ProcessDefinition processDefinition = new ProcessDefinition();
        // 기존의 process definitino에서 새로 만들 process definition의 변수로 세팅한다.
        processDefinition.setName(src.getName());
        processDefinition.setProcessVariables(src.getProcessVariables());
        // index 정보 초기화
        Index.indexX.set(0);
        Index.indexY.set(0);

        //기본 롤 정보 설정
        //전체 롤을 감싸는 부모롤 작성
        Role parentRole = new Role();
        parentRole.setName(MigUtils.getParentRoleName());
        processDefinition.addRole(parentRole);
        parentRole.setElementView(parentRole.createView());
        parentRole.getElementView().setId(parentRole.getName());
        parentRole.getElementView().setX(MigDrawPositoin.getRoleXPosition());
        parentRole.getElementView().setY(MigDrawPositoin.getRoleYPosition());

        int roleCnt = 0;
        for(Role role : src.getRoles()){
            processDefinition.addRole(role);
            role.setElementView(role.createView());
            role.getElementView().setId(role.getName());
            role.getElementView().setParent(parentRole.getElementView().getId());
            role.getElementView().setX(MigDrawPositoin.getChildRoleXPosition());
            role.getElementView().setY(MigDrawPositoin.getChildRoleYPosition()+(roleCnt*MigDrawPositoin.getChildRoleHeigth()));
            role.getElementView().setWidth(MigDrawPositoin.getChildRoleWidth());
            role.getElementView().setHeight(MigDrawPositoin.getChildRoleHeigth());
            roleCnt++;
        }

        parentRole.getElementView().setHeight(roleCnt*MigDrawPositoin.getChildRoleHeigth());

        //START EVENT 액티비티 주입
        StartEvent startEvent = new StartEvent();
        this.createStartEventView(startEvent);
        startEvent.setTracingTag(MigUtils.getNewTracingTag());
        processDefinition.addChildActivity(startEvent);
        MigUtils.addPreActivities(startEvent);
        //MigUtils.setPreActivityTracingTag(startEvent.getTracingTag());
        MigUtils.setDrawLinePreActivity(true);

        // 액티비티 변환
        for(Activity activity : src.getChildActivities()){
            keyedContext.put("root", processDefinition);
            Adapter adapter = AdapterUtil.getAdapter(activity.getClass(), getClass());
            processDefinition = (ProcessDefinition) adapter.convert(activity, keyedContext);
            // 돌때마다 y값을 초기화 해주는게 맞는건지...
            Index.indexY.set(0);
        }

        //END EVENT 액티비티 주입
        EndEvent endEvent = new EndEvent();
        this.createEndEventView(endEvent);
        endEvent.setTracingTag(MigUtils.getNewTracingTag());
        processDefinition.addChildActivity(endEvent);


        //ROLE 재조정

        //부모 롤 조정
        parentRole.getElementView().setHeight(MigUtils.getRoleTotalHeight(processDefinition));
        parentRole.getElementView().setWidth((MigDrawPositoin.getChildRoleWidth() * Index.indexX.get())+MigDrawPositoin.getParentRoleNameLabelWidth());
        parentRole.getElementView().setX( parentRole.getElementView().getWidth()/2 + MigDrawPositoin.getRoleXPosition());
        parentRole.getElementView().setY( parentRole.getElementView().getHeight()/2 + MigDrawPositoin.getRoleYPosition());

        //차일드 롤 조정
        double preRoleHeight = 0;
        int loopChildCnt = 0;
        for(Role role : processDefinition.getRoles()) {
            if (role.getElementView().getParent() != null) {
                //차일드 롤
                role.getElementView().setWidth(MigDrawPositoin.getChildRoleWidth() * Index.indexX.get());
                role.getElementView().setX(role.getElementView().getWidth() / 2 + MigDrawPositoin.getRoleXPosition() + MigDrawPositoin.getParentRoleNameLabelWidth());
                role.getElementView().setY( role.getElementView().getHeight() /2 + MigDrawPositoin.getRoleYPosition() + preRoleHeight );
                preRoleHeight += role.getElementView().getHeight();
                loopChildCnt++;
            }
        }

        //휴먼액티비티 위치 조정(롤베이스)
        Enumeration<Activity> enumeration = processDefinition.getWholeChildActivities().elements();

        while(enumeration.hasMoreElements()){
            Activity activity = (Activity)enumeration.nextElement();
            //액티비티가 트래지션이 없는 경우 endEVENT에 연결
            if(MigUtils.isNotTransition(processDefinition, activity.getTracingTag())){
                if(activity.getTracingTag().equals(endEvent.getTracingTag()) == false) {
                    SequenceFlow sequenceFlow = new SequenceFlow();

                    //set transtion
                    sequenceFlow.setSourceRef(activity.getTracingTag());
                    sequenceFlow.setTargetRef(endEvent.getTracingTag());
                    sequenceFlow.setRelationView(sequenceFlow.createView());

                    processDefinition.addSequenceFlow(sequenceFlow);
                }
            }
            //휴먼 액티비티 위치 재조정
            if( activity instanceof  org.uengine.kernel.HumanActivity ) {
                activity.getElementView().setY(activity.getElementView().getY()
                        + (processDefinition.getRole(((HumanActivity) activity).getRole().getName()).getElementView().getY()/2)
                        + MigDrawPositoin.getRoleYPosition() );
            }
        }

        // thread-safe를 위해 기존의 값을 삭제한다.
        Index.indexX.remove();
        Index.indexY.remove();

        return processDefinition;
    }
}
