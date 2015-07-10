package org.uengine.social;

import org.metaworks.annotation.Face;
import org.uengine.codi.mw3.model.Application;
import org.uengine.modeling.modeler.StandaloneProcessModeler;

@Face(ejsPath="dwr/metaworks/genericfaces/CleanObjectFace.ejs")
public class ProcessAdminApplication extends Application{

    public ProcessAdminApplication() throws Exception {
        this.standaloneProcessModeler = new StandaloneProcessModeler();
    }

    StandaloneProcessModeler standaloneProcessModeler;
        public StandaloneProcessModeler getStandaloneProcessModeler() {
            return standaloneProcessModeler;
        }
        public void setStandaloneProcessModeler(StandaloneProcessModeler standaloneProcessModeler) {
            this.standaloneProcessModeler = standaloneProcessModeler;
        }

}
