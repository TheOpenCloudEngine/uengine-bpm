package org.uengine.kernel.view;

import org.uengine.kernel.ReceiveRestMessageEventActivity;
import org.uengine.kernel.bpmn.view.EventView;

/**
 * Created by soo on 2015. 7. 10..
 */
public class ReceiveRestMessageEventActivityView extends EventView {

    public final static String SHAPE_ID = "OG.shape.bpmn.E_Start_Message";
    public final static String SHAPE_TYPE = "GEOM";
    public final static String ELEMENT_CLASSNAME = ReceiveRestMessageEventActivity.class.getName();

    public ReceiveRestMessageEventActivityView(){
        setShapeId(SHAPE_ID);
    }
}

