package org.uengine.social;

import org.metaworks.Face;
import org.metaworks.annotation.AutowiredFromClient;
import org.metaworks.component.MultiSelectBox;
import org.metaworks.dwr.MetaworksRemoteService;
import org.uengine.codi.mw3.model.ContactPanel;
import org.uengine.codi.mw3.model.IContact;
import org.uengine.codi.mw3.model.Session;
import org.uengine.codi.mw3.model.User;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jjy on 2016. 1. 11..
 */
public class RoleUserFace extends MultiSelectBox implements Face<RoleUser> {

    public RoleUserFace() {
        super();
    }

    @AutowiredFromClient
    public Session session;

    @Override
    public void setValueToFace(RoleUser value) {
        ContactPanel contactPanel = new ContactPanel();
        MetaworksRemoteService.autowire(contactPanel);

        try {
            contactPanel.load();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }


        //add myself first
        getOptionNames().add(session.getUser().getName());
        getOptionValues().add(session.getUser().getUserId());

        IContact contact = contactPanel.getList();

        try {
            while(contact.next()){
                getOptionNames().add(contact.getFriend().getName());
                getOptionValues().add(contact.getFriend().getUserId());
            };
        } catch (Exception e) {
            e.printStackTrace();
        }

        String selectedUserNames = "";
        String sep = "";
        if(value!=null && value.size()>0){
            for(User user : value.getUsers()){

                selectedUserNames += sep + user.getUserId();
                sep = ", ";
            }
        }
    }

    @Override
    public RoleUser createValueFromFace() {

        String userIds = getSelected();

        if(userIds==null || userIds.trim().length() == 0) return null;

        String[] userIdSplitted = userIds.split(", ");

        if(userIdSplitted.length == 0) return null;

        List<User> users = new ArrayList<User>();
        for(String userId : userIdSplitted){
            User user = new User();
            user.setUserId(userId);
            users.add(user);
        }

        RoleUser roleUser = new RoleUser();
        roleUser.setUsers(users);

        return roleUser;
    }
}
