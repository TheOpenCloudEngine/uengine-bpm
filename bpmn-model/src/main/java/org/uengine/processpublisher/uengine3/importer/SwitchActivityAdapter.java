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
    @Override
    public ProcessDefinition convert(ComplexActivity src, Hashtable keyedContext) throws Exception {

        Gateway gateWay = new Gateway();
        ElementView elementView = gateWay.createView();
        elementView.setX(150);
        elementView.setY(150);
        elementView.setWidth(50);
        elementView.setHeight(50);
        gateWay.setElementView(elementView);
        ProcessDefinition processDefinition5 = (ProcessDefinition) keyedContext.get("root");
        processDefinition5.addChildActivity(gateWay);
        //Index.increase();
        //Hashtable newKeyedContext = new Hashtable();
        keyedContext.put("root", processDefinition5);
       // newKeyedContext.put("index", Index.getIndex());
        return super.convert(src, keyedContext);

    }
}
