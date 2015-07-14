package org.uengine.processadmin;

import org.uengine.modeling.resource.Workbench;

/**
 * Created by jangjinyoung on 15. 7. 12..
 */
public class ProcessAdminWorkbench extends Workbench {
    public ProcessAdminWorkbench() {
        super(new ProcessAdminContainerResource());

        getResourceNavigator().getRoot().setPath("./test");
    }
}


