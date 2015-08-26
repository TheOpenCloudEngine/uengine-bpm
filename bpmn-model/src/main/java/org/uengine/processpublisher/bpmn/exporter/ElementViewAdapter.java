package org.uengine.processpublisher.bpmn.exporter;

import org.omg.spec.bpmn._20100524.di.BPMNShape;
import org.omg.spec.dd._20100524.dc.Bounds;
import org.uengine.kernel.Activity;
import org.uengine.modeling.ElementView;
import org.uengine.processpublisher.Adapter;
import org.uengine.processpublisher.ObjectFactoryUtil;

import javax.xml.namespace.QName;
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
        bpmnShape.getBounds().setX(Double.parseDouble(src.getX()));
        bpmnShape.getBounds().setY(Double.parseDouble(src.getY()));
        bpmnShape.getBounds().setWidth(Double.parseDouble(src.getWidth()));
        bpmnShape.getBounds().setHeight(Double.parseDouble(src.getHeight()));

        return bpmnShape;
    }
}
