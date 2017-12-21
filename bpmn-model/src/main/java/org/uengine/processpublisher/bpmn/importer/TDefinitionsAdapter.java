package org.uengine.processpublisher.bpmn.importer;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.omg.spec.bpmn._20100524.di.BPMNDiagram;
import org.omg.spec.bpmn._20100524.di.BPMNEdge;
import org.omg.spec.bpmn._20100524.di.BPMNPlane;
import org.omg.spec.bpmn._20100524.di.BPMNShape;
import org.omg.spec.bpmn._20100524.model.*;
import org.omg.spec.dd._20100524.di.DiagramElement;
import org.uengine.kernel.Activity;
import org.uengine.kernel.ProcessDefinition;
import org.uengine.kernel.Role;
import org.uengine.kernel.bpmn.Event;
import org.uengine.kernel.bpmn.FlowActivity;
import org.uengine.kernel.bpmn.Pool;
import org.uengine.kernel.bpmn.SequenceFlow;
import org.uengine.modeling.ElementView;
import org.uengine.modeling.RelationView;
import org.uengine.modeling.Symbol;
import org.uengine.processpublisher.Adapter;
import org.uengine.processpublisher.BPMNUtil;

import javax.xml.bind.JAXBElement;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.*;

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


        //firstly define collaboration --> TODO: pool will be supported later
        Map<String, TParticipant> collaborationParticipantByProcess = new HashMap<>();
        for(JAXBElement element : src.getRootElement()) {
            if (element.getValue() instanceof TCollaboration) {
                TCollaboration collaboration = (TCollaboration) element.getValue();

                if(collaboration.getParticipant()!=null){
                    List<TParticipant> participants = collaboration.getParticipant();
                    for(TParticipant participant: participants){
                        collaborationParticipantByProcess.put(participant.getProcessRef().getLocalPart(), participant);
                    }
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
                        rootRole.setName(tLaneSet.getName()!=null ? tLaneSet.getName() : "root");

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

                            if(role.getName()==null) role.setName("role-" + (processDefinition.getRoles().length + 1));

                            role.getElementView().setParent(tLaneSet.getId());
                            processDefinition.addRole(role);
                        }
                    }
                }

                //uEngine BPMN engine requires one or more roles defined at least.
                if(processDefinition.getRoles()==null || processDefinition.getRoles().length == 0){

                    //if there is no defined role, there are two cases -
                    // 1. the process has been defined in collaboration (pool) or
                    // 2. there no pool is defined ever.

                    // check there is collaboration (pools) defintion //TODO: pool will be supported later. Later, we will utilize visitor pattern for executing BPMN model.
                    TParticipant participant = collaborationParticipantByProcess.get(bpmnProcess.getId());
                    if(participant!=null){
                        Role role = new Role();
                        role.setName(participant.getName());
                        role.setElementView(role.createView());
                        ElementView view = role.getElementView();
                        BPMNShape bpmnShape = (BPMNShape) bpmnDiagramElementMap.get(participant.getId());

                        if(bpmnShape!=null) {
                            view.setX((int) Math.round(bpmnShape.getBounds().getX() + (bpmnShape.getBounds().getWidth() / 2)));
                            view.setY((int) Math.round(bpmnShape.getBounds().getY() + (bpmnShape.getBounds().getHeight() / 2)));
                            view.setWidth((int) Math.round(bpmnShape.getBounds().getWidth()));
                            view.setHeight((int) Math.round(bpmnShape.getBounds().getHeight()));
                            view.setId(bpmnProcess.getId());
                            view.setParent("null");
                        }
                        processDefinition.addRole(role);
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


    private Activity findAttachedActivity(ElementView eventView, List<ElementView> elementViews) {
//        // eventView size
//        double event_x_min = (eventView.getX()) - (Math.abs((eventView.getWidth()) / 2));
//        double event_x_max = (eventView.getX()) + (Math.abs((eventView.getWidth()) / 2));
//        double event_y_min = (eventView.getY()) - (Math.abs((eventView.getHeight()) / 2));
//        double event_y_max = (eventView.getY()) + (Math.abs((eventView.getHeight()) / 2));
//
//        for (ElementView elementView : elementViews) {
//            if (!(elementView instanceof EventView)) {
//                //if(elementView.getX() != null) {
//                // elementView size
//                double element_x_min = (elementView.getX()) - (Math.abs((elementView.getWidth()) / 2));
//                double element_x_max = (elementView.getX()) + (Math.abs((elementView.getWidth()) / 2));
//                double element_y_min = (elementView.getY()) - (Math.abs((elementView.getHeight()) / 2));
//                double element_y_max = (elementView.getY()) + (Math.abs((elementView.getHeight()) / 2));
//
//                boolean checkMinX = (element_x_min <= event_x_min) && (event_x_min <= element_x_max);
//                boolean checkMaxX = (element_x_min <= event_x_max) && (event_x_max <= element_x_max);
//
//                boolean checkMinY = (element_y_min <= event_y_min) && (event_y_min <= element_y_max);
//                boolean checkMaxY = (element_y_min <= event_y_max) && (event_y_max <= element_y_max);
//
//                if ((checkMinX || checkMaxX) && (checkMinY || checkMaxY) && elementView.getElement() instanceof Activity) {
//                    return (Activity) elementView.getElement();
//                }
//                //}
//            }
//        }
      return null;
    }


    private boolean isIn(ElementView elem1, ElementView elem2) {
        double x = (elem1.getX());
        double y = (elem1.getY());
        double width = (elem1.getWidth());
        double height = (elem1.getHeight());
        double left = x - (width / 2);
        double right = x + (width / 2);
        double top = y - (height / 2);
        double bottom = y + (height / 2);

        double p_x = (elem2.getX());
        double p_y = (elem2.getY());
        double p_width = (elem2.getWidth());
        double p_height = (elem2.getHeight());
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
