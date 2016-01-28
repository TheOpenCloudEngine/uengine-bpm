package org.uengine.processpublisher.bpmn.exporter;

import org.omg.spec.bpmn._20100524.di.BPMNDiagram;
import org.omg.spec.bpmn._20100524.di.BPMNPlane;
import org.omg.spec.bpmn._20100524.model.*;
import org.uengine.kernel.*;
import org.uengine.kernel.bpmn.SequenceFlow;
import org.uengine.processpublisher.Adapter;
import org.uengine.processpublisher.BPMNUtil;
import javax.xml.bind.JAXBElement;
import javax.xml.namespace.QName;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;

/**
 * Created by MisakaMikoto on 2015. 8. 4..
 */
public class ProcessDefinitionAdapter implements Adapter<ProcessDefinition, TDefinitions> {
    @Override
    public TDefinitions convert(ProcessDefinition src, Hashtable keyedContext) throws Exception {
        // make hashtable
        Hashtable context = new Hashtable();

        // make TDefinitions
        TDefinitions tDefinitions = ObjectFactoryUtil.createBPMNObject(TDefinitions.class);
        tDefinitions.setId(src.getId());
        tDefinitions.setName(src.getName());

        // make TProcess
        TProcess tProcess = ObjectFactoryUtil.createBPMNObject(TProcess.class);

        // make TCollaboration
        TCollaboration tCollaboration = ObjectFactoryUtil.createBPMNObject(TCollaboration.class);
        tCollaboration.setId("tCollaboration");
        tCollaboration.setName("tCollaboration");

        // make TParticipant
        TParticipant tParticipant = ObjectFactoryUtil.createBPMNObject(TParticipant.class);
        tParticipant.setId("Participant");
        tParticipant.setName("Pool");
        // set TProcess's attribute
        tProcess.setId("process" + tParticipant.getId());
        tProcess.setName(tParticipant.getName());
        // set tParticipant's processRef
        tParticipant.setProcessRef(new QName(tProcess.getId()));
        // add TParticipant to TCollaboration
        tCollaboration.getParticipant().add(tParticipant);
        // add TCollaboration to TDefinitions
        JAXBElement<TCollaboration> tCollaborationElement = ObjectFactoryUtil.createDefaultJAXBElement(TCollaboration.class, tCollaboration);
        tDefinitions.getRootElement().add(tCollaborationElement);

        // make BPMNDiagram
        BPMNDiagram bpmnDiagram = ObjectFactoryUtil.createBPMNObject(BPMNDiagram.class);
        //TODO: bpmndiagram id? name?
        bpmnDiagram.setId("bpmnDiagram");
        bpmnDiagram.setName("bpmnDiagram");

        // make BPMNPLane
        BPMNPlane bpmnPlane = ObjectFactoryUtil.createBPMNObject(BPMNPlane.class);
        // bpmnShape's bpmnElement = tProcess's id
        bpmnPlane.setBpmnElement(new QName(tProcess.getId()));
        // BPMNDiagram set BPMNPLane
        bpmnDiagram.setBPMNPlane(bpmnPlane);

        context.put("bpmnDiagram", bpmnDiagram);

        // find Role
        if (src.getRoles() != null && src.getRoles().length > 0) {
            // make TLaneSet
            TLaneSet tLaneSet = ObjectFactoryUtil.createBPMNObject(TLaneSet.class);
            // TODO: TLaneSet id? name?
            tLaneSet.setId("laneSetId");
            tLaneSet.setName("laneSetName");

            // uengine role = bpmn Lane
            for (Role role : src.getRoles()) {
                // role adapter -> TLane make and setting
                TLane tLane = (TLane) BPMNUtil.export(role, context);
                tLaneSet.getLane().add(tLane);

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
                    TUserTask tUserTask = (TUserTask) BPMNUtil.export(humanActivity, context);

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
                                        JAXBElement<Object> tLaneFlowNodeRefElement = ObjectFactoryUtil.createObjectJAXBElement("TLaneFlowNodeRef", tUserTask);
                                        tLane.getFlowNodeRef().add(tLaneFlowNodeRefElement);
                                    }
                                }
                            }
                        }
                    }
                    // make JAXB Element and add TUserTask
                    JAXBElement<TUserTask> tUserElement = ObjectFactoryUtil.createDefaultJAXBElement(TUserTask.class, tUserTask);
                    tProcess.getFlowElement().add(tUserElement);

                } else {
                    // TODO : etc Activities...
                }
            }
        }

        // find SequenceFlow
        if (src.getSequenceFlows() != null && src.getSequenceFlows().size() > 0) {
            for (SequenceFlow sequenceFlow : src.getSequenceFlows()) {
                // SequenceFlowAdapter -> TSequenceFlow make and setting
                TSequenceFlow tSequenceFlow = (TSequenceFlow) BPMNUtil.export(sequenceFlow, context);

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
            }
        }

        // add TProcess to TDefinitions
        JAXBElement<TProcess> tProcessElement = ObjectFactoryUtil.createDefaultJAXBElement(TProcess.class, tProcess);
        tDefinitions.getRootElement().add(tProcessElement);

        // add BPMNDiagram to TDefinitions
        tDefinitions.getBPMNDiagram().add(bpmnDiagram);

        return tDefinitions;
    }

}
