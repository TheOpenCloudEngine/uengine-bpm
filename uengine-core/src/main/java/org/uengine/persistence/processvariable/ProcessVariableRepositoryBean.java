package org.uengine.persistence.processvariable;

import javax.ejb.EntityContext;

/**
 * @author Jinyoung Jang * @ejb.bean description="value" *           display-name="ProcessVariableRepositoryBean" *           local-jndi-name="ProcessVariableRepositoryHomeLocal" *           name="ProcessVariableRepository" *           primkey-field="id" *           type="CMP" *           view-type="local"
 */


public abstract class ProcessVariableRepositoryBean implements javax.ejb.EntityBean, ProcessVariableRepositoryLocal{

	public Long ejbCreate(Long id) throws javax.ejb.CreateException{
		setVarId(id);
		
		return null;
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