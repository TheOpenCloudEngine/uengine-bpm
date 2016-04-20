package org.uengine.modeling.resource.resources;

import org.metaworks.EventContext;
import org.metaworks.ServiceMethodContext;
import org.metaworks.annotation.*;
import org.metaworks.dwr.MetaworksRemoteService;
import org.metaworks.widget.ToBlank;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.uengine.codi.CodiProcessDefinitionFactory;
import org.uengine.codi.mw3.model.Session;
import org.uengine.kernel.ProcessDefinitionFactory;
import org.uengine.modeling.resource.DefaultResource;
import org.uengine.modeling.resource.ResourceControlDelegate;

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

        definitionFactory.removeFromCache(getPath().substring(CodiProcessDefinitionFactory.codiProcessDefinitionFolder.length() + 1));
    }

    @ServiceMethod(target=ServiceMethodContext.TARGET_APPEND, inContextMenu = true, callByContent = true)
    public void openInNewWindow(){
        MetaworksRemoteService.wrapReturn(new ToBlank("resource-editor.html?resourcePath=" + getPath() + "&accessToken=" + session.getEmployee().getEmail()));
    }


    @Autowired
    public ProcessDefinitionFactory definitionFactory;

//    @Available(condition="metaworksContext == null || metaworksContext.when != 'addProcess' ")
//    @Order(6)
//    @Face(displayName="open")
//    @ServiceMethod(callByContent=true, except="children", eventBinding= EventContext.EVENT_DBLCLICK, inContextMenu=true, target= ServiceMethodContext.TARGET_POPUP)
//    public void open(@AutowiredFromClient ResourceControlDelegate resourceControlDelegate) throws Exception {
//        super.open(resourceControlDelegate);
//    }
}
