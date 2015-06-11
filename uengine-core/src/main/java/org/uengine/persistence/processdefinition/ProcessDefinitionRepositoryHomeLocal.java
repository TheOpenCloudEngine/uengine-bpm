package org.uengine.persistence.processdefinition;

//import java.rmi.RemoteException;
import javax.ejb.CreateException;
import javax.ejb.FinderException;
import java.util.Collection;

/**
 * @author Jinyoung Jang
 */

public interface ProcessDefinitionRepositoryHomeLocal extends javax.ejb.EJBLocalHome
{   
   public ProcessDefinitionRepositoryLocal create (Long id)
   	throws CreateException/*, RemoteException*/;
   
   public ProcessDefinitionRepositoryLocal findByPrimaryKey (Long id)
	   throws FinderException/*, RemoteException*/;   

	public Collection findAllProcessDefinitions ()
	 throws FinderException/*, RemoteException*/;   
	 
	public Collection findByFolder (Long folderId)
	 throws FinderException/*, RemoteException*/;   

	public ProcessDefinitionRepositoryLocal findByName (String pdName)
	 throws FinderException/*, RemoteException*/;

	public ProcessDefinitionRepositoryLocal findByAlias (String alias)
	 throws FinderException/*, RemoteException*/;
	
	public ProcessDefinitionRepositoryLocal findByNameSameLevel (String pdName, Long parent, String objType)
	 throws FinderException/*, RemoteException*/;

}