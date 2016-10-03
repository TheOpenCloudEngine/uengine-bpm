package org.uengine.kernel.view;

/**
 * Created by jjy on 2016. 10. 3..
 */
public interface IInstanceMonitor {

    public String getInstanceId();

    void setInstanceId(String instanceId);
    void load() throws Exception;
}
