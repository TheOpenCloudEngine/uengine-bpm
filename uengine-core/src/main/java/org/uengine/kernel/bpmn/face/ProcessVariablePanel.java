package org.uengine.kernel.bpmn.face;

import jersey.repackaged.com.google.common.base.Objects;
import org.metaworks.annotation.Face;
import org.uengine.kernel.ProcessVariable;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ProcessVariablePanel{

    List<ProcessVariable> processVariableList = new ArrayList<ProcessVariable>();
    @Face(faceClass=ProcessVariableListFace.class)
        public List<ProcessVariable> getProcessVariableList() {
            return processVariableList;
        }
        public void setProcessVariableList(List<ProcessVariable> processVariableList) {
            this.processVariableList = processVariableList;
        }
}
