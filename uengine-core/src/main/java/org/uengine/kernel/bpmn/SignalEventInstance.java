package org.uengine.kernel.bpmn;

import java.io.Serializable;

/**
 * Created by uengine on 2018. 3. 11..
 */
public class SignalEventInstance implements Serializable {

    String activityRef;

    String signalName;

    public String getActivityRef() {
        return activityRef;
    }

    public void setActivityRef(String activityRef) {
        this.activityRef = activityRef;
    }

    public String getSignalName() {
        return signalName;
    }

    public void setSignalName(String signalName) {
        this.signalName = signalName;
    }
}
