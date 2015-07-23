package processadmin;

import org.uengine.modeling.resource.ResourceNavigator;

/**
 * Created by jangjinyoung on 15. 7. 18..
 */
public class ProcessAdminResourceNavigator extends ResourceNavigator {

    public ProcessAdminResourceNavigator(){
        super();

        setRoot(new ProcessAdminContainerResource());

        getRoot().setPath("codi");
    }

}
