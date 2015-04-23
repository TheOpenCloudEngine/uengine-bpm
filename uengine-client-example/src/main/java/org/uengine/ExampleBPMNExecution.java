package org.uengine;


import org.uengine.kernel.DefaultProcessInstance;
import org.uengine.kernel.ProcessDefinition;
import org.uengine.kernel.ProcessInstance;
import org.uengine.processpublisher.AdapterUtil;

import java.io.File;

public class ExampleBPMNExecution {

    public static void main(String[] args) throws Exception {

        ProcessInstance.USE_CLASS = DefaultProcessInstance.class;

        ProcessDefinition processDefinition = AdapterUtil.adapt(new File("/Users/kimsh/Documents/acitiviti sample/switch.xml")); //new File("/java/autoinsurance.bpmn"));
processDefinition.afterDeserialization();

        ProcessInstance instance = processDefinition.createInstance();

        instance.set("var1", "mysql");
        instance.set("var2", "x");

        instance.execute();
    }
}
