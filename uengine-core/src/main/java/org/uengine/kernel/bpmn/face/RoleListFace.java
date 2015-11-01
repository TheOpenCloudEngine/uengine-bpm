package org.uengine.kernel.bpmn.face;

import org.metaworks.MetaworksContext;
import org.metaworks.ServiceMethodContext;
import org.metaworks.annotation.Face;
import org.metaworks.annotation.ServiceMethod;
import org.metaworks.model.MetaworksElement;
import org.metaworks.widget.ListFace;
import org.metaworks.widget.ModalWindow;
import org.uengine.kernel.Role;

import static org.metaworks.dwr.MetaworksRemoteService.wrapReturn;

/**
 * Created by Ryuha on 2015-06-11.
 */
public class RoleListFace extends ListFace<Role> {
//    @Override
//    @Face(displayName = "+")
//    @ServiceMethod(callByContent=true, target = ServiceMethodContext.TARGET_POPUP)
//    public void add() {
//        packElements();
//        MetaworksElement metaworksElement = createNewElement();
//
//        wrapReturn(new ModalWindow(metaworksElement));
//    }
}
