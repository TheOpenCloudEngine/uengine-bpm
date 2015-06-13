package org.uengine.modeling.modeler.palette;

import org.uengine.kernel.bpmn.face.ProcessVariablePanel;
import org.uengine.kernel.bpmn.face.RolePanel;
import org.uengine.modeling.Palette;

/**
 * Created by Ryuha on 2015-06-12.
 */
public class AttributePalette extends Palette{
    RolePanel rolePanel = new RolePanel();
        public RolePanel getRolePanel() {
            return rolePanel;
        }

        public void setRolePanel(RolePanel rolePanel) {
            this.rolePanel = rolePanel;
        }

    ProcessVariablePanel processVariablePanel = new ProcessVariablePanel();
        public ProcessVariablePanel getProcessVariablePanel() {
            return processVariablePanel;
        }

        public void setProcessVariablePanel(ProcessVariablePanel processVariablePanel) {
            this.processVariablePanel = processVariablePanel;
        }

    public AttributePalette() {
        this.setName("Attribute Setting");
    }

}
