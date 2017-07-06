package org.uengine.processpublisher.uengine3.importer;

import org.uengine.kernel.ComplexActivity;
import org.uengine.kernel.ProcessDefinition;

import java.util.Hashtable;

/**
 * Created by uengine on 2017. 6. 16..
 */
public class SequenceActivityAdapter extends ComplexActivityAdapter {
    @Override
    public ProcessDefinition convert(ComplexActivity complexActivity, Hashtable keyedContext) throws Exception {
        return super.convert(complexActivity, keyedContext);
    }
}
