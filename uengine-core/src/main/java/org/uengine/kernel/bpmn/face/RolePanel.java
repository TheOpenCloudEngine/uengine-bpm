package org.uengine.kernel.bpmn.face;

import org.metaworks.annotation.Face;
import org.uengine.kernel.ProcessVariable;
import org.uengine.kernel.Role;

import java.util.ArrayList;
import java.util.List;

public class RolePanel {
    List<Role> roleList = new ArrayList<Role>();

    @Face(faceClass = RoleListFace.class)
        public List<Role> getRoleList() {
            return roleList;
        }
        public void setRoleList(List<Role> roleList) {
            this.roleList = roleList;
        }

}
