package org.uengine.processpublisher.bpmn.importer;

import org.omg.spec.bpmn._20100524.di.BPMNDiagram;
import org.omg.spec.bpmn._20100524.di.BPMNEdge;
import org.omg.spec.bpmn._20100524.di.BPMNPlane;
import org.omg.spec.bpmn._20100524.di.BPMNShape;
import org.omg.spec.bpmn._20100524.model.TDefinitions;
import org.omg.spec.bpmn._20100524.model.TLane;
import org.omg.spec.bpmn._20100524.model.TLaneSet;
import org.omg.spec.bpmn._20100524.model.TProcess;
import org.omg.spec.dd._20100524.di.DiagramElement;
import org.uengine.kernel.Activity;
import org.uengine.kernel.ProcessDefinition;
import org.uengine.kernel.Role;
import org.uengine.kernel.graph.Transition;
import org.uengine.modeling.ElementView;
import org.uengine.processpublisher.Adapter;
import org.uengine.processpublisher.BPMNUtil;

import javax.xml.bind.JAXBElement;
import java.util.HashMap;
import java.util.Hashtable;

public class TDefinitionsAdapter implements Adapter<TDefinitions, ProcessDefinition>{
    @Override
    public ProcessDefinition convert(TDefinitions src, Hashtable keyedContext) throws Exception {

        ProcessDefinition processDefinition = new ProcessDefinition();
        Hashtable context = new Hashtable();
        context.put("processDefinition", processDefinition);


        HashMap bpmnDiagramElementMap = new HashMap<String, DiagramElement>();
        context.put("BPMNDiagramElementMap", bpmnDiagramElementMap);

        for(BPMNDiagram bpmnDiagram : src.getBPMNDiagram()){
            BPMNPlane plane = bpmnDiagram.getBPMNPlane();

            for(JAXBElement diagramElementNode : plane.getDiagramElement()){
                DiagramElement diagramElement = (DiagramElement) diagramElementNode.getValue();

                if(diagramElement instanceof BPMNShape){
                    bpmnDiagramElementMap.put(((BPMNShape)diagramElement).getBpmnElement().getLocalPart(), diagramElement);

                }else if(diagramElement instanceof BPMNEdge){
                    bpmnDiagramElementMap.put(((BPMNEdge)diagramElement).getBpmnElement().getLocalPart(), diagramElement);
                }
            }
        }

        for(JAXBElement element : src.getRootElement()){
            if(element.getValue() instanceof TProcess){
                TProcess bpmnProcess = (TProcess)element.getValue();
                context.put("tProcess", bpmnProcess);

                processDefinition.setName(bpmnProcess.getName());

                if(bpmnProcess.getLaneSet()!=null && bpmnProcess.getLaneSet().size()>0){
                    for(TLaneSet laneSet: bpmnProcess.getLaneSet()){
                        for(TLane tLane: laneSet.getLane()){
                            Role role = new Role();

                            role.setName(tLane.getName());

                            processDefinition.addRole(role);

                            ElementView view = role.createView();

//                            Pool pool = new Pool();
//                            pool.setName(tLane.getName());
//                            ElementView view = pool.createView();

                            BPMNShape bpmnShape = (BPMNShape) bpmnDiagramElementMap.get(tLane.getId());

                            view.setX((int) Math.round(bpmnShape.getBounds().getX()));
                            view.setY((int) Math.round(bpmnShape.getBounds().getY()));
                            view.setWidth((int) Math.round(bpmnShape.getBounds().getWidth()));
                            view.setHeight((int) Math.round(bpmnShape.getBounds().getHeight()));
                            view.setId(tLane.getId());

                            role.setElementView(view);

//                            processDefinition.addPool(pool);

                        }

                    }
                }

                if(bpmnProcess.getFlowElement()!=null && bpmnProcess.getFlowElement().size()>0){
                    for(JAXBElement flowElement: bpmnProcess.getFlowElement()){
                        context.put("parent", processDefinition);

                        Object childElement = BPMNUtil.adapt(flowElement.getValue(), context);

                        if(childElement instanceof Activity){

                            processDefinition.addChildActivity((Activity)childElement);


                        }else if(childElement instanceof Transition){
                            processDefinition.addTransition((Transition) childElement);

                        }




                    }
                }
            }

        }

        return processDefinition;
    }
}
