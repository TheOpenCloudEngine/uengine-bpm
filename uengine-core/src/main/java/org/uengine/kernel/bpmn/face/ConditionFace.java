package org.uengine.kernel.bpmn.face;

import org.metaworks.Face;
import org.uengine.kernel.Condition;
import org.uengine.modeling.modeler.condition.ConditionEditor;

/**
 * Created by Ryuha on 2015-07-30.
 */
public class ConditionFace extends ConditionEditor implements Face<Condition> {

    public ConditionFace() throws Exception {
        super();
    }

    @Override
    public void setValueToFace(Condition condition) {
        try {
            load(condition);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Condition createValueFromFace() {
        try {
            if(getConditionTree()==null) {
                return null;
            }
            return makeCondition(getConditionTree().getNode());

        } catch (Exception e) {
           throw new RuntimeException(e);
        }
    }
}
