package org.uengine.processpublisher.uengine3.importer;

import org.uengine.kernel.HumanActivity;
import org.uengine.kernel.ProcessDefinition;
import org.uengine.kernel.bpmn.SequenceFlow;
import org.uengine.modeling.ElementView;
import org.uengine.processpublisher.Adapter;
import org.uengine.processpublisher.AdapterUtil;

import java.util.Hashtable;

/**
 * Created by uengine on 2017. 6. 16..
 */
public class HumanActivityAdapter implements Adapter<HumanActivity, ProcessDefinition> {

    public final static int DIALOG_HEIGHT = 100;
    public final static int DIALOG_WIDTH = 100;

    private Hashtable calculatePositions(Hashtable hashtable) throws Exception {

        Hashtable keyedContext = new Hashtable();
        int index = (int) hashtable.get("index");
        if(index == 0) {
            String initialX = "200";
            String initialY = "200";
            keyedContext.put("x", initialX);
            keyedContext.put("y", initialY);
        } else {
            String x = 200 + (index * 200) + "";
            String y = "200";
            //TODO : 스위치 내의 휴먼 액티비티라면 y좌표도 설정해 줘야 한다.
            if(hashtable.containsKey("inGateway")) {
                //do something....
            }
            keyedContext.put("x", x);
            keyedContext.put("y", y);
        }

        return keyedContext;

    }

    @Override
    public ProcessDefinition convert(HumanActivity src, Hashtable keyedContext) throws Exception {

        ElementView elementView = src.createView();
        elementView.setX(Double.parseDouble(this.calculatePositions(keyedContext).get("x").toString()));
        elementView.setY(Double.parseDouble(this.calculatePositions(keyedContext).get("y").toString()));
        elementView.setWidth(DIALOG_WIDTH);
        elementView.setHeight(DIALOG_HEIGHT);
        elementView.setId("HumanActivity_"+src.getTracingTag());
        src.setElementView(elementView);
        ProcessDefinition processDefinition = (ProcessDefinition) keyedContext.get("root");
        processDefinition.addChildActivity(src);
        //AdapterUtil.createSequenceFlow(processDefinition, keyedContext);
        return processDefinition;
    }
}
