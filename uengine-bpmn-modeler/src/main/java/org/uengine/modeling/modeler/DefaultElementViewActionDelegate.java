package org.uengine.modeling.modeler;

import org.metaworks.dwr.MetaworksRemoteService;
import org.uengine.modeling.ElementView;
import org.uengine.modeling.ElementViewActionDelegate;
import org.uengine.modeling.PropertySettingDialog;

/**
 * Created by jjy on 2015. 11. 12..
 */
public class DefaultElementViewActionDelegate implements ElementViewActionDelegate {
    @Override
    public void onDoubleClick(ElementView elementView) {
        MetaworksRemoteService.wrapReturn(new PropertySettingDialog(elementView));
    }
}
