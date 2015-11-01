package org.uengine.modeling.modeler.palette;

import org.uengine.kernel.bpmn.face.ProcessVariablePanel;
import org.uengine.modeling.Palette;

public class ProcessVariablePalette extends Palette {

    public ProcessVariablePalette(){

        this.setName("Process Data");

        setProcessVariablePanel(new ProcessVariablePanel());
    }

    ProcessVariablePanel processVariablePanel;
        public ProcessVariablePanel getProcessVariablePanel() {
            return processVariablePanel;
        }

        public void setProcessVariablePanel(ProcessVariablePanel processVariablePanel) {
            this.processVariablePanel = processVariablePanel;
        }



}
