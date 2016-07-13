package org.uengine.processpublisher.bpmn.exporter;

import org.omg.spec.bpmn._20100524.di.BPMNDiagram;
import org.omg.spec.bpmn._20100524.di.BPMNEdge;
import org.omg.spec.bpmn._20100524.di.BPMNShape;
import org.omg.spec.bpmn._20100524.model.*;
import org.uengine.kernel.Activity;
import org.uengine.kernel.HumanActivity;
import org.uengine.kernel.bpmn.*;
import org.uengine.processpublisher.Adapter;
import org.uengine.processpublisher.BPMNUtil;

import javax.xml.bind.JAXBElement;
import javax.xml.namespace.QName;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;

/**
 * Created by MisakaMikoto on 2016. 6. 23..
 */
public class SubProcessAdapter implements Adapter<SubProcess, TSubProcess> {

    @Override
    public TSubProcess convert(SubProcess src, Hashtable keyedContext) throws Exception {
        TSubProcess tSubProcess = ObjectFactoryUtil.createBPMNObject(TSubProcess.class);
        tSubProcess.setId(src.getTracingTag());
        tSubProcess.setName(src.getName());

        String outgoing = getOutgoing(src);
        String incoming = getIncoming(src);

        if(outgoing != null && outgoing.length() > 0) {
            tSubProcess.getOutgoing().add(new QName(outgoing));
        }

        if(incoming != null && incoming.length() > 0) {
            tSubProcess.getIncoming().add(new QName(incoming));
        }

        convertChildElement(src, tSubProcess, keyedContext);
        convertChildRelation(src, tSubProcess, keyedContext);

        BPMNShape bpmnShape = (BPMNShape) BPMNUtil.exportAdapt(src.getElementView());
        bpmnShape.setBpmnElement(new QName(src.getTracingTag()));
        // expanded option !! important!!
        bpmnShape.setIsExpanded(true);
        BPMNDiagram bpmnDiagram = (BPMNDiagram) keyedContext.get("bpmnDiagram");
        // make diagramElement and PLane add bpmnShape
        bpmnDiagram.getBPMNPlane().getDiagramElement().add(ObjectFactoryUtil.createDefaultJAXBElement(BPMNShape.class, bpmnShape));

        return tSubProcess;
    }

    private String getOutgoing(SubProcess subProcess) {
        String outgoing = null;
        if(subProcess.getElementView().getFromEdge() != null && subProcess.getElementView().getFromEdge().length() > 0) {
            outgoing = subProcess.getElementView().getFromEdge();
        }
        return outgoing;
    }

    private String getIncoming(SubProcess subProcess) {
        String incoming = null;
        if(subProcess.getElementView().getToEdge() != null && subProcess.getElementView().getToEdge().length() > 0) {
            incoming = subProcess.getElementView().getToEdge();
        }
        return incoming;
    }

