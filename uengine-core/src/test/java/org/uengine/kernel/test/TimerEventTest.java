package org.uengine.kernel.test;

import org.metaworks.dwr.MetaworksRemoteService;
import org.metaworks.test.TestMetaworksRemoteService;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.uengine.kernel.*;
import org.uengine.kernel.bpmn.SequenceFlow;
import org.uengine.kernel.bpmn.TimerEvent;
import org.uengine.processmanager.ProcessManagerBean;
import org.uengine.processmanager.ProcessManagerRemote;

import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

public class TimerEventTest extends UEngineTest{

    ProcessDefinition processDefinition;

    final int timeInterval = 3000;

    /**
     * build a graph as follows:
     *
     *
     *     10 -> 9 -> 1 --------> 2 -> 3 -> 7 -> 11 -> 12
     *               (+)
     *                |
     *                +-> 5 -> 6 -> 4
     *
     *   * 5 is a TimerEvent which is attached to 1
     *
     * @throws Exception
     */
    public void setUp() throws Exception {

        //Set application context for testing
        new TestMetaworksRemoteService(
                new ClassPathXmlApplicationContext(
                          getClass().getName().replaceAll("\\.", "\\/") + "Context.xml"
                )
        );


        processDefinition = new ProcessDefinition();

        processDefinition.setProcessVariables(new ProcessVariable[]{
                ProcessVariable.forName("var1"),
                ProcessVariable.forName("var2")
        });


        for(int i=1; i<11; i++) {
            Activity a1 = new DefaultActivity();

            if(i == 1){
                ReceiveActivity rcv = new ReceiveActivity();
                rcv.setMessage("receive");

                a1 = rcv;

            }

            if(i == 5){
                TimerEvent event = new TimerEvent();
                event.setName("a5");
                event.setAttachedToRef("a1");
                event.setExpression("0/" + timeInterval / 1000 + " * * * * ?");

                a1 = event;
            }

            a1.setTracingTag("a" + i);
            processDefinition.addChildActivity(a1);

        }

        {
            SequenceFlow t1 = new SequenceFlow();
            t1.setSourceRef("a9");
            t1.setTargetRef("a1");

            processDefinition.addSequenceFlow(t1);
        }
        {
            SequenceFlow t1 = new SequenceFlow();
            t1.setSourceRef("a10");
            t1.setTargetRef("a9");

            processDefinition.addSequenceFlow(t1);
        }

        {
            SequenceFlow t1 = new SequenceFlow();
            t1.setSourceRef("a1");
            t1.setTargetRef("a2");

            processDefinition.addSequenceFlow(t1);
        }

        {
            SequenceFlow t1 = new SequenceFlow();
            t1.setSourceRef("a2");
            t1.setTargetRef("a3");

            processDefinition.addSequenceFlow(t1);
        }
        {
            SequenceFlow t1 = new SequenceFlow();
            t1.setSourceRef("a5");
            t1.setTargetRef("a6");

            processDefinition.addSequenceFlow(t1);
        }
        {
            SequenceFlow t1 = new SequenceFlow();
            t1.setSourceRef("a6");
            t1.setTargetRef("a4");

            processDefinition.addSequenceFlow(t1);
        }
        {
            SequenceFlow t1 = new SequenceFlow();
            t1.setSourceRef("a3");
            t1.setTargetRef("a7");

            processDefinition.addSequenceFlow(t1);
        }
        {
            SequenceFlow t1 = new SequenceFlow();
            t1.setSourceRef("a7");
            t1.setTargetRef("a8");

            processDefinition.addSequenceFlow(t1);
        }


        processDefinition.afterDeserialization();

        AbstractProcessInstance.USE_CLASS = DefaultProcessInstance.class;

    }

    public void testTimerEvent() throws Exception {

        ProcessManagerBean processManagerBean = (ProcessManagerBean) MetaworksRemoteService.getComponent(ProcessManagerRemote.class);

        Map options = new HashMap<String, Object>();
        options.put("ptc", processManagerBean.getTransactionContext());
        ProcessInstance instance = processDefinition.createInstance("test", options);

        instance.execute();
        assertExecutionPathEquals("Running Before Event", new String[]{
                "a10", "a9"
        }, instance);



        Vector mls = instance.getMessageListeners("event");


        Thread.sleep((long) (timeInterval * 1.2)); //after 10+ seconds later, the first timer event must be triggered.

        assertExecutionPathEquals("Triggering Event First Occurance", new String[]{
                "a10", "a9", "a5", "a6", "a4"
        }, instance);


        Thread.sleep(timeInterval); //after 20+ seconds later, the second timer event must be triggered.

        assertExecutionPathEquals("Triggering Event Second Occurance", new String[]{
                "a10", "a9", "a5", "a6", "a4", "a5", "a6", "a4"
        }, instance);


        instance.getProcessDefinition().fireMessage("receive", instance, null);
        assertExecutionPathEquals("Running After Event", new String[]{
                "a10", "a9", "a5", "a6", "a4", "a5", "a6", "a4", "a1", "a2", "a3", "a7", "a8"
        }, instance);


        Thread.sleep(timeInterval * 2);


        instance.getProcessDefinition().fireMessage("event", instance, "a5");
        assertExecutionPathEquals("Timer must be Expired", new String[]{
                "a10", "a9", "a5", "a6", "a4", "a5", "a6", "a4", "a1", "a2", "a3", "a7", "a8",
        }, instance);




    }


}
