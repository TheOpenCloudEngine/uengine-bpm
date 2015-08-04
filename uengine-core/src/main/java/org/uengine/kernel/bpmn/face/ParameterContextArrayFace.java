package org.uengine.kernel.bpmn.face;

import org.metaworks.annotation.AutowiredFromClient;
import org.metaworks.annotation.Face;
import org.metaworks.annotation.Order;
import org.metaworks.annotation.ServiceMethod;
import org.metaworks.widget.ArrayFace;
import org.metaworks.widget.ListFace;
import org.uengine.kernel.ParameterContext;

public class ParameterContextArrayFace extends ArrayFace<ParameterContext> {

    @Order(1)
    @Face(displayName="Add New")
    @ServiceMethod(callByContent=true)

    public void add(@AutowiredFromClient ProcessVariablePanel processVariablePanel) {
        super.add();
    }
}
