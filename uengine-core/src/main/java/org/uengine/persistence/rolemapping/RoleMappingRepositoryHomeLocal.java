package org.uengine.persistence.rolemapping;

//import java.rmi.RemoteException;
import javax.ejb.CreateException;
import javax.ejb.FinderException;
import java.util.Collection;

/**
 * @author Jinyoung Jang
 */

public interface RoleMappingRepositoryHomeLocal extends javax.ejb.EJBLocalHome
{   
	//TODO tuning point : mandatory fields are should be in the create parameters?
	public RoleMappingRepositoryLocal create (Long id, Long instanceId, String roleName)
		throws CreateException/*, RemoteException*/;
   
	public RoleMappingRepositoryLocal findByPrimaryKey (Long id)
		throws FinderException;   
	   
	public Collection findByInstanceIdAndRoleName (Long instId, String roleName)
		throws FinderException/*, RemoteException*/;   

	public Collection findByInstanceId (Long instanceId)
		throws FinderException/*, RemoteException*/;   
}