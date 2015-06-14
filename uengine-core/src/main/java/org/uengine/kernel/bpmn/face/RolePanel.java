package org.uengine.kernel.bpmn.face;

import org.metaworks.Refresh;
import org.metaworks.Remover;
import org.metaworks.ServiceMethodContext;
import org.metaworks.annotation.Face;
<<<<<<< HEAD
import org.metaworks.annotation.ServiceMethod;
import org.metaworks.dwr.MetaworksRemoteService;
import org.metaworks.widget.ModalWindow;
import org.uengine.kernel.ProcessVariable;
=======
>>>>>>> 120539f952a95cf03bf6bf663caab42543d65e60
import org.uengine.kernel.Role;

import javax.xml.ws.Service;
import java.util.ArrayList;
import java.util.List;

public class RolePanel{
    List<Role> roleList = new ArrayList<Role>();

    @Face(faceClass = RoleListFace.class)
        public List<Role> getRoleList() {
            return roleList;
        }
        public void setRoleList(List<Role> roleList) {
            this.roleList = roleList;
        }

    @ServiceMethod(callByContent = true, target=ServiceMethodContext.TARGET_POPUP)
    public void ok(){
        RolePanelButton rolePanelButton = new RolePanelButton();
        rolePanelButton.setRolePanel(this);

        MetaworksRemoteService.wrapReturn(new Remover(new ModalWindow()), new Refresh(this));
    }

}
