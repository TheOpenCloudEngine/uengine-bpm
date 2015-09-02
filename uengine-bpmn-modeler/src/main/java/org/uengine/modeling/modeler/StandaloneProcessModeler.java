package org.uengine.modeling.modeler;

import org.metaworks.annotation.Payload;
import org.metaworks.annotation.ServiceMethod;
import org.metaworks.widget.Clipboard;
import org.uengine.kernel.GlobalContext;
import org.uengine.kernel.ProcessDefinition;
import org.uengine.kernel.ProcessVariable;
import org.uengine.kernel.Role;
import org.uengine.kernel.bpmn.face.ProcessVariablePanel;
import org.uengine.kernel.bpmn.face.RolePanel;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

public class StandaloneProcessModeler {

    public StandaloneProcessModeler() throws Exception {


        setProcessModeler(new ProcessModeler());

        //     load();
        setFileName("/java/autoinsurance.bpmn");

        setClipboard(new Clipboard());

    }

    @ServiceMethod(keyBinding = "Ctrl+L")
    public StandaloneProcessModeler load(@Payload("fileName") String fileName) throws Exception {
//        ProcessDefinition processDefinition = BPMNUtil.adapt(new File(getFileName()));
        ProcessDefinition processDefinition = (ProcessDefinition) GlobalContext.deserialize(new FileInputStream(getFileName()), String.class);

        getProcessModeler().setModel(processDefinition);

        return this;

    }

    @ServiceMethod(callByContent=true)
    public void save() throws Exception {
//        ProcessDefinition processDefinition = BPMNUtil.adapt(new File(getFileName()));
        ProcessDefinition definition = (ProcessDefinition) getProcessModeler().createModel();


        GlobalContext.serialize(definition, new FileOutputStream(getFileName()), String.class);

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
