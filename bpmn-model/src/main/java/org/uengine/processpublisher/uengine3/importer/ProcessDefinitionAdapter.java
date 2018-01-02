package org.uengine.processpublisher.uengine3.importer;

import org.uengine.kernel.*;
import org.uengine.kernel.bpmn.EndEvent;
import org.uengine.kernel.bpmn.Gateway;
import org.uengine.kernel.bpmn.SequenceFlow;
import org.uengine.kernel.bpmn.StartEvent;
import org.uengine.modeling.ElementView;
import org.uengine.modeling.cnv.layout.CnvFlowLayout;
import org.uengine.processpublisher.*;

import java.util.Hashtable;

/**
 * Created by uengine on 2017. 6. 16..
 */
public class ProcessDefinitionAdapter extends ComplexActivityAdapter{

    public ProcessDefinitionAdapter(){}

    private StartEvent createStartEventView(StartEvent startEvent) throws Exception {
        ElementView elementView = startEvent.createView();
        elementView.setId(startEvent.getTracingTag());
        elementView.setShapeId("OG.shape.bpmn.A_Task");
        startEvent.setElementView(elementView);

        return startEvent;
    }

    private EndEvent createEndEventView(EndEvent endEvent) throws Exception {
        ElementView elementView = endEvent.createView();
        elementView.setId(endEvent.getTracingTag());
        elementView.setShapeId("OG.shape.bpmn.A_Task");
        endEvent.setElementView(elementView);
        return endEvent;
    }

    @Override
    public ConvertedContext convert(ComplexActivity complexActivity, Hashtable keyedContext) throws Exception {

        ProcessDefinition src = (ProcessDefinition) complexActivity;

        ProcessDefinition processDefinition5 = new ProcessDefinition();

        //ROLE정보 설정
        for(Role role : src.getRoles()){
            processDefinition5.addRole(role);
        }

        StartEvent startEvent = new StartEvent();
        startEvent.setTracingTag(MigUtils.getNewTracingTag());
        startEvent = this.createStartEventView(startEvent);

        EndEvent endEvent = new EndEvent();
        endEvent.setTracingTag(MigUtils.getNewTracingTag());
        endEvent = this.createEndEventView(endEvent);

        //START EVENT 액티비티 주입
        processDefinition5.addChildActivity(startEvent);
        ConvertedContext convertedContext = new ConvertedContext();
        convertedContext.setLayout(new CnvFlowLayout());
        convertedContext.getLayout().add(startEvent.getElementView());

        convertedContext.setInActivity(startEvent);
        convertedContext.setOutActivity(endEvent);

        // 기존의 process definition에서 새로 만들 process definition의 변수로 세팅한다.
        processDefinition5.setName(src.getName());
        processDefinition5.setProcessVariables(src.getProcessVariables());

        keyedContext.put("root", processDefinition5);

        // 액티비티 변환
        ConvertedContext childConvertedContext = super.convert(src, keyedContext);
        convertedContext.getLayout().add(childConvertedContext.getLayout());

        //END EVENT 액티비티 주입
        processDefinition5.addChildActivity(endEvent);
        convertedContext.getLayout().add(endEvent.getElementView());

        convertedContext.getLayout().layout();

        //set SequenceFlow
        SequenceFlow sequenceFlow = new SequenceFlow();
        sequenceFlow.setSourceRef(startEvent.getTracingTag());
        sequenceFlow.setTargetRef(childConvertedContext.getLayout().getElemnetId(1));
        processDefinition5.addSequenceFlow(sequenceFlow);

        sequenceFlow = new SequenceFlow();
        sequenceFlow.setSourceRef(childConvertedContext.getLayout().getLastElementIdInFirstGroup());
        sequenceFlow.setTargetRef(endEvent.getTracingTag());
        processDefinition5.addSequenceFlow(sequenceFlow);

        //Sequenceflow 재조정
        for(SequenceFlow srcSequenceFlow : processDefinition5.getSequenceFlows()){
            String sourceRef = srcSequenceFlow.getSourceRef();
            String targetRef = srcSequenceFlow.getTargetRef();
            String relationViewValue =  MigUtils.getRelationViewValue( processDefinition5.getActivity(sourceRef) ,processDefinition5.getActivity(targetRef),srcSequenceFlow.isOtherwise());
            if(relationViewValue != null) {
                srcSequenceFlow.setRelationView(srcSequenceFlow.createView());
                srcSequenceFlow.getRelationView().setValue(relationViewValue);
            }
        }

        return convertedContext;
    }




}
