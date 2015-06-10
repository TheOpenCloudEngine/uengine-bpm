package org.uengine.kernel.bpmn.face;

import org.metaworks.Face;
import org.metaworks.annotation.AutowiredFromClient;
import org.metaworks.component.SelectBox;
import org.uengine.kernel.ProcessVariable;

import java.util.ArrayList;

public class ProcessVariableSelectorFace extends SelectBox implements Face<ProcessVariable> {

    @AutowiredFromClient
    public ProcessVariablePanel processVariablePanel;

    @Override
    public void setValueToFace(ProcessVariable value) {

        if(processVariablePanel==null)
            return;//            throw new RuntimeException("ProcessVariablePanel is null");

        ArrayList<String> options = new ArrayList<String>();

        for(ProcessVariable processVariable : processVariablePanel.getProcessVariableList()){
            options.add(processVariable.getName());
        }

        setOptionNames(options);
        setOptionValues(options);

    }

    @Override
    public ProcessVariable createValueFromFace() {
        String variableName = getSelectedText();

        return ProcessVariable.forName(variableName);
    }
}
