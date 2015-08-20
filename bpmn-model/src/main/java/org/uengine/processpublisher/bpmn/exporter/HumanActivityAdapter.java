package org.uengine.processpublisher.bpmn.exporter;

import org.omg.spec.bpmn._20100524.di.BPMNDiagram;
import org.omg.spec.bpmn._20100524.di.BPMNShape;
import org.omg.spec.bpmn._20100524.model.TUserTask;
import org.uengine.kernel.HumanActivity;
import org.uengine.processpublisher.Adapter;
import org.uengine.processpublisher.BPMNUtil;
import org.uengine.processpublisher.ObjectFactoryUtil;

import javax.xml.namespace.QName;
import java.util.Hashtable;

/**
 * Created by uengine on 2015. 8. 14..
 */
public class HumanActivityAdapter implements Adapter<HumanActivity, TUserTask> {
    @Override
    public TUserTask convert(HumanActivity src, Hashtable keyedContext) throws Exception {
        // setting HumanActivity
        TUserTask tUserTask = ObjectFactoryUtil.createBPMNObject(TUserTask.class);
        tUserTask.setId(src.getTracingTag());
        tUserTask.setName(src.getName());
        //TODO: outgoing, incoming (uEngine parameters[])
        //tUserTask.getOutgoing().add();
        //tUserTask.getIncoming().add();

        BPMNShape bpmnShape = (BPMNShape) BPMNUtil.export(src.getElementView());
        bpmnShape.setBpmnElement(new QName(src.getTracingTag()));

        BPMNDiagram bpmnDiagram = (BPMNDiagram) keyedContext.get("bpmnDiagram");
        // make diagramElement and PLane add bpmnShape
        bpmnDiagram.getBPMNPlane().getDiagramElement().add(ObjectFactoryUtil.createDefaultJAXBElement(BPMNShape.class, bpmnShape));

        return tUserTask;
    }
}
