package org.uengine.processpublisher.uengine3.importer;

import org.uengine.kernel.HumanActivity;
import org.uengine.kernel.ProcessDefinition;
import org.uengine.modeling.ElementView;
import org.uengine.processpublisher.Adapter;
import org.uengine.processpublisher.Index;

import java.util.Hashtable;

/**
 * Created by uengine on 2017. 6. 16..
 */
public class HumanActivityAdapter implements Adapter<HumanActivity, ProcessDefinition> {

    public final static int HumanActivity_WIDTH = 100;
    public final static int HumanActivity_HEIGHT = 100;

    private void createView(HumanActivity humanActivity) throws Exception {
        int indexX = Index.indexX.get();
        int indexY = Index.indexY.get();
        ElementView elementView = humanActivity.createView();
        elementView.setX(200 + (150*indexX));
        elementView.setY(200 + (150*indexY));
        elementView.setWidth(HumanActivity_WIDTH);
        elementView.setHeight(HumanActivity_HEIGHT);
        elementView.setId("HumanActivity_" + humanActivity.getTracingTag());
        humanActivity.setElementView(elementView);
        Index.indexX.set(indexX + 1);
    }

    @Override
    public ProcessDefinition convert(HumanActivity humanActivity, Hashtable keyedContext) throws Exception {
        this.createView(humanActivity);
        ProcessDefinition processDefinition = (ProcessDefinition) keyedContext.get("root");
        processDefinition.addChildActivity(humanActivity);
        return processDefinition;
    }
}
