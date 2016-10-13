package org.uengine.kernel.test;

import org.uengine.kernel.*;
import org.uengine.kernel.bpmn.SequenceFlow;
import org.uengine.kernel.bpmn.SubProcess;

import java.io.FileOutputStream;

public class MultipleInstanceTest extends UEngineTest{

    ProcessDefinition processDefinition;
    SubProcess subProcess;

    /**
     * build a graph as follows:
     *
     *                +-------sub--------+
     *                |                  |
     *          9 --->|   1 -> 2 -> 3    |---> 7
     *                |                  |
     *                |        -         |
     *                |        -         |
     *                |        -         |
     *                +------------------+
     *
     *   * 1,2 and 3 are embraced in an embedded SubProcess

     *                +-------sub--------+
     *                |                  |
     *          9 --->|   1 -> 2 -> 3    |---> 7
     *                |                  |
     *                |        O         |
     *                +------------------+
     *
     * @throws Exception
     */
    public void setUp() throws Exception {

        processDefinition = new ProcessDefinition();

        processDefinition.setProcessVariables(new ProcessVariable[]{
                ProcessVariable.forName("var1"),
                ProcessVariable.forName("var2")
        });



        subProcess = new SubProcess();
        subProcess.setTracingTag("sub");

        subProcess.setForEachVariable(ProcessVariable.forName("var1"));

        processDefinition.addChildActivity(subProcess);


        for(int i=1; i<4; i++) {
            Activity a1 = new DefaultActivity();

            if(i==2){
                ReceiveActivity rcv = new ReceiveActivity();
                rcv.setMessage("receive");

                a1 = rcv;
            }

            a1.setTracingTag("a" + i);
            subProcess.addChildActivity(a1);
        }


        for(int i=4; i<20; i++) {
            Activity a1 = new DefaultActivity();

            a1.setTracingTag("a" + i);
            processDefinition.addChildActivity(a1);

        }


        {
            SequenceFlow t1 = new SequenceFlow();
            t1.setSourceRef("a9");
            t1.setTargetRef("sub");

            processDefinition.addSequenceFlow(t1);
        }
        {
            SequenceFlow t1 = new SequenceFlow();
            t1.setSourceRef("a1");
            t1.setTargetRef("a2");

            subProcess.addSequenceFlow(t1);
        }
        {
            SequenceFlow t1 = new SequenceFlow();
            t1.setSourceRef("a2");
            t1.setTargetRef("a3");

            subProcess.addSequenceFlow(t1);
        }

        {
            SequenceFlow t1 = new SequenceFlow();
            t1.setSourceRef("sub");
            t1.setTargetRef("a7");

            processDefinition.addSequenceFlow(t1);
        }

        processDefinition.afterDeserialization();

        AbstractProcessInstance.USE_CLASS = DefaultProcessInstance.class;

       // GlobalContext.serialize(processDefinition, new FileOutputStream(getClass().getName()+ ".process"), String.class);

    }

//    public void testMIForVariableValue() throws Exception {
//
//        ProcessInstance instance = processDefinition.createInstance();
//
//        ProcessVariableValue pvv = new ProcessVariableValue();
//        pvv.setName("var1");
//        pvv.setValue("1");
//        pvv.moveToAdd();
//        pvv.setValue("2");
//        pvv.moveToAdd();
//        pvv.setValue("3");
//
//        instance.set("var1", pvv);
//
//        instance.execute();
//
////        assertExecutionPathEquals("Running Before Event", new String[]{
////                "a9",       "a1", "a2", "a3",    "a1", "a2", "a3",      "a1", "a2", "a3",       "sub", "a7"
////        }, instance);
////
//
//
//        assertExecutionPathEquals("Running Before Event", new String[]{
//                "a9",       "a1", "a2",   "a1", "a2",   "a1", "a2"
//        }, instance);
//
//
//        instance.getProcessDefinition().fireMessage("receive", instance, null);
//
//
//    }

    public void testMIForExecutionScope() throws Exception {

        subProcess.setMultipleInstanceOption("parallel");



        ProcessInstance instance = processDefinition.createInstance();

        ProcessVariableValue pvv = new ProcessVariableValue();
        pvv.setName("var1");
        pvv.setValue("1");
        pvv.moveToAdd();
        pvv.setValue("2");
        pvv.moveToAdd();
        pvv.setValue("3");

        instance.set("var1", pvv);

        instance.execute();


        assertExecutionPathEquals("Running Before Event", new String[]{
                "a9",       "a1", "a1", "a1",
        }, instance);

       try {
            instance.getProcessDefinition().fireMessage("receive", instance, null);
        }catch(Exception e){
            fail("Receive should not be triggered");
        }


        assertExecutionPathEquals("In the main scope, receive will not be triggered", new String[]{
                "a9",       "a1", "a1", "a1",
        }, instance);


        instance.setExecutionScope("0");
        instance.getProcessDefinition().fireMessage("receive", instance, null);

        assertExecutionPathEquals("With Execution Scope 0", new String[]{
                "a9",       "a1", "a1", "a1",   "a2", "a3"
        }, instance);


        instance.setExecutionScope("1");
        instance.getProcessDefinition().fireMessage("receive", instance, null);

        assertExecutionPathEquals("With Execution Scope 1", new String[]{
                "a9",       "a1", "a1", "a1",   "a2", "a3",   "a2", "a3"
        }, instance);

        instance.setExecutionScope("2");
        instance.getProcessDefinition().fireMessage("receive", instance, null);

        assertExecutionPathEquals("With Execution Scope 2", new String[]{
                "a9",       "a1", "a1", "a1",   "a2", "a3",   "a2", "a3",  "a2", "a3",   "sub", "a7"
        }, instance);

    }


