package org.uengine.processpublisher.uengine3.importer;

import org.uengine.kernel.Activity;
import org.uengine.kernel.ProcessDefinition;
import org.uengine.kernel.SwitchActivity;
import org.uengine.kernel.bpmn.FlowActivity;
import org.uengine.kernel.bpmn.SequenceFlow;
import org.uengine.processpublisher.Adapter;
import org.uengine.processpublisher.AdapterUtil;

import java.util.Hashtable;

/**
 * Created by uengine on 2017. 6. 16..
 */
public class SwitchActivityAdapter extends ComplexActivityAdapter {
    @Override
    public FlowActivity convert(SwitchActivity src, Hashtable keyedContext) throws Exception {

        //converting conditions

        return super.convert(src, keyedContext);

    }
}
