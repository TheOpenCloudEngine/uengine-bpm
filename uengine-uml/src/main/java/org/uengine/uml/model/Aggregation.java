package org.uengine.uml.model;

/**
 * Created by uengine on 2018. 2. 7..
 */
public class Aggregation extends Association {

    String sourceMultiplicity;
    String targetMultiplicity;

    public String getSourceMultiplicity() {
        return sourceMultiplicity;
    }

    public void setSourceMultiplicity(String sourceMultiplicity) {
        this.sourceMultiplicity = sourceMultiplicity;
    }

    public String getTargetMultiplicity() {
        return targetMultiplicity;
    }

    public void setTargetMultiplicity(String targetMultiplicity) {
        this.targetMultiplicity = targetMultiplicity;
    }
}
