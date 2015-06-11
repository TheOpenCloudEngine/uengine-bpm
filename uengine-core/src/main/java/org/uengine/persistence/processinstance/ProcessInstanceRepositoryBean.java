package org.uengine.persistence.processinstance;

import javax.ejb.EntityContext;

/**
 * @author Jinyoung Jang * @ejb.bean description="value" *           display-name="ProcessInstanceRepositoryBean" *           local-jndi-name="ProcessInstanceRepositoryHomeLocal" *           name="ProcessInstanceRepository" *           primkey-field="id" *           type="CMP" *           view-type="local"
 */


public abstract class ProcessInstanceRepositoryBean implements javax.ejb.EntityBean, ProcessInstanceRepositoryLocal{

	public Long ejbCreate(Long id) throws javax.ejb.CreateException{
		setInstId(id);
		
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