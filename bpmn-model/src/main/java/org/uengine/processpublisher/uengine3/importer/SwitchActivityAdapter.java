package org.uengine.processpublisher.uengine3.importer;

import org.uengine.kernel.ComplexActivity;
import org.uengine.kernel.ProcessDefinition;
import org.uengine.kernel.bpmn.Gateway;
import org.uengine.modeling.ElementView;

import java.util.Hashtable;

/**
 * Created by uengine on 2017. 6. 16..
 */
public class SwitchActivityAdapter extends ComplexActivityAdapter {

    public final static int SwitchActivity_WIDTH = 30;
    public final static int SwitchActivity_HEIGHT = 30;

    private Gateway createView() throws Exception {
        Gateway gateWay = new Gateway();
        ElementView elementView = gateWay.createView();
        elementView.setX(150);
        elementView.setY(150);
        elementView.setWidth(SwitchActivity_WIDTH);
        elementView.setHeight(SwitchActivity_HEIGHT);
        gateWay.setElementView(elementView);
        return gateWay;
    }

    @Override
    public ProcessDefinition convert(ComplexActivity complexActivity, Hashtable keyedContext) throws Exception {

        Gateway gateWay = this.createView();
        ProcessDefinition processDefinition5 = (ProcessDefinition) keyedContext.get("root");
        processDefinition5.addChildActivity(gateWay);
        keyedContext.put("root", processDefinition5);
        return super.convert(complexActivity, keyedContext);

    }
}
