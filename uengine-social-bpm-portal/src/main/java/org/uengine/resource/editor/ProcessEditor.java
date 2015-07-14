package org.uengine.resource.editor;

import org.uengine.kernel.ProcessDefinition;
import org.uengine.modeling.modeler.ProcessModeler;
import org.uengine.modeling.resource.IEditor;

/**
 * Created by uengine on 2015. 7. 14..
 */
public class ProcessEditor extends ProcessModeler implements IEditor<ProcessDefinition> {
    @Override
    public void setEditingObject(ProcessDefinition object) {
        try {
            setModel(object);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public ProcessDefinition getEditingObject() {
        try {
            return (ProcessDefinition) getModel();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
