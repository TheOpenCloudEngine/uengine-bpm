package processadmin;

import org.oce.garuda.multitenancy.TenantContext;
import org.uengine.codi.mw3.resource.ResourceManager;
import org.uengine.modeling.resource.Workbench;

/**
 * Created by jangjinyoung on 15. 7. 12..
 */
public class ProcessAdminWorkbench extends Workbench {
    public ProcessAdminWorkbench() {
        super(new ProcessAdminContainerResource());

        getResourceNavigator().getRoot().setPath(ResourceManager.getResourcePath("codi", TenantContext.getThreadLocalInstance().getTenantId(), "."));
    }

}





