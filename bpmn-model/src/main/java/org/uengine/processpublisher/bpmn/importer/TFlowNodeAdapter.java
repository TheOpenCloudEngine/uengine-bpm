package org.uengine.processpublisher.bpmn.importer;

import org.omg.spec.bpmn._20100524.di.BPMNShape;
import org.omg.spec.bpmn._20100524.model.TFlowNode;
import org.omg.spec.dd._20100524.dc.Bounds;
import org.uengine.kernel.Activity;
import org.uengine.kernel.DefaultActivity;
import org.uengine.kernel.bpmn.FlowActivity;
import org.uengine.modeling.ElementView;
import org.uengine.processpublisher.Adapter;

import java.util.Hashtable;
import java.util.Map;

public class TFlowNodeAdapter<T extends TFlowNode, T1 extends Activity> implements Adapter<T, T1> {


    @Override
    public T1 convert(T src, Hashtable keyedContext) throws Exception {
        FlowActivity parent = (FlowActivity) keyedContext.get("parent");

        Activity activity = create(src, keyedContext);

        activity.setName(src.getName());
        activity.setTracingTag(src.getId());


        Map bpmnDiagramElementMap = (Map) keyedContext.get("BPMNDiagramElementMap");

        BPMNShape bpmnShape = (BPMNShape) bpmnDiagramElementMap.get(src.getId());

        if(bpmnShape==null){
            bpmnShape = new BPMNShape();
            bpmnShape.setBounds(new Bounds());
            bpmnShape.getBounds().setX(0);
            bpmnShape.getBounds().setY(0);
            bpmnShape.getBounds().setWidth(100);
            bpmnShape.getBounds().setHeight(50);
            //bpmnShape.getBounds().setHeight(50);

        }

        ElementView view = activity.createView();

        view.setX((int) Math.round(bpmnShape.getBounds().getX() + bpmnShape.getBounds().getWidth() / 2));
        view.setY((int) Math.round(bpmnShape.getBounds().getY() + bpmnShape.getBounds().getHeight() / 2));
        view.setWidth((int) Math.round(bpmnShape.getBounds().getWidth()));
        view.setHeight((int) Math.round(bpmnShape.getBounds().getHeight()));
        view.setId(activity.getTracingTag());

        activity.setElementView(view);

        return (T1)activity;
    }

    protected Activity create(T src, Hashtable keyedContext) {
       return new DefaultActivity();
    }

}
