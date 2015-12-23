package org.uengine.modeling.resource.resources;

import org.metaworks.annotation.AutowiredFromClient;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.uengine.codi.mw3.model.Session;
import org.uengine.modeling.resource.DefaultResource;

/**
 * Created by jangjinyoung on 15. 7. 12..
 */
@Component
@Scope("prototype")
public class UrlappResource extends DefaultResource {
    @AutowiredFromClient
    public Session session;
}
