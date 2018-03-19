package org.uengine.kernel.bpmn;

import org.uengine.kernel.Condition;
import org.uengine.kernel.NeedArrangementToSerialize;
import org.uengine.kernel.ProcessInstance;

/**
 * Created by uengine on 2018. 3. 4..
 */
public class ConditionalCatchEvent extends TimerEvent implements NeedArrangementToSerialize{
    public ConditionalCatchEvent() {
        super();
        setExpression("5");
        setScheduleType("sec");
    }

    Condition condition;
        public Condition getCondition() {
            return condition;
        }
        public void setCondition(Condition condition) {
            this.condition = condition;
        }

    int pollingIntervalInSecond;
        public int getPollingIntervalInSecond() {
            return pollingIntervalInSecond;
        }
        public void setPollingIntervalInSecond(int pollingIntervalInSecond) {
            this.pollingIntervalInSecond = pollingIntervalInSecond;
        }

    @Override
    public boolean onMessage(ProcessInstance instance, Object payload) throws Exception {
        if(getCondition().isMet(instance, ""))
            return super.onMessage(instance, payload);
        else
            return false;
    }


    @Override
    public void beforeSerialization() {

    }

    @Override
    public void afterDeserialization() {

        if(getPollingIntervalInSecond()==0) setPollingIntervalInSecond(5);

        setExpression(""+getPollingIntervalInSecond());
        setScheduleType("sec");
    }
}
