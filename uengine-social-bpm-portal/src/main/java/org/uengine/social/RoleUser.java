package org.uengine.social;

import org.metaworks.MetaworksContext;
import org.uengine.codi.mw3.model.User;
import org.uengine.kernel.NeedArrangementToSerialize;
import org.uengine.kernel.RoleMapping;

/**
 * Created by jangjinyoung on 15. 9. 27..
 */
public class RoleUser extends RoleMapping implements NeedArrangementToSerialize{

    User user;
        public User getUser() {
            return user;
        }
        public void setUser(User user) {
            this.user = user;
        }


    public RoleUser(){
        super();

        setUser(new User());
        user.setMetaworksContext(new MetaworksContext());
        user.getMetaworksContext().setWhere(User.MW3_WHERE_ROLEUSER_PICKER);
    }


    @Override
    public void beforeSerialization() {

    }

    @Override
    public void afterDeserialization() {
        if (getUser() != null) {
            setEndpoint(getUser().getUserId());
        }
    }

    @Override
    public String getEndpoint() {
        if(endpoint==null && user!=null && user.getUserId()!=null){
            endpoint = user.getUserId();
        }

        return super.getEndpoint();
    }
}
