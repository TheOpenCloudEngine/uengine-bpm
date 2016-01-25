package org.uengine.processpublisher.microsoft;

/**
 * @author Jinyoung Jang
 */

public interface Adapter<T, T1>{

    public T1 convert(T src) throws Exception;

}