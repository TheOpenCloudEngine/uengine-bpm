package org.uengine.processpublisher.bpmn.importer;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
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
import org.uengine.kernel.bpmn.Event;
import org.uengine.kernel.bpmn.SequenceFlow;
import org.uengine.modeling.ElementView;
import org.uengine.processpublisher.Adapter;
import org.uengine.processpublisher.BPMNUtil;

import javax.xml.bind.JAXBElement;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Hashtable;

public class TDefinitionsAdapter implements Adapter<TDefinitions, ProcessDefinition>{
    @Override
    public ProcessDefinition convert(TDefinitions src, Hashtable keyedContext) throws Exception {
        ProcessDefinition processDefinition = new ProcessDefinition();
        Hashtable context = new Hashtable();
        context.put("processDefinition", processDefinition);

        HashMap bpmnDiagramElementMap = new HashMap<>();
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
                TProcess bpmnProcess = (TProcess) element.getValue();
                context.put("tProcess", bpmnProcess);
                processDefinition.setName(bpmnProcess.getName());

                if(bpmnProcess.getLaneSet() != null && bpmnProcess.getLaneSet().size() > 0){
                    for(TLaneSet tLaneSet : bpmnProcess.getLaneSet()){
                        Role rootRole = new Role();
                        rootRole.setName(tLaneSet.getName());

                        ElementView view = rootRole.createView();
                        //      Pool pool = new Pool();
                        //      pool.setName(tLane.getName());
                        //      ElementView view = pool.createView();

                        BPMNShape bpmnShape = (BPMNShape) bpmnDiagramElementMap.get(tLaneSet.getId());

                        if(bpmnShape!=null) {
                            view.setX((int) Math.round(bpmnShape.getBounds().getX() + (bpmnShape.getBounds().getWidth() / 2)));
                            view.setY((int) Math.round(bpmnShape.getBounds().getY() + (bpmnShape.getBounds().getHeight() / 2)));
                            view.setWidth((int) Math.round(bpmnShape.getBounds().getWidth()));
                            view.setHeight((int) Math.round(bpmnShape.getBounds().getHeight()));
                            view.setId(tLaneSet.getId());
                            view.setParent("null");
                        }

                        rootRole.setElementView(view);
                        processDefinition.addRole(rootRole);

                        for(TLane tLane: tLaneSet.getLane()){
                            Role role = (Role) BPMNUtil.importAdapt(tLane, context);
                            role.getElementView().setParent(tLaneSet.getId());
                            processDefinition.addRole(role);
                        }
                    }
                }

                if(bpmnProcess.getFlowElement() != null && bpmnProcess.getFlowElement().size() > 0){
                    for(JAXBElement flowElement: bpmnProcess.getFlowElement()){
                        Object childElement = BPMNUtil.importAdapt(flowElement.getValue(), context);
                        if(childElement instanceof Activity){
                            processDefinition.addChildActivity((Activity) childElement);

                        } else if(childElement instanceof SequenceFlow){
                            processDefinition.addSequenceFlow((SequenceFlow) childElement);

                        } else {
                            ;
                        }
                    }
                }
            }
        }
        OpenGraphAdapter openGraphAdapter = new OpenGraphAdapter();
        openGraphAdapter.createAllActivityListAndSequenceFlowList(processDefinition);
        openGraphAdapter.createOpenGraphInformation();

        return processDefinition;
    }
}
