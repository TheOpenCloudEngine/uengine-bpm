package org.uengine.kernel.bpmn.view;

/**
 * Created by Ryuha on 2015-06-11.
 */
public class EscalationEventView extends EventView{

    public final static String SHAPE_ID = "OG.shape.bpmn.E_Intermediate_Escalation";
    public final static String SHAPE_TYPE 		 = "GEOM";
    public final static String ELEMENT_CLASSNAME = EscalationEventView.class.getName();

    public EscalationEventView(){
        setShapeId(SHAPE_ID);
    }
}
