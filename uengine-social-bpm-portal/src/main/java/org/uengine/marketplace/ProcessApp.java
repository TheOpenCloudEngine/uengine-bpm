package org.uengine.marketplace;

import org.metaworks.annotation.Face;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.uengine.codi.mw3.marketplace.App;
import org.uengine.social.SocialBPMProcessVariableTypeSelector;

/**
 * Created by jjy on 2015. 11. 5..
 */

@Component
@Scope("prototype")
public class ProcessApp extends App{
    public ProcessApp() throws Exception {
        super();

    }

    @Override
    public void load() throws Exception {
        super.load();
    }


    @Override
    @Face(faceClass=SocialBPMProcessVariableTypeSelector.class)
    public String getProjectId() {
        return super.getProjectId();
    }
}
