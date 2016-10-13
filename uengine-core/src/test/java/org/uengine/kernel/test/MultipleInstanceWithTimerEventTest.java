package org.uengine.kernel.test;

import org.metaworks.dwr.MetaworksRemoteService;
import org.metaworks.test.TestMetaworksRemoteService;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.uengine.kernel.*;
import org.uengine.kernel.bpmn.SequenceFlow;
import org.uengine.kernel.bpmn.SubProcess;
import org.uengine.kernel.bpmn.TimerEvent;
import org.uengine.processmanager.ProcessManagerBean;
import org.uengine.processmanager.ProcessManagerRemote;

import java.io.FileOutputStream;
import java.util.HashMap;
import java.util.Map;

public class MultipleInstanceWithTimerEventTest extends UEngineTest{

    ProcessDefinition processDefinition;
    SubProcess subProcess;

    private static final int timeInterval = 3000;


    /**
     * build a graph as follows:
     *
     *                +--------sub---------+
     *                |                    |
     *          9 --->| 1(timer) -> 2 -> 3 |---> 7
     *                |                    |
     *                +--------------------+
     *
     * @throws Exception
     */
    public void setUp() throws Exception {

        //Set application context for testing
        new TestMetaworksRemoteService(
                new ClassPathXmlApplicationContext(
                        "org/uengine/kernel/test/TimerEventTestContext.xml"
                )
        );

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

            if(i==1){
                TimerEvent timerEvent = new TimerEvent();
                timerEvent.setExpression("0/" + timeInterval / 1000 + " * * * * ?");

                a1 = timerEvent;

            }

            if(i==2){
                ReceiveActivity rcv = new ReceiveActivity();
                rcv.setMessage("receive");

                a1 = rcv;
            }

            a1.setTracingTag("a" + i);
            a1.setName("a" + i);
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

    }



    public void testMIForLoop() throws Exception {

        subProcess.setMultipleInstanceOption("loop");


        ProcessManagerBean processManagerBean = (ProcessManagerBean) MetaworksRemoteService.getComponent(ProcessManagerRemote.class);
            Map options = new HashMap<String, Object>();
            options.put("ptc", processManagerBean.getTransactionContext());
            ProcessInstance instance = processDefinition.createInstance("test", options);

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
                "a9",
        }, instance);

        instance.setExecutionScope("0");
        instance.getProcessDefinition().fireMessage("receive", instance, null);

        //
        assertExecutionPathEquals("The First Receive will be ignored since the timer has not been completed", new String[]{
                "a9",
        }, instance);

        Thread.sleep((long) (timeInterval * 1.2));

        assertExecutionPathEquals("After timer, the next receive will be available", new String[]{
                "a9", "a1",
        }, instance);



        instance.getProcessDefinition().fireMessage("receive", instance, null);

        assertExecutionPathEquals("After timer, the next receive will be available", new String[]{
                "a9", "a1", "a2", "a3",
        }, instance);


        Thread.sleep(timeInterval * 3); //the timer job should be removed after the TimerEvent has been completed.

        instance.setExecutionScope("1");
        instance.getProcessDefinition().fireMessage("receive", instance, null);

        assertExecutionPathEquals("Event if it waits for longer time than the recurring time interval, event must be triggered just once.", new String[]{
                "a9",       "a1", "a2", "a3",   "a1", "a2", "a3",
        }, instance);


        Thread.sleep((long) (timeInterval * 1.2));

        instance.setExecutionScope("2");
        instance.getProcessDefinition().fireMessage("receive", instance, null);

        assertExecutionPathEquals("With Execution Scope 2", new String[]{
                "a9",       "a1", "a2", "a3",   "a1", "a2", "a3",   "a1", "a2", "a3",   "sub", "a7"
        }, instance);

    }

}