    public void testMIForLoop() throws Exception {

        subProcess.setMultipleInstanceOption("loop");


        ProcessInstance instance = processDefinition.createInstance();

        ProcessVariableValue pvv = new ProcessVariableValue();
        pvv.setName("var1");
        pvv.setValue("1");
        pvv.moveToAdd();
        pvv.setValue("2");
        pvv.moveToAdd();
        pvv.setValue("3");

        instance.set("var1", pvv);

        instance.execute();


        assertExecutionPathEquals("Running Before Event", new String[]{
                "a9",       "a1",
        }, instance);

        instance.setExecutionScope("0");
        instance.getProcessDefinition().fireMessage("receive", instance, null);

        assertExecutionPathEquals("With Execution Scope 0", new String[]{
                "a9",       "a1", "a2", "a3",   "a1",
        }, instance);


        instance.setExecutionScope("1");
        instance.getProcessDefinition().fireMessage("receive", instance, null);

        assertExecutionPathEquals("With Execution Scope 1", new String[]{
                "a9",       "a1", "a2", "a3",   "a1", "a2", "a3",   "a1"
        }, instance);

        instance.setExecutionScope("2");
        instance.getProcessDefinition().fireMessage("receive", instance, null);

        assertExecutionPathEquals("With Execution Scope 2", new String[]{
                "a9",       "a1", "a2", "a3",   "a1", "a2", "a3",   "a1", "a2", "a3",   "sub", "a7"
        }, instance);

    }

    public void _testMIRefreshParallel() throws Exception {

        subProcess.setMultipleInstanceOption("parallel");


        ProcessInstance instance = processDefinition.createInstance();

        ProcessVariableValue pvv = new ProcessVariableValue();
        pvv.setName("var1");
        pvv.setValue("1");
        pvv.moveToAdd();
        pvv.setValue("2");

        instance.set("var1", pvv);

        instance.execute();


        assertExecutionPathEquals("Running Before Event", new String[]{
                "a9",       "a1", "a1",
        }, instance);


        instance.setExecutionScope("0");
        instance.getProcessDefinition().fireMessage("receive", instance, null);

        assertExecutionPathEquals("With Execution Scope 0", new String[]{
                "a9",       "a1", "a1",   "a2", "a3"
        }, instance);



        ///// now add a new branch on the fly /////

        pvv = instance.getMultiple("", "var1");
        pvv.setName("var1");
        pvv.moveToAdd();
        pvv.setValue("3");

        instance.set("var1", pvv);

        subProcess.refreshMultipleInstance(instance); // this will trigger to add new branch for 3

        assertExecutionPathEquals("With Execution Scope 0", new String[]{
                "a9",       "a1", "a1",   "a2", "a3",   "a1"
        }, instance);


        instance.setExecutionScope("3");
        instance.getProcessDefinition().fireMessage("receive", instance, null);

        assertExecutionPathEquals("With Execution Scope 1", new String[]{
                "a9",       "a1", "a1",   "a2", "a3",   "a1", "a2", "a3"
        }, instance);


    }


    public void _testMIRefreshLoop() throws Exception {

        subProcess.setMultipleInstanceOption("loop");


        ProcessInstance instance = processDefinition.createInstance();

        ProcessVariableValue pvv = new ProcessVariableValue();
        pvv.setName("var1");
        pvv.setValue("0");
        pvv.moveToAdd();
        pvv.setValue("2");

        instance.set("var1", pvv);

        instance.execute();


        assertExecutionPathEquals("Running Before Event", new String[]{
                "a9",       "a1",
        }, instance);


        instance.setExecutionScope("0");
        instance.getProcessDefinition().fireMessage("receive", instance, null);

        assertExecutionPathEquals("With Execution Scope 0", new String[]{
                "a9",       "a1",  "a2", "a3",/* of 0 */   "a1"/* of 2 */
        }, instance);



        ///// now add a new branch on the fly /////

        pvv = new ProcessVariableValue();
        pvv.setName("var1");
        pvv.setValue("0");
        pvv.moveToAdd();
        pvv.setValue("1"); //insert 2 after the 3 is running.
        pvv.moveToAdd();
        pvv.setValue("2");

        instance.set("var1", pvv);

        subProcess.refreshMultipleInstance(instance); // this will trigger to add new branch for 1

        assertExecutionPathEquals("After refreshing (0,2 -> 0,1,2)", new String[]{
                "a9",       "a1",  "a2", "a3",/* of 0 */   "a1"/* of 2 */, "a1"/* of 1 */
        }, instance);


        instance.setExecutionScope("1");
        instance.getProcessDefinition().fireMessage("receive", instance, null);

        assertExecutionPathEquals("With Execution Scope 1", new String[]{
                "a9",       "a1",  "a2", "a3",/* of 0 */   "a1"/* of 2 */, "a1", "a2", "a3"/* of 1 */
        }, instance);


    }

}
