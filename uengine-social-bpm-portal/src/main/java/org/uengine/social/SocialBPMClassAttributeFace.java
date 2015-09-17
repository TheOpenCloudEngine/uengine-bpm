package org.uengine.social;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
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

        setProcessAdminResourceNavigator(new ProcessAdminResourceNavigator());
    }

}
