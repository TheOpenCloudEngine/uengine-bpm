package org.uengine.application;

import org.uengine.uml.model.ClassDefinition;

/**
 * Created by jangjinyoung on 15. 9. 10..
 */
public abstract class ProcessApplicationDefinition extends ClassDefinition{

    @Override
    protected abstract ProcessApplication newObjectInstance();
}
