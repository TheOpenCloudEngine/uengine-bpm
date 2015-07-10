package org.uengine.kernel.bpmn.view;

import org.uengine.kernel.bpmn.Event;
import org.uengine.kernel.bpmn.Gateway;
import org.uengine.kernel.bpmn.TimerEvent;
import org.uengine.kernel.view.ActivityView;
import org.uengine.modeling.Symbol;

/**
 * Created by Ryuha on 2015-06-12.
 */
public class TimerEventView extends EventView {

    public final static String SHAPE_ID = "OG.shape.bpmn.E_Start_Timer";
    public final static String SHAPE_TYPE = "GEOM";
    public final static String ELEMENT_CLASSNAME = TimerEvent.class.getName();

    public TimerEventView(){
        setShapeId(SHAPE_ID);
    }
}
