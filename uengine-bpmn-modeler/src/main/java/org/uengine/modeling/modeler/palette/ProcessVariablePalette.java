package org.uengine.modeling.modeler.palette;

import org.uengine.kernel.bpmn.face.ProcessVariablePanel;
import org.uengine.modeling.Palette;
import org.uengine.modeling.PaletteWindow;

public class ProcessVariablePalette extends PaletteWindow {

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
