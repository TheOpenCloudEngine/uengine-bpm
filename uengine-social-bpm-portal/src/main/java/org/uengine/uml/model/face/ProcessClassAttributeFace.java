package org.uengine.uml.model.face;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.uengine.processadmin.ProcessAdminResourceNavigator;

/**
 * Created by jangjinyoung on 15. 9. 4..
 */
@Component
@Scope("prototype")
public class ProcessClassAttributeFace extends AttributeTypeSelector{

    ProcessAdminResourceNavigator processAdminResourceNavigator;
        public ProcessAdminResourceNavigator getProcessAdminResourceNavigator() {
            return processAdminResourceNavigator;
        }
        public void setProcessAdminResourceNavigator(ProcessAdminResourceNavigator processAdminResourceNavigator) {
            this.processAdminResourceNavigator = processAdminResourceNavigator;
        }

    public ProcessClassAttributeFace(){
        super();

        setProcessAdminResourceNavigator(new ProcessAdminResourceNavigator());
    }

}
