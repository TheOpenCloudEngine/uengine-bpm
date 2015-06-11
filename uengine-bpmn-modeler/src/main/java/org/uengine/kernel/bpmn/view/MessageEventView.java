package org.uengine.kernel.bpmn.view;

import org.uengine.kernel.bpmn.StartEvent;
import org.uengine.modeling.IElement;

/**
 * Created by Ryuha on 2015-06-11.
 */
public class MessageEventView extends EventView{

    public final static String SHAPE_ID = "OG.shape.bpmn.E_Intermediate_Message";
    public final static String SHAPE_TYPE = "GEOM";
    public final static String ELEMENT_CLASSNAME = StartEvent.class.getName();

    public MessageEventView(){
        setShapeId(SHAPE_ID);
    }

}
