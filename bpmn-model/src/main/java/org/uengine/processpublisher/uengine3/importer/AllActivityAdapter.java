package org.uengine.processpublisher.uengine3.importer;

import org.uengine.kernel.ComplexActivity;
import org.uengine.kernel.ProcessDefinition;
import org.uengine.kernel.bpmn.ParallelGateway;
import org.uengine.modeling.ElementView;

import java.util.Hashtable;

/**
 * Created by uengine on 2017. 6. 16..
 */
public class AllActivityAdapter extends ComplexActivityAdapter {

    public final static int AllActivity_WIDTH = 30;
    public final static int AllActivity_HEIGHT = 30;

    private ParallelGateway createView() throws Exception {
        ParallelGateway parallelGateway = new ParallelGateway();
        ElementView elementView = parallelGateway.createView();
        elementView.setX(150);
        elementView.setY(150);
        elementView.setWidth(AllActivity_WIDTH);
        elementView.setHeight(AllActivity_HEIGHT);
        parallelGateway.setElementView(elementView);

        return parallelGateway;
    }

    @Override
    public ProcessDefinition convert(ComplexActivity complexActivity, Hashtable keyedContext) throws Exception {

        ParallelGateway parallelGateway = this.createView();
        ProcessDefinition processDefinition5 = (ProcessDefinition) keyedContext.get("root");
        processDefinition5.addChildActivity(parallelGateway);
        keyedContext.put("root", processDefinition5);
        return super.convert(complexActivity, keyedContext);

    }
}
