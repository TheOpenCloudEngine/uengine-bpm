package org.uengine.modeling.resource.editor;

import org.metaworks.dwr.MetaworksRemoteService;
import org.uengine.codi.mw3.model.IInstance;
import org.uengine.codi.mw3.model.Instance;
import org.uengine.codi.mw3.model.ProcessMap;
import org.uengine.codi.mw3.model.Session;
import org.uengine.kernel.ProcessDefinition;
import org.uengine.modeling.modeler.ProcessModeler;
import org.uengine.modeling.modeler.StandaloneProcessModeler;
import org.uengine.modeling.resource.IEditor;
import org.uengine.modeling.resource.IResource;
import org.uengine.modeling.resource.Simulatable;
import org.uengine.processmanager.ProcessManagerRemote;

/**
 * Created by uengine on 2015. 7. 14..
 */
public class ProcessEditor extends ProcessModeler implements IEditor<ProcessDefinition>, Simulatable {


    public ProcessEditor() throws Exception {
        super();
    }

    @Override
    public void setEditingObject(ProcessDefinition object) {
        try {
            setModel(object);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public ProcessDefinition createEditedObject() {
        try {
            return (ProcessDefinition) createModel();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public ProcessDefinition newObject(IResource resource) {
        ProcessDefinition processDefinition = new ProcessDefinition();
        processDefinition.setName(resource.getName());

        return processDefinition;
    }

//	public ProcessDefinition getEditingObject() {
//		// TODO - implement ProcessEditor.getEditingObject
//		throw new UnsupportedOperationException();
//	}


    @Override
    public Object simulator(IResource resource) {

        Session session = MetaworksRemoteService.getComponent(Session.class);


        String userId = session.getUser().getUserId();

        IInstance recentSimulationInstance;

        try {
            recentSimulationInstance = Instance.loadRecentSimulationInstance(userId);
            recentSimulationInstance.next();
        } catch (Exception e) {
            recentSimulationInstance = null;
        }

        if (recentSimulationInstance != null) {


            try {
                return (Instance.createInstanceViewDetail(recentSimulationInstance.getInstId()));
            } catch (Exception e) {
                throw new RuntimeException(e);
            }


        } else {


            ProcessManagerRemote processManager = MetaworksRemoteService.getComponent(ProcessManagerRemote.class);

            ProcessMap processMap = new ProcessMap();
            processMap.setName("[Test] " + resource.getName());
            processMap.setDefId(resource.getPath().substring(resource.getPath().indexOf("/") + 1));
            MetaworksRemoteService.autowire(processMap);

            try {
                return processMap.simulate();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }



}
