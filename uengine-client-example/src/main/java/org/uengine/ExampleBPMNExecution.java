package org.uengine;


import org.uengine.kernel.*;
import org.uengine.kernel.bpmn.EndEvent;
import org.uengine.processpublisher.BPMNUtil;

public class ExampleBPMNExecution {

    public static void main(String[] args) throws Exception {

        AbstractProcessInstance.USE_CLASS = DefaultProcessInstance.class;

        ProcessDefinition processDefinition = BPMNUtil.importAdapt(Thread.currentThread().getContextClassLoader().getResourceAsStream("org/uengine/sample.bpmn")); //new File("/java/autoinsurance.bpmn"));
        processDefinition.afterDeserialization();

        processDefinition.setActivityFilters(new ActivityFilter[]{
                new SensitiveActivityFilter() {
                    @Override
                    public void onEvent(Activity activity, ProcessInstance instance, String eventName, Object payload) throws Exception {
                        if(Activity.ACTIVITY_FAULT.equals(eventName)){
                            /// do something when a fault occurs in activity execution
                        }

                        if(activity instanceof EndEvent && Activity.ACTIVITY_STOPPED.equals(eventName) ){
                            System.out.println(instance.getActivityCompletionHistory());
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

        instance.set("var1", "mysql");
        instance.set("var2", "x");

        instance.execute();
    }
}
