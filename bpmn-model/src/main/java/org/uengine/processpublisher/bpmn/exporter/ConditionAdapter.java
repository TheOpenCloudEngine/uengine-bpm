package org.uengine.processpublisher.bpmn.exporter;

import org.omg.spec.bpmn._20100524.model.TExpression;
import org.uengine.kernel.Condition;
import org.uengine.processpublisher.Adapter;

import java.util.Hashtable;

/**
 * Created by uengine on 2015. 8. 14..
 */
public class ConditionAdapter implements Adapter<Condition, TExpression> {
    @Override
    public TExpression convert(Condition src, Hashtable keyedContext) throws Exception {
        TExpression tExpression = ObjectFactoryUtil.createBPMNObject(TExpression.class);
        tExpression.setId(src.getName());
        tExpression.getContent().add(src);

        return tExpression;
    }
}
