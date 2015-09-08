package org.uengine.modeling.resource.resources;

import org.metaworks.annotation.AutowiredFromClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.uengine.codi.CodiProcessDefinitionFactory;
import org.uengine.codi.mw3.model.Session;
import org.uengine.kernel.ProcessDefinitionFactory;
import org.uengine.modeling.resource.DefaultResource;

/**
 * Created by jangjinyoung on 15. 7. 12..
 */
@Component
@Scope("prototype")
public class ProcessResource extends DefaultResource {

    @AutowiredFromClient
    public Session session;

    @Override
    public void save(Object editingObject) throws Exception {
        super.save(editingObject);

        definitionFactory.removeFromCache(getPath().substring("codi/".length()));
    }

    @Autowired
    public ProcessDefinitionFactory definitionFactory;
}
