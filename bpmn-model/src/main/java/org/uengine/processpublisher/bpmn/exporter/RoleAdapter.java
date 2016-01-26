package org.uengine.processpublisher.bpmn.exporter;

import org.omg.spec.bpmn._20100524.di.BPMNDiagram;
import org.omg.spec.bpmn._20100524.di.BPMNShape;
import org.omg.spec.bpmn._20100524.model.TLane;
import org.uengine.kernel.Role;
import org.uengine.processpublisher.bpmn.Adapter;
import org.uengine.processpublisher.BPMNUtil;

import javax.xml.namespace.QName;
import java.util.Hashtable;

/**
 * Created by MisakaMikoto on 2015. 8. 13..
 */
public class RoleAdapter implements Adapter<Role, TLane> {
    @Override
    public TLane convert(Role src, Hashtable keyedContext) throws Exception {
        // make TLane
        TLane tLane = ObjectFactoryUtil.createBPMNObject(TLane.class);
        // TODO: role id?
        tLane.setId(src.getElementView().getId());
        tLane.setName(src.getName());

        BPMNShape bpmnShape = (BPMNShape) BPMNUtil.export(src.getElementView());
        bpmnShape.setBpmnElement(new QName(src.getElementView().getId()));

        BPMNDiagram bpmnDiagram = (BPMNDiagram) keyedContext.get("bpmnDiagram");
        // make diagramElement and PLane add bpmnShape
        bpmnDiagram.getBPMNPlane().getDiagramElement().add(ObjectFactoryUtil.createDefaultJAXBElement(BPMNShape.class, bpmnShape));

        return tLane;
    }
}
