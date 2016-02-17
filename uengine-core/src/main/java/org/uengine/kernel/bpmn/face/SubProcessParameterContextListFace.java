package org.uengine.kernel.bpmn.face;

import org.metaworks.annotation.AutowiredFromClient;
import org.metaworks.annotation.Face;
import org.metaworks.annotation.Order;
import org.metaworks.annotation.ServiceMethod;
import org.metaworks.widget.ListFace;
import org.uengine.kernel.ParameterContext;
import org.uengine.kernel.SubProcessParameterContext;

public class SubProcessParameterContextListFace extends ListFace<SubProcessParameterContext> {

    /**
	 * 
	 * @param processVariablePanel
	 */
	@Order(1)
	@Face(displayName="Add New")
	@ServiceMethod(callByContent=true)
    public void add(@AutowiredFromClient ProcessVariablePanel processVariablePanel) {
        super.add();
    }
}

