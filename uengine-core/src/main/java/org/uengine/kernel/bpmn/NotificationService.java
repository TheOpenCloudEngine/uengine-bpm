package org.uengine.kernel.bpmn;

/**
 * Created by jjy on 2015. 11. 6..
 */
public class NotificationService {

    public boolean notificate(String userId, String message, String instanceId){
        System.out.println("Noti should be fired to " + userId + " with message : " + message);

        return true;
    }
}
