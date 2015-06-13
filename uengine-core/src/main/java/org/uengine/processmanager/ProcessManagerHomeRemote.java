package org.uengine.processmanager;

/**
 * @author Jinyoung Jang
 */

public interface ProcessManagerHomeRemote extends javax.ejb.EJBHome{
   public ProcessManagerRemote create ()
      throws java.rmi.RemoteException, javax.ejb.CreateException;
}
