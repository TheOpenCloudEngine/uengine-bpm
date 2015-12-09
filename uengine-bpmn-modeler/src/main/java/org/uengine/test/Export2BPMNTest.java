package org.uengine.test;

import org.omg.spec.bpmn._20100524.model.TDefinitions;
import org.uengine.kernel.*;
import org.uengine.kernel.bpmn.SequenceFlow;
import org.uengine.kernel.bpmn.view.SequenceFlowView;
import org.uengine.kernel.view.HumanActivityView;
import org.uengine.kernel.view.RoleView;
import org.uengine.processpublisher.BPMNUtil;
import javax.xml.bind.*;
import java.io.File;

public class Export2BPMNTest {

    public static void main(String[] args){
        ProcessDefinition processDefinition = new ProcessDefinition();

        // Role
        Role role1 = new Role();

        RoleView roleView1 = new RoleView();
        roleView1.setId("roleView1");
        roleView1.setX(294);
        roleView1.setY(152);
        roleView1.setWidth(745);
        roleView1.setHeight(85);
        roleView1.setShapeId("OG.shape.HorizontalLaneShape");

        role1.setElementView(roleView1);
        role1.setName("role1");

        Role role2 = new Role();

        RoleView roleView2 = new RoleView();
        roleView2.setId("roleView2");
        roleView2.setX(294);
        roleView2.setY(269);
        roleView2.setWidth(745);
        roleView2.setHeight(81);
        roleView2.setShapeId("OG.shape.HorizontalLaneShape");

        role2.setElementView(roleView2);
        role2.setName("role2");

        // Activity
        HumanActivity activity1 = new HumanActivity();

        HumanActivityView humanActivityView1 = new HumanActivityView();
        humanActivityView1.setId("humanView1");
        humanActivityView1.setX(375);
        humanActivityView1.setY(187);
        humanActivityView1.setWidth(70);
        humanActivityView1.setHeight(40);
        humanActivityView1.setShapeId("OG.shape.bpmn.A_HumanTask");

        activity1.setElementView(humanActivityView1);
        activity1.setTracingTag("1");
        activity1.setName("act1");
        activity1.setRole(role1);


        HumanActivity activity2 = new HumanActivity();

        HumanActivityView humanActivityView2 = new HumanActivityView();
        humanActivityView2.setId("humanView2");
        humanActivityView2.setX(818);
        humanActivityView2.setY(300);
        humanActivityView2.setWidth(70);
        humanActivityView2.setHeight(40);
        humanActivityView2.setShapeId("OG.shape.bpmn.A_HumanTask");

        activity2.setElementView(humanActivityView2);
        activity2.setTracingTag("2");
        activity2.setName("act2");
        activity2.setRole(role2);

        // sequenceFlow
        SequenceFlow sequenceFlow = new SequenceFlow();
        sequenceFlow.setName("sequenceFlow");
        sequenceFlow.setTracingTag("3");
        sequenceFlow.setSourceRef(activity1.getElementView().getId());
        sequenceFlow.setTargetRef(activity2.getElementView().getId());

        SequenceFlowView sequenceFlowView = new SequenceFlowView();
        sequenceFlowView.setId("OG_9423_8");
        sequenceFlowView.setShapeId("OG.shape.EdgeShape");
        sequenceFlowView.setX(375);
        sequenceFlowView.setY(187);
        sequenceFlowView.setWidth(818);
        sequenceFlowView.setHeight(300);
        sequenceFlow.setRelationView(sequenceFlowView);

        // processDefinition
        processDefinition.setId("_processDefinition");
        processDefinition.setName("processDefinition");
        processDefinition.addChildActivity(activity1);
        processDefinition.addChildActivity(activity2);
        processDefinition.addRole(role1);
        processDefinition.addRole(role2);
        processDefinition.getSequenceFlows().add(sequenceFlow);

        try {
            processDefinition.beforeSerialization();
            TDefinitions tDefinitions = (TDefinitions) BPMNUtil.export(processDefinition);

            org.omg.spec.bpmn._20100524.model.ObjectFactory objectFactory = new org.omg.spec.bpmn._20100524.model.ObjectFactory();
            JAXBElement<TDefinitions> element = objectFactory.createDefinitions(tDefinitions);

            File file = new File("example.xml");
            JAXBContext jaxbContext =JAXBContext.newInstance(TDefinitions.class);
            Marshaller marshaller = jaxbContext.createMarshaller();

            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            marshaller.marshal(element, file);
            marshaller.marshal(element, System.out);

        } catch (Exception e) {

            e.printStackTrace();
        }
    }

}
