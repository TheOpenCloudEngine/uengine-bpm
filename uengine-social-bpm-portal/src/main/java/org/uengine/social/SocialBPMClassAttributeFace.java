package org.uengine.social;

import org.metaworks.website.MetaworksFile;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.uengine.codi.mw3.admin.WebEditor;
import org.uengine.codi.mw3.model.User;
import org.uengine.processadmin.ProcessAdminResourceNavigator;
import org.uengine.uml.model.face.AttributeTypeSelector;

/**
 * Created by jangjinyoung on 15. 9. 4..
 */
@Component
@Scope("request")
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

        getOptionNames().add("File");
        getOptionValues().add(MetaworksFile.class.getName());

        getOptionNames().add("Long Text");
        getOptionValues().add(WebEditor.class.getName());

       // if(getProcessAdminResourceNavigator()==null)
        setProcessAdminResourceNavigator(new ProcessAdminResourceNavigator());
    }

}
