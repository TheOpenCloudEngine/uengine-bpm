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
        if(processVariablePanel!=null){

            ArrayList<String> options = new ArrayList<String>();

            for(ProcessVariable processVariable : processVariablePanel.getProcessVariableList()){
                options.add(processVariable.getName());
            }

            setOptionNames(options);
            setOptionValues(options);
        }
        if(value!=null){


            if(getOptionNames()==null || getOptionNames().size() == 0){

                ArrayList<String> options = new ArrayList<String>();

                options.add(value.getName());
                setOptionNames(options);
                setOptionValues(options);
            }

            setSelectedValue(value.getName());
        }

    }

    @Override
    public ProcessVariable createValueFromFace() {
        String variableName = getSelectedText();

        return ProcessVariable.forName(variableName);
    }
}
