package org.uengine.persistence.processdefinitionversion;

//import java.rmi.RemoteException;
import javax.ejb.CreateException;
import javax.ejb.FinderException;
import java.util.Collection;

/**
 * @author Jinyoung Jang
 */

public interface ProcessDefinitionVersionRepositoryHomeLocal extends javax.ejb.EJBLocalHome
{   
   public ProcessDefinitionVersionRepositoryLocal create (Long id)
   	throws CreateException/*, RemoteException*/;
   
	public ProcessDefinitionVersionRepositoryLocal findByPrimaryKey (Long id)
		throws FinderException/*, RemoteException*/;   
		
	public Collection findAllVersions (Long id)
		throws FinderException/*, RemoteException*/;   
		
	public Collection findByDefinitionAndVersion(Long id, Long version)
		throws FinderException/*, RemoteException*/;   
		
}