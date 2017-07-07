package org.uengine.processpublisher.uengine3.importer;

import org.uengine.kernel.ComplexActivity;
import org.uengine.kernel.ProcessDefinition;
import org.uengine.kernel.bpmn.Gateway;
import org.uengine.modeling.ElementView;
import org.uengine.processpublisher.Index;

import java.util.Hashtable;

/**
 * Created by uengine on 2017. 6. 16..
 */
public class SwitchActivityAdapter extends ComplexActivityAdapter {

    public final static int SwitchActivity_WIDTH = 30;
    public final static int SwitchActivity_HEIGHT = 30;

    private Gateway createView() throws Exception {
        int indexX = Index.indexX.get();
        int indexY = Index.indexY.get();
        Gateway gateWay = new Gateway();
        ElementView elementView = gateWay.createView();
        elementView.setX(200 + (100*indexX));
        elementView.setY(200 + (100*indexY));
        elementView.setWidth(SwitchActivity_WIDTH);
        elementView.setHeight(SwitchActivity_HEIGHT);
        gateWay.setElementView(elementView);
        Index.indexX.set(indexX + 1);
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
