package org.uengine.social;

import org.metaworks.annotation.Face;
import org.uengine.codi.mw3.model.Application;
import org.uengine.modeling.resource.example.ExampleWorkbench;

@Face(ejsPath="dwr/metaworks/genericfaces/CleanObjectFace.ejs")
public class ProcessAdminApplication extends Application{

    public ProcessAdminApplication() throws Exception {
        this.exampleWorkbench = new ExampleWorkbench();
    }

//    StandaloneProcessModeler standaloneProcessModeler;
//        public StandaloneProcessModeler getStandaloneProcessModeler() {
//            return standaloneProcessModeler;
//        }
//        public void setStandaloneProcessModeler(StandaloneProcessModeler standaloneProcessModeler) {
//            this.standaloneProcessModeler = standaloneProcessModeler;
//        }


    ExampleWorkbench exampleWorkbench;
        public ExampleWorkbench getExampleWorkbench() {
            return exampleWorkbench;
        }

        public void setExampleWorkbench(ExampleWorkbench exampleWorkbench) {
            this.exampleWorkbench = exampleWorkbench;
        }


}
