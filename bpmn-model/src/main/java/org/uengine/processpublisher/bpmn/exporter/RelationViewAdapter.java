package org.uengine.processpublisher.bpmn.exporter;

import org.omg.spec.bpmn._20100524.di.BPMNEdge;
import org.omg.spec.dd._20100524.dc.Point;
import org.uengine.modeling.ElementView;
import org.uengine.modeling.RelationView;
import org.uengine.processpublisher.Adapter;
import java.util.Hashtable;
import java.util.List;

/**
 * Created by MisakaMikoto on 2015. 8. 14..
 */
public class RelationViewAdapter implements Adapter<RelationView, BPMNEdge> {
    ElementView targetRefElementView;
    public ElementView getTargetRefElementView() {
        return targetRefElementView;
    }
    public void setTargetRefElementView(ElementView targetRefElementView) {
        this.targetRefElementView = targetRefElementView;
    }

    @Override
    public BPMNEdge convert(RelationView src, Hashtable keyedContext) throws Exception {
        // make Edge
        BPMNEdge bpmnEdge = ObjectFactoryUtil.createBPMNObject(BPMNEdge.class);
        bpmnEdge.getWaypoint().add(ObjectFactoryUtil.createBPMNObject(Point.class));
        bpmnEdge.getWaypoint().add(ObjectFactoryUtil.createBPMNObject(Point.class));

        //bpmnEdge.setId(src.getName());
        bpmnEdge.getWaypoint().get(0).setX((src.getX() - (src.getWidth() / 2)));
        bpmnEdge.getWaypoint().get(0).setY((src.getY() - (src.getHeight() / 2)));

        bpmnEdge.getWaypoint().get(1).setX(getTargetRefElementView().getX() - (getTargetRefElementView().getWidth() / 2));
        bpmnEdge.getWaypoint().get(1).setY(getTargetRefElementView().getY());

        return bpmnEdge;
    }
}
