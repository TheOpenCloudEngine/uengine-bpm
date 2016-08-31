package org.uengine.modeling.resource.resources;

import org.metaworks.EventContext;
import org.metaworks.ServiceMethodContext;
import org.metaworks.annotation.*;
import org.metaworks.dao.TransactionContext;
import org.metaworks.dwr.MetaworksRemoteService;
import org.metaworks.widget.Label;
import org.metaworks.widget.ModalWindow;
import org.metaworks.widget.ToBlank;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.uengine.codi.CodiProcessDefinitionFactory;
import org.uengine.codi.mw3.model.Session;
import org.uengine.kernel.ProcessDefinition;
import org.uengine.kernel.ProcessDefinitionFactory;
import org.uengine.modeling.HasThumbnail;
import org.uengine.modeling.Modeler;
import org.uengine.modeling.resource.*;
import org.uengine.modeling.resource.editor.ProcessEditor;

import static org.metaworks.dwr.MetaworksRemoteService.autowire;
import static org.metaworks.dwr.MetaworksRemoteService.wrapReturn;

/**
 * Created by jangjinyoung on 15. 7. 12..
 */
@Component
@Scope("prototype")
public class ProcessResource extends DefaultResource {

    @AutowiredFromClient
    public Session session;

    IEditor editor;
        public IEditor getEditor() {
            return editor;
        }
        public void setEditor(IEditor editor) {
        this.editor = editor;
    }

    @Override
    public void save(Object editingObject) throws Exception {
        ProcessDefinition processDefinition = (ProcessDefinition) editingObject;
        processDefinition.setName(getDisplayName());

        super.save(editingObject);

        definitionFactory.removeFromCache(getPath().substring(CodiProcessDefinitionFactory.codiProcessDefinitionFolder.length() + 1));
    }

    @ServiceMethod(target=ServiceMethodContext.TARGET_APPEND, inContextMenu = true, callByContent = true)
    public void openInNewWindow(){
        MetaworksRemoteService.wrapReturn(new ToBlank("resource-editor.html?resourcePath=" + getPath() + "&accessToken=" + session.getEmployee().getEmail()));
    }

    @ServiceMethod(callByContent = true, target = ServiceMethod.TARGET_POPUP, inContextMenu = true)
    public void simulate() throws Exception {

        try {

            ModalWindow runner = new ModalWindow();
            runner.setWidth(1000);
            runner.setTitle("Simulation");
            runner.setPanel(new ProcessEditor().simulator(this));
            MetaworksRemoteService.wrapReturn(runner);

        } catch (Exception e) {
            e.printStackTrace();
        }




        /*
        TransactionContext.getThreadLocalInstance().setSharedContext("isDevelopmentTime", true);
        //save();
        if(getEditor() instanceof Simulatable) {
            ModalWindow runner = new ModalWindow();

            runner.setWidth(1000);
            runner.setTitle("Simulation");

            IResource resource = new DefaultResource();
            resource.setPath(getPath());

            runner.setPanel(((Simulatable) getEditor()).simulator(resource));

            MetaworksRemoteService.wrapReturn(runner);
        }else {
            MetaworksRemoteService.wrapReturn(new ModalWindow(new Label("This resource is not supporting simulation")));
        }
        */
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
