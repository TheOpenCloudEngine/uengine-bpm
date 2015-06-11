package org.uengine.modeling.modeler;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

import org.metaworks.EventContext;
import org.metaworks.Refresh;
import org.metaworks.Remover;
import org.metaworks.ServiceMethodContext;
import org.metaworks.ToAppend;
import org.metaworks.ToEvent;
import org.metaworks.annotation.Payload;
import org.metaworks.annotation.ServiceMethod;
import org.metaworks.widget.Clipboard;
import org.uengine.kernel.GlobalContext;
import org.uengine.kernel.ProcessDefinition;
import org.uengine.kernel.ProcessVariable;
import org.uengine.kernel.bpmn.face.ProcessVariablePanel;
<<<<<<< HEAD
=======

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;
>>>>>>> c85c3036a4c18b8ee81f7cf8af970f013d8bf07e

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
<<<<<<< HEAD
        		
        ProcessVariable[] pv = processDefinition.getProcessVariables();
        
        for(int i=0; i<pv.length; i++) {
        	pList.add(pv[i]);
        }
        
        
        processVariablePanel.setProcessVariableList(pList);
        
        setProcessVariablePanel(processVariablePanel);
        
        getProcessModeler().setModel(processDefinition);
        
        return this;
        
=======

        ProcessVariable[] pv = processDefinition.getProcessVariables();

        for(int i=0; i<pv.length; i++) {
            pList.add(pv[i]);
        }


        processVariablePanel.setProcessVariableList(pList);

        setProcessVariablePanel(processVariablePanel);

        getProcessModeler().setModel(processDefinition);

        return this;

>>>>>>> c85c3036a4c18b8ee81f7cf8af970f013d8bf07e
    }

    @ServiceMethod(callByContent=true)
    public void save() throws Exception {
//        ProcessDefinition processDefinition = BPMNUtil.adapt(new File(getFileName()));
        ProcessDefinition definition = (ProcessDefinition) getProcessModeler().createModel();
<<<<<<< HEAD
        
        List<ProcessVariable> processVariablList = processVariablePanel.getProcessVariableList();
        ProcessVariable[] pvds = new ProcessVariable[processVariablList.size()];
        
        int variableIndex = 0; 
        for(ProcessVariable processVariable: processVariablList) {
        	pvds[variableIndex++] = processVariable;
        }
        
        definition.setProcessVariables(pvds);
        
=======

        List<ProcessVariable> processVariablList = processVariablePanel.getProcessVariableList();
        ProcessVariable[] pvds = new ProcessVariable[processVariablList.size()];

        int variableIndex = 0;
        for(ProcessVariable processVariable: processVariablList) {
            pvds[variableIndex++] = processVariable;
        }

        definition.setProcessVariables(pvds);

>>>>>>> c85c3036a4c18b8ee81f7cf8af970f013d8bf07e
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
