package org.bahamas.test;

import org.uengine.kernel.DefaultActivity;
import org.uengine.kernel.ProcessInstance;

public class Activity1 extends DefaultActivity {

    @Override
    protected void executeActivity(ProcessInstance instance) throws Exception {


        String var1 = (String) instance.get("var1");

        /////// do something ///////

        fireComplete(instance);
    }
}