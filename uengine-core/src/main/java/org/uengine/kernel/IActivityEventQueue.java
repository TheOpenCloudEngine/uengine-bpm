package org.uengine.kernel;

/**
 * Created by uengine on 2018. 11. 16..
 */
public interface IActivityEventQueue {
    void queue(String instanceId, String tracingTag, int retryingCount, String[] additionalParameters);
}
