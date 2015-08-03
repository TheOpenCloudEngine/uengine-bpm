package org.uengine.kernel.face;

import org.metaworks.Face;
import org.metaworks.annotation.Order;
import org.uengine.kernel.ProcessVariable;
import org.uengine.kernel.bpmn.face.ProcessVariableSelectorFace;
import org.uengine.util.UEngineUtil;

/**
 * Created by jinyoung jang on 2015. 8. 1..
 */
public class ProcessVariableExpressionFace implements Face<String> {

    ProcessVariable processVariable;
        @Order(1)
        @org.metaworks.annotation.Face(faceClass=ProcessVariableSelectorFace.class)
        public ProcessVariable getProcessVariable() {
            return processVariable;
        }
        public void setProcessVariable(ProcessVariable processVariable) {
            this.processVariable = processVariable;
        }

    String attributeExpression;
    @Order(2)
        public String getAttributeExpression() {
            return attributeExpression;
        }
        public void setAttributeExpression(String attributeExpression) {
            this.attributeExpression = attributeExpression;
        }

    @Override
    public void setValueToFace(String s) {

        if(s==null){
            setProcessVariable(null);
            setAttributeExpression(null);

            return;
        }

        if(s.indexOf(".") > 0){
            String[] pvNameAndAttrName = s.split("\\.");
            setProcessVariable(ProcessVariable.forName(pvNameAndAttrName[0]));
            setAttributeExpression(pvNameAndAttrName[1]);
        }else {

            setProcessVariable(ProcessVariable.forName(s));
            setAttributeExpression(null);
        }
    }

    @Override
    public String createValueFromFace() {
        return getProcessVariable().getName() + (UEngineUtil.isNotEmpty(getAttributeExpression()) ? "." + getAttributeExpression() : "") ;
    }
}