    private void convertChildElement(SubProcess subProcess, TSubProcess tSubProcess, Hashtable context) {
        // find Activity
        if (subProcess.getChildActivities() != null && subProcess.getChildActivities().size() > 0) {
            for (Activity activity : subProcess.getChildActivities()) {
                if (activity instanceof HumanActivity) {
                    try {
                        TUserTask tUserTask = (TUserTask) BPMNUtil.exportAdapt(activity, context);
                        // make JAXB Element and add TUserTask
                        JAXBElement<TUserTask> tUserElement = ObjectFactoryUtil.createDefaultJAXBElement(TUserTask.class, tUserTask);
                        tSubProcess.getFlowElement().add(tUserElement);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                } else if (activity instanceof SubProcess) {
                    try {
                        TSubProcess childTSubProcess = (TSubProcess) BPMNUtil.exportAdapt(activity, context);
                        // make JAXB Element and add TSubProcess
                        JAXBElement<TSubProcess> tSubProcessElement = ObjectFactoryUtil.createDefaultJAXBElement(TSubProcess.class, childTSubProcess);
                        tSubProcess.getFlowElement().add(tSubProcessElement);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                } else if(activity instanceof Event){
                    convertChildEvent((Event) activity, context, tSubProcess);

                } else {
                    ;
                }
            }
        }
    }

    private void convertChildRelation(SubProcess subProcess, TSubProcess tSubProcess, Hashtable context) {
        // find SequenceFlow
        if (subProcess.getSequenceFlows() != null && subProcess.getSequenceFlows().size() > 0) {
            for (SequenceFlow subProcessSequenceFlow : subProcess.getSequenceFlows()) {
                // SequenceFlowAdapter -> TSequenceFlow make and setting
                context.put("childActivities", subProcess.getChildActivities());

                try {
                    TSequenceFlow subProcessTSequenceFlow = (TSequenceFlow) BPMNUtil.exportAdapt(subProcessSequenceFlow, context);

                    // find process's all elements
                    if(tSubProcess.getFlowElement() != null && tSubProcess.getFlowElement().size() > 0) {
                        List<JAXBElement<? extends TFlowElement>> listTFlowElement = tSubProcess.getFlowElement();

                        // find element's sourceref, targetref
                        for(JAXBElement<? extends TFlowElement> tFlowElement : listTFlowElement) {
                            if(tFlowElement.getValue().getId().equals(subProcessSequenceFlow.getSourceRef())) {
                                subProcessTSequenceFlow.setSourceRef(tFlowElement.getValue());
                            }

                            if(tFlowElement.getValue().getId().equals(subProcessSequenceFlow.getTargetRef())) {
                                subProcessTSequenceFlow.setTargetRef(tFlowElement.getValue());
                            }
                        }
                    }

                    // find ConditionExpression
                    if (subProcessSequenceFlow.getCondition() != null) {
                        // Condition adapter -> TExpression make and setting
                        try {
                            TExpression tExpression = (TExpression) BPMNUtil.exportAdapt(subProcessSequenceFlow.getCondition());
                            // add Condition
                            subProcessTSequenceFlow.setConditionExpression(tExpression);

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    // make JAXB Element and add TSequenceFlow
                    JAXBElement<TSequenceFlow> subProcessSequenceShapeElement = ObjectFactoryUtil.createDefaultJAXBElement(TSequenceFlow.class, subProcessTSequenceFlow);
                    tSubProcess.getFlowElement().add(subProcessSequenceShapeElement);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void convertChildEvent(Event subProcessEvent, Hashtable context, TSubProcess tSubProcess) {
        if(subProcessEvent.getClass() == StartEvent.class) {
            try {
                TStartEvent tStartEvent = (TStartEvent) BPMNUtil.exportAdapt(subProcessEvent, context);
                JAXBElement<TStartEvent> tStartEventElement = ObjectFactoryUtil.createDefaultJAXBElement(TStartEvent.class, tStartEvent);
                tSubProcess.getFlowElement().add(tStartEventElement);

            } catch (Exception e) {
                e.printStackTrace();
            }

        } else if(subProcessEvent.getClass() == EndEvent.class) {
            try {
                TEndEvent tEndEvent = (TEndEvent) BPMNUtil.exportAdapt(subProcessEvent, context);
                JAXBElement<TEndEvent> tEndEventElement = ObjectFactoryUtil.createDefaultJAXBElement(TEndEvent.class, tEndEvent);
                tSubProcess.getFlowElement().add(tEndEventElement);

            } catch (Exception e) {
                e.printStackTrace();
            }

        } else if(subProcessEvent.getClass() == TimerEvent.class) {
            try {
                TBoundaryEvent tBoundaryEvent = (TBoundaryEvent) BPMNUtil.exportAdapt(subProcessEvent, context);
                JAXBElement<TBoundaryEvent> TBoundaryEventElement = ObjectFactoryUtil.createDefaultJAXBElement(TBoundaryEvent.class, tBoundaryEvent);
                tSubProcess.getFlowElement().add(TBoundaryEventElement);

            } catch (Exception e) {
                e.printStackTrace();
            }

        } else {
            ;
        }
    }
}
