package org.uengine.persistence.processdefinition;

import javax.ejb.EntityContext;

public abstract class ProcessDefinitionRepositoryBean implements javax.ejb.EntityBean, ProcessDefinitionRepositoryLocal{

	public Long ejbCreate(Long id) throws javax.ejb.CreateException{
		setDefId(id);
		setIsFolder(false);
		
		return id;
	}
	
	public void ejbPostCreate(Long id){}

	
  // standard call back methods
   public void setEntityContext(EntityContext ec){}
   public void unsetEntityContext(){}
   public void ejbLoad(){}
   public void ejbStore(){}
   public void ejbActivate(){}
   public void ejbPassivate(){}
   public void ejbRemove(){}

}