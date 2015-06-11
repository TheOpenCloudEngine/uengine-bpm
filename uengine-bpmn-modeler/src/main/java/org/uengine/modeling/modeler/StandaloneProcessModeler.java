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

        setProcessVariablePanel(new ProcessVariablePanel());

        setRolePanel(new RolePanel());
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

        List<Role> rList = new ArrayList<Role>();

        Role[] roles = processDefinition.getRoles();

        for(int j=0; j<roles.length; j++) {
            rList.add(roles[j]);
        }

        processVariablePanel.setProcessVariableList(pList);
        rolePanel.setRoleList(rList);

        setProcessVariablePanel(processVariablePanel);
        setRolePanel(rolePanel);

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

        List<Role> roleList = rolePanel.getRoleList();
        Role[] roles = new Role[roleList.size()];

        int roleIndex = 0;
        for(Role role: roleList) {
            roles[roleIndex++] = role;
        }

        definition.setProcessVariables(pvds);
        definition.setRoles(roles);

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



    RolePanel rolePanel;
        public RolePanel getRolePanel() {
            return rolePanel;
        }

        public void setRolePanel(RolePanel rolePanel) {
            this.rolePanel = rolePanel;
        }

}
