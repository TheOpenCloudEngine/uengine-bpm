package org.uengine.modeling.modeler.palette;

import org.metaworks.MetaworksContext;
import org.metaworks.annotation.ServiceMethod;
import org.metaworks.dwr.MetaworksRemoteService;
import org.metaworks.widget.ModalWindow;
import org.uengine.kernel.bpmn.face.RolePanel;
import org.uengine.kernel.bpmn.view.PoolView;
import org.uengine.kernel.bpmn.view.SubProcessView;
import org.uengine.kernel.view.HumanActivityView;
import org.uengine.kernel.view.RestWebServiceActivityView;
import org.uengine.modeling.Palette;

public class RolePalette extends Palette {

    public RolePalette(){
        this.setName("Roles");

        setRolePanel(new RolePanel());
    }

    @ServiceMethod(target= ServiceMethod.TARGET_POPUP, callByContent = true, inContextMenu = true)
    public void editRolePanel(){
        ModalWindow window = new ModalWindow(getRolePanel(), "Role Definitions");
        window.setMetaworksContext(new MetaworksContext());
        window.getMetaworksContext().setHow("full-fledged");
        MetaworksRemoteService.wrapReturn(window);
    }

    RolePanel rolePanel;
        public RolePanel getRolePanel() {
            return rolePanel;
        }
        public void setRolePanel(RolePanel rolePanel) {
            this.rolePanel = rolePanel;
        }

}
