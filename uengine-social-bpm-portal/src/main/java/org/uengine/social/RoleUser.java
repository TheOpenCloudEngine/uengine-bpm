package org.uengine.social;

import org.metaworks.MetaworksContext;
import org.metaworks.annotation.Face;
import org.metaworks.component.MultiSelectBox;
import org.uengine.codi.mw3.model.Employee;
import org.uengine.codi.mw3.model.IEmployee;
import org.uengine.codi.mw3.model.IUser;
import org.uengine.codi.mw3.model.User;
import org.uengine.kernel.NeedArrangementToSerialize;
import org.uengine.kernel.ProcessInstance;
import org.uengine.kernel.RoleMapping;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jangjinyoung on 15. 9. 27..
 */
@Face(faceClass = RoleUserFace.class)
public class RoleUser extends RoleMapping implements NeedArrangementToSerialize{

    List<User> users;
        public List<User> getUsers() {
            return users;
        }
        public void setUsers(List<User> users) {
            this.users = users;
        }



    public RoleUser(){
        super();


        setUsers(new ArrayList<User>());
//        user.setMetaworksContext(new MetaworksContext());
//        user.getMetaworksContext().setWhere(MetaworksContext.WHEN_NEW);
//        user.getMetaworksContext().setHow(User.HOW_PICKER);
    }


    @Override
    public void beforeSerialization() {

    }

    @Override
    public void afterDeserialization() {
        if (getUsers() != null) {
            for(User user : getUsers()){
                setEndpoint(user.getUserId());
                moveToAdd();
            }
        }
        beforeFirst();
    }

    @Override
    public String getEndpoint() {
        if(endpoint==null && users!=null){
            afterDeserialization();
        }

        return super.getEndpoint();
    }

    @Override
    public void fill(ProcessInstance instance) throws Exception {
//        User user = new User();
//
//        user.setUserId(getEndpoint());
//
//        IUser databaseOne = user.databaseMe();

        Employee employee = new Employee();
        employee.setEmpCode(getEndpoint());
        IEmployee databaseOne = employee.databaseMe();

        setResourceName(databaseOne.getEmpName());
    }
}
