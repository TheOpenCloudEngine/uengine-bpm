package org.uengine.resource.editor;

import org.uengine.kernel.ProcessDefinition;
import org.uengine.modeling.modeler.ProcessModeler;
import org.uengine.modeling.modeler.StandaloneProcessModeler;
import org.uengine.modeling.resource.IEditor;
import org.uengine.modeling.resource.IResource;

/**
 * Created by uengine on 2015. 7. 14..
 */
public class ProcessEditor extends StandaloneProcessModeler implements IEditor<ProcessDefinition> {


    public ProcessEditor() throws Exception {
        super();
    }

    @Override
    public void setEditingObject(ProcessDefinition object) {
        try {
            getProcessModeler().setModel(object);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public ProcessDefinition getEditingObject() {
        try {
            return (ProcessDefinition) getProcessModeler().getModel();
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
