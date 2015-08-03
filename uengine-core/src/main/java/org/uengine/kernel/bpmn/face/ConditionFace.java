package org.uengine.kernel.bpmn.face;

import org.metaworks.Face;
import org.uengine.kernel.Condition;
import org.uengine.kernel.Or;
import org.uengine.modeling.modeler.condition.ConditionEditor;
import org.uengine.modeling.modeler.condition.OrConditionFace;

/**
 * Created by Ryuha on 2015-07-30.
 */
@org.metaworks.annotation.Face(ejsPath="genericfaces/CleanObjectFace.ejs")
public class ConditionFace implements Face<Condition> {

    Or topOr;
        public Or getTopOr() {
            return topOr;
        }
        public void setTopOr(Or topOr) {
            this.topOr = topOr;
        }

    public ConditionFace() throws Exception {
        super();
    }

    @Override
    public void setValueToFace(Condition condition) {
        if(condition instanceof Or){
            setTopOr((Or)condition);
        }else{
            setTopOr(new Or());

            if(condition!=null)
                getTopOr().addCondition(condition);
        }
    }

    @Override
    public Condition createValueFromFace() {
        return getTopOr();
    }
}
