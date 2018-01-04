package org.uengine.five.framework;


/**
 * Created by uengine on 2017. 11. 10..
 */
public interface ProcessTransactionListener {
    void beforeCommit(ProcessTransactionContext tx) throws Exception;
    void beforeRollback(ProcessTransactionContext tx) throws Exception;
    void afterCommit(ProcessTransactionContext tx) throws Exception;
    void afterRollback(ProcessTransactionContext tx) throws Exception;
}