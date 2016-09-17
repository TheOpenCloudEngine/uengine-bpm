package org.uengine.kernel.face;

import org.metaworks.Face;
import org.metaworks.annotation.AutowiredFromClient;
import org.metaworks.annotation.Order;
import org.metaworks.annotation.ServiceMethod;
import org.metaworks.widget.ArrayFace;
import org.metaworks.widget.ListFace;
import org.uengine.kernel.RoleParameterContext;
import org.uengine.kernel.bpmn.face.ProcessVariablePanel;
import org.uengine.modeling.Canvas;

/**
 * Created by jjy on 2012. 1. 2..
 */
public class RoleParameterContextArrayFace extends ArrayFace<RoleParameterContext> {

    @Order(1)
    @org.metaworks.annotation.Face(displayName="Add New")
    @ServiceMethod(callByContent=true)
    public void add(@AutowiredFromClient Canvas canvas) {
        super.add();
    }

}
