package org.uengine.kernel.test;

import org.uengine.kernel.*;
import org.uengine.kernel.bpmn.BlockFinder;
import org.uengine.kernel.bpmn.SequenceFlow;
import org.uengine.kernel.bpmn.Gateway;

import java.util.*;

public class InclusiveGatewayTest extends UEngineTest{

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

            //a1.setQueuingEnabled(true);

            if(i == 7 || i==1){
                a1 = new Gateway();
            }

//            if(i == 11){
//                a1 = new DefaultActivity(){
//                    @Override
//                    protected void executeActivity(ProcessInstance instance) throws Exception {
//
//                        throw new Exception("xxxx");
//
//                        //super.executeActivity(instance);
//                    }
//                };
//
//            }

            a1.setTracingTag("a" + i);
            processDefinition.addChildActivity(a1);

            if(i==7){
                theLastJoinActivity = a1;
            }
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
            t1.setCondition(new Evaluate("var1", "==", "true"));

            processDefinition.addSequenceFlow(t1);
        }
        {
            SequenceFlow t1 = new SequenceFlow();
            t1.setSourceRef("a1");
            t1.setTargetRef("a5");
            t1.setCondition(new Evaluate("var2", "==", "true"));

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
            t1.setSourceRef("a2");
            t1.setTargetRef("a4");

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
            t1.setSourceRef("a4");
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

        AbstractProcessInstance.USE_CLASS = DefaultProcessInstance.class;


    }

    public void testPathForVar1IsTrue() throws Exception {

        processDefinition.setActivityFilters(new ActivityFilter[]{
                new SensitiveActivityFilter() {
                    @Override
                    public void onEvent(Activity activity, ProcessInstance instance, String eventName, Object payload) throws Exception {
                        if(Activity.ACTIVITY_FAULT.equals(eventName)){
                            /// do something when a fault occurs in activity execution
                        }
                    }

                    @Override
                    public void beforeExecute(Activity activity, ProcessInstance instance) throws Exception {
                    }

                    @Override
                    public void afterExecute(Activity activity, ProcessInstance instance) throws Exception {
                    }

                    @Override
                    public void afterComplete(Activity activity, ProcessInstance instance) throws Exception {
                        if(activity instanceof ProcessDefinition){
                            assertExecutionPathEquals(new String[]{
                                    "a10", "a9", "a1", "a2", "a3", "a4", "a7", "a11", "a12"
                            }, instance);

                        }
                    }

                    @Override
                    public void onPropertyChange(Activity activity, ProcessInstance instance, String propertyName, Object changedValue) throws Exception {


                    }

                    @Override
                    public void onDeploy(ProcessDefinition definition) throws Exception {

                    }
                }
        });


        ProcessInstance instance = processDefinition.createInstance();
        instance.set("var1", "true");
        instance.execute();



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
