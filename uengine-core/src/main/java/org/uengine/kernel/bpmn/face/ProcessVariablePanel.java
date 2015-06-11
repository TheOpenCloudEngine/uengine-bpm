package org.uengine.kernel.bpmn.face;

import org.metaworks.annotation.Face;
import org.metaworks.model.MetaworksList;
import org.uengine.kernel.ProcessVariable;

import java.util.ArrayList;
import java.util.List;

public class ProcessVariablePanel {

    List<ProcessVariable> processVariableList = new ArrayList<ProcessVariable>();
    @Face(faceClass=ProcessVariableListFace.class)
        public List<ProcessVariable> getProcessVariableList() {
            return processVariableList;
        }
        public void setProcessVariableList(List<ProcessVariable> processVariableList) {
            this.processVariableList = processVariableList;
        }

//    MetaworksList<ProcessVariable>  processVariableMetaworksList = new MetaworksList<ProcessVariable>();
//        public MetaworksList<ProcessVariable> getProcessVariableMetaworksList() {
//            return processVariableMetaworksList;
//        }
//        public void setProcessVariableMetaworksList(MetaworksList<ProcessVariable> processVariableMetaworksList) {
//            this.processVariableMetaworksList = processVariableMetaworksList;
//        }
}
