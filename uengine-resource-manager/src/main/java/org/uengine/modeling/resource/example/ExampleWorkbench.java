package org.uengine.modeling.resource.example;

import org.uengine.modeling.resource.Workbench;

/**
 * Created by jangjinyoung on 15. 7. 12..
 */
public class ExampleWorkbench extends Workbench {
    public ExampleWorkbench() {
        super(new ExampleContainerResource());

        getResourceNavigator().getRoot().setPath("./test");
    }
}


