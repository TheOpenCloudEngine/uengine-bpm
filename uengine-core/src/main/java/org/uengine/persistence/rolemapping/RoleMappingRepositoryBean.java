package org.uengine.persistence.rolemapping;

import javax.ejb.EntityContext;

public abstract class RoleMappingRepositoryBean implements RoleMappingRepositoryLocal, javax.ejb.EntityBean{

	public Long ejbCreate(Long id, Long instanceId, String roleName) throws javax.ejb.CreateException{
		setRoleMappingId(id);
		setInstId(instanceId);
		setRoleName(roleName);
		
		return null;
	}
	
	public void ejbPostCreate(Long id, Long instanceId, String roleName){}

  // standard call back methods
   public void setEntityContext(EntityContext ec){}
   public void unsetEntityContext(){}
   public void ejbLoad(){}
   public void ejbStore(){}
   public void ejbActivate(){}
   public void ejbPassivate(){}
   public void ejbRemove(){}

}