package org.uengine.processpublisher.bpmn.importer;

import org.omg.spec.bpmn._20100524.di.BPMNShape;
import org.omg.spec.bpmn._20100524.model.TFlowNode;
import org.omg.spec.bpmn._20100524.model.TLane;
import org.omg.spec.bpmn._20100524.model.TLaneSet;
import org.omg.spec.bpmn._20100524.model.TProcess;
import org.omg.spec.dd._20100524.dc.Bounds;
import org.uengine.kernel.Activity;
import org.uengine.kernel.DefaultActivity;
import org.uengine.kernel.Role;
import org.uengine.kernel.bpmn.FlowActivity;
import org.uengine.modeling.ElementView;
import org.uengine.processpublisher.Adapter;
import org.uengine.processpublisher.BPMNUtil;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

public class TFlowNodeAdapter<T extends TFlowNode, T1 extends Activity> implements Adapter<T, T1> {
    @Override
    public T1 convert(T src, Hashtable keyedContext) throws Exception {
        Activity activity = create(src, keyedContext);
        activity.setName(src.getName());
        activity.setTracingTag(src.getId());

        Map bpmnDiagramElementMap = (Map) keyedContext.get("BPMNDiagramElementMap");
        BPMNShape bpmnShape = (BPMNShape) bpmnDiagramElementMap.get(src.getId());

        if(bpmnShape == null){
            bpmnShape = new BPMNShape();
            bpmnShape.setBounds(new Bounds());
            bpmnShape.getBounds().setX(0);
            bpmnShape.getBounds().setY(0);
            bpmnShape.getBounds().setWidth(100);
            bpmnShape.getBounds().setHeight(50);
            //bpmnShape.getBounds().setHeight(50);
        }

        ElementView elementView = activity.createView();
        elementView.setX((int) Math.round(bpmnShape.getBounds().getX() + (bpmnShape.getBounds().getWidth() / 2)));
        elementView.setY((int) Math.round(bpmnShape.getBounds().getY() + (bpmnShape.getBounds().getHeight() / 2)));
        elementView.setWidth((int) Math.round(bpmnShape.getBounds().getWidth()));
        elementView.setHeight((int) Math.round(bpmnShape.getBounds().getHeight()));
        elementView.setId(activity.getTracingTag());

        // find elements parent
        TProcess bpmnProcess = (TProcess) keyedContext.get("tProcess");

        for(TLaneSet laneSet: bpmnProcess.getLaneSet()){
            if(isInActivityToLane(elementView, (BPMNShape) bpmnDiagramElementMap.get(laneSet.getId()))) {
                elementView.setParent(laneSet.getId());
            }

            for(TLane tLane: laneSet.getLane()){
                if(isInActivityToLane(elementView, (BPMNShape) bpmnDiagramElementMap.get(tLane.getId()))) {
                    elementView.setParent(tLane.getId());
                }
            }
        }
        activity.setElementView(elementView);

        return (T1)activity;
    }

    protected Activity create(T src, Hashtable keyedContext) {
       return new DefaultActivity();
    }

    private boolean isInActivityToLane(ElementView elementView, BPMNShape bpmnShape) {
        double x = (elementView.getX());
        double y = (elementView.getY());
        double width = (elementView.getWidth());
        double height = (elementView.getHeight());
        double left = x - (width / 2);
        double right = x + (width / 2);
        double top = y - (height / 2);
        double bottom = y + (height / 2);

        double p_x = (bpmnShape.getBounds().getX() + (bpmnShape.getBounds().getWidth() / 2));
        double p_y = (bpmnShape.getBounds().getY() + (bpmnShape.getBounds().getHeight() / 2));
        double p_width = (bpmnShape.getBounds().getWidth());
        double p_height = (bpmnShape.getBounds().getHeight());
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
