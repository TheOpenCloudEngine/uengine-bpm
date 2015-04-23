package org.uengine.processpublisher;

/**
 * @author Jinyoung Jang
 */

public interface Adapter<T, T1>{

    public T1 convert(T src, java.util.Hashtable keyedContext) throws Exception;

}