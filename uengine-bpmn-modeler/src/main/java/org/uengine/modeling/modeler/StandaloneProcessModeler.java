package org.uengine.modeling.modeler;

import org.metaworks.annotation.ServiceMethod;
import org.metaworks.widget.Clipboard;
import org.uengine.kernel.ProcessDefinition;
import org.uengine.processpublisher.AdapterUtil;

import java.io.File;

public class StandaloneProcessModeler {

    public StandaloneProcessModeler() throws Exception {


        setProcessModeler(new ProcessModeler());

   //     load();

        setClipboard(new Clipboard());
    }

    @ServiceMethod(keyBinding = "Ctrl+L")
    public ProcessModeler load() throws Exception {
        ProcessDefinition processDefinition = AdapterUtil.adapt(new File("/Users/kimsh/Documents/acitiviti sample/parallel.xml")); //new File("/java/autoinsurance.bpmn"));

        getProcessModeler().setModel(processDefinition);

        return getProcessModeler();
    }

    ProcessModeler processModeler;
        public ProcessModeler getProcessModeler() {
            return processModeler;
        }

        public void setProcessModeler(ProcessModeler processModeler) {
            this.processModeler = processModeler;
        }

    Clipboard clipboard;
        public Clipboard getClipboard() {
            return clipboard;
        }
        public void setClipboard(Clipboard clipboard) {
            this.clipboard = clipboard;
        }

}
