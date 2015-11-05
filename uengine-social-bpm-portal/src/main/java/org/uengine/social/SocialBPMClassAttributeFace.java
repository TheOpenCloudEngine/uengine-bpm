package org.uengine.social;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.uengine.codi.mw3.model.User;
import org.uengine.processadmin.ProcessAdminResourceNavigator;
import org.uengine.uml.model.face.AttributeTypeSelector;

/**
 * Created by jangjinyoung on 15. 9. 4..
 */
@Component
@Scope("prototype")
public class SocialBPMClassAttributeFace extends AttributeTypeSelector {

    ProcessAdminResourceNavigator processAdminResourceNavigator;
        public ProcessAdminResourceNavigator getProcessAdminResourceNavigator() {
            return processAdminResourceNavigator;
        }
        public void setProcessAdminResourceNavigator(ProcessAdminResourceNavigator processAdminResourceNavigator) {
            this.processAdminResourceNavigator = processAdminResourceNavigator;
        }

    public SocialBPMClassAttributeFace(){
        super();

        getOptionNames().add("User");
        getOptionValues().add(RoleUser.class.getName());

        setProcessAdminResourceNavigator(new ProcessAdminResourceNavigator());
    }

}
