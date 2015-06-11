package org.uengine.persistence.processdefinitionversion;

import java.util.Date;

import javax.ejb.EntityContext;

public abstract class ProcessDefinitionVersionRepositoryBean implements javax.ejb.EntityBean, ProcessDefinitionVersionRepositoryLocal{

	public Long ejbCreate(Long id) throws javax.ejb.CreateException{
		setDefVerId(id);
		setVer(new Long(1));
				
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