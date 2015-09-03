package org.uengine.modeling;

/**
 * @author jyj
 */
public interface IModelingTimeSensitive {
    void onModelingTime();
    void afterModelingTime();

}