package org.uengine.processpublisher.bpmn.exporter;

import org.omg.spec.bpmn._20100524.di.BPMNEdge;
import org.omg.spec.dd._20100524.dc.Point;
import org.uengine.modeling.RelationView;
import org.uengine.processpublisher.Adapter;
import org.uengine.processpublisher.ObjectFactoryUtil;
import java.util.Hashtable;

/**
 * Created by MisakaMikoto on 2015. 8. 14..
 */
public class RelationViewAdapter implements Adapter<RelationView, BPMNEdge> {
    @Override
    public BPMNEdge convert(RelationView src, Hashtable keyedContext) throws Exception {
        // make Edge
        BPMNEdge bpmnEdge = ObjectFactoryUtil.createBPMNObject(BPMNEdge.class);
        bpmnEdge.getWaypoint().add(ObjectFactoryUtil.createBPMNObject(Point.class));
        bpmnEdge.getWaypoint().add(ObjectFactoryUtil.createBPMNObject(Point.class));

        //bpmnEdge.setId(src.getName());
        bpmnEdge.getWaypoint().get(0).setX(Double.parseDouble(src.getX()));
        bpmnEdge.getWaypoint().get(0).setY(Double.parseDouble(src.getY()));
        bpmnEdge.getWaypoint().get(1).setX(Double.parseDouble(src.getWidth()));
        bpmnEdge.getWaypoint().get(1).setY(Double.parseDouble(src.getHeight()));

        return bpmnEdge;
    }
}
