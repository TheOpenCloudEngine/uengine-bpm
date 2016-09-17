package org.uengine.kernel.face;

import org.metaworks.Face;
import org.metaworks.annotation.AutowiredFromClient;
import org.metaworks.annotation.Order;
import org.metaworks.annotation.ServiceMethod;
import org.metaworks.widget.ListFace;
import org.uengine.kernel.RoleParameterContext;
import org.uengine.modeling.Canvas;

/**
 * Created by jjy on 2016. 9. 15..
 */
public class RoleParameterContextListFace extends ListFace<RoleParameterContext> {

    @Order(1)
    @org.metaworks.annotation.Face(displayName="Add New")
    @ServiceMethod(callByContent=true)
    public void add(@AutowiredFromClient Canvas canvas) {
        super.add();
    }

}
