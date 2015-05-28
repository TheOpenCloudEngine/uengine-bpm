package org.uengine.kernel.test;

import org.uengine.kernel.*;
import org.uengine.kernel.bpmn.BlockFinder;
import org.uengine.kernel.bpmn.Event;
import org.uengine.kernel.bpmn.Gateway;
import org.uengine.kernel.bpmn.SequenceFlow;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class EventTest extends UEngineTest{

    ProcessDefinition processDefinition;

    /**
     * build a graph as follows:
     *
     *
     *     10 -> 9 -> 1 --------> 2 -> 3 -> 7 -> 11 -> 12
     *               (+)
     *                |
     *                +-> 5 -> 6 -> 4
     *
     *   * 5 is EscalationEvent which is attached to 1
     *
     * @throws Exception
     */
    public void setUp() throws Exception {

        processDefinition = new ProcessDefinition();

        processDefinition.setProcessVariables(new ProcessVariable[]{
                ProcessVariable.forName("var1"),
                ProcessVariable.forName("var2")
        });


        for(int i=1; i<20; i++) {
            Activity a1 = new DefaultActivity();

            if(i == 1){
                ReceiveActivity rcv = new ReceiveActivity();
                rcv.setMessage("receive");

                a1 = rcv;

            }

            if(i == 5){
                Event event = new Event();
                event.setName("a5");
                event.setAttachedToRef("a1");

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
            t1.setTargetRef("a11");

            processDefinition.addSequenceFlow(t1);
        }
        {
            SequenceFlow t1 = new SequenceFlow();
            t1.setSourceRef("a11");
            t1.setTargetRef("a12");

            processDefinition.addSequenceFlow(t1);
        }

        processDefinition.afterDeserialization();

        ProcessInstance.USE_CLASS = DefaultProcessInstance.class;


    }

    public void testEscalationEvent() throws Exception {

//        ProcessInstance instance = processDefinition.createInstance();
//
//        instance.execute();
//        assertExecutionPathEquals("Running Before Event", new String[]{
//                "a10", "a9"
//        }, instance);
//
//
//
//
//        instance.getProcessDefinition().fireMessage("event", instance, "a5");
//        assertExecutionPathEquals("Triggering Event Once", new String[]{
//                "a10", "a9", "a5", "a6", "a4"
//        }, instance);
//
//
//        instance.getProcessDefinition().fireMessage("event", instance, "a5");
//        assertExecutionPathEquals("Running Before Twice", new String[]{
//                "a10", "a9", "a5", "a6", "a4", "a5", "a6", "a4"
//        }, instance);
//
//
//
//        // TODO:  Execution Scopes should be designated.
//
//
//        instance.getProcessDefinition().fireMessage("receive", instance, null);
//        assertExecutionPathEquals("Running After Event", new String[]{
//                "a10", "a9", "a5", "a6", "a4", "a5", "a6", "a4", "a1", "a2", "a3", "a7", "a11", "a12"
//        }, instance);
//
//
//        instance.getProcessDefinition().fireMessage("event", instance, "a5");
//        assertExecutionPathEquals("Triggering Expired Event", new String[]{
//                "a10", "a9", "a5", "a6", "a4", "a5", "a6", "a4", "a1", "a2", "a3", "a7", "a11", "a12",
//        }, instance);
//



    }


}
