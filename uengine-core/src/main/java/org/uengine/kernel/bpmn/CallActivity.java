package org.uengine.kernel.bpmn;

import org.metaworks.annotation.Face;
import org.metaworks.annotation.Hidden;
import org.uengine.kernel.SubProcessActivity;

public class CallActivity extends SubProcessActivity{

    public CallActivity() {
        super();
        setName("Call");
    }

    @Override
    @Face(faceClassName = "org.uengine.social.SocialBPMProcessDefinitionSelector")
    @Hidden(on=false)
    public String getDefinitionId() {
        return super.getDefinitionId();
    }
}
