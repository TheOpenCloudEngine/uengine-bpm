package org.uengine.modeling.modeler;

import org.metaworks.annotation.Payload;
import org.metaworks.annotation.ServiceMethod;
import org.metaworks.widget.Clipboard;
import org.uengine.kernel.GlobalContext;
import org.uengine.kernel.ProcessDefinition;
import org.uengine.processpublisher.BPMNUtil;

import java.io.File;
import java.io.FileInputStream;

public class StandaloneProcessModeler {

    public StandaloneProcessModeler() throws Exception {


        setProcessModeler(new ProcessModeler());

   //     load();
        setFileName("/java/autoinsurance.bpmn");

        setClipboard(new Clipboard());
    }

    @ServiceMethod(keyBinding = "Ctrl+L")
    public ProcessModeler load(@Payload("fileName") String fileName) throws Exception {
//        ProcessDefinition processDefinition = BPMNUtil.adapt(new File(getFileName()));
        ProcessDefinition processDefinition = (ProcessDefinition) GlobalContext.deserialize(new FileInputStream(getFileName()), String.class);

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


        public String getFileName() {
            return fileName;
        }

        public void setFileName(String fileName) {
            this.fileName = fileName;
        }

    String fileName;

    Clipboard clipboard;
        public Clipboard getClipboard() {
            return clipboard;
        }
        public void setClipboard(Clipboard clipboard) {
            this.clipboard = clipboard;
        }

}
