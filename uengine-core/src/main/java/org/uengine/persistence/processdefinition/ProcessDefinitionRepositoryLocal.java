package org.uengine.persistence.processdefinition;

//import java.rmi.RemoteException;

/**
 * @author Jinyoung Jang
 */


public interface ProcessDefinitionRepositoryLocal extends javax.ejb.EJBLocalObject{

	public Long getDefId();
	public void setDefId(Long id);

	public String getDescription();
	public void setDescription(String desc);

	public boolean getIsFolder();
	public void setIsFolder(boolean folder);

	public boolean getIsAdhoc();
	public void setIsAdhoc(boolean isAdhoc);

	public Long getParentFolder();
	public void setParentFolder(Long parent);

	public int getProdVer();
	public void setProdVer(int version);
	
	public String getObjType();
	public void setObjType(String objType);
	
	public Long getProdVerId();
	public void setProdVerId(Long version);

	public String getName();
	public void setName(String title);

	public abstract boolean getIsDeleted();
	public abstract void setIsDeleted(boolean isDeleted);
	
	public boolean getIsVisible();
	public void setIsVisible(boolean isVisible);
	
	public String getAlias();
	public void setAlias(String name);
	
	public String getSuperDefId();
	public void setSuperDefId(String superDefId);
	
	public String getComCode();
	public void setComCode(String comCode);

}