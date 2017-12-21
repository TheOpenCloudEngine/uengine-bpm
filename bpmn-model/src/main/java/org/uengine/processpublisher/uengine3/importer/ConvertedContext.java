package org.uengine.processpublisher.uengine3.importer;

import org.uengine.kernel.Activity;
import org.uengine.modeling.layout.Layout;

/**
 * Created by uengine on 2017. 12. 21..
 */
public class ConvertedContext {

    Activity inActivity;

    Activity outActivity;

    Layout layout;

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

    public Layout getLayout() {
        return layout;
    }

    public void setLayout(Layout layout) {
        this.layout = layout;
    }



}
