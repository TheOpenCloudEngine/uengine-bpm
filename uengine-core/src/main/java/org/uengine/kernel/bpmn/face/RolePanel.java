package org.uengine.kernel.bpmn.face;

import org.metaworks.MetaworksContext;
import org.metaworks.annotation.Face;
import org.metaworks.annotation.ServiceMethod;
import org.metaworks.widget.ModalWindow;
import org.uengine.kernel.Role;

import java.util.ArrayList;
import java.util.List;

import static org.metaworks.dwr.MetaworksRemoteService.wrapReturn;

public class RolePanel{
    List<Role> roleList = new ArrayList<Role>();

    @Face(faceClass = RoleListFace.class)
        public List<Role> getRoleList() {
            return roleList;
        }
        public void setRoleList(List<Role> roleList) {
            this.roleList = roleList;
        }
}
