package org.uengine.modeling.resource.editor;

import org.uengine.kernel.ProcessDefinition;
import org.uengine.modeling.modeler.ProcessModeler;
import org.uengine.modeling.modeler.StandaloneProcessModeler;
import org.uengine.modeling.resource.IEditor;
import org.uengine.modeling.resource.IResource;

/**
 * Created by uengine on 2015. 7. 14..
 */
public class ProcessEditor extends ProcessModeler implements IEditor<ProcessDefinition> {


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
}
