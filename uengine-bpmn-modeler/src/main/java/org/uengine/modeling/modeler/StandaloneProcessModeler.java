package org.uengine.modeling.modeler;

import org.metaworks.annotation.Payload;
import org.metaworks.annotation.ServiceMethod;
import org.metaworks.widget.Clipboard;
import org.uengine.kernel.GlobalContext;
import org.uengine.kernel.ProcessDefinition;
import org.uengine.kernel.ProcessVariable;
import org.uengine.kernel.bpmn.face.ProcessVariablePanel;

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

        setProcessVariablePanel(new ProcessVariablePanel());
    }

    @ServiceMethod(keyBinding = "Ctrl+L")
    public StandaloneProcessModeler load(@Payload("fileName") String fileName) throws Exception {
//        ProcessDefinition processDefinition = BPMNUtil.adapt(new File(getFileName()));
        ProcessDefinition processDefinition = (ProcessDefinition) GlobalContext.deserialize(new FileInputStream(getFileName()), String.class);

        List<ProcessVariable> pList = new ArrayList<ProcessVariable>();

        ProcessVariable[] pv = processDefinition.getProcessVariables();

        for(int i=0; i<pv.length; i++) {
            pList.add(pv[i]);
        }


        processVariablePanel.setProcessVariableList(pList);

        setProcessVariablePanel(processVariablePanel);

        getProcessModeler().setModel(processDefinition);

        return this;

    }

    @ServiceMethod(callByContent=true)
    public void save() throws Exception {
//        ProcessDefinition processDefinition = BPMNUtil.adapt(new File(getFileName()));
        ProcessDefinition definition = (ProcessDefinition) getProcessModeler().createModel();

        List<ProcessVariable> processVariablList = processVariablePanel.getProcessVariableList();
        ProcessVariable[] pvds = new ProcessVariable[processVariablList.size()];

        int variableIndex = 0;
        for(ProcessVariable processVariable: processVariablList) {
            pvds[variableIndex++] = processVariable;
        }

        definition.setProcessVariables(pvds);

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


    ProcessVariablePanel processVariablePanel;
    public ProcessVariablePanel getProcessVariablePanel() {
        return processVariablePanel;
    }

    public void setProcessVariablePanel(ProcessVariablePanel processVariablePanel) {
        this.processVariablePanel = processVariablePanel;
    }


}
