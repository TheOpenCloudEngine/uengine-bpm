package org.uengine.bpmn.testmain;


import org.uengine.kernel.AbstractProcessInstance;
import org.uengine.kernel.DefaultProcessInstance;
import org.uengine.kernel.ProcessDefinition;
import org.uengine.kernel.ProcessInstance;
import org.uengine.processpublisher.BPMNUtil;

import java.io.File;

public class ExampleBPMNExecution {

    public static void main(String[] args) throws Exception {

        AbstractProcessInstance.USE_CLASS = DefaultProcessInstance.class;

        ProcessDefinition processDefinition = BPMNUtil.importAdapt(new File("/Users/kimsh/Documents/acitiviti sample/parallel.xml")); //new File("/java/autoinsurance.bpmn"));

        ProcessInstance instance = processDefinition.createInstance();

        instance.execute();
    }
}
