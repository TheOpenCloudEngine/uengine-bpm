package org.uengine.processpublisher.bpmn.exporter;

import org.omg.spec.bpmn._20100524.di.BPMNDiagram;
import org.omg.spec.bpmn._20100524.di.BPMNShape;
import org.omg.spec.bpmn._20100524.model.TExtension;
import org.omg.spec.bpmn._20100524.model.TExtensionElements;
import org.omg.spec.bpmn._20100524.model.TUserTask;
import org.uengine.kernel.HumanActivity;
import org.uengine.processpublisher.Adapter;
import org.uengine.processpublisher.BPMNUtil;
import javax.xml.namespace.QName;
import java.util.Hashtable;

/**
 * Created by uengine on 2015. 8. 14..
 */
public class HumanActivityAdapter implements Adapter<HumanActivity, TUserTask> {
    @Override
    public TUserTask convert(HumanActivity src, Hashtable keyedContext) throws Exception {
        // setting HumanActivity
        TUserTask tUserTask = ObjectFactoryUtil.createBPMNObject(TUserTask.class);
        tUserTask.setId(src.getTracingTag());
        tUserTask.setName(src.getName());

        String outgoing = getOutgoing(src);
        String incoming = getIncoming(src);

        if(outgoing != null && outgoing.length() > 0) {
            tUserTask.getOutgoing().add(new QName(outgoing));
        }

        if(incoming != null && incoming.length() > 0) {
            tUserTask.getIncoming().add(new QName(incoming));
        }

        ElementViewAdapter elementViewAdapter = new ElementViewAdapter();
        BPMNShape bpmnShape = elementViewAdapter.convert(src.getElementView(), null);
        bpmnShape.setBpmnElement(new QName(src.getTracingTag()));

        BPMNDiagram bpmnDiagram = (BPMNDiagram) keyedContext.get("bpmnDiagram");
        // make diagramElement and PLane add bpmnShape
        bpmnDiagram.getBPMNPlane().getDiagramElement().add(ObjectFactoryUtil.createDefaultJAXBElement(BPMNShape.class, bpmnShape));

        return tUserTask;
    }

    private String getOutgoing(HumanActivity humanActivity) {
        String outgoing = null;
        if(humanActivity.getElementView().getFromEdge() != null && humanActivity.getElementView().getFromEdge().length() > 0) {
            outgoing = humanActivity.getElementView().getFromEdge();
        }
        return outgoing;
    }

    private String getIncoming(HumanActivity humanActivity) {
        String incoming = null;
        if(humanActivity.getElementView().getToEdge() != null && humanActivity.getElementView().getToEdge().length() > 0) {
            incoming = humanActivity.getElementView().getToEdge();
        }
        return incoming;
    }
}
