package org.uengine.kernel.bpmn.face;

import jersey.repackaged.com.google.common.base.Objects;
import org.metaworks.ContextAware;
import org.metaworks.MetaworksContext;
import org.metaworks.Refresh;
import org.metaworks.Remover;
import org.metaworks.annotation.AutowiredFromClient;
import org.metaworks.annotation.Face;
import org.metaworks.annotation.ServiceMethod;
import org.metaworks.dwr.MetaworksRemoteService;
import org.metaworks.widget.ModalWindow;
import org.uengine.kernel.ProcessVariable;
import org.uengine.modeling.resource.IEditor;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ProcessVariablePanel implements ContextAware {

    List<ProcessVariable> processVariableList = new ArrayList<ProcessVariable>();
    @Face(faceClass=ProcessVariableListFace.class)
        public List<ProcessVariable> getProcessVariableList() {
            return processVariableList;
        }
        public void setProcessVariableList(List<ProcessVariable> processVariableList) {
            this.processVariableList = processVariableList;
        }

//    @Face(displayName = "Edit")
//    @ServiceMethod(callByContent = true)
//    public void edit(){
//        ProcessVariablePanel processVariablePanel = new ProcessVariablePanel();
//
//        MetaworksContext metaworksContext = new MetaworksContext();
//        metaworksContext.setWhen(MetaworksContext.WHEN_EDIT);
//
//        processVariablePanel.setMetaworksContext(metaworksContext);
//
//        for(ProcessVariable processVariable : this.getProcessVariableList()){
//            processVariable.getMetaworksContext().setWhen(MetaworksContext.WHEN_EDIT);
//        }
//        processVariablePanel.setProcessVariableList(this.getProcessVariableList());
//
//        MetaworksRemoteService.wrapReturn(new ModalWindow(processVariablePanel));
//    }
//
//    @Face(displayName = "Save")
//    @ServiceMethod(callByContent = true)
//    public void save(@AutowiredFromClient List<ProcessVariable> processVariableList){
//        ProcessVariablePanel processVariablePanel = new ProcessVariablePanel();
//
//        MetaworksContext metaworksContext = new MetaworksContext();
//        metaworksContext.setWhen(MetaworksContext.WHEN_VIEW);
//
//        processVariablePanel.setMetaworksContext(metaworksContext);
//
//        for(ProcessVariable processVariable : this.getProcessVariableList()){
//            processVariable.getMetaworksContext().setWhen(MetaworksContext.WHEN_VIEW);
//        }
//        processVariablePanel.setProcessVariableList(this.getProcessVariableList());
//
//        MetaworksRemoteService.wrapReturn(new Remover(new ModalWindow()),new Refresh(processVariablePanel));
//    }

    MetaworksContext metaworksContext;

    @Override
    public MetaworksContext getMetaworksContext() {
        return metaworksContext;
    }

    @Override
    public void setMetaworksContext(MetaworksContext metaworksContext) {
        this.metaworksContext = metaworksContext;
    }
}
