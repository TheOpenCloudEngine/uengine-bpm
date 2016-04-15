package org.uengine.modeling.resource.editor;

import org.metaworks.annotation.Face;
import org.uengine.contexts.JavaClassDefinition;
import org.uengine.modeling.resource.IEditor;
import org.uengine.modeling.resource.IResource;

/**
 * Created by jjy on 2016. 4. 7..
 */
@Face(ejsPath="genericfaces/CleanObjectFace.ejs")
public class JavaclassEditor implements IEditor<JavaClassDefinition> {

    JavaClassDefinition javaClassDefinition;
        public JavaClassDefinition getJavaClassDefinition() {
            return javaClassDefinition;
        }
        public void setJavaClassDefinition(JavaClassDefinition javaClassDefinition) {
            this.javaClassDefinition = javaClassDefinition;
        }

    @Override
    public void setEditingObject(JavaClassDefinition object) {
        setJavaClassDefinition(object);
    }

    @Override
    public JavaClassDefinition createEditedObject() {
        return getJavaClassDefinition();
    }

    @Override
    public JavaClassDefinition newObject(IResource resource) {
        return new JavaClassDefinition();
    }
}
