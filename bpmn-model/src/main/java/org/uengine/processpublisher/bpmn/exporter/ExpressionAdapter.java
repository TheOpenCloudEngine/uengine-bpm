package org.uengine.processpublisher.bpmn.exporter;

import org.omg.spec.bpmn._20100524.model.TExpression;
import org.uengine.processpublisher.Adapter;

import java.util.Hashtable;

/**
 * Created by uengine on 2016. 6. 25..
 */
public class ExpressionAdapter implements Adapter<String, TExpression> {
    @Override
    public TExpression convert(String src, Hashtable keyedContext) throws Exception {
        TExpression tExpression = new TExpression();
        tExpression.getContent().add(src);

        return tExpression;
    }
}
