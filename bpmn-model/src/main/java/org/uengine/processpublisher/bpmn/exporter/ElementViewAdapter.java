package org.uengine.processpublisher.bpmn.exporter;

import org.omg.spec.bpmn._20100524.di.BPMNShape;
import org.omg.spec.dd._20100524.dc.Bounds;
import org.uengine.modeling.ElementView;
import org.uengine.processpublisher.bpmn.Adapter;
import java.util.Hashtable;

/**
 * Created by MisakaMikoto on 2015. 8. 14..
 */
public class ElementViewAdapter implements Adapter<ElementView, BPMNShape> {
    @Override
    public BPMNShape convert(ElementView src, Hashtable keyedContext) throws Exception {
        // make Shape
        BPMNShape bpmnShape = ObjectFactoryUtil.createBPMNObject(BPMNShape.class);
        bpmnShape.setBounds(ObjectFactoryUtil.createBPMNObject(Bounds.class));
        bpmnShape.getBounds().setX(src.getX());
        bpmnShape.getBounds().setY(src.getY());
        bpmnShape.getBounds().setWidth((src.getWidth()));
        bpmnShape.getBounds().setHeight((src.getHeight()));

        return bpmnShape;
    }
}
