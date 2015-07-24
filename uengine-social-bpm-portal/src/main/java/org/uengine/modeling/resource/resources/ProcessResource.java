package org.uengine.modeling.resource.resources;

import org.metaworks.annotation.AutowiredFromClient;
import org.uengine.codi.mw3.model.Session;
import org.uengine.modeling.resource.DefaultResource;

/**
 * Created by jangjinyoung on 15. 7. 12..
 */
public class ProcessResource extends DefaultResource {

    @AutowiredFromClient
    public Session session;
}
