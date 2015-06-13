package org.uengine.persistence.processinstance;

//import java.rmi.RemoteException;
import javax.ejb.CreateException;
import javax.ejb.FinderException;
import java.util.Collection;

/**
 * @author Jinyoung Jang
 */

public interface ProcessInstanceRepositoryHomeLocal extends javax.ejb.EJBLocalHome
{   
   	public ProcessInstanceRepositoryLocal create (Long id)
   	 throws CreateException/*, RemoteException*/;
   
   	public ProcessInstanceRepositoryLocal findByPrimaryKey (Long id)
	 throws FinderException/*, RemoteException*/;   

	public Collection findAllProcessInstances ()
	 throws FinderException/*, RemoteException*/;   

	public Collection findAllProcessArchives ()
	 throws FinderException/*, RemoteException*/;   
	 
	public Collection findByDefinition (Long pvdid)
	 throws FinderException/*, RemoteException*/;   

	public Collection findByDefinitionVersion (Long versionId)
	 throws FinderException/*, RemoteException*/;   

	public Collection findByDefinitionAndStatus (Long pvdid, String status)
	 throws FinderException/*, RemoteException*/;

	public Collection findByStatus (String defName)
	 throws FinderException/*, RemoteException*/;   

}