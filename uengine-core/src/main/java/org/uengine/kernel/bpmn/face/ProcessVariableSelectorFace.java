package org.uengine.kernel.bpmn.face;

import org.metaworks.Face;
import org.metaworks.annotation.AutowiredFromClient;
import org.metaworks.component.SelectBox;
import org.uengine.kernel.ProcessVariable;
import org.uengine.util.UEngineUtil;

import java.util.ArrayList;

public class ProcessVariableSelectorFace extends SelectBox implements Face<ProcessVariable> {

    @AutowiredFromClient
    public ProcessVariablePanel processVariablePanel;

    @Override
    public void setValueToFace(ProcessVariable value) {

        if(processVariablePanel!=null && processVariablePanel.getProcessVariableList() !=null){
            ArrayList<String> options = new ArrayList<String>();

            options.add("");

            for(ProcessVariable processVariable : processVariablePanel.getProcessVariableList()){
                options.add(processVariable.getName());
            }

            setOptionNames(options);
            setOptionValues(options);
        }

        if(value!=null){
            if(getOptionNames()==null || getOptionNames().size() == 0){
                ArrayList<String> options = new ArrayList<String>();
                options.add("");
                options.add(value.getName());
                setOptionNames(options);
                setOptionValues(options);
            }
            setSelectedValue(value.getName());
            setSelectedText(value.getName()); //sometimes the value can contains "."
        }
    }

    @Override
    public ProcessVariable createValueFromFace() {
        String variableName = getSelectedText();

        if(UEngineUtil.isNotEmpty(variableName))
            return ProcessVariable.forName(variableName);
        else
            return null;
    }
}
