package org.uengine.processpublisher.uengine3.importer;

import org.uengine.kernel.HumanActivity;
import org.uengine.kernel.ProcessDefinition;
import org.uengine.modeling.ElementView;
import org.uengine.processpublisher.Adapter;

import java.util.Hashtable;

/**
 * Created by uengine on 2017. 6. 16..
 */
public class HumanActivityAdapter implements Adapter<HumanActivity, ProcessDefinition> {

    public final static int HumanActivity_WIDTH = 100;
    public final static int HumanActivity_HEIGHT = 100;

    /*
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
*/
    private void createView(HumanActivity humanActivity) throws Exception {
        ElementView elementView = humanActivity.createView();
        elementView.setX(200);
        elementView.setY(200);
        elementView.setWidth(HumanActivity_WIDTH);
        elementView.setHeight(HumanActivity_HEIGHT);
        elementView.setId("HumanActivity_" + humanActivity.getTracingTag());
        humanActivity.setElementView(elementView);
    }

    @Override
    public ProcessDefinition convert(HumanActivity humanActivity, Hashtable keyedContext) throws Exception {
        this.createView(humanActivity);
        ProcessDefinition processDefinition = (ProcessDefinition) keyedContext.get("root");
        processDefinition.addChildActivity(humanActivity);
        return processDefinition;
    }
}
