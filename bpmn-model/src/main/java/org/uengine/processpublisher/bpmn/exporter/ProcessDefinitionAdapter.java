package org.uengine.processpublisher.bpmn.exporter;

import org.omg.spec.bpmn._20100524.di.BPMNDiagram;
import org.omg.spec.bpmn._20100524.di.BPMNPlane;
import org.omg.spec.bpmn._20100524.di.BPMNShape;
import org.omg.spec.bpmn._20100524.model.*;
import org.omg.spec.dd._20100524.di.Diagram;
import org.omg.spec.dd._20100524.di.DiagramElement;
import org.uengine.kernel.*;
import org.uengine.kernel.bpmn.*;
import org.uengine.processpublisher.Adapter;
import org.uengine.processpublisher.BPMNUtil;
import org.uengine.processpublisher.bpmn.importer.TStartEventAdapter;

import javax.xml.bind.JAXB;
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

        // create TDefinitions
        TDefinitions tDefinitions = createTDefinitions(src);

        // create TProcess
        TProcess tProcess = ObjectFactoryUtil.createBPMNObject(TProcess.class);

        // create TCollaboration
        TCollaboration tCollaboration = createTCollaboration();

        // create TParticipant
        TParticipant tParticipant = createTParticipant();

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

        // create BPMNDiagram
        BPMNDiagram bpmnDiagram = createBPMNDiagram();

        // make BPMNPLane
        BPMNPlane bpmnPlane = ObjectFactoryUtil.createBPMNObject(BPMNPlane.class);

        // bpmnShape's bpmnElement = tProcess's id
        bpmnPlane.setBpmnElement(new QName(tProcess.getId()));

        // BPMNDiagram set BPMNPLane
        bpmnDiagram.setBPMNPlane(bpmnPlane);

        context.put("bpmnDiagram", bpmnDiagram);

        // find Role (convert laneSet and lane)
        if (src.getRoles() != null && src.getRoles().length > 0) {
            convertTLaneSet(src, context, tProcess);
        }

        // find Activity (convert task and subProcess)
        if (src.getChildActivities() != null && src.getChildActivities().size() > 0) {
            for (Activity activity : src.getChildActivities()) {
                // if HumanActivity
                if (activity instanceof HumanActivity) {
                    convertTUserTask((HumanActivity) activity, context, tProcess);

                    // if HumanActivity
                } else if (activity instanceof SubProcess) {
                    SubProcess subProcess = (SubProcess) activity;
                    convertSubProcess(subProcess, context, tProcess);

                } else if(activity instanceof Event) {
                    convertTEvent((Event) activity, context, tProcess);

                } else {
                    ;
                }
            }
        }

        // find SequenceFlow
        if (src.getSequenceFlows() != null && src.getSequenceFlows().size() > 0) {
            for (SequenceFlow sequenceFlow : src.getSequenceFlows()) {
                // SequenceFlowAdapter -> TSequenceFlow make and setting
                convertTSequenceFlow(sequenceFlow, context, tProcess, src.getChildActivities());
            }
        }

        // add TProcess to TDefinitions
        JAXBElement<TProcess> tProcessElement = ObjectFactoryUtil.createDefaultJAXBElement(TProcess.class, tProcess);
        tDefinitions.getRootElement().add(tProcessElement);

        // add BPMNDiagram to TDefinitions
        tDefinitions.getBPMNDiagram().add(bpmnDiagram);

        return tDefinitions;
    }

    private TDefinitions createTDefinitions(ProcessDefinition src) {
        // make TDefinitions
        TDefinitions tDefinitions = ObjectFactoryUtil.createBPMNObject(TDefinitions.class);
        tDefinitions.setId(src.getId());
        tDefinitions.setName(src.getName());

        return tDefinitions;
    }

    private TCollaboration createTCollaboration() {
        TCollaboration tCollaboration = ObjectFactoryUtil.createBPMNObject(TCollaboration.class);
        tCollaboration.setId("tCollaboration");
        tCollaboration.setName("tCollaboration");

        return tCollaboration;
    }

    private TParticipant createTParticipant() {
        TParticipant tParticipant = ObjectFactoryUtil.createBPMNObject(TParticipant.class);
        tParticipant.setId("Participant");
        tParticipant.setName("Pool");

        return tParticipant;
    }

    private BPMNDiagram createBPMNDiagram() {
        BPMNDiagram bpmnDiagram = ObjectFactoryUtil.createBPMNObject(BPMNDiagram.class);
        bpmnDiagram.setId("bpmnDiagram");
        bpmnDiagram.setName("bpmnDiagram");

        return bpmnDiagram;
    }

    private void convertTLaneSet(ProcessDefinition src, Hashtable context, TProcess tProcess) {
        TLaneSet tLaneSet = ObjectFactoryUtil.createBPMNObject(TLaneSet.class);
        for (Role role : src.getRoles()) {
            // get parent is string "null" equals root role
            if(role.getElementView().getParent()==null || role.getElementView().getParent().equals("null")) {
                tLaneSet.setId(role.getElementView().getId());
                tLaneSet.setName(role.getName());

                try {
                    BPMNShape bpmnShape = (BPMNShape) BPMNUtil.exportAdapt(role.getElementView());
                    bpmnShape.setBpmnElement(new QName(role.getElementView().getId()));

                    BPMNDiagram bpmnDiagram = (BPMNDiagram) context.get("bpmnDiagram");
                    // make diagramElement and PLane add bpmnShape
                    bpmnDiagram.getBPMNPlane().getDiagramElement().add(ObjectFactoryUtil.createDefaultJAXBElement(BPMNShape.class, bpmnShape));
                    tProcess.getLaneSet().add(tLaneSet);

                } catch (Exception e) {
                    e.printStackTrace();
                }

            } else {
                try {
                    TLane tLane = (TLane) BPMNUtil.exportAdapt(role, context);
                    tLaneSet.getLane().add(tLane);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void convertTUserTask(HumanActivity humanActivity, Hashtable context, TProcess tProcess) {
        // humanActivity adapter -> TUserTask create and setting
        try {
            TUserTask tUserTask = (TUserTask) BPMNUtil.exportAdapt(humanActivity, context);

            if (humanActivity.getRole() != null) {
                // find TLaneSet
                if (tProcess.getLaneSet() != null && tProcess.getLaneSet().size() > 0) {
                    addFlowNodeRefToLane(humanActivity, tUserTask, tProcess);
                }
            }
            // make JAXB Element and add TUserTask
            JAXBElement<TUserTask> tUserElement = ObjectFactoryUtil.createDefaultJAXBElement(TUserTask.class, tUserTask);
            tProcess.getFlowElement().add(tUserElement);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void addFlowNodeRefToLane(HumanActivity humanActivity, TUserTask tUserTask, TProcess tProcess) {
        Iterator<TLaneSet> tLaneSetIterator = tProcess.getLaneSet().iterator();

        // find TLane
        while (tLaneSetIterator.hasNext()) {
            List<TLane> tLaneList = tLaneSetIterator.next().getLane();

            for (TLane tLane : tLaneList) {
                if (tLane.getName().equals(humanActivity.getRole().getName())) {
                    // ObjectMethod used only model ObjectFactory
                    JAXBElement<Object> tLaneFlowNodeRefElement = ObjectFactoryUtil.createObjectJAXBElement("TLaneFlowNodeRef", tUserTask);
                    tLane.getFlowNodeRef().add(tLaneFlowNodeRefElement);
                }
            }
        }
    }

    private void addFlowNodeRefToLane(Event event, TEvent tEvent, TProcess tProcess, Hashtable context) {
        Iterator<TLaneSet> tLaneSetIterator = tProcess.getLaneSet().iterator();

        // find TLane
        while (tLaneSetIterator.hasNext()) {
            List<TLane> tLaneList = tLaneSetIterator.next().getLane();

            for (TLane tLane : tLaneList) {
                BPMNDiagram bpmnDiagram = (BPMNDiagram) context.get("bpmnDiagram");

                for(JAXBElement bpmnDiagramElement : bpmnDiagram.getBPMNPlane().getDiagramElement()) {
                    try {
                        if(bpmnDiagramElement.getValue() instanceof BPMNShape) {
                            BPMNShape bpmnShape = (BPMNShape) bpmnDiagramElement.getValue();

                            if (bpmnShape.getBpmnElement().toString().equals(tLane.getId())) {
                                if (isInEventToLane(event, bpmnShape)) {
                                    JAXBElement<Object> tLaneFlowNodeRefElement = null;

                                    if(tEvent instanceof TStartEvent) {
                                        TStartEvent tStartEvent = (TStartEvent) tEvent;
                                        tLaneFlowNodeRefElement = ObjectFactoryUtil.createObjectJAXBElement("TLaneFlowNodeRef", tStartEvent);

                                    } else if(tEvent instanceof TEndEvent) {
                                        TEndEvent tEndEvent = (TEndEvent) tEvent;
                                        tLaneFlowNodeRefElement = ObjectFactoryUtil.createObjectJAXBElement("TLaneFlowNodeRef", tEndEvent);

                                    } else {
                                        ;
                                    }
                                    tLane.getFlowNodeRef().add(tLaneFlowNodeRefElement);
                                }
                            }
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

            }
        }
    }

    private void convertSubProcess(SubProcess subProcess, Hashtable context, TProcess tProcess) {
        // humanActivity adapter -> TUserTask create and setting
        try {
            TSubProcess tSubProcess = (TSubProcess) BPMNUtil.exportAdapt(subProcess, context);

            // make JAXB Element and add TUserTask
            JAXBElement<TSubProcess> tSubProcessElement = ObjectFactoryUtil.createDefaultJAXBElement(TSubProcess.class, tSubProcess);
            tProcess.getFlowElement().add(tSubProcessElement);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void convertTSequenceFlow(SequenceFlow sequenceFlow, Hashtable context, TProcess tProcess, List<Activity> childActivities) {
        // SequenceFlowAdapter -> TSequenceFlow make and setting
        context.put("childActivities", childActivities);
        try {
            TSequenceFlow tSequenceFlow = (TSequenceFlow) BPMNUtil.exportAdapt(sequenceFlow, context);

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
                convertTExpression(sequenceFlow, tSequenceFlow);
            }
            // make JAXB Element and add TSequenceFlow
            JAXBElement<TSequenceFlow> sequenceShapeElement = ObjectFactoryUtil.createDefaultJAXBElement(TSequenceFlow.class, tSequenceFlow);
            tProcess.getFlowElement().add(sequenceShapeElement);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void convertTExpression(SequenceFlow sequenceFlow, TSequenceFlow tSequenceFlow) {
        // Condition adapter -> TExpression make and setting
        try {
            TExpression tExpression = (TExpression) BPMNUtil.exportAdapt(sequenceFlow.getCondition());
            // add Condition
            tSequenceFlow.setConditionExpression(tExpression);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void convertTEvent(Event event, Hashtable context, TProcess tProcess) {
        if(event.getClass() == StartEvent.class) {
            try {
                TStartEvent tStartEvent = (TStartEvent) BPMNUtil.exportAdapt(event, context);
                addFlowNodeRefToLane(event, tStartEvent, tProcess, context);

                JAXBElement<TStartEvent> tStartEventElement = ObjectFactoryUtil.createDefaultJAXBElement(TStartEvent.class, tStartEvent);
                tProcess.getFlowElement().add(tStartEventElement);

            } catch (Exception e) {
                e.printStackTrace();
            }


        } else if(event.getClass() == EndEvent.class) {
            try {
                TEndEvent tEndEvent = (TEndEvent) BPMNUtil.exportAdapt(event, context);
                addFlowNodeRefToLane(event, tEndEvent, tProcess, context);

                JAXBElement<TEndEvent> tEndEventElement = ObjectFactoryUtil.createDefaultJAXBElement(TEndEvent.class, tEndEvent);
                tProcess.getFlowElement().add(tEndEventElement);

            } catch (Exception e) {
                e.printStackTrace();
            }

        } else if(event.getClass() == TimerEvent.class) {
            try {
                TBoundaryEvent tBoundaryEvent = (TBoundaryEvent) BPMNUtil.exportAdapt(event, context);
                addFlowNodeRefToLane(event, tBoundaryEvent, tProcess, context);

                JAXBElement<TBoundaryEvent> tBoundaryEventElement = ObjectFactoryUtil.createDefaultJAXBElement(TBoundaryEvent.class, tBoundaryEvent);
                tProcess.getFlowElement().add(tBoundaryEventElement);

            } catch (Exception e) {
                e.printStackTrace();
            }

        } else {
            ;
        }
    }

    private boolean isInEventToLane(Event event, BPMNShape bpmnShape) {
        double x = (event.getElementView().getX());
        double y = (event.getElementView().getY());
        double width = (event.getElementView().getWidth());
        double height = (event.getElementView().getHeight());
        double left = x - (width / 2);
        double right = x + (width / 2);
        double top = y - (height / 2);
        double bottom = y + (height / 2);

        double p_x = (bpmnShape.getBounds().getX());
        double p_y = (bpmnShape.getBounds().getY());
        double p_width = (bpmnShape.getBounds().getWidth());
        double p_height = (bpmnShape.getBounds().getHeight());
        double p_left = p_x - (p_width / 2);
        double p_right = p_x + (p_width / 2);
        double p_top = p_y - (p_height / 2);
        double p_bottom = p_y + (p_height / 2);

        return (p_left < left &&
                p_right > right &&
                p_top < top &&
                p_bottom > bottom
        );
    }
}
