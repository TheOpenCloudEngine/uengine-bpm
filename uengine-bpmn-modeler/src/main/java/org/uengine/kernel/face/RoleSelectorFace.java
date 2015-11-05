package org.uengine.kernel.face;

import org.metaworks.Face;
import org.metaworks.annotation.AutowiredFromClient;
import org.metaworks.component.SelectBox;
import org.uengine.contexts.MappingTree;
import org.uengine.kernel.Role;
import org.uengine.kernel.bpmn.face.RolePanel;
import org.uengine.modeling.Canvas;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ryuha on 2015-06-11.
 */
public class RoleSelectorFace extends SelectBox implements Face <Role>{

//    @AutowiredFromClient
//    public RolePanel rolePanel;

    @AutowiredFromClient
    public Canvas canvas;


    @Override
    public void setValueToFace(Role value) {


        if(canvas != null){
            List<Role> roleList = MappingTree.parseRoles(canvas);

            if(roleList !=null) {
                ArrayList<String> options = new ArrayList<String>();

                for (Role role : roleList) {
                    options.add(role.getName());
                }

                setOptionNames(options);
                setOptionValues(options);
            }
        }

        if(value != null) {
            if(getOptionNames() == null || getOptionNames().size() == 0){
                ArrayList<String> options = new ArrayList<String>();
                options.add(value.getName());
                setOptionNames(options);
                setOptionValues(options);
            }
            setSelectedValue(value.getName());
        }
    }

    @Override
    public Role createValueFromFace() {
        String roleName = getSelectedText();

        return Role.forName(roleName);
    }
}
