package processadmin;

import org.oce.garuda.multitenancy.TenantContext;
import org.uengine.modeling.resource.Workbench;

import java.io.File;

/**
 * Created by jangjinyoung on 15. 7. 12..
 */
public class ProcessAdminWorkbench extends Workbench {
    public ProcessAdminWorkbench() {
        super(new ProcessAdminResourceNavigator());
    }

}





