package org.uengine.processpublisher.bpmn.importer;

import org.omg.spec.bpmn._20100524.di.BPMNShape;
import org.omg.spec.bpmn._20100524.model.TLane;
import org.uengine.kernel.Role;
import org.uengine.modeling.ElementView;
import org.uengine.processpublisher.Adapter;

import java.util.HashMap;
import java.util.Hashtable;

/**
 * Created by MisakaMikoto on 2016. 7. 8..
 */
public class TLaneAdapter implements Adapter<TLane, Role> {
    @Override
    public Role convert(TLane src, Hashtable keyedContext) throws Exception {
        Role role = new Role();
        role.setName(src.getName());

        ElementView view = role.createView();
//      Pool pool = new Pool();
//      pool.setName(tLane.getName());
//      ElementView view = pool.createView();

        HashMap bpmnDiagramElementMap = (HashMap) keyedContext.get("BPMNDiagramElementMap");
        BPMNShape bpmnShape = (BPMNShape) bpmnDiagramElementMap.get(src.getId());
        view.setX((int) Math.round(bpmnShape.getBounds().getX() + (bpmnShape.getBounds().getWidth() / 2)));
        view.setY((int) Math.round(bpmnShape.getBounds().getY() + (bpmnShape.getBounds().getHeight() / 2)));
        view.setWidth((int) Math.round(bpmnShape.getBounds().getWidth()));
        view.setHeight((int) Math.round(bpmnShape.getBounds().getHeight()));
        view.setId(src.getId());

        role.setElementView(view);
//      processDefinition.addPool(pool);

        return role;
    }
}
