package org.uengine.test;

import net.sf.mpxj.ProjectFile;
import net.sf.mpxj.mpx.MPXWriter;
import net.sf.mpxj.mspdi.MSPDIWriter;
import org.uengine.kernel.GlobalContext;
import org.uengine.kernel.HumanActivity;
import org.uengine.kernel.ProcessDefinition;
import org.uengine.kernel.Role;
import org.uengine.kernel.bpmn.SequenceFlow;
import org.uengine.kernel.bpmn.view.SequenceFlowView;
import org.uengine.kernel.view.HumanActivityView;
import org.uengine.kernel.view.RoleView;
import org.uengine.processpublisher.microsoft.exporter.ProcessDefinitionAdapter;

import java.io.FileInputStream;

/**
 * Created by MisakaMikoto on 2015. 10. 21..
 */
public class Export2MSProjectTest {

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
        activity1.setDuration(3);
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
        activity1.setDuration(5);
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
//            processDefinition.beforeSerialization();
//            ProjectFile projectFile = processDefinitionAdapter.convert(processDefinition);

            ProcessDefinitionAdapter processDefinitionAdapter = new ProcessDefinitionAdapter();
            //String filePath = "/Users/uengine/oce/repository/codebase/1001/codi/ProcessDefinitionToMSProject.process";

            //ProcessDefinition pd = (ProcessDefinition) GlobalContext.deserialize(new FileInputStream(filePath), String.class);
            //pd.beforeSerialization();
            ProjectFile projectFile = processDefinitionAdapter.convert(processDefinition);

            MSPDIWriter mspdiWriter = new MSPDIWriter();

            // processDefinitionAdapter.convert 메서드를 통해
            // ProcessDefinitionToMSProject.process 에서 export_process_exampleproject.xml 으로 컨버트.

            mspdiWriter.write(projectFile, "D:\\export_process_exampleproject.xml");

        } catch (Exception e) {

            e.printStackTrace();
        }
    }

}
