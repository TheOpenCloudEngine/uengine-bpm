package org.uengine.kernel;

import org.metaworks.annotation.Hidden;
import org.metaworks.annotation.Id;
import org.uengine.modeling.Relation;

/**
 * Created by uengine on 2018. 3. 2..
 */
public abstract class AbstractFlow extends Relation implements java.io.Serializable {
    private static final long serialVersionUID = org.uengine.kernel.GlobalContext.SERIALIZATION_UID;

    private String sourceRef;

    @Hidden
    public String getSourceRef() {
        return sourceRef;
    }

    public void setSourceRef(String source) {
        this.sourceRef = source;
    }

    private String targetRef;

    @Hidden
    public String getTargetRef() {
        return targetRef;
    }

    public void setTargetRef(String target) {
        this.targetRef = target;
    }

    String tracingTag;

    @Id
    @Hidden
    public String getTracingTag() {
        return tracingTag;
    }

    public void setTracingTag(String tag) {
        tracingTag = tag;
    }
}