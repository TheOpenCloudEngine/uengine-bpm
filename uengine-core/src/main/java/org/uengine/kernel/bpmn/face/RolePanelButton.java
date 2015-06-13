package org.uengine.kernel.bpmn.face;

import org.metaworks.annotation.Hidden;
import org.metaworks.annotation.ServiceMethod;
import org.metaworks.widget.ModalWindow;

public class RolePanelButton {
    RolePanel rolePanel;
    @Hidden
        public RolePanel getRolePanel() {
            return rolePanel;
        }
        public void setRolePanel(RolePanel rolePanel) {
            this.rolePanel = rolePanel;
        }

    @ServiceMethod(callByContent = true)
    public ModalWindow open(){
        return new ModalWindow(getRolePanel());
    }
}
