package org.uengine.processpublisher.bpmn.exporter;

import org.omg.spec.bpmn._20100524.model.TLane;
import org.uengine.kernel.Role;
import org.uengine.processpublisher.Adapter;
import org.uengine.processpublisher.ObjectFactoryUtil;
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

        return tLane;
    }
}
