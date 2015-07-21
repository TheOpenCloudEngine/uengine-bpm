package org.uengine.social;

import org.metaworks.annotation.Face;
import org.uengine.codi.mw3.model.Application;
import processadmin.ProcessAdminWorkbench;

@Face(ejsPath="dwr/metaworks/genericfaces/CleanObjectFace.ejs")
public class ProcessAdminApplication extends Application{

    public ProcessAdminApplication() throws Exception {
        this.processAdminWorkbench = new ProcessAdminWorkbench();
    }

//    StandaloneProcessModeler standaloneProcessModeler;
//        public StandaloneProcessModeler getStandaloneProcessModeler() {
//            return standaloneProcessModeler;
//        }
//        public void setStandaloneProcessModeler(StandaloneProcessModeler standaloneProcessModeler) {
//            this.standaloneProcessModeler = standaloneProcessModeler;
//        }


    ProcessAdminWorkbench processAdminWorkbench;
        public ProcessAdminWorkbench getProcessAdminWorkbench() {
            return processAdminWorkbench;
        }

        public void setProcessAdminWorkbench(ProcessAdminWorkbench processAdminWorkbench) {
            this.processAdminWorkbench = processAdminWorkbench;
        }


}
