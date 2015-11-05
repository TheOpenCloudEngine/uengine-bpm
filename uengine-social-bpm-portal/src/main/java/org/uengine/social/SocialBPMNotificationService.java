package org.uengine.social;

import org.metaworks.dwr.MetaworksRemoteService;
import org.springframework.stereotype.Component;
import org.uengine.codi.mw3.Login;
import org.uengine.codi.mw3.filter.AllSessionFilter;
import org.uengine.codi.mw3.model.CommentWorkItem;
import org.uengine.codi.mw3.model.NotificationBadge;
import org.uengine.codi.mw3.model.User;
import org.uengine.kernel.bpmn.NotificationService;

import java.util.HashMap;

/**
 * Created by jjy on 2015. 11. 6..
 */
@Component
public class SocialBPMNotificationService extends NotificationService {

    @Override
    public boolean notificate(String userId, String message, String instanceId) {

//        HashMap<String, String> notiUsers = new HashMap<String, String>();
//
//        Login.getSessionIdWithUserId(session.getUser().getUserId());
//
//
//        MetaworksRemoteService.pushTargetScriptFiltered(new AllSessionFilter(notiUsers),
//                "mw3.getAutowiredObject('" + NotificationBadge.class.getName() + "').refresh",
//                new Object[]{});
//

        User user = new User();
        user.setUserId(userId);

        CommentWorkItem commentWorkItem = new CommentWorkItem();
        commentWorkItem.setTitle(message);
        commentWorkItem.setWriter(user);
        commentWorkItem.setInstId(new Long(instanceId));


        MetaworksRemoteService.autowire(commentWorkItem);

        try {
            commentWorkItem.add();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return true;
    }
}
