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
import org.uengine.kernel.*;
import org.uengine.modeling.HasThumbnail;
import org.uengine.modeling.Modeler;
import org.uengine.modeling.resource.*;
import org.uengine.modeling.resource.editor.ProcessEditor;

import java.util.ArrayList;
import java.util.List;

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
        ProcessDefinition definition = (ProcessDefinition) editingObject;
        definition.setName(getDisplayName());

        separateGlobalDefinition(definition);


        super.save(editingObject);

        definitionFactory.removeFromCache(getPath());
    }

    @Override
    public Object load() throws Exception {
        Object processDef = super.load();

        mergeGlobalDefinition((ProcessDefinition) processDef);

        return processDef;
    }

    private void mergeGlobalDefinition(ProcessDefinition definition) throws Exception {

        if(definition.isGlobal())
            return; // void recursive

        String globalDefinitionPath = getGlobalDefinitionPath();
        if(globalDefinitionPath==null)
            return; // ignore when no global def path;

        ProcessResource globalProcessResource = new ProcessResource();
        globalProcessResource.setPath(globalDefinitionPath);

        MetaworksRemoteService.autowire(globalProcessResource);

        if(resourceManager.getStorage().exists(globalProcessResource)){
            ProcessDefinition globalDef = (ProcessDefinition) globalProcessResource.load();

            List<ProcessVariable> variables = new ArrayList<ProcessVariable>();
            for(ProcessVariable processVariable : globalDef.getProcessVariables()){
                variables.add(processVariable);
            }

            for(ProcessVariable processVariable : definition.getProcessVariables()){
                if(!processVariable.isGlobal())
                    variables.add(processVariable);
            }

            ProcessVariable[] arrVariables = new ProcessVariable[variables.size()];
            arrVariables = variables.toArray(arrVariables);

            definition.setProcessVariables(arrVariables);
        }
    }

    private void separateGlobalDefinition(ProcessDefinition definition) {

        if(definition.isGlobal()) return; //void recursive call

        String globalDefinitionPath = getGlobalDefinitionPath();
        if(globalDefinitionPath==null)
            return; //if there's no global path, ignore it.

        ProcessDefinition globalDefinition = new ProcessDefinition();
        globalDefinition.setGlobal(true);

        //extracts global process variables from the definition
        List<ProcessVariable> globalProcessVariables = new ArrayList<ProcessVariable>();
        List<ProcessVariable> localProcessVariables = new ArrayList<ProcessVariable>();
        if(definition.getProcessVariables()!=null){

            for(ProcessVariable processVariable : definition.getProcessVariables()){
                if(processVariable!=null && processVariable.isGlobal()) {
                    if(!globalProcessVariables.contains(processVariable))
                        globalProcessVariables.add(processVariable);
                }else{
                    localProcessVariables.add(processVariable);
                }
            }

        }

        if(globalProcessVariables!=null && globalProcessVariables.size() > 0){
//            ProcessVariable[] arrayLocalVariables = new ProcessVariable[locallProcessVariables.size()];
//            definition.setProcessVariables(locallProcessVariables.toArray(arrayLocalVariables));

            ProcessVariable[] arrayGlobalVariables = new ProcessVariable[globalProcessVariables.size()];
            globalDefinition.setProcessVariables(globalProcessVariables.toArray(arrayGlobalVariables));

            ProcessResource globalProcessResource = new ProcessResource();

            globalProcessResource.setPath(globalDefinitionPath);
            try {
                MetaworksRemoteService.autowire(globalProcessResource);
                globalProcessResource.save(globalDefinition);
            } catch (Exception e) {
                throw new RuntimeException("Failed to save global process definition [" + globalProcessResource.getPath() + "]" , e);
            }


        }

    }

    private String getGlobalDefinitionPath(){

        VersionManager versionManager = MetaworksRemoteService.getComponent(VersionManager.class);

        versionManager.setAppName("codi");
        String logicalPath = versionManager.getLogicalPath(getPath());

        if(versionManager.getModuleName()==null) return null;

        logicalPath = versionManager.getModuleName() + "/" + logicalPath;

        String physicalPart = getPath().substring(0, getPath().length() - logicalPath.length());

        return physicalPart + versionManager.getModuleName() + "/" + GlobalContext.GLOBAL_PROCESS_PATH;
    }

    @ServiceMethod(target=ServiceMethodContext.TARGET_APPEND, inContextMenu = true, callByContent = true)
    public void openInNewWindow(){
        MetaworksRemoteService.wrapReturn(new ToBlank("resource-editor.jsp?resourcePath=" + getPath() + "&accessToken=" + session.getEmployee().getEmail()));
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
