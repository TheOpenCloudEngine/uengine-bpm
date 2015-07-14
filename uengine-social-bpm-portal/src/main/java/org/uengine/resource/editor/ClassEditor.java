package org.uengine.resource.editor;

import org.uengine.modeling.resource.IEditor;
import org.uengine.uml.model.ClassDefinition;

/**
 * Created by uengine on 2015. 7. 14..
 */
public class ClassEditor implements IEditor<ClassDefinition>{

    ClassDefinition classDefinition;

    @Override
    public void setEditingObject(ClassDefinition classDefinition) {
        this.classDefinition = classDefinition;
    }

    @Override
    public ClassDefinition getEditingObject() {
        return classDefinition;
    }
}
