package org.uengine.kernel;

import org.metaworks.annotation.Face;
import org.metaworks.annotation.Hidden;
import org.uengine.kernel.bpmn.face.ProcessVariableSelectorFace;
import org.uengine.util.UEngineUtil;

/**
 * Created by jangjinyoung on 2016. 12. 29..
 */
public class SubParamaterValueSelectionActivity extends HumanActivity {

    public SubParamaterValueSelectionActivity() {
        setName("Parameter Selection");
    }

    @Override
    @Hidden
    public String getTool() {
        String handler = "mw3." + UEngineUtil.getComponentClassName(getClass(), "handler");
        return handler;
    }

    ProcessVariable variableToBeSelected = new ProcessVariable();
    @Face(faceClass = ProcessVariableSelectorFace.class)
        public ProcessVariable getVariableToBeSelected() {
            return variableToBeSelected;
        }
        public void setVariableToBeSelected(ProcessVariable variableToBeSelected) {
            this.variableToBeSelected = variableToBeSelected;
        }


}
