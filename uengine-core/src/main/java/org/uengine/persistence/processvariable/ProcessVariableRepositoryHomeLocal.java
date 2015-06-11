package org.uengine.persistence.processvariable;

//import java.rmi.RemoteException;
import javax.ejb.CreateException;
import javax.ejb.FinderException;
import java.util.Collection;

/**
 * @author Jinyoung Jang
 */

public interface ProcessVariableRepositoryHomeLocal extends javax.ejb.EJBLocalHome
{   
   public ProcessVariableRepositoryLocal create (Long id)
   	throws CreateException/*, RemoteException*/;
   
   public ProcessVariableRepositoryLocal findByPrimaryKey (Long id)
	   throws FinderException/*, RemoteException*/;   
	   
	public Collection findAllProcessVariables ()
	 throws FinderException/*, RemoteException*/;   

	//public void removeByInstanceId (String instanceId) throws RemoveException;
	   
	public Collection findByInstanceId (Long instanceId)
	throws FinderException/*, RemoteException*/;   
}