package org.uengine.processpublisher.bpmn.exporter;

import org.omg.spec.bpmn._20100524.di.BPMNDiagram;
import org.omg.spec.bpmn._20100524.di.BPMNEdge;
import org.omg.spec.bpmn._20100524.di.BPMNPlane;
import org.omg.spec.bpmn._20100524.di.BPMNShape;
import org.omg.spec.bpmn._20100524.model.*;
import org.uengine.kernel.*;
import org.uengine.kernel.bpmn.SequenceFlow;
import org.uengine.processpublisher.Adapter;
import org.uengine.processpublisher.BPMNUtil;
import org.uengine.processpublisher.ObjectFactoryUtil;
import javax.xml.bind.JAXBElement;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;

/**
 * Created by MisakaMikoto on 2015. 8. 4..
 */
public class ProcessDefinitionAdapter implements Adapter<ProcessDefinition, TDefinitions> {
    @Override
    public TDefinitions convert(ProcessDefinition src, Hashtable keyedContext) throws Exception {
        // make TDefinitions
        TDefinitions tDefinitions = ObjectFactoryUtil.createBPMNObject(TDefinitions.class);
        tDefinitions.setId(src.getId());

        // make TProcess
        TProcess tProcess = ObjectFactoryUtil.createBPMNObject(TProcess.class);
        tProcess.setName(src.getName());

        // make BPMNDiagram
        BPMNDiagram bpmnDiagram = ObjectFactoryUtil.createBPMNObject(BPMNDiagram.class);
        //TODO: bpmndiagram id? name?
        bpmnDiagram.setId("bpmnDiagram");
        bpmnDiagram.setName("bpmnDiagram");

        // find Role
        if (src.getRoles() != null && src.getRoles().length > 0) {
            // make TLaneSet
            TLaneSet tLaneSet = ObjectFactoryUtil.createBPMNObject(TLaneSet.class);
            // TODO: TLaneSet id? name?
            tLaneSet.setId("laneSetId");
            tLaneSet.setName("laneSetName");

            // make BPMNPLane
            BPMNPlane bpmnPlane = ObjectFactoryUtil.createBPMNObject(BPMNPlane.class);
            // BPMNDiagram set BPMNPLane
            bpmnDiagram.setBPMNPlane(bpmnPlane);

            // uengine role = bpmn Lane
            for (Role role : src.getRoles()) {
                // role adapter -> TLane make and setting
                TLane tLane = (TLane) BPMNUtil.export(role);
                tLaneSet.getLane().add(tLane);

                // find Role's elementView
                if(role.getElementView() != null) {
                    // element adapter -> BPMNShape make and setting
                    BPMNShape bpmnShape = (BPMNShape) BPMNUtil.export(role.getElementView());
                    // make diagramElement and PLane add bpmnShape
                    bpmnDiagram.getBPMNPlane().getDiagramElement().add(ObjectFactoryUtil.createDefaultJAXBElement(BPMNShape.class, bpmnShape));
                }
            }
            tProcess.getLaneSet().add(tLaneSet);
        }

        // find Activity
        if (src.getChildActivities() != null && src.getChildActivities().size() > 0) {
            for (Activity activity : src.getChildActivities()) {
                // if HumanActivity
                if (activity instanceof HumanActivity) {
                    HumanActivity humanActivity = (HumanActivity) activity;

                    // humanActivity adapter -> TUserTask create and setting
                    TUserTask tUserTask = (TUserTask) BPMNUtil.export(humanActivity);

                    // find humanActiviy's elementView
                    if(humanActivity.getElementView() != null) {
                        // element adapter -> BPMNShape make and setting
                        BPMNShape bpmnShape = (BPMNShape) BPMNUtil.export(humanActivity.getElementView());
                        // make diagramElement and PLane add bpmnShape
                        bpmnDiagram.getBPMNPlane().getDiagramElement().add(ObjectFactoryUtil.createDefaultJAXBElement(BPMNShape.class, bpmnShape));
                    }

                    if(humanActivity.getRole() != null) {
                        // find TLaneSet
                        if(tProcess.getLaneSet() != null && tProcess.getLaneSet().size() > 0) {
                            Iterator<TLaneSet> tLaneSetIterator = tProcess.getLaneSet().iterator();

                            // find TLane
                            while (tLaneSetIterator.hasNext()) {
                                List<TLane> tLaneList = tLaneSetIterator.next().getLane();

                                for(TLane tLane : tLaneList) {
                                    if(tLane.getId().equals(humanActivity.getRole().getElementView().getId())) {
                                        // ObjectMethod used only model ObjectFactory
                                        JAXBElement<Object> element = ObjectFactoryUtil.createObjectJAXBElement("TLaneFlowNodeRef", tUserTask);
                                        tLane.getFlowNodeRef().add(element);
                                    }
                                }
                            }
                        }
                    }
                    // make JAXB Element and add TUserTask
                    JAXBElement<TUserTask> element = ObjectFactoryUtil.createDefaultJAXBElement(TUserTask.class, tUserTask);
                    tProcess.getFlowElement().add(element);

                } else {
                    // TODO : etc Activities...
                }
            }
        }

        // find SequenceFlow
        if (src.getSequenceFlows() != null && src.getSequenceFlows().size() > 0) {
            for (SequenceFlow sequenceFlow : src.getSequenceFlows()) {
                // SequenceFlowAdapter -> TSequenceFlow make and setting
                TSequenceFlow tSequenceFlow = (TSequenceFlow) BPMNUtil.export(sequenceFlow);

                // find process's all elements
                if(tProcess.getFlowElement() != null && tProcess.getFlowElement().size() > 0) {
                    List<JAXBElement<? extends TFlowElement>> listTFlowElement = tProcess.getFlowElement();

                    // find element's sourceref, targetref
                    for(JAXBElement<? extends TFlowElement> tFlowElement : listTFlowElement) {
                        if(tFlowElement.getValue().getId().equals(sequenceFlow.getSourceRef())) {
                            tSequenceFlow.setSourceRef(tFlowElement.getValue());
                        }

                        if(tFlowElement.getValue().getId().equals(sequenceFlow.getTargetRef())) {
                            tSequenceFlow.setTargetRef(tFlowElement.getValue());
                        }
                    }
                }

                // find ConditionExpression
                if (sequenceFlow.getCondition() != null) {
                    // Condition adapter -> TExpression make and setting
                    TExpression tExpression = (TExpression) BPMNUtil.export(sequenceFlow.getCondition());
                    // add Condition
                    tSequenceFlow.setConditionExpression(tExpression);
                }
                // make JAXB Element and add TSequenceFlow
                JAXBElement<TSequenceFlow> sequenceShapeElement = ObjectFactoryUtil.createDefaultJAXBElement(TSequenceFlow.class, tSequenceFlow);
                tProcess.getFlowElement().add(sequenceShapeElement);

                // find relationView
                if(sequenceFlow.getRelationView() != null) {
                    // element adapter -> BPMNEdge make and setting
                    BPMNEdge bpmnEdge = (BPMNEdge) BPMNUtil.export(sequenceFlow.getRelationView());
                    // make diagramElement and PLane add BPMNEdge
                    bpmnDiagram.getBPMNPlane().getDiagramElement().add(ObjectFactoryUtil.createDefaultJAXBElement(BPMNEdge.class, bpmnEdge));
                }
            }
        }
        // add TProcess to TDefinitions
        JAXBElement<TProcess> element = ObjectFactoryUtil.createDefaultJAXBElement(TProcess.class, tProcess);
        tDefinitions.getRootElement().add(element);

        // add BPMNDiagram to TDefinitions
        tDefinitions.getBPMNDiagram().add(bpmnDiagram);

        return tDefinitions;
    }

}
