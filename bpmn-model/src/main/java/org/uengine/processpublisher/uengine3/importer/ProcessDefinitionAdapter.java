package org.uengine.processpublisher.uengine3.importer;

import org.uengine.kernel.*;
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
public class ProcessDefinitionAdapter extends ComplexActivityAdapter{

    public ProcessDefinitionAdapter(){}

    private StartEvent createStartEventView(StartEvent startEvent) throws Exception {
        ElementView elementView = startEvent.createView();
        //elementView.setViewType("org.uengine.kernel.view.DefaultActivityView");
        elementView.setShapeId("OG.shape.bpmn.A_Task");
        elementView.setX(MigDrawPositoin.getStartEventXPosition());
        elementView.setY(MigDrawPositoin.getStartEventYPosition());
        elementView.setWidth(MigDrawPositoin.getStartEventWidth());
        elementView.setHeight(MigDrawPositoin.getStartEventHeight());
        startEvent.setElementView(elementView);
        return startEvent;
    }


    private EndEvent createEndEventView(EndEvent endEvent) throws Exception {
        ElementView elementView = endEvent.createView();
        //elementView.setViewType("org.uengine.kernel.view.DefaultActivityView");
        elementView.setShapeId("OG.shape.bpmn.A_Task");
        elementView.setWidth(MigDrawPositoin.getEndEventWidth());
        elementView.setHeight(MigDrawPositoin.getEndEventHeight());
        endEvent.setElementView(elementView);
        return endEvent;
    }

    @Override
    public ConvertedContext convert(ComplexActivity complexActivity, Hashtable keyedContext) throws Exception {

        ProcessDefinition src = (ProcessDefinition) complexActivity;


        ProcessDefinition processDefinition = new ProcessDefinition();
        // 기존의 process definitino에서 새로 만들 process definition의 변수로 세팅한다.
        processDefinition.setName(src.getName());
        processDefinition.setProcessVariables(src.getProcessVariables());


        //START EVENT 액티비티 주입
        StartEvent startEvent = new StartEvent();
        this.createStartEventView(startEvent);
        startEvent.setTracingTag(MigUtils.getNewTracingTag());
        processDefinition.addChildActivity(startEvent);


//        Hashtable options = new Hashtable();
        keyedContext.put("root", processDefinition);

        // 액티비티 변환
        ConvertedContext convertedContext = super.convert(src, keyedContext);

        //END EVENT 액티비티 주입
        EndEvent endEvent = new EndEvent();
        this.createEndEventView(endEvent);
        endEvent.setTracingTag(MigUtils.getNewTracingTag());
        processDefinition.addChildActivity(endEvent);

        convertedContext.getLayout().layout();

        return convertedContext;
    }
}
