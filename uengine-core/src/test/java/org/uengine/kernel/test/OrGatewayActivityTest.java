package org.uengine.kernel.test;

import junit.framework.TestCase;
import org.uengine.kernel.*;
import org.uengine.kernel.graph.Transition;
import java.util.*;

public class OrGatewayActivityTest extends TestCase{

    ProcessDefinition processDefinition;

    /**
     * build a graph as follows:
     *
     *
     *     10 -> 9 -> 1 --------> 2 -> 3 -> 7 -> 11 -> 12
     *                |            \        ^
     *                |             v       |
     *                +-> 5 -> 6 -> 4 ------+
     *
     *   * 1 and 7 are OR-Gateway-Activities
     *   * path 1-->2 is under condition that var1 == true
     *   * path 1-->5 is under condition that var2 == true
     *
     * @throws Exception
     */
    public void setUp() throws Exception {

        processDefinition = new ProcessDefinition();

        processDefinition.setProcessVariables(new ProcessVariable[]{
                ProcessVariable.forName("var1"),
                ProcessVariable.forName("var2")
        });

        Activity theLastJoinActivity = null;


        for(int i=1; i<20; i++) {
            Activity a1 = new DefaultActivity();

            if(i == 7 || i==1){
                a1 = new GatewayActivity();
            }

            a1.setTracingTag("a" + i);
            processDefinition.addChildActivity(a1);

            if(i==7){
                theLastJoinActivity = a1;
            }
        }

        {
            Transition t1 = new Transition();
            t1.setSource("a9");
            t1.setTarget("a1");

            processDefinition.addTransition(t1);
        }
        {
            Transition t1 = new Transition();
            t1.setSource("a10");
            t1.setTarget("a9");

            processDefinition.addTransition(t1);
        }

        {
            Transition t1 = new Transition();
            t1.setSource("a1");
            t1.setTarget("a2");
            t1.setCondition(new Evaluate("var1", "==", "true"));

            processDefinition.addTransition(t1);
        }
        {
            Transition t1 = new Transition();
            t1.setSource("a1");
            t1.setTarget("a5");
            t1.setCondition(new Evaluate("var2", "==", "true"));

            processDefinition.addTransition(t1);
        }
        {
            Transition t1 = new Transition();
            t1.setSource("a2");
            t1.setTarget("a3");

            processDefinition.addTransition(t1);
        }
        {
            Transition t1 = new Transition();
            t1.setSource("a2");
            t1.setTarget("a4");

            processDefinition.addTransition(t1);
        }
        {
            Transition t1 = new Transition();
            t1.setSource("a5");
            t1.setTarget("a6");

            processDefinition.addTransition(t1);
        }
        {
            Transition t1 = new Transition();
            t1.setSource("a6");
            t1.setTarget("a4");

            processDefinition.addTransition(t1);
        }
        {
            Transition t1 = new Transition();
            t1.setSource("a3");
            t1.setTarget("a7");

            processDefinition.addTransition(t1);
        }
        {
            Transition t1 = new Transition();
            t1.setSource("a4");
            t1.setTarget("a7");

            processDefinition.addTransition(t1);
        }
        {
            Transition t1 = new Transition();
            t1.setSource("a7");
            t1.setTarget("a11");

            processDefinition.addTransition(t1);
        }
        {
            Transition t1 = new Transition();
            t1.setSource("a11");
            t1.setTarget("a12");

            processDefinition.addTransition(t1);
        }

        processDefinition.afterDeserialization();

        ProcessInstance.USE_CLASS = DefaultProcessInstance.class;


    }

    public void testPathForVar1IsTrue() throws Exception {

        ProcessInstance instance = processDefinition.createInstance();
        instance.set("var1", "true");
        instance.execute();
        assertExecutionPathEquals(new String[]{
                "a10", "a9", "a1", "a2", "a3", "a4", "a7", "a11", "a12"
        }, instance);
    }


    public void testPathForVar2IsTrue() throws Exception {

        ProcessInstance instance = processDefinition.createInstance();
        instance.set("var2", "true");
        instance.execute();
        assertExecutionPathEquals(new String[]{
                "a10", "a9", "a1", "a5", "a6", "a4", "a7", "a11", "a12"
        }, instance);
    }

    public void testPathForAllVarIsTrue() throws  Exception{
        ProcessInstance instance = processDefinition.createInstance();


        instance = processDefinition.createInstance();
        instance.set("var1", "true");
        instance.set("var2", "true");
        instance.execute();

        assertExecutionPathEquals(new String[]{
                "a10", "a9", "a1", "a2", "a3", "a4", "a5", "a6", "a4", "a7", "a11", "a12"
        }, instance);

    }

    public void assertExecutionPathEquals(String[] expectedPaths, ProcessInstance instance) {
        assertExecutionPathEquals(null, expectedPaths, instance);
    }

    public void assertExecutionPathEquals(String message, String[] expectedPaths, ProcessInstance instance) {
        List expectedPathsInSet = new ArrayList();
        for(int i=0; i<expectedPaths.length; i++) {
            String path = expectedPaths[i];
            expectedPathsInSet.add(path);
        }

        if(message!=null)
            assertEquals(message, expectedPathsInSet, instance.getActivityCompletionHistory());
        else
            assertEquals(expectedPathsInSet, instance.getActivityCompletionHistory());
    }

    public void testBlockFinder(){

        Collection<Activity> members = BlockFinder.getBlockMembers(processDefinition.getActivity("a7"));

        assertNotNull(members);

        Set<String> names = new HashSet<String>();
        for(Activity activity : members){
            names.add(activity.getTracingTag());
        }

        Collection<String> expected = new HashSet<String>();
        expected.add("a1");
        expected.add("a2");
        expected.add("a3");
        expected.add("a4");
        expected.add("a5");
        expected.add("a6");
//        expected.add("a7");  //except the starting

        assertTrue(names.containsAll(expected));

    }


}
