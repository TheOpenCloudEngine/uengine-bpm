package org.uengine.modeling.resource.resources;

import org.metaworks.annotation.AutowiredFromClient;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.uengine.codi.mw3.model.Session;
import org.uengine.modeling.resource.DefaultResource;
import org.uengine.uml.model.ClassDefinition;

/**
 * Created by jangjinyoung on 15. 7. 12..
 */
@Component
@Scope("prototype")
public class ClassResource extends DefaultResource {

    @AutowiredFromClient
    public Session session;

    @Override
    public void save(Object editingObject) throws Exception {
        ClassDefinition classDefinition = (ClassDefinition) editingObject;
        classDefinition.setName(getPath());

        super.save(editingObject);
    }
}
