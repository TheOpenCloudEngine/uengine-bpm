package org.uengine.persistence.rolemapping;

import java.util.Iterator;

/**
 * 
 * @author Jinyoung Jang
 * DB2 dosen't work well with batched SQL updates.
 *
 */
public class DB2RoleMappingDAOType extends RoleMappingDAOType{
	public void removeRoleMappings (String instanceId, Iterator keyStrings) throws Exception {

		if(keyStrings!=null){
			boolean addOnce = keyStrings.hasNext();
			while(keyStrings.hasNext()){
				RoleMappingDAO roleMapping = createDAOImpl(removeRoleMapping_SQL);
	
				String roleName = (String)keyStrings.next();
				roleMapping.setRoleName(roleName);
				roleMapping.setInstId(new Long(instanceId));
				//roleMapping.addBatch();
				
(new Exception("[DB2RoleMappingDAOType:removeRoleMappings] instanceId = " + instanceId + "; RoleName = " + roleName)).printStackTrace();

				roleMapping.update();
			}
			
/*			if(addOnce)
				roleMapping.updateBatch();				
*/		}
	}

}
