package org.uengine.kernel.face;

import org.metaworks.Face;
import org.metaworks.annotation.Range;
import org.uengine.kernel.Evaluate;
import org.uengine.kernel.ProcessVariable;
import org.uengine.kernel.bpmn.face.ProcessVariableSelectorFace;

/**
 * Created by kimsh on 2015. 8. 1..
 */
public class EvaluateFace implements Face<Evaluate> {


    ProcessVariable processVariable;
        @org.metaworks.annotation.Face(faceClass = ProcessVariableSelectorFace.class)
        public ProcessVariable getProcessVariable() {
            return processVariable;
        }
        public void setProcessVariable(ProcessVariable processVariable) {
            this.processVariable = processVariable;
        }

    String comparator;
        @Range(options={"==", "!=", ">=", ">", "<", "<=", "contains", "not contains"}
                , values={"==", "!=", ">=", ">", "<", "<=", "contains", "not contains"})
        public String getComparator() {
            return comparator;
        }
        public void setComparator(String comparator) {
            this.comparator = comparator;
        }

    String valueType;
    @Range(options={"Text", "Number", "Date"}, values={"java.lang.String", "java.lang.Long", "java.util.Calendar"})
        public String getValueType() {
            return valueType;
        }
        public void setValueType(String valueType) {
            this.valueType = valueType;
        }

    @Override
    public void setValueToFace(Evaluate evaluate) {
        if(evaluate==null)
            evaluate = new Evaluate();

        evaluate.getCondition();
    }

    @Override
    public Evaluate createValueFromFace() {
        return null;
    }
}
