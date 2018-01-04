package org.uengine.processpublisher.uengine3.importer;

import org.uengine.kernel.Activity;
import org.uengine.modeling.cnv.layout.CnvLayout;

/**
 * Created by uengine on 2017. 12. 21..
 */
public class ConvertedContext {

    Activity inActivity;

    Activity outActivity;

    CnvLayout layout;

    public Activity getInActivity() {
        return inActivity;
    }

    public void setInActivity(Activity inActivity) {
        this.inActivity = inActivity;
    }

    public Activity getOutActivity() {
        return outActivity;
    }

    public void setOutActivity(Activity outActivity) {
        this.outActivity = outActivity;
    }

    public CnvLayout getLayout() {
        return layout;
    }

    public void setLayout(CnvLayout layout) {
        this.layout = layout;
    }



}
