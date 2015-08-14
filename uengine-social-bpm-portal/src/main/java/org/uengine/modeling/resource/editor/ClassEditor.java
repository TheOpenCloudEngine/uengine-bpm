package org.uengine.modeling.resource.editor;

import org.uengine.modeling.resource.IEditor;
import org.uengine.modeling.resource.IResource;
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
    public ClassDefinition createEditedObject() {
        return classDefinition;
    }

    @Override
    public ClassDefinition newObject(IResource resource) {
        ClassDefinition classDefinition = new ClassDefinition();
        return classDefinition;
    }
}
