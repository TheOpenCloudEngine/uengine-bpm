package org.uengine.modeling.resource.editor;

import org.metaworks.annotation.Face;
import org.uengine.application.URLApplicationDefinition;
import org.uengine.modeling.resource.IEditor;
import org.uengine.modeling.resource.IResource;
import org.uengine.uml.model.ClassDefinition;

/**
 * Created by uengine on 2015. 7. 14..
 */
@Face(ejsPath="genericfaces/CleanObjectFace.ejs")
public class UrlappEditor implements IEditor<URLApplicationDefinition>{


    URLApplicationDefinition urlApplicationDefinition;
        public URLApplicationDefinition getUrlApplicationDefinition() {
            return urlApplicationDefinition;
        }
        public void setUrlApplicationDefinition(URLApplicationDefinition urlApplicationDefinition) {
            this.urlApplicationDefinition = urlApplicationDefinition;
        }


    @Override
    public void setEditingObject(URLApplicationDefinition urlApplicationDefinition) {
        setUrlApplicationDefinition(urlApplicationDefinition);
    }

    @Override
    public URLApplicationDefinition createEditedObject() {
        return getUrlApplicationDefinition();
    }

    @Override
    public URLApplicationDefinition newObject(IResource resource) {
        URLApplicationDefinition urlApplicationDefinition = new URLApplicationDefinition();
        return urlApplicationDefinition;
    }
}
